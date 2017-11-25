//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.SplashTasksFragment;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This is the application launcher Activity. It is the first Activity to show to the user and its responsibility is to
 * show the initialization progress of the Application data structures. It is based on the generic MVC Activity that
 * shown Views on a Header section and a list of Views on the List section. The elements to show on the list are the
 * different initialization tasks to execute or pending execution and composed in the standard list way. We start with a
 * model node that that gets converted to a Part on the Factory and that Part manages the Renderer View that is finally
 * shown on the screen.
 *
 * @author Adam Antinoo
 */
public class NeoComSplashActivity extends ANeoComPagerActivity {
	/**
	 * Class to perform the update of the user information as a background task. If the data is not present or
	 * if the information is obsolete we launch a task to update and download the pilot information.
	 *
	 * @author Adam Antinoo
	 */
	//- CLASS IMPLEMENTATION ...................................................................................
	private class NeoComInitialization extends AsyncTask<Void, Void, Boolean> {
		// - F I E L D - S E C T I O N ............................................................................
		protected Activity _activity = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public NeoComInitialization(final Activity act) {
			_activity = act;
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		/**
		 * This is the main method where we do the application data initialization. The process has some steps
		 * that are listed below:
		 * <ol>
		 * <li>STEP 01. Check required files availability on app directory.</li>
		 * </ol>
		 */
		@Override
		protected Boolean doInBackground(final Void... entry) {
			logger.info(">> [NeoComInitialization.doInBackground]");

			// Just check that the files exist and if not load the Api list and their characters.
			logger.info(">> [NeoComInitialization.doStep01]");
			this.updateStateLabel("Checking application databases...");
			// STEP 01. Check required files availability on app directory.
			this.createAppDir();
			this.createCacheDirectories();
			logger.info("-- [NeoComInitialization.doStep01]> STEP 01. App Directories created");
			// STEP 02. Check existence of required files.
			if (!this.checkAppFile(NeoComAppConnector.getSingleton().getResourceString(R.string.ccpdatabasefilename))) {
				// TODO The CCP database is not distributed with the application. If not available then close the App
				this.copyFromAssets(R.string.ccpdatabasefilename);
			}
			logger.info("-- [NeoComInitialization.doStep01]> STEP 02. Required files on place");

			// Create the login information that later will be requested to the user at the login page.
			this.updateStateLabel("Processing Character Information...");
			NeoComAppConnector.getSingleton().getModelStore().accessLoginList();
			logger.info("<< [NeoComInitialization.doStep01]");

			// Start the timers for background processing.
			// TODO Point where to start the background services.
			// Do the start controlled by a settings toggle.
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_activity);
			boolean blockBackgroundTimer = sharedPrefs.getBoolean(AppWideConstants.preferenceKeys.prefkey_BlockBackgorundTimer.name(), false);
			if(!blockBackgroundTimer)NeoComApp.getSingletonApp().startTimer();
			logger.info("<< [NeoComInitialization.doInBackground]");
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean result) {
			// Check if we have pilots to show or we have to go to the AddAPI page.
			if (result) {
				startActivity(new Intent(_activity, LoginListActivity.class));
			}
		}

		private boolean checkAppFile(final String resourceString) {
			return NeoComAppConnector.getSingleton().getStorageConnector().checkStorageResource(
					NeoComAppConnector.getSingleton().getStorageConnector().accessAppStorage(null), resourceString);
		}

		/**
		 * Copies the parameter files from the assets installation directory to the user application directory.
		 * Current contents are the static database and the initial API list if found.
		 *
		 * @param resourceId
		 */
		private void copyFromAssets(final int resourceId) {
			InputStream istream = null;
			OutputStream ostream = null;
			String resourceName = NeoComAppConnector.getSingleton().getResourceString(resourceId);
			//		final Context context = singleton.getApplicationContext();
			try {
				istream = _activity.getAssets().open(resourceName);
				final File destination = NeoComAppConnector.getSingleton().getStorageConnector().accessAppStorage(resourceName);
				destination.createNewFile();
				ostream = new FileOutputStream(destination);
				final byte[] buffer = new byte[8192];
				int length;
				while ((length = istream.read(buffer)) > 0) {
					ostream.write(buffer, 0, length);
				}
				ostream.flush();
				logger.info("-- Copied resource from assets [" + resourceName + "]");
			} catch (final Exception e) {
				e.printStackTrace();
				logger.severe("E> Failed to copy resource: " + resourceName);
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

		/**
		 * Creates the application directory if it already does not exist on the sdcard.
		 */
		private boolean createAppDir() {
			// Create file application directory if it doesn't exist
			final File sdcarddir = new File(Environment.getExternalStorageDirectory(),
					NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
							/*+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix)*/);
			final boolean existence = sdcarddir.exists();
			if (!sdcarddir.exists()) {
				sdcarddir.mkdir();
			}
			return !existence;
		}

		private void createCacheDirectories() {
			// Create additional cache folders.
			File cachedir = NeoComAppConnector.getSingleton().getStorageConnector().getCacheStorage();
			cachedir.mkdir();
			File alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.drawablecachefoldername));
			alternatedir.mkdir();
			alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
			alternatedir.mkdir();
		}

		private void updateStateLabel(final String newLabel) {
//			SplashActivity.this.runOnUiThread(new Runnable() {
//				public void run() {
//					stateLabel.setText(newLabel);
//					stateLabel.invalidate();
//				}
//			});
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("NeoComSplashActivity");
	private static final int	MY_PERMISSIONS_REQUEST_READ_CONTACTS	= 1234;

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		logger.info(">> [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
	//	_actionBar.setDisplayHomeAsUpEnabled(false);
		// Hide the Actionbar because this activity should not show it.
		//		_actionBar.hide();
	//	hideBar();
		try {
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new SplashTasksFragment().setVariant(ENeoComVariants.SPLASH_SCREEN.name()), page++);
		} catch (Exception rtex) {
			logger.warning("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		logger.info("<< [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
	}
	protected void onStart() {
		logger.info(">> [SplashActivity.onStart]");
		super.onStart();
		//	stateLabel.setText("Instantiating user selected theme...");
//		stateLabel.setText("Checking application database...");
//		stateLabel.invalidate();
		//		String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
		//    requestPermissions(permissions,WRITE_EXTERNAL_STORAGE);
		if ( Build.VERSION.SDK_INT < 23) {
			new NeoComInitialization(this).execute();
		}
		if (Build.VERSION.SDK_INT >= 23) {
			String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
			if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				// Should we show an explanation?
				boolean ask = this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if (!ask) {
					this.requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
							MY_PERMISSIONS_REQUEST_READ_CONTACTS);
				}
			} else {
				new NeoComInitialization(this).execute();
			}
		}
		logger.info("<< [SplashActivity.onStart]");
	}
}
