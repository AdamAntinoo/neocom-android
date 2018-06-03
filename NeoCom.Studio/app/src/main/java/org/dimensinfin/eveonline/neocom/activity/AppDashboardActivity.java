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
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerExceptions;
import org.dimensinfin.eveonline.neocom.fragment.AppDashboardFragment;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AppDashboardActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AppDashboardActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E

	/**
	 * Just create the fragment page. There are no parameters not customization up to this moment. This is the simplest initialiation code
	 * for an Activity and the only code to integrate it into the MVC.
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [AppDashboardActivity.onCreate]");
		super.onCreate(savedInstanceState);
		// Create the fragment page. This is a single page activity with to input parameters.
		try {
			// Remove the go home for this initial Activity.
			getActionBar().setDisplayHomeAsUpEnabled(false);

			this.addPage(new AppDashboardFragment());
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("AppDashboardActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
		logger.info("<< [AppDashboardActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
