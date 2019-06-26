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
package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.APublicCredentialActivity;
//import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.fragment.RefiningDetailFragment;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailActivity extends APublicCredentialActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("RefiningDetailActivity");

	// - F I E L D - S E C T I O N ............................................................................
	/** The type identifier of the resource to refine. */
	private int resourceTypeId = 0;
	/**
	 * If available the location identifier of the refining place. I should search on the locations database and get the refining rate I
	 * can achieve from that place.
	 */
//	private long refiningLocationId = -1;

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [MiningLedgerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			// Get the pilot identifier. This is a mandatory parameter and should be validated.
			if (null == extras)
				throw new NeoComException("[MiningLedgerActivity.onCreate]> 'onCreate' event did not received a valid set of 'extras'.");
			resourceTypeId = extras.getInt(EExtras.REFINETYPE_IDENTIFIER.name(), 1230);
//			refiningLocationId=extras.getInt(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name(),34);

			// Validate the reception of mandatory valid parameters.
			if (resourceTypeId <= 1)
				throw new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
						+ " not fount on the list of credentials.");
//			if (pilotIdentifier <= 1)
//				throw new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
//						+ " not fount on the list of credentials.");

			// TODO - The credential was already validated. So the pilot identifier.
//			// Validate the pointer Credential that should be made available to this Activity fragments.
//			credential = searchCredential(pilotIdentifier);
//			if (null == credential)
//				throw new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
//						+ " not fount on the list of credentials.");

			// Configure the new custom Action Bar
			setupActionBar(credential);

			this.addPage(new RefiningDetailFragment()
					.setResourceTypeId(resourceTypeId)
					.setVariant(PageNamesType.MINING_EXTRACTIONS_TODAY.name()));
			//			} else {
			//				AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
			//						, new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
			//								+ " not fount on the list of credentials.")
			//						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			//				this.addPage(new ApplicationExceptionReportFragment());
			//			}
			//				}
			//			}
			//			}
			//			else {
			//				AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
			//						, new NeoComException("[MiningLedgerActivity.onCreate]> 'onCreate' event did not received a valid set of 'extras'.")
			//						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			//				this.addPage(new ApplicationExceptionReportFragment());
			//			}
		} catch (NeoComException neoe) {
			AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
					, neoe, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			neoe.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
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
		resourceTypeId = savedInstanceState.getInt(EExtras.REFINETYPE_IDENTIFIER.name());
//		refiningLocationId = savedInstanceState.getLong(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name());
		//		pilotIdentifier = savedInstanceState.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
		//		credential = searchCredential(pilotIdentifier);
		//		if (null == credential) {
		//			AndroidGlobalDataManager.registerException("MiningLedgerActivity.onCreate"
		//					, new NeoComException("[MiningLedgerActivity.onCreate]> Credential id " + pilotIdentifier
		//							+ " not fount on the list of credentials.")
		//					, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
		//			this.addPage(new ApplicationExceptionReportFragment());
		//		}
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
		//		outState.putInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), pilotIdentifier);
		outState.putInt(EExtras.REFINETYPE_IDENTIFIER.name(), resourceTypeId);
//		outState.putLong(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name(), refiningLocationId);
	}

	//	protected Credential searchCredential( final int identifier ) {
	//		for (Credential cred : AndroidGlobalDataManager.accessAllCredentials()) {
	//			if (cred.getAccountId() == identifier) return cred;
	//		}
	//		return null;
	//	}
}
// - UNUSED CODE ............................................................................................
//[01]
