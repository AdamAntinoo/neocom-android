//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.EDataBlock;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;

import java.util.Set;

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
		if (NeoComAppConnector.getSingleton().checkNetworkAccess()) {
			Set<NeoComCharacter> activeCharacters = NeoComAppConnector.getSingleton().getModelStore().accessCharacterList();
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
							NeoComAppConnector.getSingleton().getCacheConnector().clearPendingRequest(localizer);
							NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
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
							NeoComAppConnector.getSingleton().getCacheConnector().clearPendingRequest(localizer);
							NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
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
