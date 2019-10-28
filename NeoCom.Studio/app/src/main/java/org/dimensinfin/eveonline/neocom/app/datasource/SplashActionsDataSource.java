package org.dimensinfin.eveonline.neocom.app.datasource;

import java.sql.SQLException;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.domain.Spacer;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.NewLoginAction;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.domain.WarningPanel;
import org.dimensinfin.eveonline.neocom.app.persisters.CredentialPersistent;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

public class SplashActionsDataSource extends MVCDataSource {
	private String name;
	private String version;

	// -  C O M P O N E N T S
//		private ESIDataAdapter esiDataAdapter;
	private CredentialRepository credentialRepository;

//		private List<InitialisationTask> initializationTasksList = new ArrayList<>();

	@Override
	public void prepareModel() {
		logger.info(">> [SplashActionsDataSource.prepareModel]");
		this.addHeaderContents(new AppVersion.Builder()
				                       .withAppName(this.name)
				                       .withAppVersion(this.version)
				                       .build());
		logger.info("<< [SplashActionsDataSource.prepareModel]");
	}

	//		@Override
//		public void collaborate2Model() {
//			try {
//				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
	@Override
	public void collaborate2Model() {
		try {
			logger.info(">> [SplashActionsDataSource.collaborate2Model]");
			// - S E C T I O N   H E A D E R
			logger.info("-- [SplashActionsDataSource.collaborate2Model]> Section Header");
			// TODO - The header cannot be tested. Move this agsin to the header once the MVC is reimplemented.
//				this.addHeaderContents(new AppVersion()
//						                       .setAppName(this.name)
//						                       .setAppVersion(this.version));
			if (!NeoComApplication.checkNetworkAccess()) {
				this.addHeaderContents(new WarningPanel.Builder()
						                       .withType("WARNING")
						                       .withDescription(
								                       "The is no network connection. Application functionality in hold.")
						                       .build());
			}
//				for (InitialisationTask task : this.initializationTasksList) {
//					this.addModelContents(task.schedule(2));
//				}
			// - S E C T I O N   D A T A
//				this.addModelContents(new AppVersion()
//						.setAppName(this.name)
//						.setAppVersion(this.version));
			// Add the panel title up front of the Credential list.
			this.addModelContents(new PanelTitle("CREDENTIAL LIST"));
			if (NeoComApplication.checkNetworkAccess()) {
				for (Credential credential : this.credentialRepository.accessAllCredentials()) {
					if (this.validateCredential(credential)) { // Validate credentials so incomplete credentials are removed.
						this.addModelContents(credential);
						logger.info("-- [CredentialListDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
								, credential.getAccountName());
					}
				}
			}
			// Add the node to display the new New Login Button.
			this.addModelContents(new Spacer.Builder().withType(SpacerType.LINE_WHITE).build());
			this.addModelContents(new NewLoginAction());
		} catch (RuntimeException runtime) {
			logger.info("-- [SplashActionsDataSource.collaborate2Model]> RuntimeException: {}", runtime.getMessage());
			this.addModelContents(new ExceptionReport(runtime));
			// Add the node to display the new New Login Button.
			this.addModelContents(new Spacer.Builder().withType(SpacerType.LINE_WHITE).build());
			this.addModelContents(new NewLoginAction());
		}
		logger.info("<< [SplashActionsDataSource.collaborate2Model]");
	}

	private boolean validateCredential( final Credential credential ) {
		return !credential.getRefreshToken().equalsIgnoreCase("-TOKEN-");
	}


	/**
	 * Detect events from the controllers and if the affect the Credentials (because they have updaters) then try to persist the
	 * new instance values on the repository.
	 *
	 * @param event the event to be processed. Event have a property name that is used as a selector.
	 */
	@Override
	public synchronized void receiveEvent( final IntercommunicationEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			final Object target = event.getNewValue();
			if ((null != target) && (target instanceof CredentialController)) { // This only applies to Credentials
				try {
					new CredentialPersistent.Builder()
							.withCredentialRepository(this.credentialRepository)
							.build()
							.persist(((CredentialController) target).getModel());
				} catch (SQLException sqle) {
					logger.info("EX [SplashActionsDataSource.receiveEvent]> Exception persisting Credential update: {}",
							sqle.getMessage());
				}
			}
			return;
		}
		super.receiveEvent(event);
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

		public Builder withName( final String name ) {
			this.onConstruction.name = name;
			return this;
		}

		public Builder withVersion( final String version ) {
			this.onConstruction.version = version;
			return this;
		}

		public Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
			this.onConstruction.credentialRepository = credentialRepository;
			return this;
		}
	}
}
