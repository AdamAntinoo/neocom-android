package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryDataSource;

public class PlanetaryColoniesFragment extends AMVCPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant())
				       .registerActivity(EActivities.PLANETARY_COLONY_LAYOUT_ACTIVITY.name(), PlanetaryColonyLayoutActivity.class);
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(EVariants.PLANETS_LIST.name()))
			return new PlanetaryDataSource.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.getCredential())
					       .withEsiAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
					       .build();
		throw new NeoComRuntimeException("Planetary variant has not defined Data Source to generate content.");
	}

	@Override
	public View generateActionBarView() {
		final PlanetaryActionBar actionBar = new PlanetaryActionBar.Builder()
				                                     .withTitleTopLeft(this.getCredential().getAccountName())
				                                     .withSubtitle(EVariants.PLANETS_LIST.getTitle())
				                                     .withActivityIconRef(org.dimensinfin.eveonline.neocom.R.drawable.planets)
				                                     .withPilotIdentifier(this.getCredential().getAccountId())
				                                     .build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(EVariants.PLANETS_LIST.name())
				                                                                               .createController(actionBar);
		return this.convertActionBarView(controller);
	}

	public Credential getCredential() {
		return credential;
	}

	public PlanetaryColoniesFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}
}
