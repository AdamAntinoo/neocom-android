package org.dimensinfin.eveonline.neocom.app.activity;

import android.content.Intent;
import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.MenuActivatedActivity;

public class AppSplashActivity extends MenuActivatedActivity {
	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new AppSplashFragment().setVariant(PageNamesType.SPLASH.name()));
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.verifyNetwork();
	}

	/**
	 * Check the network availability each time we enter this page. If the network is down or disabled then go back to the Initilization activity
	 * to wait there for startup.
	 */
	protected void verifyNetwork() {
		if (NeoComApplication.checkNetworkAccess()) return;
		this.startActivity(new Intent(this, AppInitializationActivity.class));
	}
}
