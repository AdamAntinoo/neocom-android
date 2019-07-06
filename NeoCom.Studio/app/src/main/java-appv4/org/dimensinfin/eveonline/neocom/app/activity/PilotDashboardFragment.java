package org.dimensinfin.eveonline.neocom.app.activity;

import android.view.View;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComActionBarController;
import org.dimensinfin.eveonline.neocom.app.pilot.PilotV3;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComActionBarV2;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComMenuBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;

/**
 * @author Adam Antinoo
 */
public class PilotDashboardFragment extends AMVCPagerFragment {
	private Credential credential;

	public Credential getCredential() {
		return credential;
	}

	public PilotDashboardFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	// - O V E R R I D E S
	//	@Override
	//	public String getSubtitle() {
	//		return null;
	//	}
	//
	//	@Override
	//	public String getPageTitle() {
	//		return null;
	//	}

	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new PilotDashboardDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       //				       .addIdentifier(this.getPageTitle())
				       .addIdentifier("PILOT-DASHBOARD")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withCredential(this.getCredential())
				       .withDownloaderService(NeoComComponentFactory.getSingleton().getDownloaderService())
				       .build();
	}

	@Override
	public View generateActionBarView() {
		final NeoComActionBarV2 actionBar = new NeoComActionBarV2.Builder()
				                                    .withTitleTopLeft(this.getResources().getString(R.string.appname))
				                                    .withTitleTopRight(this.getResources().getString(R.string.appversionname))
				                                    .withSubtitle(this.getCredential().getAccountName())
				                                    .build();
		// Get a controller from the model, then the view from the render.
		final NeoComActionBarController controller = (NeoComActionBarController) new AppControllerFactory("SPLASH")
				                                                                         .createController(actionBar);
		return this.convertActionBarView(controller);
	}

	// - P I L O T D A S H B O A R D D A T A S O U C E
	public static class PilotDashboardDataSource extends AMVCDataSource {
		// -  S H A R E D   C O M P O N E N T S
		private Credential credential;
		private DataDownloaderService downloaderService;

		public Credential getCredential() {
			return this.credential;
		}

		public PilotDashboardDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}

		public PilotDashboardDataSource setDownloaderService( final DataDownloaderService downloaderService ) {
			this.downloaderService = downloaderService;
			return this;
		}

		@Override
		public void prepareModel() {
			logger.info(">> [PilotDashboardDataSource.prepareModel]");
			this.addHeaderContents(new PilotV3.Builder(this.getCredential(), this.downloaderService).build());
			this.addHeaderContents(new NeoComMenuBar(this.getCredential()));
			logger.info("<< [PilotDashboardDataSource.prepareModel]");
		}

		@Override
		public void collaborate2Model() {
		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<PilotDashboardDataSource, Builder> {
			private PilotDashboardDataSource onConstruction;

			@Override
			protected PilotDashboardDataSource getActual() {
				if (null == this.onConstruction) this.onConstruction = new PilotDashboardDataSource();
				return this.onConstruction;
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}

			public Builder withCredential( final Credential credential ) {
				this.getActual().setCredential(credential);
				return this;
			}

			public Builder withDownloaderService( final DataDownloaderService downloaderService ) {
				this.getActual().setDownloaderService(downloaderService);
				return this;
			}
		}
	}
}
