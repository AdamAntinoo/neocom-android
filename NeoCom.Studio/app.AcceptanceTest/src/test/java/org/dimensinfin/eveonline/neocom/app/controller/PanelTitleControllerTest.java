package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.render.PanelTitleRender;

public class PanelTitleControllerTest {
	@Test
	public void constructorContract() {
		final PanelTitle model = Mockito.mock( PanelTitle.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final PanelTitleController panelTitleController = new PanelTitleController(
				model, factory );
		Assertions.assertNotNull( panelTitleController );
	}

	@Test
	public void buildRender() {
		// Given
		final PanelTitle model = Mockito.mock( PanelTitle.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final PanelTitleController panelTitleController = new PanelTitleController(
				model, factory );
		final IRender obtained = panelTitleController.buildRender( context );
		// Assertions
		Assertions.assertTrue( obtained instanceof PanelTitleRender );
	}
}