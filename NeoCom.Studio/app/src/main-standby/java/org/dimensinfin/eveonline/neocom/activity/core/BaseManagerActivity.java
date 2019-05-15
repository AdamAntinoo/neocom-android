//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//                 the game data to help capsuleers organize and prioritize activities. The strong points are
//                 help at the Industry level tracking and calculating costs and benefits. Also the market
//                 information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//                 from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//                 designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//                 rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;

//- CLASS IMPLEMENTATION ...................................................................................
public abstract class BaseManagerActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	//	private String loginid = null;
	private int characterid = -1;
	protected Bundle extras = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	//	public abstract String getName ();

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		logger.info(">> [BaseManagerActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			if ( null != savedInstanceState ) {
				extras = savedInstanceState;
				characterid = savedInstanceState.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			} else {
				extras = this.getIntent().getExtras();
				characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			}
		} catch (RuntimeException rtex) {
			logger.warning("RTEX [BaseManagerActivity.onCreate]> " + rtex.getMessage());
			GlobalAndroidDataManager.registerRuntimeException("BaseManagerActivity.onCreate"
					, rtex, GlobalDataManagerExceptions.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			//			this.stopActivity(new RuntimeException("RTEX [BaseManagerActivity.onCreate]> " + rtex.getMessage()));
		}
		logger.info("<< [BaseManagerActivity.onCreate]"); //$NON-NLS-1$
	}

	@Override
	protected void onRestoreInstanceState (final Bundle savedInstanceState) {
		characterid = savedInstanceState.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
	}
}
//- UNUSED CODE ............................................................................................
