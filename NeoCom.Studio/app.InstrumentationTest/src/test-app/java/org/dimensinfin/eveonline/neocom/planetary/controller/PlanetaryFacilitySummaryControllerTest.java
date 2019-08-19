package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilitySummary;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PlanetaryFacilitySummaryControllerTest {

	@Test
	public void buildRender() {
		final PlanetaryFacilitySummary model = Mockito.mock(PlanetaryFacilitySummary.class);
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryFacilitySummaryController controller = new PlanetaryFacilitySummaryController(model, factory);
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
	}
}
