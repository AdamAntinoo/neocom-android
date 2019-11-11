package org.dimensinfin.eveonline.neocom.app.activity;

import android.view.View;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComActionBarController;
import org.dimensinfin.eveonline.neocom.app.datasource.PilotDashboardDataSource;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComActionBarV2;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class PilotDashboardFragment extends MVCPagerFragment {
	private Credential credential;

	public Credential getCredential() {
		return this.credential;
	}

	public PilotDashboardFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory(this.getVariant());
//				.registerActivity(PageNamesType.PLANET_FACILITIES_DETAILS.name(), PlanetaryColoniesActivity.class);
//				.registerActivity(PageNamesType.MINING_LEDGER.name(), MiningLedgerActivity.class);
	}

	@Override
	public IDataSource createDS() {
		return new PilotDashboardDataSource.Builder()
				.addIdentifier(this.getVariant())
				.addIdentifier(this.getCredential().getAccountId())
				.addIdentifier("PILOT-DASHBOARD")
				.withVariant(this.getVariant())
				.withExtras(this.getExtras())
				.withFactory(this.createFactory())
				.withEsiDataAdapter(NeoComComponentFactory.getSingleton().getESIDataProvider())
				.withCredential(this.getCredential())
				.build();
	}

	@Override
	public View generateActionBarView() {
		final NeoComActionBarV2 actionBar = new NeoComActionBarV2.Builder()
				.withTitleTopLeft(this.getResources().getString(R.string.appname))
				.withTitleTopRight(this.getResources().getString(R.string.appversion))
				.withSubtitle(this.getCredential().getAccountName())
				.build();
		// Get a controller from the model, then the view from the render.
		final NeoComActionBarController controller = (NeoComActionBarController) new AppControllerFactory("SPLASH")
				.createController(actionBar);
		return this.convertActionBarView(controller);
	}
}
