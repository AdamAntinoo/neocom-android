package org.dimensinfin.eveonline.neocom.app.support.worlds;

import android.app.Activity;

import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.mockito.Mockito;

public class NeoComWorld {
	private Activity targetActivity;
	private Activity spyActivity;

	public Activity getTargetActivity() {
		return this.targetActivity;
	}

	public void setTargetActivity( final AppSplashActivity activity ) {
		this.targetActivity = activity;
		this.spyActivity = Mockito.spy(this.targetActivity);
	}
}
