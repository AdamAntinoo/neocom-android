package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class PlanetaryActionBarControllerTest {
	@Test
	public void buildRender() {
		final PlanetaryActionBar model = Mockito.mock(PlanetaryActionBar.class);
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryActionBarController controller = new PlanetaryActionBarController(model, factory);
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
	}
}
