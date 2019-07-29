package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ColonyPackControllerTest {
	private static ColonyPack colony;
	private static PlanetaryControllerFactory factory;
	private static ColonyPackController controller;

	@Before
	public void setUp() {
		colony = Mockito.mock(ColonyPack.class);
		factory = Mockito.mock(PlanetaryControllerFactory.class);
		controller = new ColonyPackController(colony, factory);
		Assert.assertNotNull(controller);
	}

//	@Test
//	public void onClick() {
//		final View view = Mockito.mock(View.class);
//		controller.onClick(view);
//	}

//	@Test
//	public void onLongClick() {
//		final View view = Mockito.mock(View.class);
//		controller.onLongClick(view);
//	}

//	@Test
//	public void toggleExpanded() {
//		controller.toggleExpanded();
//		Mockito.verify(colony, new Times(1)).toggleExpand();
//	}

	@Test
	public void buildRender() {
		final Context context = Mockito.mock(Context.class);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
	}
}
