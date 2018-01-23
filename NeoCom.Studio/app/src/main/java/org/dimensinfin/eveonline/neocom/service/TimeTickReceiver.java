//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.model.eve.Station;
import com.beimin.eveapi.parser.eve.ConquerableStationListParser;
import com.beimin.eveapi.response.eve.StationListResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.nikr.eve.jeveasset.data.Citadel;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.database.NeoComDatabase;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.enums.ERequestClass;
import org.dimensinfin.eveonline.neocom.enums.ERequestState;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.manager.DownloadManager;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.Outpost;
import org.dimensinfin.eveonline.neocom.network.NetworkManager;
import org.dimensinfin.eveonline.neocom.services.PendingRequestEntry;
import org.dimensinfin.eveonline.neocom.storage.DataManagementModelStore;
import org.joda.time.Instant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * This class is a one minute timer activated at startup. It will search for data obsolete and fire the
 * services required to update that data by downloading from CCP servers using the required API calls.<br>
 * There are two sets of services, one for character data and the other for market data that have quite
 * different mechanics. All data updates the background counters that inform the user that some background
 * services are active and the number of elements on their queues.
 *
 * @author Adam Antinoo
 */
public class TimeTickReceiver extends BroadcastReceiver {
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class Job {
		private static final String DEFAULT_REF_VALUE = "-REF-";

		public static String constructReference (final EDataUpdateJobs type, final long identifier) {
			return new StringBuffer(type.name()).append("/").append(identifier).toString();
		}

		// - F I E L D - S E C T I O N ............................................................................
		private String reference = DEFAULT_REF_VALUE;
		private long credentialId = -1;
		private EDataUpdateJobs jobClass = EDataUpdateJobs.READY;
		private Runnable task = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public Job (final TimeStamp job) {
			super();
			// Set som of the fields from the parameter.
			reference = job.getReference();
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		//		private Job setReference (final String reference) {
		//			this.reference = reference;
		//			return this;
		//		}

		public String getReference () {
			return reference;
		}

		public Job setCredentialIdentifier (final long identifier) {
			credentialId = identifier;
			return this;
		}

		public Job setJobClass (final EDataUpdateJobs jobClass) {
			this.jobClass = jobClass;
			return this;
		}

		public Job setTask (final Runnable task) {
			this.task = task;
			return this;
		}

		public Future<?> submit () throws NeoComException {
			// Check that all the parameters are valid and are filled with data.
			if ( null == task ) throw new NeoComException("[Job]> Jobs task is not defined. Nothing to run.");
			if ( DEFAULT_REF_VALUE.equalsIgnoreCase(reference) )
				throw new NeoComException("[Job]> Reference not set. Unexpected initialization error.");
			if ( -1 == credentialId )
				throw new NeoComException("[Job]> Credential not identified.. Cannot run job on unidentified target.");

			// Launch job and read back the future to control the execution
			return UpdateJobManager.downloadExecutor.submit(task);
		}
	}

	/**
	 * The main responsibility of this class is to have a unique list of update jobs. If every minute we check for
	 * data to update and that data is already scheduled but not completed we can found a second and third requests
	 * that will also have to be executes.
	 * So we need something between the launcher of updated and the executor that removed already registered
	 * updates and do not request them again.
	 * Using an specific executor for this task will isolate the run effect from other tasks but anyway it
	 * requires some way for the job to notify its state so it can clear the request after completed or remove it
	 * if the process fails or gets interrupted.
	 * With the use of utures we can track pending jobs and be sure the update mechanics are followed as
	 * requested.
	 */
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class UpdateJobManager {
		// - S T A T I C - S E C T I O N ..........................................................................
		private static final List<Job> jobList = new Vector();
		private static final ExecutorService downloadExecutor = Executors.newSingleThreadExecutor();
		public static final Hashtable<String, Future<?>> runningJobs = new Hashtable();

		/**
		 * Submits the job to out private executor. Store the Future to control job duplicated and to check when the
		 * job completes. The job reference can be used a primary key to detect job duplicates and collision.
		 *
		 * @param newJob the job to update some information.
		 */
		public synchronized static void submit (final Job newJob) {
			try {
				// Search for the job to detect duplications
				final String identifier = newJob.getReference();
				final Future<?> hit = runningJobs.get(identifier);
				if ( null == hit ) {
					// New job. Launch it and store the reference.
					logger.info("-- [UpdateJobManager.submit]> Launching job {}",newJob.getReference());
					final Future<?> future = newJob.submit();
					runningJobs.put(identifier, future);
				} else {
					// Check for job completed.
					if ( hit.isDone() ) {
						// The job with this same reference has completed. We can launch a new one.
						final Future<?> future = newJob.submit();
						runningJobs.put(identifier, future);
					}
				}
			} catch (NeoComException neoe) {
				neoe.printStackTrace();
			}
		}

		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................

		// - M E T H O D - S E C T I O N ..........................................................................
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	protected class UpdateCitadelsTask extends AsyncTask<Void, Void, Void> {

		// - F I E L D - S E C T I O N ............................................................................
		//		private final AbstractNewPagerFragment fragment;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public UpdateCitadelsTask () {
			//			this.fragment = fragment;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * The datasource is ready and the new hierarchy should be created from the current model. All the stages
		 * are executed at this time both the model contents update and the list of parts to be used on the
		 * ListView. First, the model is checked to be initialized and if not then it is created. Then the model
		 * is run from start to end to create all the visible elements and from this list then we create the full
		 * list of the parts with their right renders.<br>
		 * This is the task executed every time a datasource gets its model modified and hides all the update time
		 * from the main thread as it is recommended by Google.
		 */
		@Override
		protected Void doInBackground (final Void... arg0) {
			TimeTickReceiver.logger.info(">> [TimeTicketReceiver.UpdateCitadelsTask.doInBackground]");
			String destination = "https://stop.hammerti.me.uk/api/citadel/all";
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				public void checkClientTrusted (final X509Certificate[] certs, final String authType) {
				}

				public void checkServerTrusted (final X509Certificate[] certs, final String authType) {
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers () {
					return null;
				}
			}};
			// Install the all-trusting trust manager
			SSLContext sc;
			InputStream in = null;
			try {
				sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new Verifier();

				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				ObjectMapper mapper = new ObjectMapper(); //create once, reuse
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				URL url = new URL(destination);
				URLConnection con = url.openConnection();
				con.setRequestProperty("Accept-Encoding", "gzip");
				con.setRequestProperty("Accept-Encoding", "html");
				con.setRequestProperty("Accept-Encoding", "json");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				long contentLength = con.getContentLength();
				InputStream inputStream = con.getInputStream();
				String contentEncoding = con.getContentEncoding();
				if ( "gzip".equals(contentEncoding) ) {
					in = new GZIPInputStream(inputStream);
				} else {
					in = inputStream;
				}
				Map<Long, Citadel> results = mapper.readValue(in, new TypeReference<Map<Long, Citadel>>() {
				});
				if ( results != null ) {
					for (Map.Entry<Long, Citadel> entry : results.entrySet()) {
						// Convert each Citadel to a new Location and update the database if needed.
						EveLocation loc = new EveLocation(entry.getKey(), entry.getValue());
						TimeTickReceiver.logger
								.info("-- [TimeTicketReceiver.UpdateCitadelsTask.doInBackground]> Created location: " + loc);
					}
				}
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} finally {
				if ( in != null ) {
					try {
						in.close();
					} catch (IOException ex) {
						ex.printStackTrace();
						//No problem...
					}
				}
			}
			TimeTickReceiver.logger.info("<< [TimeTicketReceiver.UpdateCitadelsTask.doInBackground]");
			return null;
		}
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	protected class UpdateOutpostsTask extends AsyncTask<Void, Void, Void> {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public UpdateOutpostsTask () {
			//			this.fragment = fragment;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * The datasource is ready and the new hierarchy should be created from the current model. All the stages
		 * are executed at this time both the model contents update and the list of parts to be used on the
		 * ListView. First, the model is checked to be initialized and if not then it is created. Then the model
		 * is run from start to end to create all the visible elements and from this list then we create the full
		 * list of the parts with their right renders.<br>
		 * This is the task executed every time a datasource gets its model modified and hides all the update time
		 * from the main thread as it is recommended by Google.
		 */
		@Override
		protected Void doInBackground (final Void... arg0) {
			TimeTickReceiver.logger.info(">> [TimeTicketReceiver.UpdateOutpostsTask.doInBackground]");
			try {
				//				StationListResponse response = null;
				ConquerableStationListParser parser = new ConquerableStationListParser();
				StationListResponse response = parser.getResponse();
				if ( null != response ) {
					Map<Long, Station> stations = response.getStations();
					for (Long stationid : stations.keySet()) {
						// Convert the station to an EveLocation
						EveLocation loc = new EveLocation(stations.get(stationid));
						TimeTickReceiver.logger
								.info("-- [TimeTicketReceiver.UpdateOutpostsTask.doInBackground]> Created location: " + loc);
					}
				}
			} catch (final RuntimeException rtex) {
				rtex.printStackTrace();
			} catch (ApiException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			TimeTickReceiver.logger.info("<< [TimeTicketReceiver.UpdateOutpostsTask.doInBackground]");
			return null;
		}
	}

	final class Verifier implements HostnameVerifier {
		public boolean verify (final String hostname, final SSLSession session) {
			return true;
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("TimeTickReceiver");
	private static boolean BLOCKED_STATUS = false;
	private static int LAUNCH_LIMIT = 30;

	// Transform the received OK instance into a NeoCom compatible model instance.
	private static ModelMapper modelMapper = new ModelMapper();

	static {
		modelMapper.getConfiguration()
		           .setFieldMatchingEnabled(true)
		           .setMethodAccessLevel(AccessLevel.PRIVATE);
	}

	// - F I E L D - S E C T I O N ............................................................................
	private Context _context = null;
	private int limit = 0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public TimeTickReceiver (final Context context) {
		_context = context;
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Receive an intent on every minute tick.<br>
	 * We should scan the list of pending data for queued requests and after termination we have to check for
	 * character structures that need update depending on their different valid periods.<br>
	 * Pending requests are processed by priority and the number of requests queued to the service is limited so
	 * more requests will be queued on the next tick if they are more than the queue limit.<br>
	 * The user data is refreshed on step two and the time limit is one hour.<br>
	 * The asset information is also updated on step 2 but with a duration of 8 hours. <br>
	 * Every minute the process checks for pending market data downloads.
	 */
	@Override
	public void onReceive (final Context context, final Intent intent) {
		TimeTickReceiver.logger.info(">> [TimeTickReceiver.onReceive]");
		// Run only if the network is active.
		if ( !NeoComAppConnector.getSingleton().checkNetworkAccess() ) return;
		// Or if the service is disables by configuration.
		if ( this.blockedDownload() ) return;

		// STEP 01. Launch pending Data Requests
		// Get requests pending from the queue service.
		Vector<PendingRequestEntry> requests = NeoComApp.getSingletonApp().getCacheConnector().getAndroidPendingRequests();
		if ( requests.size() > 0 ) TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Pending requests: {}",
				requests.size());
		synchronized (requests) {
			// Process request by priority. Additions to queue are limited.
			limit = 0;
			for (PendingRequestEntry entry : requests)
				if ( entry.state == ERequestState.PENDING ) {
					// Filter only MARKETDATA requests.
					if ( entry.reqClass == ERequestClass.MARKETDATA ) {
						if ( this.blockedMarket() ) continue;
						if ( limit <= TimeTickReceiver.LAUNCH_LIMIT ) {
							this.launchMarketUpdate(entry);
						}
					}
					// Filter the rest of the character data to be updated
					if ( entry.reqClass == ERequestClass.CHARACTERUPDATE ) {
						this.launchCharacterDataUpdate(entry);
					}
					if ( entry.reqClass == ERequestClass.CITADELUPDATE ) {
						// Launch the update and remove the event from the queue.
						new UpdateCitadelsTask().execute();
						NeoComApp.getSingletonApp().getCacheConnector().clearPendingRequest(entry.getIdentifier());
					}
					if ( entry.reqClass == ERequestClass.OUTPOSTUPDATE ) {
						// Launch the update and remove the event from the queue.
						new UpdateOutpostsTask().execute();
						NeoComApp.getSingletonApp().getCacheConnector().clearPendingRequest(entry.getIdentifier());
					}
				}
		}

		// STEP 02. Check Credentials for pending data to update.
		final List<Credential> credentialList = DataManagementModelStore.accessCredentialList();
		TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Accessing credentials. Credentials found: {}",
				credentialList.size());
		for (Credential cred : credentialList) {
			//			// Get the list of timestamps pending for this Credential.
			//			final List<TimeStamp> stamps = cred.needsUpdate();
			//			TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Credential [{}] - Received {} timestamps.", cred.getAccountName(), stamps.size());
			// Scan all the possible TimeStamps we have to check for each Credential.
			// If the TS do not exists then we launch the job and create it.
			// Set up the complete list.
			final ArrayList<EDataUpdateJobs> joblist = new ArrayList<>();
			joblist.add(EDataUpdateJobs.CHARACTER_CORE);
			//			joblist.add(EDataUpdateJobs.CHARACTER_FULL);
			joblist.add(EDataUpdateJobs.ASSETDATA);
			//		joblist.add(EDataUpdateJobs.BLUEPRINTDATA);
			//			joblist.add(EDataUpdateJobs.INDUSTRYJOBS);
			//			joblist.add(EDataUpdateJobs.MARKETORDERS);
			joblist.add(EDataUpdateJobs.COLONYDATA);
			//			joblist.add(EDataUpdateJobs.SKILL_DATA);


			// Not all timestamps should have to be defined so scan them and remove from the list all the ones processed.
			//			for (TimeStamp job : stamps) {
			//				doProcessJob(job, cred);
			//				//				joblist.remove(EDataUpdateJobs.CHARACTER_CORE);
			//			}

			// Now process all the rest of the jobs that where not cleared from the list. That means they had no timestamp.
			for (EDataUpdateJobs jobName : joblist) {
				try {
					final String reference = Job.constructReference(jobName, cred.getAccountId());
					// Search for the TS and check the expiration time.
					final TimeStamp ts = NeoComDatabase.getImplementer().getTimeStampDao().queryForId(reference);
					if ( null == ts ) {
						TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Generating job request for {}.", reference);
						final TimeStamp newts = new TimeStamp(reference, Instant.now())
								.setCredentialId(cred.getAccountId());
						doProcessJob(newts, cred);
					} else {
						// Check if time point has already happened.
						if ( ts.getTimeStamp() < Instant.now().getMillis() ){
							TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Time point past. Generating job request for {}.",
									reference);
							doProcessJob(ts, cred);
					}}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//[01]
		}
		TimeTickReceiver.logger.info("<< TimeTickReceiver.onReceive [{} - {}/{}]", requests.size(), NeoComApp.marketCounter, NeoComApp.topCounter);
	}

	/**
	 * With the TS and the Credential check id the data is still valid. If the timestamp has elapsed then we should
	 * request the launch of the job. It is possible that this has been detected also on previous iterations but the
	 * Manager filter should remove the duplication of jobs.
	 *
	 * @param dataIdentifier the time stamp and reference identifier for the job to launch.
	 * @param credential     the credential to use during the update.
	 */

	private void doProcessJob (final TimeStamp dataIdentifier, final Credential credential) {
		// Calculate job reference to match the job request.
		TimeTickReceiver.logger.info(">> [TimeTickReceiver.doProcessJob]> Job Type: {} - Job credential: {}"
				, dataIdentifier.getReference()
				, credential.getAccountName());
		String currentrequestReference = Job.constructReference(EDataUpdateJobs.CHARACTER_CORE, credential.getAccountId());
		// Check that the request is a COLONY_DATA update request.
		if ( dataIdentifier.getReference().equalsIgnoreCase(currentrequestReference) ) {
			// Check if valid data time has elapsed.
	//		if ( dataIdentifier.getTimeStamp() < Instant.now().getMillis() ) {
				// Submit the job to the manager
				final Job newJob = new Job(dataIdentifier)
						.setCredentialIdentifier(credential.getAccountId())
						.setJobClass(EDataUpdateJobs.CHARACTER_CORE)
						.setTask(() -> {
							NeoComAppConnector.getSingleton().getCacheConnector().incrementTopCounter();
							TimeTickReceiver.logger.info("-- [Job.CHARACTER_CORE]> Downloading Pilot v1 information for: [{}]", credential.getAccountName());
							ModelFactory.udpatePilotV1(credential.getAccountId());
							NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
						});
				UpdateJobManager.submit(newJob);
	//		}
		}
		currentrequestReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, credential.getAccountId());
		// Check that the request is a COLONYDATA update request.
		if ( dataIdentifier.getReference().equalsIgnoreCase(currentrequestReference) ) {
			if ( dataIdentifier.getTimeStamp() < Instant.now().getMillis() ) {
				// Submit the job to the manager
				final Job newJob = new Job(dataIdentifier)
						.setCredentialIdentifier(credential.getAccountId())
						.setJobClass(EDataUpdateJobs.COLONYDATA)
						.setTask(() -> {
							NeoComAppConnector.getSingleton().getCacheConnector().incrementTopCounter();
							try {
								TimeTickReceiver.logger.info(
										"-- [Job.COLONYDATA]> Downloading Colony list for: [{}]", credential.getAccountName());
								// Get the list of planet colonies for this credential and update each one on the database.
								final List<GetCharactersCharacterIdPlanets200Ok> colonyInstances = NetworkManager.getCharactersCharacterIdPlanets(Long.valueOf(credential.getAccountId()).intValue(), credential.getRefreshToken(), "tranquility");
								for (GetCharactersCharacterIdPlanets200Ok colonyOK : colonyInstances) {
									TimeTickReceiver.logger.info("-- [Job.COLONYDATA]> Mapping Colony: [{}]", colonyOK.getPlanetId());
									Colony col = modelMapper.map(colonyOK, Colony.class);
									TimeTickReceiver.logger.info("-- [Job.COLONYDATA]> Saving Colony data at DB: [{}]", col.getPlanetId());
									col.create(colonyOK.getPlanetId());
									NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
								}
							} catch (RuntimeException rtex) {
								rtex.printStackTrace();
							}
						});
				UpdateJobManager.submit(newJob);
			}
		}
		currentrequestReference = Job.constructReference(EDataUpdateJobs.ASSETDATA, credential.getAccountId());
		// Check that the request is a ASSETDATA update request.
		if ( dataIdentifier.getReference().equalsIgnoreCase(currentrequestReference) ) {
			if ( dataIdentifier.getTimeStamp() < Instant.now().getMillis() ) {
				// Submit the job to the manager
				final Job newJob = new Job(dataIdentifier)
						.setCredentialIdentifier(credential.getAccountId())
						.setJobClass(EDataUpdateJobs.ASSETDATA)
						.setTask(() -> {
							NeoComAppConnector.getSingleton().getCacheConnector().incrementTopCounter();
							TimeTickReceiver.logger.info(
									"-- [TimeTickReceiver.onReceive]> Downloading asset list for: [{}]", credential.getAccountName());
							// Get the list of planet colonies for this credential and update the character assets at the database.
							final DownloadManager downloader = new DownloadManager(credential);
							downloader.downloadPilotAssetsESI();
						});
				UpdateJobManager.submit(newJob);
			}
		}
	}

	private boolean blockedDownload () {
		// Read the flag values from the preferences.
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
		boolean blockDownload = sharedPrefs.getBoolean(AppWideConstants.preference.PREF_BLOCKDOWNLOAD, false);
		return blockDownload;
	}

	private boolean blockedMarket () {
		// Read the flag values from the preferences.
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
		boolean blockDownload = sharedPrefs.getBoolean(AppWideConstants.preference.PREF_BLOCKMARKET, false);
		return blockDownload;
	}

	private void citadelLocationUpdate () {
		TimeTickReceiver.logger.info(">> [TimeTicketReceiver.citadelLocationUpdate]> Citadels updating");
		String destination = "https://stop.hammerti.me.uk/api/citadel/all";
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			public void checkClientTrusted (final X509Certificate[] certs, final String authType) {
			}

			public void checkServerTrusted (final X509Certificate[] certs, final String authType) {
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers () {
				return null;
			}
		}};
		// Install the all-trusting trust manager
		SSLContext sc;
		InputStream in = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify (final String hostname, final SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			ObjectMapper mapper = new ObjectMapper(); //create once, reuse
			URL url = new URL(destination);
			URLConnection con = url.openConnection();
			con.setRequestProperty("Accept-Encoding", "gzip");
			//			long contentLength = con.getContentLength();
			String contentEncoding = con.getContentEncoding();
			InputStream inputStream = con.getInputStream();
			if ( "gzip".equals(contentEncoding) ) {
				in = new GZIPInputStream(inputStream);
			} else {
				in = inputStream;
			}
			Map<Long, Citadel> results = mapper.readValue(in, new TypeReference<Map<Long, Citadel>>() {
			});
			if ( results != null ) { //Updated OK
				for (Map.Entry<Long, Citadel> entry : results.entrySet()) {
					// Convert each Citadel to a new Location and update the database if needed.
					EveLocation loc = new EveLocation(entry.getKey(), entry.getValue());
					TimeTickReceiver.logger
							.info("-- [TimeTicketReceiver.UpdateCitadelsTask.doInBackground]> Created location: " + loc);
				}
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					//No problem...
				}
			}
		}
		//[02]
	}

	private void launchCharacterDataUpdate (final PendingRequestEntry entry) {
		TimeTickReceiver.logger.info(
				"-- [TimeTickReceiver.launchCharacterDataUpdate]> Character Update Request Class [" + entry.reqClass + "]");
		Intent serialService = new Intent(_context, CharacterUpdaterService.class);
		Number content = entry.getContent();
		serialService.putExtra(AppWideConstants.EExtras.EXTRA_CHARACTER_LOCALIZER.name(), content.longValue());
		if ( null != _context ) {
			_context.startService(serialService);
		}
		entry.state = ERequestState.ON_PROGRESS;
		//		// Increment the counter.
		//		NeoComApp.topCounter++;
	}

	private void launchMarketUpdate (final PendingRequestEntry entry) {
		TimeTickReceiver.logger
				.info("-- [TimeTickReceiver.launchService Market]> Update Request Class [" + entry.reqClass + "]");
		if ( null != _context ) {
			Intent serialService = new Intent(_context, MarketUpdaterService.class);
			Number content = entry.getContent();
			TimeTickReceiver.logger
					.info("-- [TimeTickReceiver.launchMarketUpdate]> Posting update. Item ID [" + content + "]");
			serialService.putExtra(AppWideConstants.extras.EXTRA_MARKETDATA_LOCALIZER, content.intValue());
			_context.startService(serialService);
			entry.state = ERequestState.ON_PROGRESS;
			// Increment the counter.
			NeoComApp.marketCounter++;
			limit++;
		}
	}

	private void outpostLocationUpdate () {
		// Check if the outpotst already loaded.
		//	if ((null == outpostsCache) || (outpostsCache.size() < 1)) {
		// Making a request to url and getting response
		String jsonStr = this.readJsonData();
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			// Getting JSON Array node
			JSONArray outposts = jsonObj.getJSONArray("items");
			// Looping through all outposts
			int counter = 1;
			for (int i = 0; i < outposts.length(); i++) {
				Outpost o = new Outpost();
				JSONObject post = outposts.getJSONObject(i);
				int id = post.getInt("facilityID");
				o.setFacilityID(id);
				JSONObject intermediate = post.getJSONObject("solarSystem");
				o.setSolarSystem(intermediate.getLong("id"));
				o.setName(post.getString("name"));
				intermediate = post.getJSONObject("region");
				o.setRegion(intermediate.getLong("id"));
				intermediate = post.getJSONObject("owner");
				o.setOwner(intermediate.getLong("id"));
				intermediate = post.getJSONObject("type");
				o.setType(intermediate.getLong("id"));

				// Create the part with the Outpost
				EveLocation loc = new EveLocation(o);
				TimeTickReceiver.logger.info("-- Part counter " + counter++);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//		}
		// Search for the item
		//		Outpost hit = outpostsCache.get(Long.valueOf(locationID).intValue());
		//		EveLocation location = new EveLocation(locationID);
		//		if (null != hit) {
		//			EveLocation systemLocation = searchLocationbyID(hit.getSolarSystem());
		//			location.setStation(hit.getName());
		//			location.setSystemID(hit.getSolarSystem());
		//			location.setSystem(systemLocation.getSystem());
		//			location.setConstellationID(systemLocation.getConstellationID());
		//			location.setConstellation(systemLocation.getConstellation());
		//			location.setRegionID(systemLocation.getRegionID());
		//			location.setRegion(systemLocation.getRegion());
		//			location.setSecurity(systemLocation.getSecurity());
		//		}
		//		return location;
	}

	private String readJsonData () {
		StringBuffer data = new StringBuffer();
		try {
			String str = "";
			InputStream is = NeoComAppConnector.getSingleton().getStorageConnector().accessInternalStorage("outposts.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			if ( is != null ) {
				while ((str = reader.readLine()) != null) {
					data.append(str);
				}
			}
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toString();
	}
}

// - UNUSED CODE ............................................................................................
//[01]
//				try {
//					// Get access to the database record for this Character cache time stamp.
//					final String reference = this.getCharacterID() + ".CHARACTERDATA";
//					getDownloadManager()._characterCacheTime = ModelAppConnector.getSingleton().getDBConnector().getTimeStampDAO().queryForId(reference);
//					if ( null == getDownloadManager()._characterCacheTime ) return EDataUpdateJobs.CHARACTERDATA;
//				} catch (final SQLException sqle) {
//					return EDataUpdateJobs.CHARACTERDATA;
//				}
//			}
//			if ( ModelAppConnector.getSingleton().checkExpiration(getDownloadManager()._characterCacheTime.getTimeStamp(), ModelWideConstants.NOW) )
//				return EDataUpdateJobs.CHARACTERDATA;
//			// Check for asset cached time.
//			if ( null == getDownloadManager()._assetsCacheTime ) {
//				try {
//					// Get access to the database record for this Character cache time stamp.
//					final String reference = this.getCharacterID() + ".ASSETDATA";
//					getDownloadManager()._assetsCacheTime = ModelAppConnector.getSingleton().getDBConnector().getTimeStampDAO().queryForId(reference);
//					if ( null == getDownloadManager()._assetsCacheTime ) return EDataUpdateJobs.ASSETDATA;
//				} catch (final SQLException sqle) {
//					return EDataUpdateJobs.ASSETDATA;
//				}
//			}
//			if ( ModelAppConnector.getSingleton().checkExpiration(getDownloadManager()._assetsCacheTime.getTimeStamp(), ModelWideConstants.NOW) )
//				return EDataUpdateJobs.ASSETDATA;
//			// Check for blueprints cached time.
//			if ( null == getDownloadManager()._blueprintsCacheTime ) {
//				try {
//					// Get access to the database record for this Character cache time stamp.
//					final String reference = this.getCharacterID() + ".BLUEPRINTDATA";
//					getDownloadManager()._blueprintsCacheTime = ModelAppConnector.getSingleton().getDBConnector().getTimeStampDAO().queryForId(reference);
//					if ( null == getDownloadManager()._blueprintsCacheTime ) return EDataUpdateJobs.BLUEPRINTDATA;
//				} catch (final SQLException sqle) {
//					return EDataUpdateJobs.BLUEPRINTDATA;
//				}
//			}
//			if ( ModelAppConnector.getSingleton().checkExpiration(getDownloadManager()._blueprintsCacheTime.getTimeStamp(), ModelWideConstants.NOW) )
//				return EDataUpdateJobs.BLUEPRINTDATA;
//
//			// If not returned before we have nothing to update
//			return EDataUpdateJobs.READY;

//[02]
//		 CitadelSettings	citadelSettings	= new CitadelSettings();
//		if (citadelSettings.getNextUpdate().after(new Date()) && true && true) { //Check if we can update now
//			//				if (updateTask != null) {
//			//					updateTask.addError(DialoguesUpdate.get().citadel(), "Not allowed yet.\r\n(Fix: Just wait a bit)");
//			//				}
//			logger.info("	Citadels failed to update (NOT ALLOWED YET)");
//			return;
//		}
// Update citadel
//		InputStream in = null;
//		try { //Update from API
//			ObjectMapper mapper = new ObjectMapper(); //create once, reuse
//			URL url = new URL("https://stop.hammerti.me.uk/api/citadel/all");
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestProperty("Accept-Encoding", "gzip");
//
//			long contentLength = con.getContentLength();
//			String contentEncoding = con.getContentEncoding();
//			InputStream inputStream = con.getInputStream();
//			if ("gzip".equals(contentEncoding)) {
//				in = new GZIPInputStream(inputStream);
//			} else {
//				in = inputStream;
//			}
//			Map<Long, Citadel> results = mapper.readValue(in, new TypeReference<Map<Long, Citadel>>() {
//			});
//			if (results != null) { //Updated OK
//				for (Map.Entry<Long, Citadel> entry : results.entrySet()) {
//					// Convert each Citadel to a new Location and update the database if needed.
//					EveLocation loc = new EveLocation(entry.getKey(), entry.getValue());
//					//					citadelSettings.put(entry.getKey(), entry.getValue());
//					//					saveCitadel(entry.getKey(), entry.getValue());
//				}
//			}
//			//			citadelSettings.setNextUpdate();
//			//				saveCitadel(citadelSettings);
//			logger.info("	Updated citadels for jEveAssets");
//		} catch (IOException ex) {
//			//				if (updateTask != null) {
//			//					updateTask.addError(DialoguesUpdate.get().citadel(), ex.getMessage());
//			//				}
//			//				logger.("	Citadels failed to update", ex);
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException ex) {
//					//No problem...
//				}
//			}
//		}
