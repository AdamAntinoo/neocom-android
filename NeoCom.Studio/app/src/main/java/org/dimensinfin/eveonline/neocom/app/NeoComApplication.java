package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.services.MinuteTickReceiver;

/**
 * This class is the android application instance. Their responsibility is to have access tot any global data structures
 * and also to give access to them to any android class.
 * On initialisation it will only connect special classes to the ESI data source adapter to they are able to manage
 * access to the Eve Game Data.
 *
 * @author Adam Antinoo
 */
public class NeoComApplication extends Application {
	public static boolean checkNetworkAccess() {
		final ConnectivityManager cm = (ConnectivityManager) NeoComComponentFactory.getSingleton().getApplication()
				                                                     .getSystemService( Context.CONNECTIVITY_SERVICE );
		final NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if ((netInfo != null) && netInfo.isConnectedOrConnecting()) return true;
		return false;
	}

	public static boolean checkSDCardAvailability() {
		// TODO - This should be implemented.
		return true;
	}

	/**
	 * Creates the application directory if it already does not exist. This folder goes on the private storage space of the application under the
	 * path Android/data/data/org.dimensinfin...
	 */
	public static boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File( getPrivateAppSpace()
				, NeoComComponentFactory.getSingleton().getApplication().getApplicationContext()
						  .getResources().getString( R.string.appname ) );
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		return sdCardDir.exists();
	}

	public static boolean createPublicAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File( getPublicAppSpace()
				, NeoComComponentFactory.getSingleton().getApplication()
						  .getApplicationContext()
						  .getResources().getString( R.string.appname ) );
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		return sdCardDir.exists();
	}

	private static File getPrivateAppSpace() {
		return NeoComComponentFactory.getSingleton().getApplication().getApplicationContext().getExternalFilesDir( null );
	}

	private static File getPublicAppSpace() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * Creates the additional directories on the private application storage section...
	 */
	public static boolean createCacheDirectories() {
		File cachedir = new File( getPrivateAppSpace()
				, NeoComComponentFactory.getSingleton().getApplication().getApplicationContext()
						  .getResources().getString( R.string.appname )
						  + "/" +
						  NeoComComponentFactory.getSingleton().getConfigurationProvider()
								  .getResourceString( "P.cache.directory.path" ) );
		cachedir.mkdir();
		return cachedir.exists();
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
		NeoComLogger.enter();
		super.onCreate();
		NeoComLogger.info( "Connect Component Factory to application..." );
		NeoComComponentFactory.initialiseSingleton( this ); // Create the component factory connected to this app.
		NeoComLogger.info( "Start job scheduler..." );
		this.startScheduler(); // Start the job scheduler
		NeoComLogger.exit();
	}

	@Override
	public void onTerminate() {
		NeoComLogger.enter();
		this.terminateScheduler();
		super.onTerminate();
		NeoComLogger.exit();
	}

	/**
	 * This action can fail so wrap into a method to control the result.
	 */
	public Integer getNeoComDatabaseVersion() {
		try {
			return Integer.valueOf( this.getResources().getString( R.string.appdatabaseversion ) );
		} catch (final RuntimeException runtime) { return 0;}
	}

	/**
	 * Start the background broadcast receiver to intercept the minute tick and process the data structures
	 * checking elements that should be updated because they are obsolete. New updates will be launched as
	 * separate job processes controlled by the scheduler.
	 */
	private void startScheduler() {
		if (null == this.timeTickReceiver) {
			this.timeTickReceiver = new MinuteTickReceiver();
			this.registerReceiver( this.timeTickReceiver, new IntentFilter( Intent.ACTION_TIME_TICK ) );
		}
	}

	private void terminateScheduler() {
		if (null != this.timeTickReceiver) this.unregisterReceiver( this.timeTickReceiver );
	}

	public boolean isSchedulerActive() {
		if (null != this.timeTickReceiver) return true;
		else return false;
	}
}
