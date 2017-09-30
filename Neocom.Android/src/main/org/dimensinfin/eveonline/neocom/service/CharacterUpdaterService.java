//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.service;

import java.util.Set;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.EDataBlock;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

// - CLASS IMPLEMENTATION ...................................................................................
public class CharacterUpdaterService extends IntentService {

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CharacterUpdaterService() {
		super("CharacterUpdaterService");
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * We can process two types of events. <code>CHARACTERDATA</code> where we update frequent character
	 * information and <code>ASSETDATA</code> that is the call to down the heavy load of the list of character
	 * assets.<br>
	 * The new blueprints will be added with the assets of a first approach but maybe they will be set on their
	 * own type.
	 */
	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.i("CharacterUpdaterService", ">> [CharacterUpdaterService.onHandleIntent]");
		Long localizer = (Long) intent.getSerializableExtra(AppWideConstants.EExtras.EXTRA_CHARACTER_LOCALIZER.name());
		// Be sure we have access to the network. Otherwise intercept the exceptions.
		if (NeoComApp.checkNetworkAccess()) {
			Set<NeoComCharacter> activeCharacters = AppConnector.getModelStore().accessCharacterList();
			NeoComCharacter pilot = null;
			for (NeoComCharacter neocomchar : activeCharacters) {
				if (neocomchar.getCharacterID() == localizer) {
					pilot = neocomchar;
					break;
				}
			}
			if (null != pilot) {
				// Pilot signaled for update. Locate the next data set to update because its cache has expired.
				EDataBlock datacode = pilot.needsUpdate();
				try {
					Log.i("CharacterUpdaterService", "-- [CharacterUpdaterService.onHandleIntent] EDataBlock to process: "
							+ pilot.getName() + " - " + datacode);
					switch (datacode) {
						case CHARACTERDATA:
							pilot.updateCharacterInfo();
							AppConnector.getCacheConnector().clearPendingRequest(localizer);
							AppConnector.getCacheConnector().decrementTopCounter();
							break;
						case ASSETDATA:
							// New data model decouples the character from the data managers. But requires to know if Pilot or Corporation.
							if (pilot.isCorporation()) {
								pilot.getAssetsManager().downloadCorporationAssets();
							} else {
								pilot.getAssetsManager().downloadPilotAssets();
							}
							//							pilot.downloadAssets();
							//							pilot.downloadBlueprints();
							AppConnector.getCacheConnector().clearPendingRequest(localizer);
							AppConnector.getCacheConnector().decrementTopCounter();
							break;
						//													case BLUEPRINTDATA:
						////							pilot.downloadAssets();
						//							pilot.downloadBlueprints();
						//							NeoComApp.getTheCacheConnector().clearPendingRequest(Long.valueOf(localizer).toString());
						//							NeoComApp.topCounter--;
						//							break;
						//						case INDUSTRYJOBS:
						//							pilot.downloadIndustryJobs();
						//							NeoComApp.getTheCacheConnector().clearPendingRequest(Long.valueOf(localizer).toString());
						//							NeoComApp.topCounter--;
						//							break;
						//						case MARKETORDERS:
						//							pilot.downloadMarketOrders();
						//							NeoComApp.getTheCacheConnector().clearPendingRequest(Long.valueOf(localizer).toString());
						//							NeoComApp.topCounter--;
						//							break;

						default:
							break;
					}
					// Clean the top counter if completed.
					if (NeoComApp.topCounter < 0) {
						NeoComApp.topCounter = 0;
					}
				} catch (RuntimeException rtex) {
				}
			}
			// Relaunch more jobs if completed.
			NeoComApp.runTimer();
		}
		Log.i("CharacterUpdaterService", "<< CharacterUpdaterService.onHandleIntent");
	}
}

// - UNUSED CODE ............................................................................................
