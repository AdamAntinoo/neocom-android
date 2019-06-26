package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;

public class PlanetaryColoniesActivity extends ACredentialActivity {
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
//		this.setupActionBar(this.generateActionBar());
		this.addPage(new PlanetaryColoniesFragment()
				             .setCredential(this.getCredential())
				             .setVariant(PageNamesType.PLANETS_LIST.name()));
	}
}
