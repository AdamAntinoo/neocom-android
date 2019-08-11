package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.visual.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.visual.tests.app.credential.VTCredentialsDataSource;

public class VTCredentialsFragment extends MVCPagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		return new VTCredentialsDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier("VISUAL TESTS")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				       .withCredentialRepository(NeoComComponentFactory.getSingleton().getCredentialRepository())
				       .build();
	}
}
