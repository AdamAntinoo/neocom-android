package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;

import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.AuthenticatedFragment;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryColonyContentsDataSource;

public class PlanetaryColonyContentsFragment extends AuthenticatedFragment {

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new PlanetaryColonyContentsDataSource.Builder()
				.addIdentifier(this.getVariant())
				.addIdentifier(this.getCredential().getAccountId())
				.addIdentifier(this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name()))
				.withVariant(this.getVariant())
				.withExtras(this.getExtras())
				.withFactory(this.createFactory())
				.withCredential(this.getCredential())
				.withEsiAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				.withPlanetaryRepository(NeoComComponentFactory.getSingleton().getPlanetaryRepository())
				.build();
	}

	@Override
	public View generateActionBarView() {
		final PlanetaryActionBar actionBar = new PlanetaryActionBar.Builder()
				.withTitleTopLeft(this.getCredential().getAccountName())
				.withSubtitle(PageNamesType.PLANET_FACILITIES_DETAILS.getPageTitle())
				.withActivityIconRef(org.dimensinfin.eveonline.neocom.R.drawable.planets)
				.withPilotIdentifier(this.getCredential().getAccountId())
				.build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(PageNamesType.PLANETS_LIST.name())
				.createController(actionBar);
		return this.convertActionBarView(controller);
	}
}
