package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dimensinfin.android.mvc.activity.AMVCMultiPageActivity;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.CustomTypefaceSpan;

public class ANeoComActivity extends AMVCMultiPageActivity {
	private static Menu activityMenu = null;
	private View neocomActionBar;

	@Override
	protected void onStart() {
		super.onStart();
		this.validatePermissions();
	}

	@Override
	public boolean onCreateOptionsMenu( final Menu menu ) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.neocom_menu, menu);
		activityMenu = menu;

		// Change the style for the application menu. This changes the text font and the background.
		// - B A C K G R O U N D - By intercepting the creation of the manu view.
		getLayoutInflater().setFactory(( String name, Context context, AttributeSet attrs ) -> {
			if (name.startsWith("com.android.internal.view.menu.ListMenuItemView")) {
				try {
					// Create the view and replace the desired attributes before releasing.
					final View view = getLayoutInflater().createView(name, null, attrs);
					new Handler().post(() -> {
						// Set the background drawable
						view.setBackgroundResource(R.drawable.blacktraslucent80);
					});
					return view;
				} catch (InflateException ie) {
					ie.printStackTrace();
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
				}
			}
			return null;
		});
		// - F O N T - By replacing this property on each menu item found.
		for (int i = 0; i < menu.size(); i++) {
			MenuItem mi = menu.getItem(i);

			//for applying a font to subMenu ...
			SubMenu subMenu = mi.getSubMenu();
			if (subMenu != null && subMenu.size() > 0) {
				for (int j = 0; j < subMenu.size(); j++) {
					MenuItem subMenuItem = subMenu.getItem(j);
					applyFontToMenuItem(subMenuItem);
				}
			}
			applyFontToMenuItem(mi);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected( final MenuItem item ) {
		switch (item.getItemId()) {
			//			case android.R.id.home:
			//				// This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users
			//				// to navigate up one level in the application planetaryFacility. For more details, see the Navigation pattern on Android Design:
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

	protected void applyFontToMenuItem( MenuItem mi ) {
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Proxima Nova Thin.ttf");
		SpannableString mNewTitle = new SpannableString(mi.getTitle());
		mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mi.setTitle(mNewTitle);
	}

	protected void validatePermissions() {
		//		if (BuildConfig.DEBUG) return;
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					10);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.INTERNET},
					20);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
					30);
		}
	}
}
