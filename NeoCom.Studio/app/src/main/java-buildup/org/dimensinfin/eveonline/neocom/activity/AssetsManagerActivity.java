//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
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
package org.dimensinfin.eveonline.neocom.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.ACredentialPublicActivity;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.AssetsFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetsManagerOwner;
import org.dimensinfin.eveonline.neocom.managers.AssetsManager;

/**
 * This activity collects tha pages related to the presentation and aggregation og pilot's assets. The different pages show the same
 * asset record but filtered and presented in different aggregations.
 * The Activity relays the data access to a Manager so the data can be reused by different pages and does not need to be reloaded
 * every time se swipe to another page.
 *
 * P A R A M E T E R S
 * This Activity requires the <b>CAPSULEER_IDENTIFIER</b> to be able to locate the current active pilot's assets.
 * @author Adam Antinoo
 */
//- CLASS IMPLEMENTATION ...................................................................................
public class AssetsManagerActivity extends ACredentialPublicActivity implements IAssetsManagerOwner {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private AssetsManager assetsManager = null;

	// - M E T H O D - S E C T I O N ..........................................................................
//	public boolean onCreateOptionsMenu( Menu menu ) {
//		final MenuInflater inflater = this.getMenuInflater();
//		inflater.inflate(R.menu.neocom_menu, menu);
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//		return super.onCreateOptionsMenu(menu);
//	}

	// --- I A S S E T S M A N A G E R O W N E R   I N T E R F A C E
	public AssetsManager accessAssetsManager() {
		return this.assetsManager;
	}

	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [AssetsManagerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		// Initialization performed on superclass. We just can be centered on the creation of the fragments.
		try {
			// Initialize this Activity assets manager to get access to all the data.
			this.assetsManager = new AssetsManager(this.credential);
			this.addPage(new AssetsFragment().setVariant(NeoComApplication.EVariants.ASSETS_BYLOCATION.name()));
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("AssetsManagerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [AssetsManagerActivity.onCreate]");
	}
}
//- UNUSED CODE ............................................................................................
