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

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.BaseManagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.AssetsFragment;

//- CLASS IMPLEMENTATION ...................................................................................
public class AssetsManagerActivity extends BaseManagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public boolean onCreateOptionsMenu (Menu menu) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.neocom_menu, menu);
		//		NeoComModelStore.getSingleton().setAppMenu(menu);

		// Inflate the menu; this adds items to the action bar if it is present.
		//		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		AssetsManagerActivity.logger.info(">> [AssetsManagerActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// We have the extras already loaded. Add the pilot id to simulate we have been called.
			_extras.putInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), 92223647);
			// TODO - Check that the correct extras arrive to the Fragment.
			this.addPage(new AssetsFragment().setVariant(ENeoComVariants.ASSETS_BYLOCATION.name()));
		} catch (RuntimeException rtex) {
//			AssetsManagerActivity.logger.warning("RTEX [AssetsManagerActivity.onCreate]> " + rtex.getMessage());
			GlobalAndroidDataManager.registerRuntimeException("AssetsManagerActivity.onCreate"
					, rtex, GlobalDataManagerExceptions.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			//			this.stopActivity(new RuntimeException("RTEX [AssetsManagerActivity.onCreate]> " + rtex.getMessage()));
		}
		AssetsManagerActivity.logger.info("<< [AssetsManagerActivity.onCreate]"); //$NON-NLS-1$
	}
}
//- UNUSED CODE ............................................................................................
