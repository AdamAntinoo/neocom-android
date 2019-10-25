package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.dimensinfin.eveonline.neocom.services.MinuteTickReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the android application instance. Their responsibility is to have access tot any global data structures
 * and also to give access to them to any android class.
 * On initialisation it will only connect special classes to the ESI data source adapter to they are able to manage
 * access to the Eve Game Data.
 *
 * @author Adam Antinoo
 */
public class NeoComApplication extends Application {
	// TODO - This should be removed once the version 3 is merged into the production application.
	public enum EVariants {
		MINING_EXTRACTIONS_TODAY, MINING_EXTRACTIONS_ALL,
		ASSETS_BYLOCATION, ASSETS_BYCATEGORY, ASSETS_SEARCH,
		SKILLS_QUEUE, SKILLS_TRAINED,
		SHIPS_BYTYPE
	}

	private static Logger logger = LoggerFactory.getLogger(NeoComApplication.class);

	public static boolean checkNetworkAccess() {
		final ConnectivityManager cm = (ConnectivityManager) NeoComComponentFactory.getSingleton().getApplication()
				                                                     .getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if ((netInfo != null) && netInfo.isConnectedOrConnecting()) return true;
		return false;
	}

	public static boolean checkSDCardAvailability() {
		// TODO - This should be implemented.
		return true;
	}
	private BroadcastReceiver timeTickReceiver;

	// - A P P L I C A T I O N   L I F E   C Y C L E

	/**
	 * This Application lifecycle event is only called when the application is started or completely removed from memory.
	 * The important and key point of this code is to initialize the Global chain because if the app is not propagated
	 * to the Connectors the core calls may fail.
	 */
	@Override
	public void onCreate() {
		logger.info(">> [NeoComApplication.onCreate]");
		super.onCreate();
		logger.info("-- [NeoComApplication.onCreate]> Connect Component Factory to application...");
		NeoComComponentFactory.initialiseSingleton(this); // Create the component factory connected to this app.
		logger.info("-- [NeoComApplication.onCreate]> Start job scheduler...");
		this.startScheduler(); // Start the job scheduler
		logger.info("<< [NeoComApplication.onCreate]");
	}

	@Override
	public void onTerminate() {
		logger.info(">> [NeoComApplication.onTerminate]");
		this.terminateScheduler();
		super.onTerminate();
		logger.info("<< [NeoComApplication.onTerminate]");
	}

	// - P R I V A T E   S E C T I O N
//	private void createApplicationDirectories() {
//		this.createAppDir();
//		this.createCacheDirectories();
//	}
//
//	private boolean createAppDir() {
//		// Create file application directory if it doesn't exist
//		final File sdCardDir = new File(Environment.getExternalStorageDirectory(),
//				this.getResources().getString(R.string.appname));
//		if (!sdCardDir.exists()) sdCardDir.mkdir();
//		return sdCardDir.exists();
//	}
//
//	private boolean createCacheDirectories() {
//		File cachedir = new File(Environment.getExternalStorageDirectory(),
//				this.getResources().getString(R.string.appname) +
//						"/" +
//						NeoComComponentFactory.getSingleton().getConfigurationProvider().getResourceString("P.cache.directory.path"));
//		cachedir.mkdir();
//		return cachedir.exists();
//	}

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate job processes controlled by the scheduler.
	 */
	private void startScheduler() {
		if (null == this.timeTickReceiver) {
			this.timeTickReceiver = new MinuteTickReceiver.Builder(this).build();
			this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		}
	}

	private void terminateScheduler() {
		if (null != this.timeTickReceiver) this.unregisterReceiver(this.timeTickReceiver);
	}

	public boolean isSchedulerActive() {
		if (null != this.timeTickReceiver) return true;
		else return false;
	}
}
