package org.dimensinfin.eveonline.neocom.app;

import java.io.IOException;
import java.util.List;

import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManagerZBase;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManagerCache;
import org.dimensinfin.eveonline.neocom.esiswagger.api.PlanetaryInteractionApi;
import org.dimensinfin.eveonline.neocom.esiswagger.api.UniverseApi;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseAncestries200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseBloodlines200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import retrofit2.Response;
import retrofit2.Retrofit;

public class ESINetworkManagerMock extends ESINetworkManager {
	protected static Retrofit mockRetrofit;
	//	protected static Retrofit noOAuthRetrofit;

	//	static {
	//		mockRetrofit = NeoComRetrofitMock.build(neocomAuth20, AGENT, cacheDataFile, cacheSize, timeout);
	//		noOAuthRetrofit = NeoComRetrofitNoOAuthHTTP.build(neocomAuth20, AGENT, cacheDataFile, cacheSize, timeout);
	//	}

	// - U N I V E R S E
	public static GetUniverseTypesTypeIdOk getUniverseTypeById( final String server, final int typeId ) {
		logger.info(">> [ESINetworkManagerMock.getUniverseTypeById]");
		final DateTime startTimePoint = DateTime.now();
		try {
			// Create the request to be returned so it can be called.
			final Response<GetUniverseTypesTypeIdOk> itemListResponse = neocomRetrofitNoAuth
					                                                            .create(UniverseApi.class)
					                                                            .getUniverseTypesTypeId(typeId
							                                                            , "en-us"
							                                                            , server
							                                                            , null
							                                                            , null)
					                                                            .execute();
			if (!itemListResponse.isSuccessful()) {
				return null;
			} else return itemListResponse.body();
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

	public static List<GetUniverseAncestries200Ok> getUniverseAncestries( final String server ) {
		logger.info(">> [ESINetworkManagerMock.getUniverseAncestries]");
		final DateTime startTimePoint = DateTime.now();
		try {
			// Create the request to be returned so it can be called.
			final Response<List<GetUniverseAncestries200Ok>> ancestriesResponse = neocomRetrofitNoAuth
					                                                                      .create(UniverseApi.class)
					                                                                      .getUniverseAncestries("en-us"
							                                                                      , server
							                                                                      , null
							                                                                      , null)
					                                                                      .execute();
			if (!ancestriesResponse.isSuccessful()) {
				return null;
			} else return ancestriesResponse.body();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException runtime) {
			runtime.printStackTrace();
		} finally {
			logger.info("<< [ESINetworkManager.getUniverseAncestries]> [TIMING] Full elapsed: {}"
					, new Duration(startTimePoint, DateTime.now()).getMillis() + "ms");
		}
		return null;
	}

	public static List<GetUniverseBloodlines200Ok> getUniverseBloodlines( final String server ) {
		logger.info(">> [ESINetworkManagerMock.getUniverseBloodlines]");
		final DateTime startTimePoint = DateTime.now();
		try {
			// Create the request to be returned so it can be called.
			final Response<List<GetUniverseBloodlines200Ok>> bloodlinesResponse = neocomRetrofitNoAuth
					                                                                      .create(UniverseApi.class)
					                                                                      .getUniverseBloodlines("en-us"
							                                                                      , server
							                                                                      , null
							                                                                      , null)
					                                                                      .execute();
			if (!bloodlinesResponse.isSuccessful()) {
				return null;
			} else return bloodlinesResponse.body();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException runtime) {
			runtime.printStackTrace();
		} finally {
			logger.info("<< [ESINetworkManager.getUniverseBloodlines]> [TIMING] Full elapsed: {}"
					, new Duration(startTimePoint, DateTime.now()).getMillis() + "ms");
		}
		return null;
	}

	public static List<GetUniverseRaces200Ok> getUniverseRaces( final String server ) {
		logger.info(">> [ESINetworkManagerMock.getUniverseRaces]");
		final DateTime startTimePoint = DateTime.now();
		try {
			// Create the request to be returned so it can be called.
			final Response<List<GetUniverseRaces200Ok>> racesResponse = neocomRetrofitNoAuth
					                                                            .create(UniverseApi.class)
					                                                            .getUniverseRaces("en-us"
							                                                            , server
							                                                            , null
							                                                            , null)
					                                                            .execute();
			if (!racesResponse.isSuccessful()) {
				return null;
			} else return racesResponse.body();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException runtime) {
			runtime.printStackTrace();
		} finally {
			logger.info("<< [ESINetworkManager.getUniverseRaces]> [TIMING] Full elapsed: {}"
					, new Duration(startTimePoint, DateTime.now()).getMillis() + "ms");
		}
		return null;
	}

	// - P L A N E T A R Y   I N T E R A C T I O N
	public static GetUniverseSchematicsSchematicIdOk getUniversePlanetarySchematicsById( final String server, final int schematicId ) {
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

	public static List<GetCharactersCharacterIdPlanets200Ok> getCharactersCharacterIdPlanets( final int identifier
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
			final Response<List<GetCharactersCharacterIdPlanets200Ok>> planetaryApiResponse = mockRetrofit
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

	public static GetCharactersCharacterIdPlanetsPlanetIdOk getCharactersCharacterIdPlanetsPlanetId( final int identifier
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
			final Response<GetCharactersCharacterIdPlanetsPlanetIdOk> planetaryApiResponse = mockRetrofit
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

	//	protected ESINetworkManagerMock( final IConfigurationProvider configurationProvider, final IFileSystem fileSystemAdapter ) {
	//		super ( configurationProvider,  fileSystemAdapter );
	//	}
	//
	//	public ESINetworkManagerMock() {
	//	}

	// - B U I L D E R
	public static class Builder extends ESINetworkManagerZBase.Builder {
		public Builder( final IConfigurationProvider configurationProvider, final IFileSystem fileSystemAdapter ) {
			super(configurationProvider, fileSystemAdapter);
		}

		public ESINetworkManagerZBase build() {
			final ESINetworkManagerZBase parent = super.build();
			// Initialize retrofits.
			mockRetrofit = new NeoComRetrofitMock.Builder()
					               .withNeoComOAuth20(singleton.getConfiguredOAuth("Tranquility"))
					               .withEsiServerLocation(parent.configurationProvider.getResourceString("P.esi.data.server.location"
							               , "https://esi.evetech.net/latest/"))
					               .withAgent(AGENT)
					               .withCacheDataFile(cacheDataFile)
					               .withCacheSize(CACHE_SIZE)
					               .withTimeout(TIMEOUT)
					               .build();
			return parent;
		}
	}
}
