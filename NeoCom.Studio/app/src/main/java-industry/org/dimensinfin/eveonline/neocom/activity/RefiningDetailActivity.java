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

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.activity.ui.ACustomActionBarActivity;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.MineralResourcesFragment;
import org.dimensinfin.eveonline.neocom.fragment.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.fragment.RefiningDetailFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailActivity extends ACustomActionBarActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("RefiningDetailActivity");

	// - F I E L D - S E C T I O N ............................................................................
	/** The type identifier of the resource to refine. */
	private int resourceTypeId = 0;
	/**
	 * If available the location identifier of the refining place. I should search on the locations database and get the refining rate I
	 * can achieve from that place.
	 */
	private long refiningLocationId = -1;

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			// Get the pilot identifier. This is a mandatory parameter and should be validated.
			if (null != extras) {
				resourceTypeId = extras.getInt(NeoComApplication.EExtras.REFINETYPE_IDENTIFIER.name(), 1230);
				if (resourceTypeId > 0) {
						// Configure the new custom Action Bar
//						setupActionBar();

						this.addPage(new RefiningDetailFragment().setResourceTypeId(resourceTypeId)
								.setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()));
				}
			} else {
				AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
						, new NeoComException("[MiningLedgerActivity.onCreate]> 'onCreate' event did not received a valid set of 'extras'.")
						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
				this.addPage(new ApplicationExceptionReportFragment());
			}
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("MiningLedgerActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [MiningLedgerActivity.onCreate]");
	}

	@Override
	protected void onRestoreInstanceState( final Bundle savedInstanceState ) {
		resourceTypeId = savedInstanceState.getInt(NeoComApplication.EExtras.REFINETYPE_IDENTIFIER.name());
		refiningLocationId = savedInstanceState.getLong(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState( final Bundle outState ) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(NeoComApplication.EExtras.REFINETYPE_IDENTIFIER.name(), resourceTypeId);
		outState.putLong(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name(), refiningLocationId);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
