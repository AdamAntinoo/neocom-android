//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.PlanetaryFragment;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String	loginid			= null;
	private long		characterid	= -1;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getName() {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_PlanetaryManagerActivity);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		PlanetaryManagerActivity.logger.info(">> [PlanetaryManagerActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
//		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			Bundle extras = null;
			if (null != savedInstanceState) {
				extras = savedInstanceState;
				loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			} else {
				extras = this.getIntent().getExtras();
				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			}
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new PlanetaryFragment().setVariant(ENeoComVariants.PLANETARY_BYLOCATION.name()).setExtras(extras),
					page++);
			//			this.addPage(new AssetsFragment().setVariant(EAssetVariants.ASSETS_MATERIALS.name()).setExtras(extras), page++);
		} catch (Exception rtex) {
			PlanetaryManagerActivity.logger.warning("RTEX> [PlanetaryManagerActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX> [PlanetaryManagerActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		PlanetaryManagerActivity.logger.info("<< [PlanetaryManagerActivity.onCreate]"); //$NON-NLS-1$
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
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
	}
}
//- UNUSED CODE ............................................................................................
