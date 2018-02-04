//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.BaseManagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.AssetsFragment;

//- CLASS IMPLEMENTATION ...................................................................................
public class SearchAssetsActivity extends BaseManagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getName () {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_title_AssetsManagerActivity);
	}


	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent (Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent (Intent intent) {
		if ( Intent.ACTION_SEARCH.equals(intent.getAction()) ) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			createFilterFragment(query);
		}
	}

	protected void createFilterFragment (final String filter) {
		logger.info(">> [AssetsManagerActivity.onCreate]"); //$NON-NLS-1$
		//		super.onCreate(savedInstanceState);
		try {
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			extras.putInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), 92223647);
			extras.putString(AppWideConstants.EExtras.EXTRA_ASSETFILTER.name(), filter);
			this.addPage(new AssetsFragment().setVariant(ENeoComVariants.ASSETS_SEARCH.name()).setExtras(extras), page++);
			//			this.addPage(new AssetsFragment().setVariant(ENeoComVariants.ASSETS_BYCATEGORY.name()).setExtras(extras),
			// page++);
		} catch (RuntimeException rtex) {
			SearchAssetsActivity.logger.warning("RTEX [AssetsManagerActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [AssetsManagerActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		//		this.updateInitialTitle();
		logger.info("<< [AssetsManagerActivity.onCreate]"); //$NON-NLS-1$
	}
}
//- UNUSED CODE ............................................................................................
