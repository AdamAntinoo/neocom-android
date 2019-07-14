package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;

public class PlanetaryColoniesContentsActivity extends ACredentialActivity {
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new PlanetaryColoniesContentsFragment()
				.setCredential(this.getCredential())
				.setVariant(PageNamesType.PLANET_FACILITIES_DETAILS.name()));
	}
}
