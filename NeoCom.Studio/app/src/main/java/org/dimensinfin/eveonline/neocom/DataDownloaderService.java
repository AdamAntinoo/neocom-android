package org.dimensinfin.eveonline.neocom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.main.INeoComDownloadCallback;
import org.dimensinfin.eveonline.neocom.main.PilotDataSections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDownloaderService {
	private static final Logger logger = LoggerFactory.getLogger(DataDownloaderService.class);
	private static final ExecutorService downloaderExecutor = Executors.newSingleThreadExecutor();

	public void accessPilotPublicData( final INeoComDownloadCallback callbackDestination, final PilotDataSections section ) {
		logger.info("-- [DataDownloaderService.accessPilotPublicData]> Posting request: {}", section.name());
		downloaderExecutor.submit(() -> {
			logger.info("-- [DataDownloaderService.accessPilotPublicData]> Downloading public data information...");
			final GetCharactersCharacterIdOk publicData = ESINetworkManager.getCharactersCharacterId(
					callbackDestination.getCredential().getAccountId()
					, callbackDestination.getCredential().getRefreshToken()
					, callbackDestination.getCredential().getDataSource());
			// Callback the pilot instance with the data.
			logger.info("-- [DataDownloaderService.accessPilotPublicData]> Completed data download. Sending data to callback.");
			if (null != publicData) callbackDestination.sendEvent(section, publicData);
			else
				logger.info("-- [DataDownloaderService.accessPilotPublicData]> Failed public data download. Null contents.");
		});
	}
}
