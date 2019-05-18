package org.dimensinfin.eveonline.neocom.sharedcode.dataservice;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;
import org.dimensinfin.eveonline.neocom.sharedcode.INeoComDownloadCallback;
import org.dimensinfin.eveonline.neocom.sharedcode.PilotDataSections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDownloaderService {
	private static final Logger logger = LoggerFactory.getLogger(DataDownloaderService.class);
	private static final ExecutorService downloaderExecutor = Executors.newSingleThreadExecutor();

	// - P I L O T
	public void accessPilotPublicData( final INeoComDownloadCallback callbackDestination, final PilotDataSections section ) {
		logger.info("-- [DataDownloaderService.accessPilotPublicData]> Posting request: {}", section.name());
		downloaderExecutor.submit(() -> {
			logger.info("-- [DataDownloaderService.accessPilotPublicData]> Downloading public data information...");
			final GetCharactersCharacterIdOk publicData = ESINetworkManager.getCharactersCharacterId(
					callbackDestination.getCredential().getAccountId()
					, callbackDestination.getCredential().getRefreshToken()
					, callbackDestination.getCredential().getDataSource());
			// Callback the pilot instance with the data.
			logger.info("-- [DataDownloaderService.accessPilotPublicData]> Completed public data download. Sending data to callback.");
			if (null != publicData) callbackDestination.signalCompletion(section, publicData);
			else
				logger.info("-- [DataDownloaderService.accessPilotPublicData]> Failed public data download. Null contents.");
		});
	}

	/**
	 * Search for the race information on the ESI cache. If the race data is not found then we should start a background download for all that
	 * data and when completed update the information on the callback instance.
	 *
	 * @param callbackDestination the instance to be notified when the data is found or the background download completes.
	 * @param section             the data that was requested. This is important to the callback to know where to put the data received.
	 */
	public void accessPilotRace( final INeoComDownloadCallback callbackDestination, final PilotDataSections section ) {
		logger.info("-- [DataDownloaderService.accessPilotRace]> Posting request: {}", section.name());
		// Search on cache before going further.

		downloaderExecutor.submit(() -> {
			logger.info("-- [DataDownloaderService.accessPilotRace]> Downloading race data information...");
			final List<GetUniverseRaces200Ok> racesList = ESINetworkManager.getUniverseRaces(callbackDestination.getCredential().getDataSource());
			// Callback the pilot instance with the data.
			logger.info("-- [DataDownloaderService.accessPilotRace]> Completed race data download. Sending data to callback.");
			if (null != racesList) callbackDestination.signalCompletion(section, racesList);
			else
				logger.info("-- [DataDownloaderService.accessPilotRace]> Failed race data download. Null contents.");
		});
	}

//	// - M A R K E T D A T A
//	public void accessMarketPrices( final INeoComDownloadCallback callbackDestination, final MarketDataSections section ) {
//		logger.info("-- [DataDownloaderService.accessMarketPrices]> Posting request: {}", section.name());
//		downloaderExecutor.submit(() -> {
//			logger.info("-- [DataDownloaderService.accessMarketPrices]> Downloading market prices data information...");
//			final GetCharactersCharacterIdOk publicData = ESINetworkManager.getCharactersCharacterId(
//					callbackDestination.getCredential().getAccountId()
//					, callbackDestination.getCredential().getRefreshToken()
//					, callbackDestination.getCredential().getDataSource());
//			// Callback the pilot instance with the data.
//			logger.info("-- [DataDownloaderService.accessMarketPrices]> Completed market prices data download. Sending data to callback.");
//			if (null != publicData) callbackDestination.signalCompletion(section, publicData);
//			else
//				logger.info("-- [DataDownloaderService.accessMarketPrices]> Failed market prices data download. Null contents.");
//		});
//	}

	// - B U I L D E R
	public static class Builder {
		private DataDownloaderService onConstruction;

		public Builder() {
			this.onConstruction = new DataDownloaderService();
		}

		public DataDownloaderService builder() {
			return this.onConstruction;
		}
	}
}
