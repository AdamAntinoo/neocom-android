package org.dimensinfin.eveonline.neocom.planetary.activity;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryDataSource;

import static org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColoniesActivity.EPlanetaryVariants.PLANET_FACILITIES;

public class PlanetaryColoniesFragment extends AMVCPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new PlanetaryControllerFactory(this.getVariant())
				.registerActivity ( PLANET_FACILITIES.name(), PlanetaryColonyLayoutActivity.class);
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETS_LIST.name()))
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

	public Credential getCredential() {
		return credential;
	}

	public PlanetaryColoniesFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}
}
