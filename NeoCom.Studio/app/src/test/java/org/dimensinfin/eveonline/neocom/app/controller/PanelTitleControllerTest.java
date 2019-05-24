package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.render.PanelTitleRender;

import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PanelTitleRender.class)
public class PanelTitleControllerTest {

	@Test
	public void builder_complete() {
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		final AppControllerFactory factory = Mockito.mock(AppControllerFactory.class);
		final PanelTitleController controller = new PanelTitleController.Builder(model, factory)
				                                        .withVariant("TEST VARIANT")
				                                        .build();
		Assert.assertNotNull(controller);
	}

	@Test(expected = NullPointerException.class)
	public void builder_null() {
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		//		final AppControllerFactory factory = Mockito.mock(AppControllerFactory.class);
		final PanelTitleController controller = new PanelTitleController.Builder(model, null)
				                                        .withVariant("TEST VARIANT")
				                                        .build();
	}

	@Test
	public void buildRender() throws Exception {
		final Context context = Mockito.mock(Context.class);
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		final AppControllerFactory factory = Mockito.mock(AppControllerFactory.class);
		final PanelTitleController controller = new PanelTitleController.Builder(model, factory)
				                                        .withVariant("TEST VARIANT")
				                                        .build();
		final PanelTitleRender render = Mockito.mock(PanelTitleRender.class);
		Assert.assertNotNull(controller);
		PowerMockito.whenNew(PanelTitleRender.class).withArguments(model, context).thenReturn(render);
		final IRender obtained = controller.buildRender(context);
		Assert.assertNotNull(obtained);
	}
}
