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
package org.dimensinfin.eveonline.neocom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.conf.GlobalAndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.database.NeoComAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.database.SDEAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.datamngmt.FileSystemAndroidImplementation;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.datamngmt.MarketDataServer;
import org.dimensinfin.eveonline.neocom.model.ANeoComEntity;
import org.dimensinfin.eveonline.neocom.services.TimeTickReceiver;
import org.dimensinfin.eveonline.neocom.services.TimedUpdater;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComApplication extends Application {
	public enum EExtras {
		FIRE_AUTHORIZATION_FLOW, CAPSULEER_IDENTIFIER
	}
public enum EVariants{
		MINING_EXTRACTIONS_TODAY, MINING_EXTRACTIONS_ALL, MINING_EXTRACTIONS_TEST, MINING_EXTRACTIONS_VERIFICATION
}
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("NeoComApplication");

	/** Market Data Downloader service. It is imeplemtned with threads and not as Android service. */
	public static MarketDataServer mdServer = null;
	/** Background receiver to process pending jobs. Wakes every minute to fire download pending checks. */
	private static BroadcastReceiver timeTickReceiver = null;
	private static TimedUpdater timedService = null;

	/** Direct entry point to fire a timer tick event . */
	public static void runTimer() {
		timeTickReceiver.onReceive(AndroidGlobalDataManager.getAppContext(), null);
	}

	public static void runTicker() {
		timedService.timeTick();
	}

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComApplication() {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate async tasks.
	 */
	public void startTimer() {
		if ( null == timeTickReceiver ) {
			timeTickReceiver = new TimeTickReceiver(this);
			timedService = new TimedUpdater();
			this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		}
	}
	// --- A P P L I C A T I O N   L I F E   C Y C L E

	/**
	 * This Application lifecycle event is only called when the application is started or completely removed from memory.
	 * The important and key point of this code is to initialize the Global chain because if the app is not propagated
	 * to the Connectors the core calls may fail.
	 */
	@Override
	public void onCreate() {
		logger.info(">> [NeoComApp.onCreate]");
		super.onCreate();

		// Connect the application to the global context to be able to use all the resources.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Context to the Global...");
		AndroidGlobalDataManager.setApplicationContext(this);

		// Connect the file system to be able to read the assets and other application resources stored externally.
		logger.info("-- [NeoComApp.onCreate]> Connecting the File System to Global...");
		AndroidGlobalDataManager.installFileSystem(new FileSystemAndroidImplementation(
				AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname)
		));

		// Connect the Configuration manager. Dothis after the file system because it reads configuration files.
		logger.info("-- [NeoComApp.onCreate]> Connecting the Configuration Manager...");
		AndroidGlobalDataManager.connectConfigurationManager(new GlobalAndroidConfigurationProvider("properties"));

		// Initialize the Model with the current global instance.
		logger.info("-- [NeoComApp.onCreate]> Connecting Global to Model...");
		ANeoComEntity.connectGlobal(new AndroidGlobalDataManager());

		// Initializing the ESI api network controller.
		ESINetworkManager.initialize();

		// Check if the files and folders to run on Android are in place. If not create/copy them.
		createAppDir();
		createCacheDirectories();
		copyPreloadFiles();

		// Check if the SDE database file is at the right place. If not copy it.
		if ( !AndroidGlobalDataManager.checkAppFile(
				AndroidGlobalDataManager.getResourceString("R.database.sdedatabase.databasename")) ) {
			// TODO The CCP database is not distributed with the application. If not available then close the App
			this.copyFromAssets(AndroidGlobalDataManager.getResourceString("R.database.sdedatabase.databasename"));
		}

		// Connect the SDE database.
		logger.info("-- [NeoComApp.onCreate]> Connecting SDE database...");
		try {
			AndroidGlobalDataManager.connectSDEDBConnector(new SDEAndroidDBHelper()
					.setDatabaseSchema(AndroidGlobalDataManager.getResourceString("R.database.sdedatabase.databaseschema"))
					.setDatabasePath(AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname) + "/")
					.setDatabaseName(AndroidGlobalDataManager.getResourceString("R.database.sdedatabase.databasename"))
					.build()
			);
		} catch (SQLException sqle) {
			AndroidGlobalDataManager.registerException("NeoComApp.onCreate", sqle, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			sqle.printStackTrace();
		}
		ANeoComEntity.connectSDEHelper(new AndroidGlobalDataManager().getSDEDBHelper());

		// Connect the NeoCom database.
		logger.info("-- [NeoComApp.onCreate]> Connecting NeoCom private database...");
		try {
			AndroidGlobalDataManager.connectNeoComDBConnector(new NeoComAndroidDBHelper.NeoComAndroidDBHelperBuilder()
					.setDatabaseLocation(AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname))
					.setDatabaseName(AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appdatabasefilename))
					.setDatabaseVersion(Integer.valueOf(AndroidGlobalDataManager.getApplication().getResources().getString(
							R.string.appdatabaseversion)))
					.build()
			);
		} catch (SQLException sqle) {
			AndroidGlobalDataManager.registerException("NeoComApp.onCreate", sqle, GlobalDataManagerExceptions.EExceptionSeverity.SEVERE);
			sqle.printStackTrace();
		}

		//		// Connect the MarketData service.
		logger.info("-- [NeoComMicroServiceApplication.main]> Starting Market Data service...");
		mdServer = new MarketDataServer().start();
		GlobalDataManager.setMarketDataManager(mdServer);

		// Load the Locations cache to speed up the Citadel and Outpost search.
		logger.info("-- [NeoComApp.onCreate]> Read Locations data cache...");
		AndroidGlobalDataManager.readLocationsDataCache();

		// Connect the Timed Upgrade scan.
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
	 * Creates the application directory if it already does not exist on the sdcard.
	 */
	private boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdcarddir = new File(Environment.getExternalStorageDirectory()
				, AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname));
		final boolean existence = sdcarddir.exists();
		if ( !sdcarddir.exists() ) {
			sdcarddir.mkdir();
		}
		return !existence;
	}

	private void createCacheDirectories() {
		// Create additional cache folders.
		File cachedir = new File(Environment.getExternalStorageDirectory()
				, AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname)
				+ "/" + AndroidGlobalDataManager.getResourceString("R.cache.directorypath"));
		//				+ AndroidGlobalDataManager.getResourceString("R.cache.cachefoldername"));
		cachedir.mkdir();
		//		File alternatedir = new File(cachedir
		//				, AndroidGlobalDataManager.getApplication().getResources().getString(R.string.drawablecachefoldername));
		//		alternatedir.mkdir();
		//		alternatedir = new File(cachedir,
		//				NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
		//		alternatedir.mkdir();
	}

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
				if ( ostream != null ) {
					ostream.close();
				}
				if ( istream != null ) {
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
