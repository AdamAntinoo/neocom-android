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
package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.fragment.core.ApplicationExceptionReportFragment;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.interfaces.IPilotActivity;
import org.dimensinfin.eveonline.neocom.model.PilotV2;

/**
 * This special class contains the common code for all the Activities that have access to the Credential. The code will store the
 * needed data on pausing and recover the same Credential on recover. Only in the case the database was cleared during that two
 * events we should have an exception. The destination in such cases should be the rendering ot the ExceptiomManagementFragment.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class APublicCredentialActivity extends APublicMenuActivity implements ICredentialActivity, IPilotActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	/** When the user selects a Credential we pass to the target Activity the pilot identifier. */
	protected int pilotIdentifier = 0;
	/** Recovered credential extracted from the current list of Credentials using the pilot identifier as a key. */
	protected Credential credential = null;
	/** Reference to the Pilot associated to the Credential. This fields should be valid to be exported to DS and Parts. */
	protected PilotV2 pilot = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	protected Credential searchCredential( final int identifier ) {
		for (Credential cred : AndroidGlobalDataManager.accessAllCredentials("RE")) {
			if (cred.getAccountId() == identifier) return cred;
		}
		return null;
	}

	// --- I C R E D E N T I A L A C T I V I T Y   I N T E R F A C E
	@Override
	public Credential getCredential() {
		return this.credential;
	}

	// --- I P I L O T   I N T E R F A C E
	@Override
	public PilotV2 getPilot() {
		return this.pilot;
	}

	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [APublicCredentialActivity.onCreate]");
		super.onCreate(savedInstanceState);
		try {
			// Get the pilot identifier. This is a mandatory parameter and should be validated.
			if (null != extras) {
				pilotIdentifier = extras.getInt(EExtras.CAPSULEER_IDENTIFIER.name(), 0);
				if (pilotIdentifier > 0) {
					// Validate the pointer Credential that should be made available to this Activity fragments.
					credential = searchCredential(pilotIdentifier);
					if (null != credential) {
						// Configure the new custom Action Bar
						setupActionBar(credential);
					} else {
						AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
								, new NeoComException("[APublicCredentialActivity.onCreate]> Credential id " + pilotIdentifier
										+ " not fount on the list of credentials.")
								, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
						this.addPage(new ApplicationExceptionReportFragment());
					}
				}
			} else {
				AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
						, new NeoComException("[APublicCredentialActivity.onCreate]> 'onCreate' event did not received a valid set of 'extras'.")
						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
				this.addPage(new ApplicationExceptionReportFragment());
			}
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("APublicCredentialActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
			this.addPage(new ApplicationExceptionReportFragment());
		}
		logger.info("<< [APublicCredentialActivity.onCreate]");
	}

	@Override
	protected void onRestoreInstanceState( final Bundle savedInstanceState ) {
		pilotIdentifier = savedInstanceState.getInt(EExtras.CAPSULEER_IDENTIFIER.name());
		credential = searchCredential(pilotIdentifier);
		if (null != credential) {
			// Get access tot the Pilot information and store a reference on the Activity.
			try {
				pilot = AndroidGlobalDataManager.fastRequestPilotV2(credential);
			} catch (NeoComRegisteredException neoe) {
				AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
						, neoe
						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
				this.addPage(new ApplicationExceptionReportFragment());
			}
		} else {
			AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
					, new NeoComException("[APublicCredentialActivity.onCreate]> Credential id " + pilotIdentifier
							+ " not fount on the list of credentials.")
					, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			this.addPage(new ApplicationExceptionReportFragment());
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of reactivation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState( final Bundle outState ) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(EExtras.CAPSULEER_IDENTIFIER.name(), pilotIdentifier);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
