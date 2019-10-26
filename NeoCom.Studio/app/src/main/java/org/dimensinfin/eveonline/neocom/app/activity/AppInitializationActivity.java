package org.dimensinfin.eveonline.neocom.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

public class AppInitializationActivity extends Activity {
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;
	protected ActionBar actionBar;
	protected LinearContainer linearContainer;

	// - T E S T   A C C E S S O R S
	public LinearContainer getContainerView() {
		return this.linearContainer;
	}

	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.injectComponents(); // Connect to the application components.
		this.setContentView(R.layout.activity_initialization); // Set the layout to the core context
		this.deactivateActionBar();
		this.linearContainer = new LinearContainer.Builder()
				                       .withActivity(this)
				                       .withLayoutId(R.id.initializationLayout)
		.build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.linearContainer.clear();
		// Create the header component and insert it into the layout.
		final AppVersionController appInfo = new AppVersionController(
				new AppVersion.Builder()
						.withAppName(this.getResources().getString(R.string.appname))
						.withAppVersion("v" + this.getResources().getString(R.string.appversionnumber))
						.build(),
				this.getControllerFactory());
		final IRender appInfoRender = appInfo.buildRender(this.getApplicationContext());
		final View appInfoView = appInfoRender.getView();
		this.linearContainer.addView(appInfoView);
	}

	protected void injectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}

	protected void deactivateActionBar() {
		if (null != this.getActionBar()) {
			this.actionBar = this.getActionBar();
			this.actionBar.hide();
			this.actionBar.setDisplayHomeAsUpEnabled(true);

		}
	}

	private ControllerFactory getControllerFactory() {
		return new AppControllerFactory(PageNamesType.INITIALIZATION.name());
	}
}