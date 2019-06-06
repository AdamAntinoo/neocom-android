package org.dimensinfin.eveonline.neocom.app.adapters;

import java.sql.SQLException;
import java.util.Objects;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.conf.AndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.conf.GlobalAndroidPreferencesProvider;
import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.database.NeoComAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;
import org.dimensinfin.eveonline.neocom.services.ESIDataPersistenceService;
import org.dimensinfin.eveonline.neocom.services.TimedUpdater;

import com.j256.ormlite.dao.Dao;

/**
 * This is a singleton with global access that will contain application component references so they can be injected to other components. The creation
 * of those components will be down internally on demand and with the knowledge of what components depend on other components.
 *
 * @author Adam Antinoo
 */
public class NeoComAndroid {
	public static final String DEFAULT_ESI_SERVER = "Tranquility";

	private static NeoComAndroid singleton;
	private static Application application;

	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystemAdapter;
	private IGlobalPreferencesManager preferencesProvider;
	private ESIGlobalAdapter esiAdapter;
	private DataDownloaderService downloaderService;
	private NeoComAndroidDBHelper neocomDBHelper;
	private ESIDataPersistenceService persistenceService;
	private CredentialRepository credentialRepository;
	private MiningRepository miningRepository;
	private TimedUpdater timedService;

	public static NeoComAndroid initialiseSingleton( final Application newApplication ) {
		application = newApplication;
		if (null == singleton) singleton = new NeoComAndroid();
		return singleton;
	}

	public static NeoComAndroid getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException("NeoCom global singleton is not instantiated. Please complete initialisation.");
		return singleton;
	}

	public Application getApplication() {
		if (null == application)
			throw new NeoComRuntimeException("NeoCom global singleton is not instantiated. Please complete initialisation.");
		return application;
	}

	public IConfigurationProvider getConfigurationProvider() {
		if (null == this.configurationProvider) {
			this.configurationProvider = new AndroidConfigurationProvider.Builder(
					application.getResources().getString(R.string.propertiesLocation))
					                             .withApplicationContext(application)
					                             .build();
		}
		return this.configurationProvider;
	}

	public IGlobalPreferencesManager getPreferencesProvider() {
		if (null == this.preferencesProvider) {
			preferencesProvider = new GlobalAndroidPreferencesProvider.Builder()
					                      .withApplication(this.getApplication())
					                      .build();
		}
		return this.preferencesProvider;
	}


	public IFileSystem getFileSystemAdapter() {
		if (null == this.fileSystemAdapter) {
			fileSystemAdapter = new AndroidFileSystemAdapter.Builder(application.getResources().getString(R.string.appname), application)
					                    .build();
		}
		return this.fileSystemAdapter;
	}

	public ESIGlobalAdapter getEsiAdapter() {
		if (null == this.esiAdapter) {
			esiAdapter = new ESIGlobalAdapter.Builder(this.getConfigurationProvider(), this.getFileSystemAdapter())
					             .build();
		}
		return this.esiAdapter;
	}

	public DataDownloaderService getDownloaderService() {
		if (null == this.downloaderService) {
			downloaderService = new DataDownloaderService.Builder(this.getEsiAdapter())
					                    .build();
		}
		return this.downloaderService;
	}

	public TimedUpdater getTimedService() {
		if (null == this.timedService) {
			timedService = new TimedUpdater.Builder()
					               .withESIAdapter(this.getEsiAdapter())
								   .withPreferencesProvider(this.getPreferencesProvider())
					               .withESIDataPersistenceService(this.getPersistenceService())
								   .withCredentialRepository(this.getCredentialRepository())
					               .build();

		}
		return this.timedService;
	}

	public ESIDataPersistenceService getPersistenceService() {
		if (null == this.persistenceService) {
			try {
				final Dao<MiningExtraction, String> miningExtractionDao = this.getNeoComDBHelper().getMiningExtractionDao();
				persistenceService = new ESIDataPersistenceService.Builder()
						                     .withEsiAdapter(esiAdapter)
						                     .withMiningRepository(this.getMiningRepository())
						                     .build();
			} catch (SQLException sqle) {
				persistenceService = null;
				Objects.requireNonNull(persistenceService);
			}
		}
		return this.persistenceService;
	}

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

	public void SDE() {

	}

	/*


		// Load the SDE data
		//		SDEExternalDataManager.loadSDEData();


		// Connect the NeoCom database.
		logger.info("-- [NeoComApp.onCreate]> Connecting NeoCom private database...");
		//		try {
		AndroidGlobalDataManager.connectNeoComDBConnector(new NeoComAndroidDBHelper.Builder().build());
*/
}
