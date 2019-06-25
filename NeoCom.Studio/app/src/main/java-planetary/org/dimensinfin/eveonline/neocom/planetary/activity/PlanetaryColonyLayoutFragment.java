package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;

import org.dimensinfin.android.mvc.activity.ASimplePagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryColonyDataSource;

public class PlanetaryColonyLayoutFragment extends ASimplePagerFragment {
	private Credential credential;

//	@Override
//	public String getSubtitle() {
//		return "POC Single Colony";
//	}
//
//	@Override
//	public String getTitle() {
//		return "PLANETARY DATA";
//	}

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(EVariants.PLANET_FACILITIES.name()))
			return new PlanetaryColonyDataSource.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
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
				                                     .withSubtitle("-Planet Name-")
				                                     .withActivityIconRef(R.drawable.planets)
				                                     .withPilotIdentifier(this.getCredential().getAccountId())
				                                     .build();
		// Get a controller from the model, then the view from the render.
		final PlanetaryActionBarController controller = (PlanetaryActionBarController) new PlanetaryControllerFactory(EVariants.PLANET_FACILITIES.name())
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
