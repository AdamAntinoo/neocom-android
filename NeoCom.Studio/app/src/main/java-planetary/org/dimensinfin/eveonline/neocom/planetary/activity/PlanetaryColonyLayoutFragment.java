package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;

import org.dimensinfin.android.mvc.activity.CanvasPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryColonyLayoutDataSource;

public class PlanetaryColonyLayoutFragment extends CanvasPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PageNamesType.PLANET_FACILITIES_LAYOUT.name()))
			return new PlanetaryColonyLayoutDataSource.Builder()
					.addIdentifier(this.getVariant())
					.addIdentifier(this.getCredential().getAccountId())
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
				.withSubtitle(PageNamesType.PLANET_FACILITIES_LAYOUT.getPageTitle())
				.withActivityIconRef(R.drawable.planets)
				.withPilotIdentifier(this.getCredential().getAccountId())
				.build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller =
				(PlanetaryActionBarController) new PlanetaryControllerFactory(PageNamesType.PLANET_FACILITIES_LAYOUT.name())
						.createController(actionBar);
		return this.convertActionBarView(controller);
	}

	public Credential getCredential() {
		return credential;
	}

	public PlanetaryColonyLayoutFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}
}
