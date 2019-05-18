package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.activity.core.ANeoComActivity;
import org.dimensinfin.eveonline.neocom.app.controller.SplashActionBarController;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;

public class AppSplashActivity extends ANeoComActivity {
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.setupActionBar(this.generateActionBar()); // Set the page action bar component
		this.addPage(new AppSplashFragment());
	}

	private View generateActionBar() {
		// Create the bar content model.
		final SplashActionBar actionBar = new SplashActionBar.Builder()
				                                  .withPreferences(PreferenceManager.getDefaultSharedPreferences(this))
				                                  .build();
		// Get a controller from the model, then the view from the render.
		final SplashActionBarController controller = (SplashActionBarController) new AppControllerFactory("SPLASH")
				                                                                         .createController(actionBar);
		final IRender holder = controller.buildRender(this);
		holder.updateContent();
		return holder.getView();
	}
}
