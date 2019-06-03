package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColoniesActivity;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;
import org.dimensinfin.eveonline.neocom.planetary.renders.BasicIndustryStructureRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.PlanetaryFacility4ListRender;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PlanetaryFacilityControllerTest {
	private static IPlanetaryFacility model;
	private static PlanetaryControllerFactory factory;

	@Before
	public void setUp() throws Exception {
		model = Mockito.mock(IPlanetaryFacility.class);
		factory = Mockito.mock(PlanetaryControllerFactory.class);
	}

	@Test
	public void buildRender_variantA() {
		final PlanetaryFacilityController controller = (PlanetaryFacilityController) new PlanetaryFacilityController(model, factory)
				                                                                             .setRenderMode(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETS_LIST.name());
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
		Assert.assertTrue(obtained instanceof PlanetaryFacility4ListRender);
	}

	@Test
	public void buildRender_variantB() {
		final IPlanetaryFacility model = Mockito.mock(IPlanetaryFacility.class);
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryFacilityController controller = (PlanetaryFacilityController) new PlanetaryFacilityController(model, factory)
				                                                                             .setRenderMode(PlanetaryColoniesActivity.EPlanetaryVariants.PLANET_FACILITIES.name());
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
		Assert.assertTrue(obtained instanceof BasicIndustryStructureRender);
	}

	@Test(expected = NeoComRuntimeException.class)
	public void buildRender_notfound() {
		final IPlanetaryFacility model = Mockito.mock(IPlanetaryFacility.class);
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryFacilityController controller = (PlanetaryFacilityController) new PlanetaryFacilityController(model, factory)
				                                                                             .setRenderMode("NOT FOUND");
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		//		Assert.assertNotNull(obtained);
		//		Assert.assertTrue(obtained instanceof BasicIndustryStructureRender);
	}
}
