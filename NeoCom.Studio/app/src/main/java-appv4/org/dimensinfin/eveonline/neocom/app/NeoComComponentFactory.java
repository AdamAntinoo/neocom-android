package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.adapters.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.adapters.StoreCacheManager;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidFileSystemAdapter;
import org.dimensinfin.eveonline.neocom.app.adapters.GlobalAndroidPreferencesProvider;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.app.adapters.SDEAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.LocationRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.domain.EveItem;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.market.MarketDataSet;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

/**
 * This is a singleton with global access that will contain application component references so they can be injected to other components. The creation
 * of those components will be down internally on demand and with the knowledge of what components depend on other components.
 *
 * @author Adam Antinoo
 */
public class NeoComComponentFactory {
	public static final String DEFAULT_ESI_SERVER = "Tranquility";
	protected static Logger logger = LoggerFactory.getLogger(NeoComComponentFactory.class);
	private static NeoComComponentFactory singleton; // The singleton for the factory. This should be created at start time and setting the application.
	private static Application application; // The link to the application that is required by the component factory.
	protected CredentialRepository credentialRepository;
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystemAdapter;
	@Deprecated
	private IGlobalPreferencesManager preferencesProvider;
	//	private ESIGlobalAdapter esiAdapter;
	private ESIDataAdapter esiDataAdapter;
	private StoreCacheManager storageCache;
	private NeoComAndroidDBHelper neocomDBHelper;
	private ISDEDatabaseAdapter sdeDatabaseAdapter;
	@Deprecated
	private DataDownloaderService downloaderService;
	private MiningRepository miningRepository;
	private PlanetaryRepository planetaryRepository;
	private LocationCatalogService locationCatalogService;
	private LocationRepository locationRepository;

	public static NeoComComponentFactory initialiseSingleton( final Application newApplication ) {
		Objects.requireNonNull(newApplication);
		application = newApplication;
		if (null == singleton) singleton = new NeoComComponentFactory();
		return singleton;
	}

	public static NeoComComponentFactory getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException("NeoCom global singleton is not instantiated. Please complete initialisation.");
		return singleton;
	}

	// - G E T T E R S
	public Application getApplication() {
		if (null == application)
			throw new NeoComRuntimeException("NeoCom application is not associated to factory. Please complete initialisation.");
		return application;
	}

	// - A C C E S S O R S
	public IConfigurationProvider getConfigurationProvider() {
		if (null == this.configurationProvider) {
			this.configurationProvider = new AndroidConfigurationProvider.Builder(
					application.getResources().getString(R.string.propertiesLocation))
					                             .withApplicationContext(application)
					                             .build();
		}
		return this.configurationProvider;
	}

	@Deprecated
	public IGlobalPreferencesManager getPreferencesProvider() {
		if (null == this.preferencesProvider) {
			preferencesProvider = new GlobalAndroidPreferencesProvider.Builder()
					                      .withApplication(this.getApplication())
					                      .build();
		}
		return this.preferencesProvider;
	}

	public IFileSystem getFileSystemAdapter() {
		if (null == this.fileSystemAdapter)
			fileSystemAdapter = new AndroidFileSystemAdapter.Builder(application.getResources().getString(R.string.appname),
			                                                         application).build();
		return this.fileSystemAdapter;
	}

//	@Deprecated
//	public ESIGlobalAdapter getEsiAdapter() {
//		if (null == this.esiAdapter) {
//			esiAdapter = new ESIGlobalAdapter.Builder(this.getConfigurationProvider(), this.getFileSystemAdapter())
//					             .build();
//		}
//		return this.esiAdapter;
//	}

	public ESIDataAdapter getEsiDataAdapter() {
		if (null == this.esiDataAdapter) {
			this.esiDataAdapter = new ESIDataAdapter.Builder()
					                      .withConfigurationProvider(this.getConfigurationProvider())
					                      .withFileSystem(this.getFileSystemAdapter())
					                      .withLocationCatalogService(this.getLocationCatalogService())
					                      .build();
			EveItem.injectEsiDataAdapter(this.esiDataAdapter);
			NeoComUpdater.injectsEsiDataAdapter(this.esiDataAdapter);
			MarketDataSet.injectEsiDataAdapter(this.esiDataAdapter);
		}
		return this.esiDataAdapter;
	}

	public LocationCatalogService getLocationCatalogService() {
		if (null == this.locationCatalogService) {
			this.locationCatalogService = new LocationCatalogService.Builder()
					                              .withConfigurationProvider(this.getConfigurationProvider())
					                              .withFileSystem(this.getFileSystemAdapter())
					                              .withSDEDatabaseAdapter(this.getSDEDatabaseAdapter())
					                              .withLocationRepository(this.getLocationRepository())
					                              .build();
		}
		return this.locationCatalogService;
	}

	public LocationRepository getLocationRepository() {
		if (null == this.locationRepository) {
			try {
				this.locationRepository = new LocationRepository.Builder()
						                          .withSDEDatabaseAdapter(this.getSDEDatabaseAdapter())
						                          .withLocationDao(this.getSDEDatabaseAdapter().getLocationDao())
						                          .build();
			} catch (SQLException sqle) {
				this.locationRepository = null;
				Objects.requireNonNull(this.locationRepository);
			}
		}
		return this.locationRepository;
	}

	@Deprecated
	public DataDownloaderService getDownloaderService() {
		if (null == this.downloaderService) {
			downloaderService = new DataDownloaderService.Builder(this.getEsiDataAdapter())
					                    .build();
		}
		return this.downloaderService;
	}

//	public ESIDataPersistenceService getPersistenceService() {
//		if (null == this.persistenceService) {
//			try {
//				final Dao<MiningExtraction, String> miningExtractionDao = this.getNeoComDBHelper().getMiningExtractionDao();
//				persistenceService = new ESIDataPersistenceService.Builder()
//						                     .withEsiAdapter(esiDataAdapter)
//						                     .withMiningRepository(this.getMiningRepository())
//						                     .build();
//			} catch (SQLException sqle) {
//				persistenceService = null;
//				Objects.requireNonNull(persistenceService);
//			}
//		}
//		return this.persistenceService;
//	}

	public CredentialRepository getCredentialRepository() {
		if (null == this.credentialRepository) {
			try {
				credentialRepository = new CredentialRepository.Builder()
						                       .withCredentialDao(this.getNeoComDBHelper().getCredentialDao())
						                       .build();
			} catch (SQLException sqle) {
				credentialRepository = null;
				Objects.requireNonNull(credentialRepository);
			}
		}
		return this.credentialRepository;
	}

	public MiningRepository getMiningRepository() {
		if (null == this.miningRepository) {
			try {
				miningRepository = new MiningRepository.Builder()
						                   .withMiningExtractionDao(this.getNeoComDBHelper().getMiningExtractionDao())
						                   .build();
			} catch (SQLException sqle) {
				miningRepository = null;
				Objects.requireNonNull(miningRepository);
			}
		}
		return this.miningRepository;
	}

	public NeoComAndroidDBHelper getNeoComDBHelper() {
		if (null == this.neocomDBHelper) {
			try {
				neocomDBHelper = new NeoComAndroidDBHelper.Builder()
						                 .withAppContext(application)
						                 .withFileSystem(this.getFileSystemAdapter())
						                 .setDatabaseLocation(application.getResources().getString(R.string.appname))
						                 .setDatabaseName(application.getResources().getString(R.string.appdatabasefilename))
						                 .setDatabaseVersion(Integer.valueOf(application.getResources().getString(
								                 R.string.appdatabaseversion)))
						                 .build();
			} catch (SQLException sqle) {
				neocomDBHelper = null;
				Objects.requireNonNull(neocomDBHelper);
			}
		}
		return this.neocomDBHelper;
	}

	public ISDEDatabaseAdapter getSDEDatabaseAdapter() {
		if (null == this.sdeDatabaseAdapter) {
			this.sdeDatabaseAdapter = new SDEAndroidDBHelper.Builder()
					                          .withDatabasePath("")
					                          .withDatabaseName("sde.db")
					                          .withFileSystemAdapter(this.getFileSystemAdapter())
					                          .build();
		}
		return this.sdeDatabaseAdapter;
	}

	public PlanetaryRepository getPlanetaryRepository() {
		if (null == this.planetaryRepository) {
			this.planetaryRepository = new PlanetaryRepository.Builder()
					                           .withSDEDatabaseAdapter(this.getSDEDatabaseAdapter())
					                           .build();
		}
		return this.planetaryRepository;
	}
}
