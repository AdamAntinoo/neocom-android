package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;

/**
 * This Proof Of Concept project will show the Planetary Colony detailed planetaryFacility inside a two component view using the standard MVC
 * developed for the NeoCom projects. It will only show a single colony for a predefined character so all that identification and parametrisation
 * should be declared as constants input to the Activity in the form of Extras, that is the real way the Activity would receive such
 * data in a final application implementation.
 *
 * Then we should define the character unique <b>account identifier</b> to be used to locate the Credential authorization planetaryFacility, we should also
 * set the target ESI server to use on the requests but probably that definition should be something set on configuration and not
 * selectable to the application interface and finally we should select one of the planetary colonies as if that was the planet
 * selected by the user on the UI.
 *
 * @author Adam Antinoo on 05/01/2018.
 */
public class PlanetaryColonyLayoutActivity extends ACredentialActivity {
	/** During the Activity creation just create the pages that should compose this feature UI pages. */
	public void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [PlanetaryColonyLayoutActivity.onCreate]");
		super.onCreate(savedInstanceState);
//		this.setupActionBar(this.generateActionBar());
		this.addPage(new PlanetaryColonyLayoutFragment()
				             .setCredential(this.getCredential())
				             .setVariant(PageNamesType.PLANET_FACILITIES.name()));
		logger.info("<< [PlanetaryColonyLayoutActivity.onCreate]");
	}
}
