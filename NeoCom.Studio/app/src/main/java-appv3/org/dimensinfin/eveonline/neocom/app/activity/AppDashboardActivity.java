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
package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.ALinearActivity;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.AppDashboardFragment;
import org.dimensinfin.eveonline.neocom.ui.NeoComActionBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * This Activity has an special layout. Instead showing the different Fragments into adjacent pages that can de changed by sweeping
 * them, this special page should put the fragments one below the previous one composing a list of panels. Initially this code belongs
 * to the NeoCom project but is the code can be made generic it should add to the functionality found on the MVC.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AppDashboardActivity extends ALinearActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public void addLinear( AbstractPagerFragment newFrag ) {
		logger.info(">> [AppDashboardActivity.addLinear]");
		// Before checking if we have already this fragment we should get its unique identifier.
		Fragment frag = this.getSupportFragmentManager().findFragmentByTag(_linearAdapter.getFragmentId(_linearAdapter.getNextPosition()));
		if (null == frag) {
			_linearAdapter.addLinear(newFrag);
			// Now add the configured fragment to the activity container layout.
			// Begin the transaction
			logger.info("-- [AppDashboardActivity.addLinear]> Fragment: {}", newFrag.getClass().getSimpleName());
			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
			// Add the fragment to the container with the new fragment.
			ft.add(R.id.fragmentContainer, newFrag);
			ft.commit();
		} else {
			if (frag instanceof AbstractPagerFragment) {
				// Reuse a previous created Fragment. But watch some of the fields have been removed.
				((AbstractPagerFragment) frag).setVariant(newFrag.getVariant());
				newFrag = (AbstractPagerFragment) frag;
			} else
				throw new RuntimeException("RTEX [AppDashboardActivity.addLinear]> The fragment located does not inherit the required functionality. Does not extend AbstractPagerFragment.");
		}
		// Be sure the Fragment activity points to a valid activity.
		newFrag.setAppContext(this);
		// Copy the Activity extras to the Fragment. This avoids forgetting to set this by the developer.
		newFrag.setExtras(extras);
		logger.info("<< [AppDashboardActivity.addLinear]");
	}

	// --- A C T I V I T Y   L I F E C Y C L E

	/**
	 * Just create the fragment page. There are no parameters not customization up to this moment. This is the simplest initialiation
	 * code for an Activity and the only code to integrate it into the MVC.
	 *
	 * This Activity planetaryFacility is quite simple. It just contains a linear vertical layout that should contain the declared fragments.
	 * Fragments are added in order and usually should not change. In that case we should add an external storage planetaryFacility to store the
	 * list of fragments and use it to recreate the layout contents on any content change event.
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [AppDashboardActivity.onCreate]");
		super.onCreate(savedInstanceState);

		if (null != this.getActionBar()) {
			// Create and configure the Custom Action Bar for this activity. It will show the application version and the server status
			// Activate the custom ActionBar
			this.getActionBar().show();
			this.getActionBar().setDisplayHomeAsUpEnabled(false);
			this.getActionBar().setDisplayShowCustomEnabled(true);
			this.getActionBar().setDisplayShowTitleEnabled(false);

			// Create a new Action Bar view from the layout.
			NeoComActionBar neocomActionBarView = new NeoComActionBar(R.layout.actionbar_neocombar)
					.setTitleLeft(AndroidGlobalDataManager.getResourceString(R.string.appname))
					.setTitleRight(AndroidGlobalDataManager.getResourceString(R.string.appversionname));
			this.getActionBar().setCustomView(neocomActionBarView.getView());
		}
		// Cleanup previous fragments from the view container if any.
		_fragmentContainer.removeAllViews();

		// Create and add the fragment to the layout container.
		this.addLinear(new AppDashboardFragment());
		//			this.addLinear(new EveOnlineNotificationsFragment());
//		this.addLinear(new CredentialListFragment());
		logger.info("<< [AppDashboardActivity.onCreate]");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Server status has network access so it has to be executed on a background thread.
		AndroidGlobalDataManager.getSingleton().submitJob(() -> {
			neocomActionBarView.setSubtitleSpanned(serverStatus4Actionbar());
			neocomActionBarView.getView().invalidate();
		});
		startProgress(0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//		// Remove fragments from the manager.
		//		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		//		// Remove the fragment for cleanup.
		//		if (null != _linearAdapter)
		//			for (Fragment frag : _linearAdapter.getFragments())
		//				if (null != frag) ft.remove(frag);
		//		ft.commit();

		logger.info(">< [AppDashboardActivity.onDestroy]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
