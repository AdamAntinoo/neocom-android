package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.os.Bundle;
import android.view.View;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;

public class PlanetaryColoniesActivity extends ACredentialActivity {
	public enum EPlanetaryVariants {
		PLANETS_LIST("PLANETS LIST"),
		PLANET_INSTALLATIONS("PLANET INSTALLATIONS");

		private String title;

		EPlanetaryVariants( final String title ) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.setupActionBar(this.generateActionBar());
		this.addPage(new PlanetaryColoniesFragment()
				             .setCredential(this.getCredential())
				             .setVariant(EPlanetaryVariants.PLANETS_LIST.name()));
	}

	private View generateActionBar() {
		final PlanetaryActionBar actionBar = new PlanetaryActionBar.Builder()
				                                     .withTitleTopLeft(this.getCredential().getAccountName())
				                                     .withSubtitle(EPlanetaryVariants.PLANETS_LIST.getTitle())
				                                     .withActivityIconRef(R.drawable.planets)
				                                     .withPilotIdentifier(this.getCredential().getAccountId())
				                                     .build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(EPlanetaryVariants.PLANETS_LIST.name())
				                                                                               .createController(actionBar);
		final IRender holder = controller.buildRender(this);
		holder.updateContent();
		return holder.getView();
	}
}
