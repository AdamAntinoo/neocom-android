package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;
import android.view.View;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;

public class PlanetaryColoniesActivity extends ACredentialActivity {
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
//		this.setupActionBar(this.generateActionBar());
		this.addPage(new PlanetaryColoniesFragment()
				             .setCredential(this.getCredential())
				             .setVariant(EVariants.PLANETS_LIST.name()));
	}
}
