//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity.core;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tlabs.android.evanova.adapter.ApplicationCloudAdapter;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.NeoComSplashActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.enums.ERequestClass;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class ANeoComPagerActivity extends AbstractPagerActivity {
	/**
	 * Asynchronous task to perform actions out of main thread when a menu item is selected.
	 *
	 * @author Adam Antinoo
	 */
	//- CLASS IMPLEMENTATION ...................................................................................
	private class BackgroundMenuAction extends AsyncTask<Void, Void, Boolean> {
		//	private static final boolean	showProgress	= false;
		// - F I E L D - S E C T I O N ............................................................................
		@SuppressWarnings("unused")
		private Activity _activity = null;
		private int _action = 0;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public BackgroundMenuAction (final Activity act, final int action) {
			_activity = act;
			_action = action;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This is the main method where we do the application data initialization. The process has some steps
		 * that are listed below:
		 * <ol>
		 * <li>STEP 01. Check required files availability on app directory.</li>
		 * <li>STEP 02. Check existence of required files.</li>
		 * <li>STEP 03. Initialize the Model store and force a refresh from the api list file.</li>
		 * </ol>
		 */
		@Override
		protected Boolean doInBackground (final Void... entry) {
			switch (_action) {
				case R.id.action_updateaccounts:
					NeoComModelStore.getSingleton().initialize();
					break;
			}
			return true;
		}
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ANeoComPagerActivity () {
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Store a reference of the current activity at the Application Model Store. This reference is not being used but may
	 * help accessing some UI resources. Also can be used to stablish direct callback communications to activities from
	 * model instances.
	 */
	@Override
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Register this Activity as the current active Activity.
		NeoComModelStore.getSingleton().activateActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu (final Menu menu) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.neocom_menu, menu);
		NeoComModelStore.getSingleton().setAppMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_settings:
				final Intent intent = new Intent(this, SettingsActivity.class);
				this.startActivity(intent);
				return false;
			case R.id.action_fullreload:
				// Do a reload of the application data. First do a cleanup of the data structures to force a reload.
				// This is a hard reset of the application.
				NeoComModelStore.getSingleton().cleanModel();
				this.startActivity(new Intent(this, NeoComSplashActivity.class));
				return true;
			case R.id.action_downloadlocations:
				// Insert into the download queue the action to download the locations.
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.CITADELUPDATE);
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.OUTPOSTUPDATE);
				return true;
			case R.id.action_updateaccounts:
				// Clean the model store data and force a reload of new data from Database.
				// This is equivalent to a soft restart.
				ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(new Runnable() {
					@Override
					public void run () {
						NeoComModelStore.getSingleton().initialize();
					}
				});
				return true;
//				new BackgroundMenuAction(this, R.id.action_updateaccounts).execute();
//				break;
			//			case R.id.action_createFitting:
			//				// Create demo fittings to test the save/restore and continue the development.
			//				Fitting onConstructionFit = new Fitting();
			//				onConstructionFit.setName("Testing - Crusader");
			//				onConstructionFit.addHull(11184);
			//				onConstructionFit.fitModule(6719, 4);
			//				onConstructionFit.fitModule(5973);
			//				onConstructionFit.fitModule(5405);
			//				onConstructionFit.fitModule(5839);
			//				onConstructionFit.fitModule(5849);
			//				onConstructionFit.fitModule(11563);
			//				onConstructionFit.fitModule(33076);
			//				onConstructionFit.fitRig(26929);
			//				onConstructionFit.fitRig(26929);
			//				onConstructionFit.addCargo(244, 4);
			//				onConstructionFit.addCargo(240, 4);
			//				NeoComModelStore.getSingleton().addFitting(onConstructionFit, onConstructionFit.getName());
			//				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart () {
		super.onStart();
		NeoComApp.updateProgressSpinner();
	}
}

// - UNUSED CODE ............................................................................................
