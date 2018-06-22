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
package org.dimensinfin.eveonline.neocom.activity.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.services.UpdateJobManager;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class APublicMenuActivity extends APublicActionBarActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Menu activityMenu = null;

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public boolean onCreateOptionsMenu( final Menu menu ) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.neocom_menu, menu);
		activityMenu = menu;

		getLayoutInflater().setFactory(new LayoutInflater.Factory() {
			@Override
			public View onCreateView( String name, Context context,
			                          AttributeSet attrs ) {

				if (name.startsWith("com.android.internal.view.menu2")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);

						new Handler().post(new Runnable() {
							public void run() {

								// set the background drawable
								view.setBackgroundResource(R.drawable.blacktraslucent80);

								// set the text color
								//								((TextView) view).setTextColor(Color.WHITE);
								//								final Typeface typefaceName = Typeface.createFromAsset(AndroidGlobalDataManager.getAppContext().getAssets()
								//										, "fonts/Proxima Nova Bold.ttf");
								//								((TextView) view).setTypeface(typefaceName);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				if (name.startsWith("com.android.internal.view.menu.ListMenuItemView")) {
					try {
						// Create the view and replace the desired attributes before releasing.
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(() -> {
							// Set the background drawable
							view.setBackgroundResource(R.drawable.blacktraslucent80);
//							@SuppressLint("RestrictedApi") final MenuItemImpl itemData = view.getItemData();
//							@SuppressLint("RestrictedApi") final View action = itemData.getActionView();

							// set the text color
							//								((TextView) view).setTextColor(Color.WHITE);
							//								final Typeface typefaceName = Typeface.createFromAsset(AndroidGlobalDataManager.getAppContext().getAssets()
							//										, "fonts/Proxima Nova Bold.ttf");
							//								((TextView) view).setTypeface(typefaceName);
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected( final MenuItem item ) {
		switch (item.getItemId()) {
			//			case android.R.id.home:
			//				// This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users
			//				// to navigate up one level in the application structure. For more details, see the Navigation pattern on Android Design:
			//				//
			//				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//				NavUtils.navigateUpFromSameTask(this);
			//				return true;
			case R.id.action_settings:
				final Intent intent = new Intent(this, SettingsActivity.class);
				this.startActivity(intent);
				return false;
			//			case R.id.action_fullreload:
			//				// Do a reload of the application data. First do a cleanup of the data structures to force a reload.
			//				// This is a hard reset of the application.
			//				NeoComModelStore.getSingleton().cleanModel();
			//				this.startActivity(new Intent(this, NeoComSplashActivity.class));
			//				return true;
			//			case R.id.action_downloadlocations:
			//				// Insert into the download queue the action to download the locations.
			//				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.CITADELUPDATE);
			//				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.OUTPOSTUPDATE);
			//				return true;
			//			case R.id.action_updateaccounts:
			//				// Clean the model store data and force a reload of new data from Database.
			//				// This is equivalent to a soft restart.
			//				ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(new Runnable() {
			//					@Override
			//					public void run () {
			//						NeoComModelStore.getSingleton().initialize();
			//					}
			//				});
			//				return true;
			//			//				new BackgroundMenuAction(this, R.id.action_updateaccounts).execute();
			//			//				break;
			//			case R.id.action_showactivejobs:
			//				this.startActivity(new Intent(this, ActiveJobsActivity.class));
			//				return true;
			//			//							// Create demo fittings to test the save/restore and continue the development.
			//			//							Fitting onConstructionFit = new Fitting();
			//			//							onConstructionFit.setName("Testing - Crusader");
			//			//							onConstructionFit.addHull(11184);
			//			//							onConstructionFit.fitModule(6719, 4);
			//			//							onConstructionFit.fitModule(5973);
			//			//							onConstructionFit.fitModule(5405);
			//			//							onConstructionFit.fitModule(5839);
			//			//							onConstructionFit.fitModule(5849);
			//			//							onConstructionFit.fitModule(11563);
			//			//							onConstructionFit.fitModule(33076);
			//			//							onConstructionFit.fitRig(26929);
			//			//							onConstructionFit.fitRig(26929);
			//			//							onConstructionFit.addCargo(244, 4);
			//			//							onConstructionFit.addCargo(240, 4);
			//			//							NeoComModelStore.getSingleton().addFitting(onConstructionFit, onConstructionFit.getName());
			//			//							return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Gets the menu context and updates the background progress indicator with the counters.
	 * @param countIndicator
	 */
	protected void startProgress( final double countIndicator ) {
		// Activate menu icon of progress.
		//		final Menu menu = NeoComModelStore.getSingleton().getAppMenu();
		if (null != activityMenu) {
			final MenuItem updatingItem = activityMenu.findItem(R.id.action_launchUpdate);
			final LayoutInflater mInflater = (LayoutInflater) AndroidGlobalDataManager.getAppContext()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
			if (null != updatingItem) {
				updatingItem.setVisible(true);
				updatingItem.setActionView(actionView);
			}
			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
			if (null != counter) {
				counter.setVisibility(View.VISIBLE);
				// REFACTOR This should be configured on the XML and not on the
				// code. If the progress is set on a library
				// then it should be styled.
				//				counter.setTypeface(NeoComApp.daysFace);
				counter.setText(backgroundCountersData());
			}
			actionView.invalidate();
		}
	}

	//	/**
	//	 * Hides the background progress indicator.
	//	 */
	//	private void stopProgress() {
	//		// Clear the update progress.
	//		final Menu menu = NeoComModelStore.getSingleton().getAppMenu();
	//		if ( null != menu ) {
	//			final MenuItem updatingItem = menu.findItem(R.id.action_launchUpdate);
	//			final LayoutInflater mInflater = (LayoutInflater) NeoComModelStore.getSingleton().getActivity()
	//					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	//			final View actionView = mInflater.inflate(R.layout.actionbar_indeterminateprogress, null);
	//			if ( null != updatingItem ) {
	//				updatingItem.setActionView(null);
	//				updatingItem.setVisible(false);
	//			}
	//			final TextView counter = (TextView) actionView.findViewById(R.id.progressCounter);
	//			if ( null != counter ) {
	//				counter.setVisibility(View.GONE);
	//			}
	//			actionView.invalidate();
	//		}
	//	}

	private String backgroundCountersData() {
		int marketJobs = AndroidGlobalDataManager.currentMarketDataManager().reportMarketDataJobs();
		int backgroundJobs = UpdateJobManager.getPendingJobsCount();
		return backgroundJobs + "." + marketJobs;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
