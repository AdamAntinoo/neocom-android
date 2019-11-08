package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.adapters.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.adapters.IFileSystem;
import org.dimensinfin.eveonline.neocom.adapters.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.adapters.StoreCacheManager;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidFileSystemAdapter;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidNeoComDBHelper;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidSDEDBHelper;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.LocationRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.domain.EveItem;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.provider.AssetsRepository;
import org.dimensinfin.eveonline.neocom.updaters.NeoComUpdater;

/**
 * This is a singleton with global access that will contain application component references so they can be injected to other components. The creation
 * of those components will be down internally on demand and with the knowledge of what components depend on other components.
 *
 * @author Adam Antinoo
 */
public class NeoComComponentFactory {
	//	public static final String DEFAULT_ESI_SERVER = "Tranquility";
//	protected static Logger logger = LoggerFactory.getLogger(NeoComComponentFactory.class);
	private static NeoComComponentFactory singleton; // The singleton for the factory. This should be created at start time and setting the application.
	private static NeoComApplication application; // The link to the application that is required by the component factory.

	public static NeoComComponentFactory initialiseSingleton( final Application newApplication ) {
		Objects.requireNonNull( newApplication );
		application = (NeoComApplication) newApplication;
		if (null == singleton) singleton = new NeoComComponentFactory();
		return singleton;
	}

	public static NeoComComponentFactory getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException( "NeoCom global singleton is not instantiated. Please complete initialisation." );
		return singleton;
	}

	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystemAdapter;
	//	@Deprecated
//	private IGlobalPreferencesManager preferencesProvider;
	//	private ESIGlobalAdapter esiAdapter;
	private ESIDataAdapter esiDataAdapter;
	private AndroidNeoComDBHelper neocomDBHelper;
	private ISDEDatabaseAdapter sdeDatabaseAdapter;
	private LocationCatalogService locationCatalogService;

	//	@Deprecated
//	private DataDownloaderService downloaderService;
	private CredentialRepository credentialRepository;
	private LocationRepository locationRepository;
	private AssetsRepository assetsRepository;
	private MiningRepository miningRepository;
	private PlanetaryRepository planetaryRepository;

	private StoreCacheManager storageCache;

	// - G E T T E R S
	public NeoComApplication getApplication() {
		if (null == application)
			throw new NeoComRuntimeException( "NeoCom application is not associated to factory. Please complete initialisation." );
		return application;
	}

	// - A C C E S S O R S
	public IConfigurationProvider getConfigurationProvider() {
		try {
			if (null == this.configurationProvider) {
				this.configurationProvider = new AndroidConfigurationProvider.Builder()
						                             .withApplicationContext( application )
						                             .withConfigurationDirectory(
								                             application.getResources().getString( R.string.propertiesLocation ) )
						                             .build();
			}
		} catch (final IOException ioe) {
			singleton = null;
			throw new NeoComRuntimeException( "Configuration provider initialization exception. Application not configured." );
		}
		return this.configurationProvider;
	}

	public IFileSystem getFileSystemAdapter() {
		if (null == this.fileSystemAdapter)
			fileSystemAdapter = new AndroidFileSystemAdapter.Builder()
					                    .withApplicationContext( application )
					                    .withApplicationDirectory( application.getResources().getString( R.string.appname ) )
					                    .build();
		return this.fileSystemAdapter;
	}

	public ESIDataAdapter getEsiDataAdapter() {
		if (null == this.esiDataAdapter) {
			this.esiDataAdapter = new ESIDataAdapter.Builder()
					                      .withConfigurationProvider( this.getConfigurationProvider() )
					                      .withFileSystemAdapter( this.getFileSystemAdapter() )
					                      .withLocationCatalogService( this.getLocationCatalogService() )
					                      .build();
			EveItem.injectEsiDataAdapter( this.esiDataAdapter );
			NeoComUpdater.injectsEsiDataAdapter( this.esiDataAdapter );
//			MarketDataSet.injectEsiDataAdapter(this.esiDataProvider);
		}
		return this.esiDataAdapter;
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
//					                              .withSDEDatabaseAdapter(this.getSDEDatabaseAdapter())
					                              .withLocationRepository( this.getLocationRepository() )
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

	public AssetsRepository getAssetsRepository() {
		if (null == this.assetsRepository) {
			try {
				this.assetsRepository = new AssetsRepository.Builder()
						                        .withAssetDao( this.getNeoComDBHelper().getAssetDao() )
						                        .build();
			} catch (SQLException sqle) {
				this.assetsRepository = null;
				Objects.requireNonNull( this.assetsRepository );
			}
		}
		return this.assetsRepository;
	}
//
//	@Deprecated
//	public DataDownloaderService getDownloaderService() {
//		if (null == this.downloaderService) {
//			downloaderService = new DataDownloaderService.Builder(this.getEsiDataAdapter())
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
						                   .withEsiDataAdapter( this.getEsiDataAdapter() )
						                   .withMiningExtractionDao( this.getNeoComDBHelper().getMiningExtractionDao() )
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
