package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.SharedPreferences;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.domain.WarningPanel;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.EErrorType;
import org.dimensinfin.eveonline.neocom.exception.NeoComError;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

public class AppSplashFragment extends AMVCPagerFragment {
	// - O V E R R I D E S
	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return this.getActivityContext().getResources().getString(R.string.appname);
	}

	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new SplashActionsDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier(this.getTitle())
				       .addIdentifier("SPLASH")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       //				       .withCacheStatus(true)
				       .withName(this.getActivityContext().getResources().getString(R.string.appname))
				       .withVersion(this.getActivityContext().getResources().getString(R.string.appversionname))
				       .withApplicationContext(this.getActivityContext().getApplicationContext())
				       .build();
	}

	/**
	 * This is the first data source to be created. It belongs to the Splash activity that will show two sets of data. The first
	 * set is shown on the header and are the list of initialisation tasks and their current status.
	 * The second set are the available credentials for the server selected, be it the production or the testing.
	 *
	 * Both sets of data require network access so I have to add a network detection and change the data source results depending on the
	 * state of that detection.
	 */
	// - C R E D E N T I A L   D A T A S O U R C E
	public static class SplashActionsDataSource extends AMVCDataSource {
		private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
		private String name;
		private String version;
		private Context applicationContext;
		private SharedPreferences preferences;

		// -  S H A R E D   C O M P O N E N T S
		private ESIGlobalAdapter esiAdapter;

		private List<Callable<List<NeoComError>>> initializationTasksList = new ArrayList<>();

		private SplashActionsDataSource setName( final String name ) {
			this.name = name;
			return this;
		}

		private SplashActionsDataSource setVersion( final String version ) {
			this.version = version;
			return this;
		}

		public Context getApplicationContext() {
			return applicationContext;
		}

		public SplashActionsDataSource setApplicationContext( final Context applicationContext ) {
			this.applicationContext = applicationContext;
			return this;
		}

		@Override
		public void prepareModel() {
			logger.info(">> [SplashActionsDataSource.prepareModel]");
			// Change the contents depending on the network status
			if (NeoComApplication.checkNetworkAccess()) {
				// - D O W N L O A D - M A R K E T P R I C E S
				initializationTasksList.add(() -> {
					final List<NeoComError> exceptionList = new ArrayList<>();
					try {
						this.esiAdapter.downloadItemPrices();
					} catch (RuntimeException rtex) {
						exceptionList.add(new NeoComError.Builder(rtex)
								                  .withType(EErrorType.RUNTIME)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					} catch (Exception ex) {
						exceptionList.add(new NeoComError.Builder(ex)
								                  .withType(EErrorType.EXCEPTION)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					}
					return exceptionList;
				});
				// - D O W N L O A D - P I L O T F A M I L Y
				initializationTasksList.add(() -> {
					final List<NeoComError> exceptionList = new ArrayList<>();
					try {
						this.esiAdapter.downloadPilotFamilyData();
					} catch (RuntimeException rtex) {
						exceptionList.add(new NeoComError.Builder(rtex)
								                  .withType(EErrorType.RUNTIME)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					} catch (Exception ex) {
						exceptionList.add(new NeoComError.Builder(ex)
								                  .withType(EErrorType.EXCEPTION)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					}
					return exceptionList;
				});
				// - D O W N L O A D - C I T A D E L S
				initializationTasksList.add(() -> {
					final List<NeoComError> exceptionList = new ArrayList<>();
					try {
						AndroidGlobalDataManager.readLocationsDataCache();
					} catch (RuntimeException rtex) {
						exceptionList.add(new NeoComError.Builder(rtex)
								                  .withType(EErrorType.RUNTIME)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					} catch (Exception ex) {
						exceptionList.add(new NeoComError.Builder(ex)
								                  .withType(EErrorType.EXCEPTION)
								                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
								                  .withOrigin("SplashActionsDataSource.collaborate2Model")
								                  .build());
					}
					return exceptionList;
				});
			} else {
				this.addHeaderContents(new AppVersion()
						                       .setName(this.name)
						                       .setVersion(this.version));
				this.addHeaderContents(new WarningPanel.Builder()
						                       .withType("WARNING")
						                       .withDescription("The is no network connection. Application functionality in hold.")
						                       .build());
			}
			logger.info("<< [SplashActionsDataSource.prepareModel]");
		}

		@Override
		public void collaborate2Model() {
			if (NeoComApplication.checkNetworkAccess()) {
				try {
					logger.info(">> [SplashActionsDataSource.collaborate2Model]");
					// - S E C T I O N   H E A D E R
					logger.info("-- [SplashActionsDataSource.collaborate2Model]> Section Header");
					this.addHeaderContents(new AppVersion()
							                       .setName(this.name)
							                       .setVersion(this.version));
					for (Callable<List<NeoComError>> task : this.initializationTasksList) {
						this.addHeaderContents(new InitialisationTask(task).schedule());
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
			}
			logger.info("<< [SplashActionsDataSource.collaborate2Model]");
		}

		private boolean validateCredential( final Credential credential ) {
			if (credential.getRefreshToken().equalsIgnoreCase("-TOKEN-")) return false;
			return true;
		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<SplashActionsDataSource, Builder> {
			private SplashActionsDataSource onConstruction;

			@Override
			protected SplashActionsDataSource getActual() {
				if (null == this.onConstruction) this.onConstruction = new SplashActionsDataSource();
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

			public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
				this.onConstruction.esiAdapter = esiAdapter;
				return this;
			}
		}
	}
}
