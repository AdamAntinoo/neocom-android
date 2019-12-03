package org.dimensinfin.eveonline.neocom.app.activity;

import android.view.View;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.render.RenderViewGenerator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.datasource.PilotDashboardDataSource;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.DashboardActionBar;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;

public class PilotDashboardFragment extends AuthenticatedFragment {
	/**
	 * Do not forget to add the pages that can be jumped to from this activity.
	 */
	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory( this.getVariant() )
				       .registerActivity( PageNamesType.ASSETS_BYLOCATION.name(), AssetDisplayActivity.class );
//				.registerActivity(PageNamesType.PLANET_FACILITIES_DETAILS.name(), PlanetaryColoniesActivity.class)
//				.registerActivity(PageNamesType.MINING_LEDGER.name(), MiningLedgerActivity.class);
	}

	@Override
	public IDataSource createDS() {
		return new PilotDashboardDataSource.Builder()
				       .addIdentifier( "PILOT-DASHBOARD" )
				       .addIdentifier( this.getVariant() )
				       .addIdentifier( this.getCredential().getAccountId() )
				       .withVariant( this.getVariant() )
				       .withExtras( this.getExtras() )
				       .withFactory( this.createFactory() )
				       .withEsiDataProvider( NeoComComponentFactory.getSingleton().getESIDataProvider() )
				       .withCredential( this.getCredential() )
				       .build();
	}

	@Override
	public View generateActionBarView() {
		return new RenderViewGenerator.Builder<DashboardActionBar>()
				       .withContext( this.getActivityContext() )
				       .withModel( new DashboardActionBar.Builder()
						                   .withAppName( this.getResources().getString( R.string.appname ) )
						                   .withAppVersion( this.getResources().getString( R.string.appversion ) )
						                   .withPilotName( this.getCredential().getAccountName() )
						                   .build() )
				       .withFactory( new AppControllerFactory( PageNamesType.PILOT_DASHBOARD.name() ) )
				       .generateView();
	}
}
