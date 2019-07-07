package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.activity.ASimplePagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.appvisualtest.datasources.VisualTestsDataSource;

public class VisualTestsPlanetaryLayoutFragment extends ASimplePagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new VisualTestsDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier("VISUAL TESTS")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				       .withPlanetaryRepository(NeoComComponentFactory.getSingleton().getPlanetaryRepository())
				       .build();
	}
}
