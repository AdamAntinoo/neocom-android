package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.activity.ASimplePagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.appvisualtest.datasources.VisualTestsLayoutDataSource;

public class VisualTestsPlanetaryLayoutFragment extends ASimplePagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new VisualTestsLayoutDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier(this.getTitle())
				       .addIdentifier("VISUAL TESTS")
				       .addIdentifier("LAYOUTS")
				       .withVariant(EVariants.PLANET_FACILITIES.name())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				       .build();
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
