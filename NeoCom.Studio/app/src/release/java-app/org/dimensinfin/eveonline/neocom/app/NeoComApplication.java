package org.dimensinfin.eveonline.neocom.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.database.SDEAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.model.ANeoComEntity;
import org.dimensinfin.eveonline.neocom.services.MarketDataServer;
import org.dimensinfin.eveonline.neocom.services.TimeTickReceiver;
import org.dimensinfin.eveonline.neocom.services.TimedUpdater;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the android application instance. Their responsibility is to give access to the common adapters and data structures
 * to any service or class on the application. During initialization the only task is to create the different adapters specific for
 * the Android implementation of the NeoCom system.
 *
 * NeoCom uses a shared library to use the same code on the Android implementation and on the SpringBoot backend implementation.
 *
 * @author Adam Antinoo
 */
public class NeoComApplication extends Application {
	public enum EVariants {
		MINING_EXTRACTIONS_TODAY, MINING_EXTRACTIONS_ALL,
		ASSETS_BYLOCATION, ASSETS_BYCATEGORY, ASSETS_SEARCH,
		SKILLS_QUEUE, SKILLS_TRAINED,
		SHIPS_BYTYPE
	}

	private static Logger logger = LoggerFactory.getLogger(NeoComApplication.class);

	/** Market Data Downloader service. It is imeplemtned with threads and not as Android service. */
	public static MarketDataServer mdServer = null;
	/** Background receiver to process pending jobs. Wakes every minute to fire download pending checks. */
	private static BroadcastReceiver timeTickReceiver = null;
	private static TimedUpdater timedService;

	/** Direct entry point to fire a timer tick event . */
	public static void runTimer() {
		timeTickReceiver.onReceive(AndroidGlobalDataManager.getAppContext(), null);
	}

	public static void setTimedUpdater( final TimedUpdater service ) {
		timedService = service;
	}

	public static void runTicker() {
		if (null == timedService) timedService = NeoComAndroid.getSingleton().getTimedService();
		if (null != timedService) timedService.timeTick();
	}

	public static boolean checkNetworkAccess() {
		final ConnectivityManager cm = (ConnectivityManager) NeoComAndroid.getSingleton().getApplication()
				                                                     .getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if ((netInfo != null) && netInfo.isConnectedOrConnecting()) return true;
		return false;
	}

	// - F I E L D - S E C T I O N
	private NeoComAndroid neocom;

	// - C O N S T R U C T O R - S E C T I O N
	public NeoComApplication() {
		super();
	}

	// - M E T H O D - S E C T I O N

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate async tasks.
	 */
	public void startTimer() {
		if (null == timeTickReceiver) {
			final boolean tickActivate = Boolean.valueOf(
					NeoComAndroid.getSingleton().getConfigurationProvider().getResourceString("R.runtime.downloadTick.timer"
							, "true"));
			final int tickTime = Integer.valueOf(
					NeoComAndroid.getSingleton().getConfigurationProvider().getResourceString("R.runtime.downloadTick.cycleTime"
							, "5"));
			timeTickReceiver = new TimeTickReceiver.Builder(this)
					                   .withTickActivate(tickActivate)
					                   .withTickTime(tickTime)
					                   .build();
			this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		}
	}

	//	private TimedUpdater getBackgroundUpdater() {
	//		if (null == timedService)
	//			timedService = new TimedUpdater.Singletoner()
	//					               .withESIAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
	//					               .build();
	//		return timedService;
	//	}
	// - A P P L I C A T I O N   L I F E   C Y C L E
	private void oldInitialization() {
		//		final IFileSystem fileSystemAdapter = new AndroidFileSystemAdapter.Builder(this.getResources().getString(R.string.appname), this)
		//				                                      .build();
		// Connect the application to the global context to be able to use all the resources.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Context to the Global...");
		AndroidGlobalDataManager.setApplicationContext(this);

		// Connect the file system to be able to read the assets and other application resources stored externally.
		logger.info("-- [NeoComApp.onCreate]> Connecting the File System to Global...");
		AndroidGlobalDataManager.installFileSystem(NeoComAndroid.getSingleton().getFileSystemAdapter());

		// Connect the Configuration manager. Do this after the file system because it reads configuration files.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Configuration Manager...");
		//		final IConfigurationProvider configurationProvider = new AndroidConfigurationProvider.Builder(
		//				this.getResources().getString(R.string.propertiesLocation))
		//				                                                     .withApplicationContext(this)
		//				                                                     .build();
		AndroidGlobalDataManager.connectConfigurationManager(NeoComAndroid.getSingleton().getConfigurationProvider());

		logger.info("-- [NeoComApp.onCreate]> Connecting the Preferences Manager...");
		AndroidGlobalDataManager.connectPreferencesManager(NeoComAndroid.getSingleton().getPreferencesProvider());
		AndroidGlobalDataManager.connectNeoComDBConnector(NeoComAndroid.getSingleton().getNeoComDBHelper());

		// Initialize the Model with the current global instance.
		logger.info("-- [NeoComApp.onCreate]> Connecting Global to Model...");
		ANeoComEntity.connectGlobal(new AndroidGlobalDataManager.Builder(NeoComAndroid.getSingleton().getConfigurationProvider()
				, NeoComAndroid.getSingleton().getFileSystemAdapter()
				, new ESIAndroidAdapter.Builder(NeoComAndroid.getSingleton().getConfigurationProvider()
				, NeoComAndroid.getSingleton().getFileSystemAdapter())
						  .build())
				                            .build());

		// Connect the SDE database.
		logger.info("-- [NeoComApp.onCreate]> Connecting SDE database...");
		try {
			AndroidGlobalDataManager.connectSDEDBConnector(new SDEAndroidDBHelper()
					                                               .setDatabaseSchema(AndroidGlobalDataManager.getResourceString("P.database.sdedatabase.databaseschema"))
					                                               .setDatabasePath(AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname) + "/")
					                                               .setDatabaseName(AndroidGlobalDataManager.getResourceString("P.database.sdedatabase.database.name"))
					                                               .build()
			);
		} catch (SQLException sqle) {
			AndroidGlobalDataManager.registerException("NeoComApp.onCreate", sqle, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			sqle.printStackTrace();
		}
		ANeoComEntity.connectSDEHelper(AndroidGlobalDataManager.getSingleton().getSDEDBHelper());

		logger.info("-- [NeoComMicroServiceApplication.main]> Starting Market Data service...");
		mdServer = new MarketDataServer().start();
		GlobalDataManager.getSingleton().connectMarketDataManager(mdServer);
	}

	/**
	 * This Application lifecycle event is only called when the application is started or completely removed from memory.
	 * The important and key point of this code is to initialize the Global chain because if the app is not propagated
	 * to the Connectors the core calls may fail.
	 */
	@Override
	public void onCreate() {
		logger.info(">> [NeoComApplication.onCreate]");
		super.onCreate();
		NeoComAndroid.initialiseSingleton(this); // Create the component factory but empty of contents.
		//		Stetho.initializeWithDefaults(this);
		this.oldInitialization();
		// Do global injections
		//		EsiItemV2.injectEveItemProvider(NeoComComponentFactory.getSingleton(.get));
		EsiItemV2.injectDownloaderService(NeoComAndroid.getSingleton().getDownloaderService());

		// Check if the SDE database file is at the right place. If not copy it.
		if (!AndroidGlobalDataManager.checkAppFile(
				AndroidGlobalDataManager.getResourceString("P.database.sdedatabase.database.name"))) {
			// TODO The CCP database is not distributed with the application. If not available then close the App
			this.copyFromAssets(AndroidGlobalDataManager.getResourceString("P.database.sdedatabase.database.name"));
		}

		// Connect the Timed Updater scan.
		logger.info("-- [NeoComApp.main]> Connecting the background timed download scanner...");
		startTimer();
		logger.info("<< [NeoComApp.onCreate]");
	}

	/**
	 * Events called during the Application life cycle.
	 */
	@Override
	public void onTerminate() {
		logger.info(">> [NeoComApp.onTerminate]");
		this.unregisterReceiver(timeTickReceiver);
		super.onTerminate();
		logger.info("<< [NeoComApp.onTerminate]");
	}

	// --- P R I V A T E   S E C T I O N


	/**
	 * Copies the parameter files from the assets installation directory to the user application directory.
	 * Current contents are the static database and the initial API list if found.
	 */
	private void copyFromAssets( final String sourceFileName ) {
		InputStream istream = null;
		OutputStream ostream = null;
		//		String resourceName = NeoComAppConnector.getSingleton().getResourceString(resourceId);
		//		final Context context = singleton.getApplicationContext();
		try {
			istream = AndroidGlobalDataManager.openAsset4Input(sourceFileName);
			final File destination = new File(AndroidGlobalDataManager.accessResource4Path(sourceFileName));
			destination.createNewFile();
			ostream = new FileOutputStream(destination);
			final byte[] buffer = new byte[8192];
			int length;
			while ((length = istream.read(buffer)) > 0) {
				ostream.write(buffer, 0, length);
			}
			ostream.flush();
			logger.info("-- [NeoComApp.copyFromAssets]> Copied resource from assets [" + sourceFileName + "]");
		} catch (final Exception e) {
			e.printStackTrace();
			logger.error("E> [NeoComApp.copyFromAssets] Failed to copy resource: " + sourceFileName);
		} finally {
			try {
				if (ostream != null) {
					ostream.close();
				}
				if (istream != null) {
					istream.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	public void copyPreloadFiles() {
		try {
			for (String name : AndroidGlobalDataManager.getApplication().getAssets().list("preloadcache")) {
				copyFromAssets("preloadcache/" + name);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NeoComApplication [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
