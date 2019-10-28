package org.dimensinfin.eveonline.neocom.app.activity;

import android.preference.PreferenceManager;
import android.view.View;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.SplashActionBarController;
import org.dimensinfin.eveonline.neocom.app.datasource.SplashActionsDataSource;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;

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
				       .withVersion(this.getActivityContext().getResources().getString(R.string.appversion))
				       .withCredentialRepository(NeoComComponentFactory.getSingleton().getCredentialRepository())
				       .build();
	}

	@Override
	public View generateActionBarView() {
		// Create the bar content model.
		final SplashActionBar actionBar = new SplashActionBar.Builder()
				                                  .withPreferences(PreferenceManager.getDefaultSharedPreferences(
						                                  this.getActivityContext()))
				                                  .build();
		// Get a controller from the model, then the view from the render.
		final SplashActionBarController controller = (SplashActionBarController) new AppControllerFactory(this.getVariant())
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
}
