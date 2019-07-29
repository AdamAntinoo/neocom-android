package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;
import org.dimensinfin.eveonline.neocom.model.EveItem;

public class VisualTestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		NeoComComponentFactory.initialiseSingleton(this);
		NeoComUpdater.injectsEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter());
		EveItem.injectEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter());
	}
}
