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

import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.APublicActionBarActivity;
import org.dimensinfin.eveonline.neocom.activity.core.APublicCredentialActivity;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.dimensinfin.eveonline.neocom.ui.NeoComActionBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotDashboardActivity extends APublicCredentialActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("PilotDashboardActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable Bundle savedInstanceState ) {
		logger.info(">> [PilotDashboardActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			// Get the pilot identifier. This is a mandatory parameter and should be validated.
			if (null != extras) {
				pilotIdentifier = extras.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), 0);
				if (pilotIdentifier > 0) {
					// Validate the pointer Credential that should be made available to this Activity fragments.
					credential = searchCredential(pilotIdentifier);
					if (null != credential) {
						// Configure the new custom Action Bar as a dashboard action bar.
						if (null != this.getActionBar()) {
							// Create and configure the Custom Action Bar for this activity. It will show the application version and the server status
							// Activate the custom ActionBar
							this.getActionBar().show();
							this.getActionBar().setDisplayHomeAsUpEnabled(true);
							this.getActionBar().setDisplayShowCustomEnabled(true);
							this.getActionBar().setDisplayShowTitleEnabled(false);

							// Create a new Action Bar view from the layout.
							NeoComActionBar neocomActionBarView = new NeoComActionBar(R.layout.actionbar_neocombar)
									.setTitleLeft(AndroidGlobalDataManager.getResourceString(R.string.appname))
									.setTitleRight(AndroidGlobalDataManager.getResourceString(R.string.appversionname));
							// Server status has network access so it has to be executed on a background thread.
							AndroidGlobalDataManager.submitJob2ui(() -> {
								neocomActionBarView.setSubtitleSpanned(APublicActionBarActivity.serverStatus4Actionbar());
							});
							this.getActionBar().setCustomView(neocomActionBarView.getView());
						}

						this.addPage(new PilotDashboardFragment());
					} else {
						AndroidGlobalDataManager.registerException("PilotDashboardActivity.onCreate"
								, new NeoComException("[PilotDashboardActivity.onCreate]> Credential id " + pilotIdentifier
										+ " not fount on the list of credentials.")
								, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
						this.addPage(new ApplicationExceptionReportFragment());
					}
				}
			}
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("PilotDashboardActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [PilotDashboardActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
