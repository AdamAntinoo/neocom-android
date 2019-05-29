package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;
import android.view.View;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;

/**
 * This Proof Of Concept project will show the Planetary Colony detailed structure inside a two component view using the standard MVC
 * developed for the NeoCom projects. It will only show a single colony for a predefined character so all that identification and parametrisation
 * should be declared as constants input to the Activity in the form of Extras, that is the real way the Activity would receive such
 * data in a final application implementation.
 *
 * Then we should define the character unique <b>account identifier</b> to be used to locate the Credential authorization structure, we should also
 * set the target ESI server to use on the requests but probably that definition should be something set on configuration and not
 * selectable to the application interface and finally we should select one of the planetary colonies as if that was the planet
 * selected by the user on the UI.
 *
 * @author Adam Antinoo on 05/01/2018.
 */
public class ColonyLayoutActivity extends ACredentialActivity {
	public static final String CHARACTER_ACCOUNT_IDENTIFIER = "CHARACTER_ACCOUNT_IDENTIFIER";
	public static final String ESI_TARGET = "ESI_TARGET";
	public static final String COLONY_IDENTIFIER = "COLONY_IDENTIFIER";

	/** During the Activity creation just create the pages that should compose this feature UI pages. */
	public void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [ColonyLayoutActivity.onCreate]");
		super.onCreate(savedInstanceState);
		this.setupActionBar(this.generateActionBar());
		this.addPage(new PlanetaryColoniesFragment()
				             .setCredential(this.getCredential())
				             .setVariant(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETARY_COLONIES.name()));

		//		// Load into the extras the parameters as if this intent was processed from another UI page.
		//		final Bundle extras = this.getExtras();
		//		extras.putLong(CHARACTER_ACCOUNT_IDENTIFIER, 92002067);
		//		extras.putString(ESI_TARGET, "tranquility");
		//		extras.putLong(COLONY_IDENTIFIER, 40237774);

		//		final IPagerFragment fragment = new ColonyLayoutFragment();
		//		this.addPage(fragment);
		logger.info("<< [ColonyLayoutActivity.onCreate]");
	}

	private View generateActionBar() {
		final PlanetaryActionBar actionBar = new PlanetaryActionBar.Builder()
				                                     .withTitleTopLeft(this.getCredential().getAccountName())
				                                     .withSubtitle(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETARY_COLONIES.name())
				                                     .withActivityIconRef(R.drawable.planets)
				                                     .withPilotIdentifier(this.getCredential().getAccountId())
				                                     .build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETARY_COLONIES.name())
				                                                                               .createController(actionBar);
		final IRender holder = controller.buildRender(this);
		holder.updateContent();
		return holder.getView();
	}
}
