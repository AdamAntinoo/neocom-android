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
import org.dimensinfin.eveonline.neocom.activity.core.ACredentialPublicActivity;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.MineralResourcesFragment;
import org.dimensinfin.eveonline.neocom.fragment.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This activity will display the list of Mining Extractions. On the NeoCom implementation we detect the incoming of new data by
 * quantity differentiation and we generate delta records to report the increase of mining ore during time and not only by date
 * aggregation.
 *
 * We need some input data to identify the Pilot or Corporation from where to get that mining records.
 *
 * Activities are the key data repository for the rest of the objects. If the extras do not convey the required information the
 * Activity should stop and display the appropiate message for the user. All checks should be made at this level and during the
 * complete lifecycle. Once control goes to Fragments or DataSources we understand that all checks are validated and that the
 * required data is available.
 *
 * P A R A M E T E R S
 * This Activity requires the <b>CAPSULEER_IDENTIFIER</b> to be able to extract the right credential from the current list of
 * Credentials. If this parameter is not found or the matching credential is not available we should cancel the normal display of
 * the page and go to some Exception reporting panel.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerActivity extends ACredentialPublicActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()));
			this.addPage(new MineralResourcesFragment().setCredential(credential));
			this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()));
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("MiningLedgerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [MiningLedgerActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
