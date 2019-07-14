package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryColoniesDataSource;

public class PlanetaryColoniesFragment extends AuthenticatedFragment {

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant())
				       .registerActivity(EActivities.PLANETARY_COLONY_LAYOUT_ACTIVITY.name(), PlanetaryColonyLayoutActivity.class);
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PageNamesType.PLANETS_LIST.name()))
			return new PlanetaryColoniesDataSource.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.getCredential())
					       .withEsiAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
					       .withPlanetaryRepository(NeoComComponentFactory.getSingleton().getPlanetaryRepository())
					       .build();
		throw new NeoComRuntimeException("Planetary variant has not defined Data Source to generate content.");
	}

	@Override
	public View generateActionBarView() {
		final PlanetaryActionBar actionBar = new PlanetaryActionBar.Builder()
				                                     .withTitleTopLeft(this.getCredential().getAccountName())
				                                     .withSubtitle(PageNamesType.PLANETS_LIST.getPageTitle())
				                                     .withActivityIconRef(org.dimensinfin.eveonline.neocom.R.drawable.planets)
				                                     .withPilotIdentifier(this.getCredential().getAccountId())
				                                     .build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(PageNamesType.PLANETS_LIST.name())
				                                                                               .createController(actionBar);
		return this.convertActionBarView(controller);
	}
}
