package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.activity.CanvasPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.appvisualtest.datasources.VisualTestsDataSource;
import org.dimensinfin.eveonline.neocom.visual.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;

public class VisualTestsPlanetaryLayoutFragment extends CanvasPagerFragment {
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
