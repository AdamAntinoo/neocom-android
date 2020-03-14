package org.dimensinfin.eveonline.neocom.mining.activity;

import android.view.View;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.render.RenderViewGenerator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.MiningActionBar;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;

public class MiningLedgerFragment extends AuthenticatedFragment {
	@Override
	public IControllerFactory createFactory() {
		return new MiningControllerFactory(this.getVariant())
				       .registerActivity( PageNamesType.RESOURCE_DETAILS.name(), PilotDashboardActivity.class);
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningLedgerDataSourceToday.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.getCredential())
					       .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getESIDataProvider())
					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
						   .withBackendService( NeoComComponentFactory.getSingleton().getBackendService() )
						   .withLocationCatalogService( NeoComComponentFactory.getSingleton().getLocationCatalogService() )
					       .build();
//		if (this.getVariant().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_ALL.name()))
//			return new MiningLedgerDataSourceAll.Builder()
//					       .addIdentifier(this.getVariant())
//					       .addIdentifier(this.getCredential().getAccountId())
//					       .withVariant(this.getVariant())
//					       .withExtras(this.getExtras())
//					       .withFactory(this.createFactory())
//					       .withCredential(this.getCredential())
//					       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
//					       .build();
		throw new NeoComRuntimeException("Mining variant has not defined Data Source to generate content.");
	}

	@Override
	public View generateActionBarView() {
//		// Create the bar content model.
//		final MiningActionBar actionBar = new MiningActionBar.Builder()
//				                                  .withTitleTopLeft(this.getCredential().getAccountName())
//				                                  .withSubtitle(PageNamesType.PLANET_FACILITIES_DETAILS.getPageTitle())
//				                                  .withActivityIconRef(R.drawable.miningledger)
//				                                  .withPilotIdentifier(this.getCredential().getAccountId())
//				                                  .build();
//		// Get a controller from the model, then the view from the render.
//		final MiningActionBarController controller = (MiningActionBarController) new AppControllerFactory("SPLASH")
//				                                                                         .createController(actionBar);
//		return this.convertActionBarView(controller);
		return new RenderViewGenerator.Builder<MiningActionBar>()
				       .withContext( this.getActivityContext() )
				       .withModel( new MiningActionBar.Builder()
						                   .withTitleTopLeft(this.getCredential().getAccountName())
						                   .withSubtitle(PageNamesType.PLANET_FACILITIES_DETAILS.getPageTitle())
						                   .withActivityIconRef(R.drawable.miningledger)
						                   .withPilotIdentifier(this.getCredential().getAccountId())
						                   .build() )
				       .withFactory( new AppControllerFactory( this.getVariant() ) )
				       .generateView();
	}
}
