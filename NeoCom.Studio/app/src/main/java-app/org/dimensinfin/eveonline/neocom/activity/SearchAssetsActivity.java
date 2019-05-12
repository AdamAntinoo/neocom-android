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

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.activity.core.APublicCredentialActivity;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.AssetsFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;

/**
 * The search Activity can accept intents from the application of from the Android core. We then implement the code at the
 * <code>handleIntent</code> method to allow reception of intents from creation of from moving the Activity to the front when it is
 * already crated.
 *
 * P A R A M E T E R S
 * This Activity requires the <b>CAPSULEER_IDENTIFIER</b> to be able to locate the current active pilot's assets.
 * @author Adam Antinoo
 */
//- CLASS IMPLEMENTATION ...................................................................................
public class SearchAssetsActivity extends APublicCredentialActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent( Intent intent ) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent( Intent intent ) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			createFilterFragment(query);
		}
	}

	protected void createFilterFragment( final String filter ) {
		logger.info(">> [SearchAssetsActivity.onCreate]");
		try {
			this.addPage(new AssetsFragment().setVariant(NeoComApplication.EVariants.ASSETS_SEARCH.name()));
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("SearchAssetsActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [SearchAssetsActivity.onCreate]");
	}
}
//- UNUSED CODE ............................................................................................
