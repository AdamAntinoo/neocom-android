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
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.MineralResourcesFragment;
import org.dimensinfin.eveonline.neocom.fragment.MiningLedgerFragment;

/**
 * This activity will display the list of Mining Extractions. On the NeoCom implementation we detect the incoming of new data by quantity
 * differentiation and we generate delta records to report the increase of mining ore during time and not only by date aggregation.
 *
 * We need some input data to identify the Pilot or Corporation from where to get that mining records.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private int pilotIdentifier = 0;

	// - M E T H O D - S E C T I O N ..........................................................................

	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			if ( null != _extras ) {
				pilotIdentifier = _extras.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), 0);
			}
			if ( pilotIdentifier > 0 ) {
				this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()));
				this.addPage(new MiningLedgerFragment().setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()));
				this.addPage(new MineralResourcesFragment().setPilotIdentifier(pilotIdentifier));
			}
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("MiningLedgerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
		logger.info("<< [MiningLedgerActivity.onCreate]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
