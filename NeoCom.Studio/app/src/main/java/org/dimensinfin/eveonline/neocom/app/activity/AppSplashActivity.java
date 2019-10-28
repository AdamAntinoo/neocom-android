package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.activity.core.MenuActivatedActivity;

public class AppSplashActivity extends MenuActivatedActivity {
	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new AppSplashFragment());
	}
}
