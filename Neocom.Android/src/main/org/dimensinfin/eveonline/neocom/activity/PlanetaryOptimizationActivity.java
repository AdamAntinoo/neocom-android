//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.PlanetaryOptimizationFragment;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import android.os.Bundle;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryOptimizationActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String	loginid			= null;
	private long		characterid	= -1;
	private long		locationid	= -2;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getName() {
		return "Assets Manager";
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		PlanetaryOptimizationActivity.logger.info(">> [PlanetaryOptimizationActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			Bundle extras = null;
			if (null != savedInstanceState) {
				extras = savedInstanceState;
				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
				locationid = extras.getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());
			} else {
				extras = this.getIntent().getExtras();
				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
				locationid = extras.getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());
			}
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new PlanetaryOptimizationFragment().setVariant(ENeoComVariants.PLANETARY_OPTIMIZATION.name())
					.setExtras(extras), page++);
			//			this.addPage(new AssetsFragment().setVariant(EAssetVariants.ASSETS_MATERIALS.name()).setExtras(extras), page++);
		} catch (Exception rtex) {
			PlanetaryOptimizationActivity.logger
					.warning("RTEX> [PlanetaryOptimizationActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX> [PlanetaryOptimizationActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		PlanetaryOptimizationActivity.logger.info("<< [PlanetaryOptimizationActivity.onCreate]"); //$NON-NLS-1$
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		locationid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 *
	 */
	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), loginid);
		outState.putLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
		outState.putLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name(), locationid);
	}
}
//- UNUSED CODE ............................................................................................
