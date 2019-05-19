package org.dimensinfin.eveonline.neocom.app.pilot;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDownloaderService {
	private static final Logger logger = LoggerFactory.getLogger(DataDownloaderService.class);
	private static final ExecutorService downloaderExecutor = Executors.newSingleThreadExecutor();

	private ESIGlobalAdapter esiAdapter;

	private DataDownloaderService() {
	}

	// - P I L O T
	public void accessPilotPublicData( final IPilotDataDownloadCallback callbackDestination, final PilotDataSections section ) {
		logger.info("-- [DataDownloaderService.accessPilotPublicData]> Posting request: {}", section.name());
		downloaderExecutor.submit(() -> {
			logger.info("-- [DataDownloaderService.accessPilotPublicData]> Downloading public data information...");
			final GetCharactersCharacterIdOk publicData = this.esiAdapter.getCharactersCharacterId(
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
	public void accessPilotRace( final IPilotDataDownloadCallback callbackDestination, final PilotDataSections section ) {
		logger.info("-- [DataDownloaderService.accessPilotRace]> Searching data: {}", section.name());
		final GetUniverseRaces200Ok race = this.esiAdapter.searchSDERace(callbackDestination.getRaceId());
		if (null != race) callbackDestination.signalCompletion(section, race);
		else
			logger.info("-- [DataDownloaderService.accessPilotRace]> Failed race data assign. Null contents.");
	}

	// - B U I L D E R
	public static class Builder {
		private DataDownloaderService onConstruction;

		public Builder() {
			this.onConstruction = new DataDownloaderService();
		}

		public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}

		public DataDownloaderService builder() {
			Objects.requireNonNull(this.onConstruction.esiAdapter);
			return this.onConstruction;
		}
	}
}
