package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;
import org.dimensinfin.eveonline.neocom.services.MinuteTickReceiver;

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
	private BroadcastReceiver timeTickReceiver;

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
		NeoComComponentFactory.initialiseSingleton(this); // Create the component factory but empty of contents.
		NeoComUpdater.injectsEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter());
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

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate job processes controlled by the scheduler.
	 */
	protected void startScheduler() {
		if (null == this.timeTickReceiver) {
			this.timeTickReceiver = new MinuteTickReceiver.Builder(this).build();
			this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		}
	}

	protected void terminateScheduler() {
		if (null != this.timeTickReceiver) this.unregisterReceiver(this.timeTickReceiver);
	}
	// --- P R I V A T E   S E C T I O N


	//	/**
	//	 * Copies the parameter files from the assets installation directory to the user application directory.
	//	 * Current contents are the static database and the initial API list if found.
	//	 */
	//	private void copyFromAssets( final String sourceFileName ) {
	//		InputStream istream = null;
	//		OutputStream ostream = null;
	//		//		String resourceName = NeoComAppConnector.getSingleton().getResourceString(resourceId);
	//		//		final Context context = singleton.getApplicationContext();
	//		try {
	//			istream = AndroidGlobalDataManager.openAsset4Input(sourceFileName);
	//			final File destination = new File(AndroidGlobalDataManager.accessResource4Path(sourceFileName));
	//			destination.createNewFile();
	//			ostream = new FileOutputStream(destination);
	//			final byte[] buffer = new byte[8192];
	//			int length;
	//			while ((length = istream.read(buffer)) > 0) {
	//				ostream.write(buffer, 0, length);
	//			}
	//			ostream.flush();
	//			logger.info("-- [NeoComApp.copyFromAssets]> Copied resource from assets [" + sourceFileName + "]");
	//		} catch (final Exception e) {
	//			e.printStackTrace();
	//			logger.error("E> [NeoComApp.copyFromAssets] Failed to copy resource: " + sourceFileName);
	//		} finally {
	//			try {
	//				if (ostream != null) {
	//					ostream.close();
	//				}
	//				if (istream != null) {
	//					istream.close();
	//				}
	//			} catch (final IOException e) {
	//			}
	//		}
	//	}

	//	public void copyPreloadFiles() {
	//		try {
	//			for (String name : AndroidGlobalDataManager.getApplication().getAssets().list("preloadcache")) {
	//				copyFromAssets("preloadcache/" + name);
	//			}
	//		} catch (IOException ioe) {
	//			ioe.printStackTrace();
	//		}
	//	}
	//
	//	@Override
	//	public String toString() {
	//		StringBuffer buffer = new StringBuffer("NeoComApplication [ ");
	//		buffer.append("name: ").append(0);
	//		buffer.append("]");
	//		buffer.append("->").append(super.toString());
	//		return buffer.toString();
	//	}
}
