package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.SharedPreferences;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.AndroidFileSystemAdapter;
import org.dimensinfin.eveonline.neocom.app.ESIAndroidAdapter;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.exception.EErrorType;
import org.dimensinfin.eveonline.neocom.app.exception.NeoComError;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.conf.AndroidConfigurationProvider;
import org.dimensinfin.eveonline.neocom.conf.GlobalAndroidPreferencesProvider;
import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.database.NeoComAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.services.TimedUpdater;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.DataDownloaderService;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.EveItemProvider;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.NeoComAndroid;

public class AppSplashFragment extends MVCPagerFragment {
	// - O V E R R I D E S
	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return this.getAppContext().getResources().getString(R.string.appname);
	}

	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new CredentialDatasource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier(this.getTitle())
				       .addIdentifier("SPLASH")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withCacheStatus(true)
				       .withName(this.getAppContext().getResources().getString(R.string.appname))
				       .withVersion(this.getAppContext().getResources().getString(R.string.appversionname))
				       .withApplicationContext(this.getAppContext().getApplicationContext())
				       .build();
	}

	// - C R E D E N T I A L   D A T A S O U R C E
	public static class CredentialDatasource extends AMVCDataSource {
		private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
		private String name;
		private String version;
		private Context applicationContext;
		private SharedPreferences preferences;

		// -  S H A R E D   C O M P O N E N T S
		private IFileSystem fileSystemAdapter;
		private IConfigurationProvider configurationProvider;
		private IGlobalPreferencesManager preferencesProvider;
		private ESIGlobalAdapter esiAdapter;
		private NeoComAndroidDBHelper neocomDBHelper;

		private List<Callable<List<NeoComError>>> initializationTasksList = new ArrayList<>();

		private CredentialDatasource setName( final String name ) {
			this.name = name;
			return this;
		}

		private CredentialDatasource setVersion( final String version ) {
			this.version = version;
			return this;
		}

		public Context getApplicationContext() {
			return applicationContext;
		}

		public CredentialDatasource setApplicationContext( final Context applicationContext ) {
			this.applicationContext = applicationContext;
			return this;
		}

		@Override
		public void prepareModel() {
			logger.info(">> [CredentialDatasource.prepareModel]");
			// Create the model for the components initialisation. Keep this simple until mode functionality is added.
			// - C O M P O N E N T S   C O R E
			initializationTasksList.add(() -> {
				final List<NeoComError> exceptionList = new ArrayList<>();
				try {
					fileSystemAdapter = new AndroidFileSystemAdapter.Builder(
							this.getApplicationContext().getResources().getString(R.string.appname)
							, this.getApplicationContext())
							                    .build();
					configurationProvider = new AndroidConfigurationProvider.Builder(
							this.getApplicationContext().getResources().getString(R.string.propertiesLocation))
							                        .withApplicationContext(this.getApplicationContext())
							                        .build();
					preferencesProvider = new GlobalAndroidPreferencesProvider();
					esiAdapter = new ESIAndroidAdapter.Builder(configurationProvider, fileSystemAdapter)
							             .build();
				} catch (RuntimeException rtex) {
					exceptionList.add(new NeoComError.Builder(rtex)
							                  .withType(EErrorType.RUNTIME)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				} catch (Exception ex) {
					exceptionList.add(new NeoComError.Builder(ex)
							                  .withType(EErrorType.EXCEPTION)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				}
				return exceptionList;
			});
			// - N E O C O M D B H E L P E R
			initializationTasksList.add(() -> {
				final List<NeoComError> exceptionList = new ArrayList<>();
				try {
					neocomDBHelper = new NeoComAndroidDBHelper.Builder()
							                 .withAppContext(this.getApplicationContext())
							                 .withFileSystem(fileSystemAdapter)
							                 .setDatabaseLocation(this.getApplicationContext().getResources().getString(R.string.appname))
							                 .setDatabaseName(this.getApplicationContext().getResources().getString(R.string.appdatabasefilename))
							                 .setDatabaseVersion(Integer.valueOf(this.getApplicationContext().getResources().getString(
									                 R.string.appdatabaseversion))
							                 )
							                 .build();
				} catch (RuntimeException rtex) {
					exceptionList.add(new NeoComError.Builder(rtex)
							                  .withType(EErrorType.RUNTIME)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				} catch (Exception ex) {
					exceptionList.add(new NeoComError.Builder(ex)
							                  .withType(EErrorType.EXCEPTION)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				}
				return exceptionList;
			});
			// - C O M P O N E N T   F A C T O R Y
			initializationTasksList.add(() -> {
				final List<NeoComError> exceptionList = new ArrayList<>();
				try {
					final NeoComAndroid neocom = new NeoComAndroid.Singletoner()
							                             .withApplicationContext(this.getApplicationContext())
							                             .withFileSystemAdapter(fileSystemAdapter)
							                             .withConfigurationProvider(configurationProvider)
							                             .withPreferencesProvider(preferencesProvider)
							                             .withESIAdapter(esiAdapter)
							                             .withDataDownloaderService(new DataDownloaderService())
							                             .withMiningRepository(new MiningRepository.Builder()
									                                                   .withMiningExtractionDao(neocomDBHelper.getMiningExtractionDao())
									                                                   .build()
							                             )
							                             .withEveItemProvider(new EveItemProvider.Builder(esiAdapter).build())
							                             .build();
				} catch (RuntimeException rtex) {
					exceptionList.add(new NeoComError.Builder(rtex)
							                  .withType(EErrorType.RUNTIME)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				} catch (Exception ex) {
					exceptionList.add(new NeoComError.Builder(ex)
							                  .withType(EErrorType.EXCEPTION)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				}
				return exceptionList;
			});
			// - T I M E D   S E R V I C E
			initializationTasksList.add(() -> {
				final List<NeoComError> exceptionList = new ArrayList<>();
				try {
					final TimedUpdater timedService = new TimedUpdater.Builder()
							                                  .withESIAdapter(NeoComAndroid.getSingleton().getEsiAdapter())
							                                  .build();
					// Send the timer to the application singleton.
					NeoComApplication.setTimedUpdater(timedService);
				} catch (RuntimeException rtex) {
					exceptionList.add(new NeoComError.Builder(rtex)
							                  .withType(EErrorType.RUNTIME)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				} catch (Exception ex) {
					exceptionList.add(new NeoComError.Builder(ex)
							                  .withType(EErrorType.EXCEPTION)
							                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
							                  .withOrigin("CredentialDatasource.collaborate2Model")
							                  .build());
				}
				return exceptionList;
			});

			logger.info("<< [CredentialDatasource.prepareModel]");
		}

		@Override
		public void collaborate2Model() {
			try {
				logger.info(">> [CredentialDatasource.collaborate2Model]");
				// - S E C T I O N   H E A D E R
				logger.info("-- [CredentialDatasource.collaborate2Model]> Section Header");
				this.addHeaderContents(new AppVersion()
						                       .setName(this.name)
						                       .setVersion(this.version));
				// Add the initialisation tasks read from the list.
				for (Callable<List<NeoComError>> task : this.initializationTasksList) {
					this.addHeaderContents(new InitialisationTask.Builder()
							                       .withTask(task)
							                       .build());
				}
				// - S E C T I O N   D A T A
				// Add the panel title up front of the Credential list.
				this.addModelContents(new PanelTitle("CREDENTIAL LIST"));
				//				final String esiServer = this.preferences.getString(DATASOURCE_PREFERENCE, NeoComAndroid.DEFAULT_ESI_SERVER).toUpperCase();
				for (Credential credential : AndroidGlobalDataManager.accessAllCredentials("ee")) {
					if (this.validateCredential(credential)) { // Validate credentials so incomplete credentials are removed.
						this.addModelContents(credential);
						logger.info("-- [CredentialListDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
								, credential.getAccountName());
					}
				}
				// Add the node to display the new New Login Button.
				this.addModelContents(new Separator().setType(Separator.ESeparatorType.LINE_WHITE));
				this.addModelContents(new NewLoginAction());
			} catch (RuntimeException runtime) {
				runtime.printStackTrace();
			}
			//			this.addModelContents(new ColonyMap());
			logger.info("<< [CredentialDatasource.collaborate2Model]");
		}

		private boolean validateCredential( final Credential credential ) {
			if (credential.getRefreshToken().equalsIgnoreCase("-TOKEN-")) return false;
			return true;
		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<CredentialDatasource, Builder> {
			private CredentialDatasource onConstruction;

			@Override
			protected CredentialDatasource getActual() {
				if (null == this.onConstruction) this.onConstruction = new CredentialDatasource();
				return this.onConstruction;
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}

			public Builder withName( final String name ) {
				this.getActual().setName(name);
				return this;
			}

			public Builder withVersion( final String version ) {
				this.getActual().setVersion(version);
				return this;
			}

			public Builder withApplicationContext( final Context applicationContext ) {
				this.getActual().setApplicationContext(applicationContext);
				return this;
			}
			//			public Singletoner withCredentialRepository( final CredentialRepository credentialRepository){
			//				this.onConstruction.credentialRepository = credentialRepository;
			//				return this;
			//			}
		}
	}
}
