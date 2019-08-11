package org.dimensinfin.eveonline.neocom.app.activity;

import android.preference.PreferenceManager;
import android.view.View;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.controller.SplashActionBarController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.NewLoginAction;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.domain.WarningPanel;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.persisters.CredentialPersistent;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;
import org.dimensinfin.eveonline.neocom.core.EEvents;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

import java.beans.PropertyChangeEvent;
import java.sql.SQLException;

public class AppSplashFragment extends MVCPagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory(this.getVariant())
				.registerActivity(PageNamesType.PILOT_DASHBOARD.name(), PilotDashboardActivity.class)
				.registerActivity(PageNamesType.AUTHORIZATION.name(), AuthorizationFlowActivity.class);
	}

	@Override
	public IDataSource createDS() {
		return new SplashActionsDataSource.Builder()
				.addIdentifier(this.getVariant())
				.addIdentifier("SPLASH")
				.withVariant(this.getVariant())
				.withExtras(this.getExtras())
				.withFactory(this.createFactory())
				.withName(this.getActivityContext().getResources().getString(R.string.appname))
				.withVersion(this.getActivityContext().getResources().getString(R.string.appversionname))
//				.withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				.withCredentialRepository(NeoComComponentFactory.getSingleton().getCredentialRepository())
				.build();
	}

	@Override
	public View generateActionBarView() {
		// Create the bar content model.
		final SplashActionBar actionBar = new SplashActionBar.Builder()
				.withPreferences(PreferenceManager.getDefaultSharedPreferences(this.getActivityContext()))
				.build();
		// Get a controller from the model, then the view from the render.
		final SplashActionBarController controller = (SplashActionBarController) new AppControllerFactory("SPLASH")
				.createController(actionBar);
		return this.convertActionBarView(controller);
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
	public static class SplashActionsDataSource extends MVCDataSource {
		private String name;
		private String version;

		// -  C O M P O N E N T S
//		private ESIDataAdapter esiDataAdapter;
		private CredentialRepository credentialRepository;

//		private List<InitialisationTask> initializationTasksList = new ArrayList<>();

		@Override
		public void prepareModel() {
//			logger.info(">> [SplashActionsDataSource.prepareModel]");
//			logger.info("<< [SplashActionsDataSource.prepareModel]");
		}

		@Override
		public void collaborate2Model() {
			try {
				logger.info(">> [SplashActionsDataSource.collaborate2Model]");
				// - S E C T I O N   H E A D E R
				logger.info("-- [SplashActionsDataSource.collaborate2Model]> Section Header");
				// TODO - The header cannot be tested. Move this agsin to the header once the MVC is reimplemented.
//				this.addHeaderContents(new AppVersion()
//						                       .setName(this.name)
//						                       .setVersion(this.version));
				if (!NeoComApplication.checkNetworkAccess()) {
					this.addHeaderContents(new WarningPanel.Builder()
							.withType("WARNING")
							.withDescription("The is no network connection. Application functionality in hold.")
							.build());
				}
//				for (InitialisationTask task : this.initializationTasksList) {
//					this.addModelContents(task.schedule(2));
//				}
				// - S E C T I O N   D A T A
				this.addModelContents(new AppVersion()
						.setName(this.name)
						.setVersion(this.version));
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
				this.addModelContents(new Separator().setType(Separator.ESeparatorType.LINE_WHITE));
				this.addModelContents(new NewLoginAction());
			} catch (RuntimeException runtime) {
				logger.info("-- [SplashActionsDataSource.collaborate2Model]> RuntimeException: {}", runtime.getMessage());
				this.addModelContents(new ExceptionReport(runtime));
				// Add the node to display the new New Login Button.
				this.addModelContents(new Separator().setType(Separator.ESeparatorType.LINE_WHITE));
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
		public synchronized void propertyChange( final PropertyChangeEvent event ) {
			if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
				final Object target = event.getNewValue();
				if ((null != target) && (target instanceof CredentialController)) { // This only applies to Credentials
					try {
						new CredentialPersistent.Builder()
								.withCredentialRepository(this.credentialRepository)
								.build()
								.persist(((CredentialController) target).getModel());
					} catch (SQLException sqle) {
						logger.info("EX [SplashActionsDataSource.propertyChange]> Exception persisting Credential update: {}", sqle.getMessage());
					}
				}
				return;
			}
			super.propertyChange(event);
		}

		// - B U I L D E R
		public static class Builder extends MVCDataSource.BaseBuilder<SplashActionsDataSource, Builder> {
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

//			public Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
//				this.onConstruction.esiDataAdapter = esiDataAdapter;
//				return this;
//			}

			public Builder withCredentialRepository( final CredentialRepository credentialRepository ) {
				this.onConstruction.credentialRepository = credentialRepository;
				return this;
			}
		}
	}
}
