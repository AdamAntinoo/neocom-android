package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.render.MiningExtractionResourceRender;

public class MiningResourceControllerTest {
	@Test
	public void constructorContract() {
		final Resource model = Mockito.mock( Resource.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final MiningResourceController miningResourceController = new MiningResourceController(
				model, factory );
		Assertions.assertNotNull( miningResourceController );
		Assertions.assertEquals( SpacerType.LINE_DARKBLUE, miningResourceController.getEncaserTheme() );
	}

	@Test
	public void buildRender() {
		// Given
		final Resource model = Mockito.mock( Resource.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final MiningResourceController miningResourceController = new MiningResourceController(
				model, factory );
		final IRender obtained = miningResourceController.buildRender( context );
		// Assertions
		Assertions.assertTrue( obtained instanceof MiningExtractionResourceRender );
	}

	@Test
	public void buildRenderMININGEXTRACTIONSTODAY() {
		// Given
		final Resource model = Mockito.mock( Resource.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final AndroidController miningResourceController = new MiningResourceController(
				model, factory ).setRenderMode( PageNamesType.MINING_EXTRACTIONS_TODAY.name() );
		final IRender obtained = miningResourceController.buildRender( context );
		// Assertions
		Assertions.assertTrue( obtained instanceof MiningExtractionResourceRender );
	}
}
