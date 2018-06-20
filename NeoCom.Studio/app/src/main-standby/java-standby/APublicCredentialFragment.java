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
package org.dimensinfin.eveonline.neocom.fragment.core;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.model.PilotV2;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class APublicCredentialFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("APublicCredentialFragment");

	// - F I E L D - S E C T I O N ............................................................................
	/** When the user selects a Credential we pass to the target Activity the pilot identifier. */
	protected int pilotIdentifier = 0;
	/** Recovered credential extracted from the current list of Credentials using the pilot identifier as a key. */
	protected Credential credential = null;
	/** Reference to the Pilot associated to the Credential. This fields should be valid to be exported to DS and Parts. */
	protected PilotV2 pilot = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	protected Credential searchCredential( final int identifier ) {
		for (Credential cred : AndroidGlobalDataManager.accessAllCredentials()) {
			if (cred.getAccountId() == identifier) return cred;
		}
		return null;
	}
	// --- F R A G M E N T   L I F E C Y C L E

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of reactivation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	public void onSaveInstanceState( final Bundle outState ) {
		logger.info(">> [APublicCredentialFragment.onSaveInstanceState]");
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), pilotIdentifier);
		logger.info("<< [APublicCredentialFragment.onSaveInstanceState]");
	}

	@Override
	public void onActivityCreated( @Nullable final Bundle savedInstanceState ) {
		logger.info(">> [APublicCredentialFragment.onActivityCreated]");
		if (null != savedInstanceState) {
			pilotIdentifier = savedInstanceState.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
			credential = searchCredential(pilotIdentifier);
			if (null != credential) {
				// Get access tot the Pilot information and store a reference on the Activity.
				try {
					pilot = AndroidGlobalDataManager.fastRequestPilotV2(credential);
				} catch (NeoComRegisteredException neoe) {
					AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
							, neoe
							, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					neoe.printStackTrace();
				}
			} else {
				AndroidGlobalDataManager.registerException("APublicCredentialActivity.onCreate"
						, new NeoComException("[APublicCredentialActivity.onCreate]> Credential id " + pilotIdentifier
								+ " not fount on the list of credentials.")
						, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			}
		}
		super.onActivityCreated(savedInstanceState);
		logger.info("<< [APublicCredentialFragment.onActivityCreated]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
