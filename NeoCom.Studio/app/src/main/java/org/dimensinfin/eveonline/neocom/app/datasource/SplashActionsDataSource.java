package org.dimensinfin.eveonline.neocom.app.datasource;

import java.sql.SQLException;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.domain.Spacer;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.NewLoginAction;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.persisters.CredentialPersistent;
import org.dimensinfin.eveonline.neocom.backend.NeoComBackendService;
import org.dimensinfin.eveonline.neocom.backend.rest.v1.CredentialStoreResponse;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationService;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.service.scheduler.CredentialJobGeneratorJob;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.service.scheduler.conf.ISchedulerConfiguration;
import org.dimensinfin.eveonline.neocom.service.scheduler.domain.CronSchedulePropertyDefinitions;
import org.dimensinfin.eveonline.neocom.updater.CredentialUpdater;

/**
 * This data source will read all registered credentials on the repository and generate a record for each one of them. During the process it should
 * check the credential validity and if valid synchronize the new credential data to the backend server so it can delegate some periodic tasks.
 * After credential synchronization then we can start the local scheduled tasks to update the local repository.
 */
public class SplashActionsDataSource extends MVCDataSource {
	private static final String CREDENTIAL_LIST_PANEL_TITLE = "CREDENTIAL LIST";
	private static final String DEFAULT_CREDENTIAL_JOB_GENERATOR_SCHEDULE = "0/5 - *";
	private String name;
	private String version;

	// -  C O M P O N E N T S
	private IConfigurationService configurationService;
	private ESIDataProvider esiDataProvider;
	private LocationCatalogService locationCatalogService;
	private AssetRepository assetRepository;
	private CredentialRepository credentialRepository;
	private MiningRepository miningRepository;
	private ISchedulerConfiguration schedulerConfiguration;
	private NeoComBackendService backendService;

	@LogEnterExit
	@Override
	public void prepareModel() {
		// - S E C T I O N   H E A D E R
		NeoComLogger.enter();
		this.addHeaderContents( new AppVersion.Builder()
				                        .withAppName( this.name )
				                        .withAppVersion( this.version )
				                        .build() );
		// Make sure the credential job generator is registered.
		JobScheduler.getJobScheduler().registerJob(
				new CredentialJobGeneratorJob.Builder()
						.withConfigurationService( this.configurationService )
						.withAssetRepository( this.assetRepository )
						.withCredentialRepository( this.credentialRepository )
						.withMiningRepository( this.miningRepository )
						.withEsiDataProvider( this.esiDataProvider )
						.withLocationCatalogService( this.locationCatalogService )
						.withSchedulerConfiguration( this.schedulerConfiguration )
						.addCronSchedule( this.configurationService.getResourceString(
								CronSchedulePropertyDefinitions.CRON_SCHEDULE_CREDENTIAL_JOB_GENERATOR,
								DEFAULT_CREDENTIAL_JOB_GENERATOR_SCHEDULE ) )
						.build()
		);
		NeoComLogger.exit();
	}

	@LogEnterExit
	@Override
	public void collaborate2Model() {
		NeoComLogger.enter();
		try {
			// - S E C T I O N   D A T A
			// Add the panel title up front of the Credential list.
			this.addModelContents( new PanelTitle( CREDENTIAL_LIST_PANEL_TITLE ) );
			for (Credential credential : this.credentialRepository.accessAllCredentials()) {
				if (this.validateCredential( credential )) { // Validate credentials so incomplete credentials are removed.
					this.addModelContents( credential );
					NeoComLogger.info( "Adding '{}' to the _dataModelRoot", credential.getAccountName() );
					// Synchronize the credential data to the backend server and get the new authorization token.
					final String jwtToken = this.synchronizeCredential( credential );
					if (null != jwtToken) credential.setJwtToken( jwtToken );
					// Prepare the jobs to launch
					new CredentialUpdater( credential ).update( false );
				}
			}
			this.addModelContents( new Spacer.Builder().withType( SpacerType.LINE_WHITE ).build() );
			this.addModelContents( new NewLoginAction() ); // Add the node to display the new New Login Button.
		} catch (final RuntimeException runtime) {
			NeoComLogger.info( "RuntimeException: {}", runtime );
			this.addModelContents( new ExceptionReport( runtime ) );
			// Add the node to display the new New Login Button.
			this.addModelContents( new Spacer.Builder().withType( SpacerType.LINE_WHITE ).build() );
			this.addModelContents( new NewLoginAction() );
		} finally {
			NeoComLogger.exit();
		}
	}

	private boolean validateCredential( final Credential credential ) {
		return !credential.getRefreshToken().equalsIgnoreCase( "-TOKEN-" );
	}

	/**
	 * Store the credential on the backend repository to allow unattended periodic tasks. If the process fails the tokes is null so this feature
	 * will get disabled automatically.
	 *
	 * @param credential the credential to share with the backend.
	 * @return the JWT token to be used on next call to the backend to retrieve data.
	 */
	private String synchronizeCredential( final Credential credential ) {
		final CredentialStoreResponse credentialResponse = this.backendService.storeCredential( credential );
		if (null != credentialResponse) return credentialResponse.getJwtToken();
		else return null;
	}

	/**
	 * Detect events from the controllers and if the affect the Credentials (because they have updaters) then try to persist the
	 * new instance values on the repository.
	 *
	 * @param event the event to be processed. Event have a property name that is used as a selector.
	 */
	@Override
	public synchronized void receiveEvent( final IntercommunicationEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase( EEvents.EVENT_REFRESHDATA.name() )) {
			final Object target = event.getNewValue();
			if (target instanceof CredentialController) { // This only applies to Credentials
				try {
					new CredentialPersistent.Builder()
							.withCredentialRepository( this.credentialRepository )
							.build()
							.persist( ((CredentialController) target).getModel() );
				} catch (final SQLException sqle) {
					NeoComLogger.info( "Exception persisting Credential update.", sqle.getCause().toString() );
				}
			}
			return;
		}
		super.receiveEvent( event );
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<SplashActionsDataSource, SplashActionsDataSource.Builder> {
		private SplashActionsDataSource onConstruction;

		public Builder() {
			super();
			this.onConstruction = new SplashActionsDataSource();
		}

		@Override
		protected SplashActionsDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new SplashActionsDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public SplashActionsDataSource.Builder withName( final String name ) {
			if (null != name) this.onConstruction.name = name;
			return this;
		}

		public SplashActionsDataSource.Builder withVersion( final String version ) {
			if (null != version) this.onConstruction.version = version;
			return this;
		}

		public SplashActionsDataSource.Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			Objects.requireNonNull( credentialRepository );
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}

		public SplashActionsDataSource.Builder withMiningRepository( final MiningRepository miningRepository ) {
			Objects.requireNonNull( miningRepository );
			this.onConstruction.miningRepository = miningRepository;
			return this;
		}

		public SplashActionsDataSource.Builder withAssetRepository( final AssetRepository assetRepository ) {
			Objects.requireNonNull( assetRepository );
			this.onConstruction.assetRepository = assetRepository;
			return this;
		}

		public SplashActionsDataSource.Builder withConfigurationService( final IConfigurationService configurationService ) {
			Objects.requireNonNull( configurationService );
			this.onConstruction.configurationService = configurationService;
			return this;
		}

		public SplashActionsDataSource.Builder withEsiDataProvider( final ESIDataProvider esiDataProvider ) {
			Objects.requireNonNull( esiDataProvider );
			this.onConstruction.esiDataProvider = esiDataProvider;
			return this;
		}

		public SplashActionsDataSource.Builder withBackendService( final NeoComBackendService backendService ) {
			Objects.requireNonNull( backendService );
			this.onConstruction.backendService = backendService;
			return this;
		}

		public SplashActionsDataSource.Builder withSchedulerConfiguration( final ISchedulerConfiguration schedulerConfiguration ) {
			Objects.requireNonNull( schedulerConfiguration );
			this.onConstruction.schedulerConfiguration = schedulerConfiguration;
			return this;
		}

		public SplashActionsDataSource.Builder withLocationCatalogService( final LocationCatalogService locationCatalogService ) {
			Objects.requireNonNull( locationCatalogService );
			this.onConstruction.locationCatalogService = locationCatalogService;
			return this;
		}

		@Override
		public SplashActionsDataSource build() {
			super.build();
			Objects.requireNonNull( this.onConstruction.configurationService );
			Objects.requireNonNull( this.onConstruction.esiDataProvider );
			Objects.requireNonNull( this.onConstruction.locationCatalogService );
			Objects.requireNonNull( this.onConstruction.credentialRepository );
			Objects.requireNonNull( this.onConstruction.assetRepository );
			Objects.requireNonNull( this.onConstruction.miningRepository );
			Objects.requireNonNull( this.onConstruction.schedulerConfiguration );
			Objects.requireNonNull( this.onConstruction.backendService );
			return this.onConstruction;
		}
	}
}
