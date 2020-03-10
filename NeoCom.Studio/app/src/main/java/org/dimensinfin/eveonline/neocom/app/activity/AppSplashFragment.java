package org.dimensinfin.eveonline.neocom.app.activity;

import android.preference.PreferenceManager;
import android.view.View;
import androidx.annotation.WorkerThread;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.render.RenderViewGenerator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidSchedulerConfiguration;
import org.dimensinfin.eveonline.neocom.app.datasource.SplashActionsDataSource;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;

public class AppSplashFragment extends MVCPagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new AppControllerFactory( this.getVariant() )
				       .registerActivity( PageNamesType.PILOT_DASHBOARD.name(), PilotDashboardActivity.class )
				       .registerActivity( PageNamesType.AUTHORIZATION.name(), AuthorizationFlowActivity.class );
	}

	@Override
	public IDataSource createDS() {
		return new SplashActionsDataSource.Builder()
				       .addIdentifier( this.getVariant() )
				       .addIdentifier( "SPLASH" )
				       .withVariant( this.getVariant() )
				       .withExtras( this.getExtras() )
				       .withFactory( this.createFactory() )
				       .withName( this.getActivityContext().getResources().getString( R.string.appname ) )
				       .withVersion( this.getActivityContext().getResources().getString( R.string.appversion ) )
				       .withConfigurationService( NeoComComponentFactory.getSingleton().getConfigurationService() )
				       .withCredentialRepository( NeoComComponentFactory.getSingleton().getCredentialRepository() )
				       .withAssetRepository( NeoComComponentFactory.getSingleton().getAssetRepository() )
				       .withMiningRepository( NeoComComponentFactory.getSingleton().getMiningRepository() )
				       .withEsiDataProvider( NeoComComponentFactory.getSingleton().getESIDataProvider() )
				       .withLocationCatalogService( NeoComComponentFactory.getSingleton().getLocationCatalogService() )
				       .withBackendService( NeoComComponentFactory.getSingleton().getBackendService() )
				       .withSchedulerConfiguration( new AndroidSchedulerConfiguration.Builder()
						                                    .withPreferencesProvider( NeoComComponentFactory.getSingleton().getPreferencesProvider() )
						                                    .build() )
				       .build();
	}

	@WorkerThread
	@Override
	public View generateActionBarView() {
		return new RenderViewGenerator.Builder<SplashActionBar>()
				       .withContext( this.getActivityContext() )
				       .withModel( new SplashActionBar.Builder()
						                   .withEsiDataProvider( NeoComComponentFactory.getSingleton().getESIDataProvider() )
						                   .withPreferences( PreferenceManager.getDefaultSharedPreferences(
								                   this.getActivityContext() ) )
						                   .build() )
				       .withFactory( new AppControllerFactory( this.getVariant() ) )
				       .generateView();
	}
}
