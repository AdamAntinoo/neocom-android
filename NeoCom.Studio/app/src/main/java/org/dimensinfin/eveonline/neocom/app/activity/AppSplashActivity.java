package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;

public class AppSplashActivity extends MVCMultiPageActivity {
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new SplashFragment());
	}
}
