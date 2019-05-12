package org.dimensinfin.eveonline.neocom.activity;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.factory.CoreControllerFactory;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

public class SplashFragment extends MVCPagerFragment {
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
		return new CoreControllerFactory(this.getVariant());
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
				       .build();
	}

	// - C R E D E N T I A L   D A T A S O U R C E
	public static class CredentialDatasource extends AMVCDataSource {
		private String name;
		private String version;

		private CredentialDatasource setName( final String name ) {
			this.name = name;
			return this;
		}

		private CredentialDatasource setVersion( final String version ) {
			this.version = version;
			return this;
		}

		@Override
		public void prepareModel() {
			logger.info(">> [CredentialDatasource.prepareModel]");
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
				// - S E C T I O N   D A T A
				// Add the panel title up front of the Credential list.
				this.addModelContents(new PanelTitle("CREDENTIAL LIST"));
				for (Credential credential : AndroidGlobalDataManager.accessAllCredentials()) {
					this.addModelContents(credential);
					logger.info("-- [CredentialListDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
							, credential.getAccountName());
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
		}
	}
}
