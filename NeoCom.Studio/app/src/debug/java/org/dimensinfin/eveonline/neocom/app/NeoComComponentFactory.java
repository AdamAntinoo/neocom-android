package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.adapter.StoreCacheManager;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidConfigurationService;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidFileSystemAdapter;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidNeoComDBHelper;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidSDEDBHelper;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.LocationRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.domain.NeoItem;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.ESIUniverseDataProvider;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;
import org.dimensinfin.eveonline.neocom.provider.RetrofitFactory;
import org.dimensinfin.eveonline.neocom.updater.NeoComUpdater;

/**
 * This is a singleton with global access that will contain application component references so they can be injected to other components. The creation
 * of those components will be down internally on demand and with the knowledge of what components depend on other components.
 *
 * @author Adam Antinoo
 */
public class NeoComComponentFactory {
	private static NeoComComponentFactory singleton; // The singleton for the factory. This should be created at start time and setting the application.
	private static NeoComApplication application; // The link to the application that is required by the component factory.

	public static NeoComComponentFactory initialiseSingleton( final Application newApplication ) {
		Objects.requireNonNull( newApplication );
		application = (NeoComApplication) newApplication;
		if (null == singleton) singleton = new NeoComComponentFactory();
		return singleton;
	}

	/**
	 * The singleton is not created on first request because it requires the application instance. So if the application did not call the
	 * initialisation then this throws an exception as a safeguard.
	 * @return
	 */
	public static NeoComComponentFactory getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException( "NeoCom global singleton is not instantiated. Please complete initialisation." );
		return singleton;
	}

	// - COMPONENTS
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystemAdapter;
	private RetrofitFactory retrofitFactory;
	private StoreCacheManager storeCacheManager;
	private ESIDataProvider esiDataProvider;
	private ESIUniverseDataProvider esiUniverseDataProvider;
	private LocationCatalogService locationCatalogService;
	private StoreCacheManager storageCache;

	private AndroidNeoComDBHelper neocomDBHelper;
	private ISDEDatabaseAdapter sdeDatabaseAdapter;

	//	@Deprecated
//	private DataDownloaderService downloaderService;
	private CredentialRepository credentialRepository;
	private LocationRepository locationRepository;
	private AssetRepository assetRepository;
	private MiningRepository miningRepository;
	private PlanetaryRepository planetaryRepository;

	// - G E T T E R S
	public NeoComApplication getApplication() {
		if (null == application)
			throw new NeoComRuntimeException( "NeoCom application is not associated to factory. Please complete initialisation." );
		return application;
	}

	// - R E P L A C E M E N T S -- D E B U G   O N L Y
	public void replaceCredentialRepository( final CredentialRepository credentialRepository ) {
		this.credentialRepository = credentialRepository;
	}

	public void replaceAssetRepository( final AssetRepository assetRepository ) {
		this.assetRepository = assetRepository;
	}

	public void replaceESIDataProvider( final ESIDataProvider dataProvider ) {
		this.esiDataProvider = dataProvider;
	}

	// - A C C E S S O R S
	public IConfigurationProvider getConfigurationProvider() {
//		try {
			if (null == this.configurationProvider) {
				this.configurationProvider = new AndroidConfigurationService.Builder()
						                             .withApplicationContext( application )
						                             .withConfigurationDirectory(
								                             application.getResources().getString( R.string.propertiesLocation ) )
						                             .build();
			}
//		} catch (final IOException ioe) {
//			singleton = null;
//			throw new NeoComRuntimeException( "Configuration provider initialization exception. Application not configured." );
//		}
		return this.configurationProvider;
	}

	public IFileSystem getFileSystemAdapter() {
		if (null == this.fileSystemAdapter)
			this.fileSystemAdapter = new AndroidFileSystemAdapter.Builder()
					                    .withApplicationContext( application )
					                    .withApplicationDirectory( application.getResources().getString( R.string.appname ) )
					                    .build();
		return this.fileSystemAdapter;
	}

	public RetrofitFactory getRetrofitFactory() {
		if (null == this.retrofitFactory) {
			this.retrofitFactory = new RetrofitFactory.Builder()
					                       .withConfigurationProvider( this.getConfigurationProvider() )
					                       .withFileSystemAdapter( this.getFileSystemAdapter() )
					                       .build();
		}
		return this.retrofitFactory;
	}

	public StoreCacheManager getStoreCacheManager() {
		if (null == this.storeCacheManager) {
			this.storeCacheManager = new StoreCacheManager.Builder()
					                         .withConfigurationProvider( this.getConfigurationProvider() )
					                         .withFileSystemAdapter( this.getFileSystemAdapter() )
					                         .withRetrofitFactory( this.getRetrofitFactory() )
					                         .build();
		}
		return this.storeCacheManager;
	}

	public ESIDataProvider getESIDataProvider() {
		if (null == this.esiDataProvider) {
			this.esiDataProvider = new ESIDataProvider.Builder()
					                       .withConfigurationProvider( this.getConfigurationProvider() )
					                       .withFileSystemAdapter( this.getFileSystemAdapter() )
					                       .withLocationCatalogService( this.getLocationCatalogService() )
					                       .withStoreCacheManager( this.getStoreCacheManager() )
					                       .withRetrofitFactory( this.getRetrofitFactory() )
					                       .build();
			NeoComUpdater.injectsEsiDataAdapter( this.esiDataProvider );
		}
		return this.esiDataProvider;
	}

	public ESIUniverseDataProvider getESIUniverseDataProvider() {
		if (null == this.esiUniverseDataProvider) {
			this.esiUniverseDataProvider = new ESIUniverseDataProvider.Builder()
					                               .withConfigurationProvider( this.getConfigurationProvider() )
					                               .withFileSystemAdapter( this.getFileSystemAdapter() )
					                               .withRetrofitFactory( this.getRetrofitFactory() )
					                               .withStoreCacheManager( this.getStoreCacheManager() )
					                               .build();
			NeoItem.injectEsiUniverseDataAdapter( this.esiUniverseDataProvider );
		}
		return this.esiUniverseDataProvider;
	}

	public ISDEDatabaseAdapter getSDEDatabaseAdapter() {
		try {
			if (null == this.sdeDatabaseAdapter) {
				final String databaseName = this.getApplication().getResources().getString( R.string.sdedatabasefilename );
				this.sdeDatabaseAdapter = new AndroidSDEDBHelper.Builder()
						                          .withAppContext( this.getApplication() )
						                          .withDatabaseName( databaseName )
						                          .build( this.getFileSystemAdapter() );
			}
		} catch (final IOException ioe) {
			throw new NeoComRuntimeException( "SDE database open failure. Filesystem exception during open action." );
		}
		return this.sdeDatabaseAdapter;
	}

	public LocationCatalogService getLocationCatalogService() {
		if (null == this.locationCatalogService) {
			this.locationCatalogService = new LocationCatalogService.Builder()
					                              .withConfigurationProvider( this.getConfigurationProvider() )
					                              .withFileSystemAdapter( this.getFileSystemAdapter() )
					                              .withRetrofitFactory( this.getRetrofitFactory() )
					                              .withESIUniverseDataProvider( this.getESIUniverseDataProvider() )
					                              .build();
		}
		return this.locationCatalogService;
	}

	public LocationRepository getLocationRepository() {
		if (null == this.locationRepository) {
//			try {
			this.locationRepository = new LocationRepository.Builder()
					                          .withSDEDatabaseAdapter( this.getSDEDatabaseAdapter() )
//						                          .withLocationDao(this.getSDEDatabaseAdapter().getLocationDao())
					                          .build();
//			} catch (SQLException sqle) {
//				this.locationRepository = null;
//				Objects.requireNonNull(this.locationRepository);
//			}
		}
		return this.locationRepository;
	}

	public AssetRepository getAssetRepository() {
		if (null == this.assetRepository) {
			try {
				this.assetRepository = new AssetRepository.Builder()
						                       .withAssetDao( this.getNeoComDBHelper().getAssetDao() )
						                       .withConnection4Transaction( this.getNeoComDBHelper().getConnectionSource() )
						                       .build();
			} catch (SQLException sqle) {
				this.assetRepository = null;
				Objects.requireNonNull( this.assetRepository );
			}
		}
		return this.assetRepository;
	}
//
//	@Deprecated
//	public DataDownloaderService getDownloaderService() {
//		if (null == this.downloaderService) {
//			downloaderService = new DataDownloaderService.Builder(this.getESIDataProvider())
//					                    .build();
//		}
//		return this.downloaderService;
//	}


//	@Deprecated
//	public IGlobalPreferencesManager getPreferencesProvider() {
//		if (null == this.preferencesProvider) {
//			preferencesProvider = new AndroidPreferencesProvider.Builder()
//					                      .withApplication(this.getApplication())
//					                      .build();
//		}
//		return this.preferencesProvider;
//	}

//	public ESIDataPersistenceService getPersistenceService() {
//		if (null == this.persistenceService) {
//			try {
//				final Dao<MiningExtraction, String> miningExtractionDao = this.getNeoComDBHelper().getMiningExtractionDao();
//				persistenceService = new ESIDataPersistenceService.Builder()
//						                     .withEsiAdapter(esiDataProvider)
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
						                       .withCredentialDao( this.getNeoComDBHelper().getCredentialDao() )
						                       .build();
			} catch (SQLException sqle) {
				credentialRepository = null;
				Objects.requireNonNull( credentialRepository );
			}
		}
		return this.credentialRepository;
	}

	public void setCredentialRepository( final CredentialRepository credentialRepository ) {
		this.credentialRepository = credentialRepository;
	}

	public MiningRepository getMiningRepository() {
		if (null == this.miningRepository) {
			try {
				miningRepository = new MiningRepository.Builder()
						                   .withMiningExtractionDao( this.getNeoComDBHelper().getMiningExtractionDao() )
						                   .withLocationCatalogService( this.getLocationCatalogService() )
						                   .build();
			} catch (SQLException sqle) {
				miningRepository = null;
				Objects.requireNonNull( miningRepository );
			}
		}
		return this.miningRepository;
	}

	public void setMiningRepository( final MiningRepository miningRepository ) {
		this.miningRepository = miningRepository;
	}

	public AndroidNeoComDBHelper getNeoComDBHelper() {
		if (null == this.neocomDBHelper) {
			try {
				neocomDBHelper = new AndroidNeoComDBHelper.Builder()
						                 .withAppContext( application )
						                 .withFileSystem( this.getFileSystemAdapter() )
						                 .setDatabaseLocation( application.getResources().getString( R.string.appname ) )
						                 .setDatabaseName( application.getResources().getString( R.string.appdatabasefilename ) )
						                 .setDatabaseVersion( this.getApplication().getNeoComDatabaseVersion() )
						                 .build();
			} catch (IOException sqle) {
				neocomDBHelper = null;
				Objects.requireNonNull( neocomDBHelper );
			}
		}
		return this.neocomDBHelper;
	}

	public PlanetaryRepository getPlanetaryRepository() {
		if (null == this.planetaryRepository) {
			this.planetaryRepository = new PlanetaryRepository.Builder()
					                           .withSDEDatabaseAdapter( this.getSDEDatabaseAdapter() )
					                           .build();
		}
		return this.planetaryRepository;
	}
}
