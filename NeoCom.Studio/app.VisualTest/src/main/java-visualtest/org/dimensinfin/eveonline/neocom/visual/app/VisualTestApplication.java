package org.dimensinfin.eveonline.neocom.visual.app;

import android.app.Application;

public class VisualTestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		NeoComComponentFactory.initialiseSingleton(this);
	}
}
