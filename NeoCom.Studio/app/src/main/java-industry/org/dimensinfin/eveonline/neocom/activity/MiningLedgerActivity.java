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

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.ui.ACustomActionBarActivity;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.fragment.MineralResourcesFragment;
import org.dimensinfin.eveonline.neocom.fragment.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.dimensinfin.eveonline.neocom.ui.NeoComActionBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This activity will display the list of Mining Extractions. On the NeoCom implementation we detect the incoming of new data by quantity
 * differentiation and we generate delta records to report the increase of mining ore during time and not only by date aggregation.
 * <p>
 * We need some input data to identify the Pilot or Corporation from where to get that mining records.
 * <p>
 * Activities are the key data repository for the rest of the objects. If the extras do not convey the required information the Activity
 * should stop and display the appropiate message for the user. All checks should be made at this level and during the complete lifecycle
 * . Once control goes to Fragments or DataSources we understand that all checks are validated and that the required data is available.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerActivity extends ACustomActionBarActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerActivity");

	// - F I E L D - S E C T I O N ............................................................................
	/** When the user selects a Credential we pass to the target Activity the pilot identifier. */
	private int pilotIdentifier = 0;
	/** Recovered credential extracted from the current list of Credentials using the pilot identifier as a key. */
	private Credential credential = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			// Get the pilot identifier. This is a mandatory parameter and should be validated.
			if (null != extras) {
				pilotIdentifier = extras.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), 0);
				if (pilotIdentifier > 0) {
					// Validate the pointer Credential that should be made available to this Activity fragments.
					credential = searchCredential(pilotIdentifier);
					if (null != credential) {
						// Configure the new custom Action Bar
						setupActionBar(credential);

						this.addPage(new MiningLedgerFragment().setCredential(credential)
								.setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_TODAY.name()));
						this.addPage(new MiningLedgerFragment().setCredential(credential)
								.setVariant(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()));
						this.addPage(new MineralResourcesFragment().setCredential(credential));
					} else {
						AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
								, new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
										+ " not fount on the list of credentials.")
								, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
						this.addPage(new ApplicationExceptionReportFragment());
					}
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
		pilotIdentifier = savedInstanceState.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
		credential = searchCredential(pilotIdentifier);
		if (null == credential) {
			AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
					, new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
							+ " not fount on the list of credentials.")
					, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			this.addPage(new ApplicationExceptionReportFragment());
		}
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
		outState.putInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), pilotIdentifier);
	}

	protected Credential searchCredential( final int identifier ) {
		for (Credential cred : AndroidGlobalDataManager.accessAllCredentials()) {
			if (cred.getAccountId() == identifier) return cred;
		}
		return null;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
