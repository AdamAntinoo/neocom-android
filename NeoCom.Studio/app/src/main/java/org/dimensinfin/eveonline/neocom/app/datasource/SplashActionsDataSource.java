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
import org.dimensinfin.eveonline.neocom.asset.service.AssetDownloadProcessorJob;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.updater.CredentialUpdater;

/**
 * This data source will read all registered credentials on the repository and generate a record for each one of them. During the process it should
 * check the credential validity and if valid synchronize the new credential data to the backend server so it can delegate some periodic tasks.
 * After credential synchronization then we can start the local scheduled tasks to update the local repository.
 */
public class SplashActionsDataSource extends MVCDataSource {
	private String name;
	private String version;

	// -  C O M P O N E N T S
	private CredentialRepository credentialRepository;
	private AssetRepository assetRepository;
	private ESIDataProvider esiDataProvider;
	private LocationCatalogService locationCatalogService;

	@LogEnterExit
	@Override
	public void prepareModel() {
		// - S E C T I O N   H E A D E R
		logger.info( ">> [SplashActionsDataSource.prepareModel]" );
		this.addHeaderContents( new AppVersion.Builder()
				                        .withAppName( this.name )
				                        .withAppVersion( this.version )
				                        .build() );
		logger.info( "<< [SplashActionsDataSource.prepareModel]" );
	}

	@LogEnterExit
	@Override
	public void collaborate2Model() {
		try {
			logger.info( ">> [SplashActionsDataSource.collaborate2Model]" );
			// - S E C T I O N   D A T A
			// Add the panel title up front of the Credential list.
			this.addModelContents( new PanelTitle( "CREDENTIAL LIST" ) );
			for (Credential credential : this.credentialRepository.accessAllCredentials()) {
				if (this.validateCredential( credential )) { // Validate credentials so incomplete credentials are removed.
					this.addModelContents( credential );
					logger.info( "-- [CredentialListDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
							, credential.getAccountName() );
					new CredentialSchedulerGenerator( credential )
							.defineJobs( credentialLambda -> JobScheduler.getJobScheduler()
									                                 .registerJob(
											                                 new AssetDownloadProcessorJob.Builder()
													                                 .withCredential( credentialLambda )
													                                 .withAssetRepository( this.assetRepository )
													                                 .withEsiDataProvider( this.esiDataProvider )
													                                 .withLocationCatalogService( this.locationCatalogService )
													                                 .addCronSchedule( "0/15 - *" )
													                                 .build()
									                                 )
							);
					new CredentialUpdater( credential ).update( false );
				}
			}
			this.addModelContents( new Spacer.Builder().withType( SpacerType.LINE_WHITE ).build() );
			this.addModelContents( new NewLoginAction() ); // Add the node to display the new New Login Button.
		} catch (final RuntimeException runtime) {
			logger.info( "-- [SplashActionsDataSource.collaborate2Model]> RuntimeException: {}", runtime.getMessage() );
			this.addModelContents( new ExceptionReport( runtime ) );
			// Add the node to display the new New Login Button.
			this.addModelContents( new Spacer.Builder().withType( SpacerType.LINE_WHITE ).build() );
			this.addModelContents( new NewLoginAction() );
		}
		logger.info( "<< [SplashActionsDataSource.collaborate2Model]" );
	}

	private boolean validateCredential( final Credential credential ) {
		return !credential.getRefreshToken().equalsIgnoreCase( "-TOKEN-" );
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
			if ((null != target) && (target instanceof CredentialController)) { // This only applies to Credentials
				try {
					new CredentialPersistent.Builder()
							.withCredentialRepository( this.credentialRepository )
							.build()
							.persist( ((CredentialController) target).getModel() );
				} catch (SQLException sqle) {
					logger.info( "EX [SplashActionsDataSource.receiveEvent]> Exception persisting Credential update: {}",
							sqle.getMessage() );
				}
			}
			return;
		}
		super.receiveEvent( event );
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<SplashActionsDataSource, SplashActionsDataSource.Builder> {
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

		public SplashActionsDataSource.Builder withName( final String name ) {
			this.onConstruction.name = name;
			return this;
		}

		public SplashActionsDataSource.Builder withVersion( final String version ) {
			this.onConstruction.version = version;
			return this;
		}

		public SplashActionsDataSource.Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			Objects.requireNonNull( credentialRepository );
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}

		public SplashActionsDataSource.Builder withAssetRepository( final AssetRepository assetRepository ) {
			Objects.requireNonNull( assetRepository );
			this.onConstruction.assetRepository = assetRepository;
			return this;
		}

		public SplashActionsDataSource.Builder withEsiDataProvider( final ESIDataProvider esiDataProvider ) {
			Objects.requireNonNull( esiDataProvider );
			this.onConstruction.esiDataProvider = esiDataProvider;
			return this;
		}

		public SplashActionsDataSource.Builder withLocationCatalogService( final LocationCatalogService locationCatalogService ) {
			Objects.requireNonNull( locationCatalogService );
			this.onConstruction.locationCatalogService = locationCatalogService;
			return this;
		}

		public SplashActionsDataSource build() {
			final SplashActionsDataSource instance = super.build();
			Objects.requireNonNull( this.onConstruction.credentialRepository );
			Objects.requireNonNull( this.onConstruction.assetRepository );
			Objects.requireNonNull( this.onConstruction.esiDataProvider );
			Objects.requireNonNull( this.onConstruction.locationCatalogService );
			return instance;
		}
	}
}
