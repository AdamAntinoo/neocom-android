package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.mining.domain.AggregationPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.render.AggregationPanelTitleRender;

public class AggregationPanelTitleControllerTest {
	@Test
	public void constructorContract() {
		final AggregationPanelTitle model = Mockito.mock( AggregationPanelTitle.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final AggregationPanelTitleController aggregationPanelTitleController = new AggregationPanelTitleController(
				model, factory );
		Assertions.assertNotNull( aggregationPanelTitleController );
	}

	@Test
	public void buildRender() {
		// Given
		final AggregationPanelTitle model = Mockito.mock( AggregationPanelTitle.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final AggregationPanelTitleController aggregationPanelTitleController = new AggregationPanelTitleController(
				model, factory );
		final IRender obtained = aggregationPanelTitleController.buildRender( context );
		// Assertions
		Assertions.assertTrue( obtained instanceof AggregationPanelTitleRender );
	}
}