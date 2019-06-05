package org.dimensinfin.eveonline.neocom.app.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerCache;
import org.dimensinfin.eveonline.neocom.esiswagger.api.IndustryApi;
import org.dimensinfin.eveonline.neocom.esiswagger.api.PlanetaryInteractionApi;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdMining200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.facebook.stetho.common.ArrayListAccumulator;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ESINetworkManagerMock extends ESIGlobalAdapter {
	protected ESINetworkManagerMock( final IConfigurationProvider configurationProvider, final IFileSystem fileSystemAdapter ) {
		super(configurationProvider, fileSystemAdapter);
	}

	// - P L A N E T A R Y   I N T E R A C T I O N
	public  GetUniverseSchematicsSchematicIdOk getUniversePlanetarySchematicsById( final String server, final int schematicId ) {
		logger.info(">> [ESINetworkManagerMock.getUniversePlanetarySchematicsById]");
		final DateTime startTimePoint = DateTime.now();
		try {
			// Create the request to be returned so it can be called.
			final Response<GetUniverseSchematicsSchematicIdOk> schematicistResponse = neocomRetrofitNoAuth
					                                                                          .create(PlanetaryInteractionApi.class)
					                                                                          .getUniverseSchematicsSchematicId(schematicId
							                                                                          , "en-us"
							                                                                          , null)
					                                                                          .execute();
			if (!schematicistResponse.isSuccessful()) {
				return null;
			} else return schematicistResponse.body();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException runtime) {
			runtime.printStackTrace();
		} finally {
			logger.info("<< [ESINetworkManager.getMarketsPrices]> [TIMING] Full elapsed: {}"
					, new Duration(startTimePoint, DateTime.now()).getMillis() + "ms");
		}
		return null;
	}

	public List<GetCharactersCharacterIdPlanets200Ok> getCharactersCharacterIdPlanets( final int identifier
			, final String refreshToken, final String server ) {
		logger.info(">> [ESINetworkManager.getCharactersCharacterIdPlanets]");
		final Chrono accessFullTime = new Chrono();
		// Store the response at the cache or if there is a network failure return the last access if available
		final String reference = constructCachePointerReference(GlobalDataManagerCache.ECacheTimes.CHARACTER_COLONIES, identifier);
		// Check if network is available and we have configured allowed access to download data.
		//		if ( allowDownloadPass() ) {
		try {
			// Set the refresh to be used during the request.
			NeoComRetrofitHTTP.setRefeshToken(refreshToken);
			String datasource = GlobalDataManager.TRANQUILITY_DATASOURCE;
			if (null != server) datasource = server;
			// Create the request to be returned so it can be called.
			final Response<List<GetCharactersCharacterIdPlanets200Ok>> planetaryApiResponse = neocomRetrofitMock
					                                                                                  .create(PlanetaryInteractionApi.class)
					                                                                                  .getCharactersCharacterIdPlanets(identifier, datasource, null, null).execute();
			if (planetaryApiResponse.isSuccessful()) {
				// Store results on the cache.
				okResponseCache.put(reference, planetaryApiResponse);
				return planetaryApiResponse.body();
			} else return (List<GetCharactersCharacterIdPlanets200Ok>) okResponseCache.get(reference).body();
		} catch (IOException ioe) {
			logger.error("EX [ESINetworkManager.getCharactersCharacterIdPlanets]> [EXCEPTION]: {}", ioe.getMessage());
			ioe.printStackTrace();
			// Return cached response if available
			return (List<GetCharactersCharacterIdPlanets200Ok>) okResponseCache.get(reference).body();
		} catch (RuntimeException rte) {
			logger.error("EX [ESINetworkManager.getCharactersCharacterIdPlanets]> [EXCEPTION]: {}", rte.getMessage());
			rte.printStackTrace();
			// Return cached response if available
			return (List<GetCharactersCharacterIdPlanets200Ok>) okResponseCache.get(reference).body();
		} finally {
			logger.info("<< [ESINetworkManager.getCharactersCharacterIdPlanets]> [TIMING] Full elapsed: {}"
					, accessFullTime.printElapsed(Chrono.ChronoOptions.SHOWMILLIS));
		}
		//		} else return (List<GetCharactersCharacterIdPlanets200Ok>) okResponseCache.get(reference).body();
	}

	public GetCharactersCharacterIdPlanetsPlanetIdOk getCharactersCharacterIdPlanetsPlanetId( final int identifier
			, final int planetid, final String refreshToken, final String server ) {
		logger.info(">> [ESINetworkManager.getCharactersCharacterIdPlanetsPlanetId]");
		final Chrono accessFullTime = new Chrono();
		// Store the response at the cache or if there is a network failure return the last access if available
		final String reference = constructCachePointerReference(GlobalDataManagerCache.ECacheTimes.PLANETARY_INTERACTION_STRUCTURES, identifier);
		// Check if network is available and we have configured allowed access to download data.
		//		if (allowDownloadPass()) {
		try {
			// Set the refresh to be used during the request.
			NeoComRetrofitHTTP.setRefeshToken(refreshToken);
			String datasource = GlobalDataManager.TRANQUILITY_DATASOURCE;
			if (null != server) datasource = server;
			// Create the request to be returned so it can be called.
			final Response<GetCharactersCharacterIdPlanetsPlanetIdOk> planetaryApiResponse = neocomRetrofit
					                                                                                 .create(PlanetaryInteractionApi.class)
					                                                                                 .getCharactersCharacterIdPlanetsPlanetId(identifier, planetid, datasource, null, null).execute();
			if (planetaryApiResponse.isSuccessful()) {
				// Store results on the cache.
				okResponseCache.put(reference, planetaryApiResponse);
				return planetaryApiResponse.body();
			} else return (GetCharactersCharacterIdPlanetsPlanetIdOk) okResponseCache.get(reference).body();
		} catch (IOException ioe) {
			logger.error("EX [ESINetworkManager.getCharactersCharacterIdPlanets]> [EXCEPTION]: {}", ioe.getMessage());
			ioe.printStackTrace();
			// Return cached response if available
			return (GetCharactersCharacterIdPlanetsPlanetIdOk) okResponseCache.get(reference).body();
		} catch (RuntimeException rte) {
			logger.error("EX [ESINetworkManager.getCharactersCharacterIdPlanets]> [EXCEPTION]: {}", rte.getMessage());
			rte.printStackTrace();
			// Return cached response if available
			return (GetCharactersCharacterIdPlanetsPlanetIdOk) okResponseCache.get(reference).body();
		} finally {
			logger.info("<< [ESINetworkManager.getCharactersCharacterIdPlanetsPlanetId]> [TIMING] Full elapsed: {}"
					, accessFullTime.printElapsed(Chrono.ChronoOptions.SHOWMILLIS));
		}
		//		} else return (GetCharactersCharacterIdPlanetsPlanetIdOk) okResponseCache.get(reference).body();
	}

	// -  M I N I N G

	/**
	 * This method encapsulates the call to the esi server to retrieve the current list of mining operations. This listing will contain the operations
	 * for the last 30 days. It will be internally cached during 1800 seconds so we have to check the hour change less frequently.
	 *
	 * @param identifier   the character unique identifier.
	 * @param refreshToken the authorization refresh token to be used on this call if the current toked is expired.
	 * @param server       the esi data server to use, tranquility or singularity.
	 * @return the list of mining actions performed during the last 30 days.
	 */
	public List<GetCharactersCharacterIdMining200Ok> getCharactersCharacterIdMining( final int identifier
			, final String refreshToken
			, final String server ) {
		logger.info("-- [MARKETORDERS]> Entering ESI");
		logger.info(">> [ESINetworkManager.getCharactersCharacterIdMining]");
		final Chrono accessFullTime = new Chrono();
		//		List<GetCharactersCharacterIdMining200Ok> returnMiningList = new ArrayList<>(1000);
		try {
			String datasource = GlobalDataManager.TRANQUILITY_DATASOURCE;
			if (null != server) datasource = server;
			int pageCounter = 0;
			final Response<List<GetCharactersCharacterIdMining200Ok>> industryApiResponse = neocomRetrofitMock
					                                                                                .create(IndustryApi.class)
					                                                                                .getCharactersCharacterIdMining(identifier, datasource, null, pageCounter, null).execute();
			logger.info("-- [MARKETORDERS]> Processing ESI data: {}", industryApiResponse.isSuccessful());
			if (industryApiResponse.isSuccessful()) {
				logger.info("-- [MARKETORDERS]> Processing ESI data records: {}", industryApiResponse.body().size());
				return industryApiResponse.body();
			} else return new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException rtex) {
			// Check if the problem is a connection reset.
			if (rtex.getMessage().toLowerCase().contains("connection reset")) {
				logger.info("EX [ESINetworkManager.getCharactersCharacterIdMining]> Exception: {}", rtex.getMessage());
			}
		} finally {
			logger.info("<< [ESINetworkManager.getCharactersCharacterIdMining]> [TIMING] Full elapsed: {}", accessFullTime.printElapsed(Chrono.ChronoOptions.SHOWMILLIS));
		}
		return new ArrayList<>();
	}

	// - F I E L D S
	protected static Retrofit neocomRetrofitMock;
}
