//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity.core;

//- IMPORT SECTION .........................................................................................
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.LoginListActivity;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

// - CLASS IMPLEMENTATION ...................................................................................
public class SplashActivity extends Activity {
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
			SplashActivity.logger.info(">> [NeoComInitialization.doInBackground]");

			// Just check that the files exist and if not load the Api list and their characters.
			SplashActivity.logger.info(">> [NeoComInitialization.doStep01]");
			this.updateStateLabel("Checking application database...");
			// STEP 01. Check required files availability on app directory.
			this.createAppDir();
			this.createCacheDirectories();
			SplashActivity.logger.info("-- [NeoComInitialization.doStep01]> STEP 01. App Directories created");
			// STEP 02. Check existence of required files.
			if (!this.checkAppFile(NeoComAppConnector.getSingleton().getResourceString(R.string.ccpdatabasefilename))) {
				// TODO The CCP database is not distributed with the application. If not available then close the App
				this.copyFromAssets(R.string.ccpdatabasefilename);
			}
			//			if (AppWideConstants.DEVELOPMENT)
			//				if (!this.checkAppFile(AppConnector.getResourceString(R.string.apikeysfilename))) {
			//				this.copyFromAssets(R.string.apikeysfilename);
			//				}
			SplashActivity.logger.info("-- [NeoComInitialization.doStep01]> STEP 02. Required files on place");

			// Create the login information that later will be requested to the user at the login page.
			this.updateStateLabel("Loading user data...");
			AppConnector.getModelStore().accessLoginList();
			SplashActivity.logger.info("<< [NeoComInitialization.doStep01]");

			// Start the timers for background processing.
			NeoComApp.getSingletonApp().startTimer();
			SplashActivity.logger.info("<< [NeoComInitialization.doInBackground]");
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean result) {
			// Check if we have pilots to show or we have to go to the AddAPI page.
			if (result) {
				SplashActivity.this.startActivity(new Intent(_activity, LoginListActivity.class));
			}
		}

		private boolean checkAppFile(final String resourceString) {
			return AppConnector.getStorageConnector()
					.checkStorageResource(AppConnector.getStorageConnector().accessAppStorage(null), resourceString);
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
				final File destination = AppConnector.getStorageConnector().accessAppStorage(resourceName);
				destination.createNewFile();
				ostream = new FileOutputStream(destination);
				final byte[] buffer = new byte[8192];
				int length;
				while ((length = istream.read(buffer)) > 0) {
					ostream.write(buffer, 0, length);
				}
				ostream.flush();
				SplashActivity.logger.info("-- Copied resource from assets [" + resourceName + "]");
			} catch (final Exception e) {
				e.printStackTrace();
				SplashActivity.logger.severe("E> Failed to copy resource: " + resourceName);
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
							+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix));
			final boolean existence = sdcarddir.exists();
			if (!sdcarddir.exists()) {
				sdcarddir.mkdir();
			}
			return !existence;
		}

		private void createCacheDirectories() {
			// Create additional cache folders.
			File cachedir = AppConnector.getStorageConnector().getCacheStorage();
			cachedir.mkdir();
			File alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.drawablecachefoldername));
			alternatedir.mkdir();
			alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
			alternatedir.mkdir();
		}

		private void updateStateLabel(final String newLabel) {
			SplashActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					stateLabel.setText(newLabel);
					stateLabel.invalidate();
				}
			});
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger		logger			= Logger.getLogger("SplashActivity");

	// - F I E L D - S E C T I O N ............................................................................
	/** Shows the current initialization stage. */
	private final TextView	stateLabel	= null;

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This is almost the first android code to be executed. We will only check that core initialization was
	 * performed. We will prepare the splash screen and do any other thing that has to be done only a single
	 * time during the life of the application. Activities may get destroyed on memory so we have to save any
	 * permanent state.<br>
	 * Currently there are no permanent state that is not stored on the Static and User models.<br>
	 * The new code also adds the Action bar and start to play with it as a resource for better navigation.
	 * 
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		SplashActivity.logger.info(">> [SplashActivity.onCreate]");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_splash);
		//		stateLabel = (TextView) this.findViewById(R.id.stateLabel);
		//		stateLabel.setTypeface(NeoComApp.daysFace);
		SplashActivity.logger.info("<< [SplashActivity.onCreate]");
	}

	/**
	 * This code is performed anytime this activity is created so that means sometimes. The code has to be
	 * strong to detect things already done and do not try to repeat. Part of the action is coded inside
	 * background activities implemented as an AsyncTask or set of tasks that will serialize any call to those
	 * background activities.<br>
	 * The content is to open the display, start the waiting animation while the background tasks perform the
	 * download of the data and then finish. There is a wait link connected to the EVENT detection for the
	 * property changer so when the background task ends it will send an event that would allow interception or
	 * even the launch from the last task.<br>
	 * The splash activity will have a set of image buttons to enter the different functions of the application,
	 * mainly the addition of new keys that is a must to be on working state and the selection of a pilot to
	 * work with.
	 */
	@Override
	protected void onStart() {
		SplashActivity.logger.info(">> [SplashActivity.onStart]");
		super.onStart();
		//	stateLabel.setText("Instantiating user selected theme...");
		stateLabel.setText("Checking application database...");
		stateLabel.invalidate();
		new NeoComInitialization(this).execute();
		SplashActivity.logger.info("<< [SplashActivity.onStart]");
	}
}
//- UNUSED CODE ............................................................................................
