package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.render.DailyExtractionResourcesContainerRender;

public class DailyExtractionResourcesContainerControllerTest {
	@Test
	public void constructorContract() {
		final DailyExtractionResourcesContainer model = Mockito.mock( DailyExtractionResourcesContainer.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final DailyExtractionResourcesContainerController dailyExtractionResourcesContainerController = new DailyExtractionResourcesContainerController(
				model, factory );
		Assertions.assertNotNull( dailyExtractionResourcesContainerController );
		Assertions.assertEquals( SpacerType.LINE_GREEN, dailyExtractionResourcesContainerController.getEncaserTheme() );
	}

	@Test
	public void wants2Collaborate() {
		// Given
		final DailyExtractionResourcesContainer model = Mockito.mock( DailyExtractionResourcesContainer.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		// Test
		final DailyExtractionResourcesContainerController dailyExtractionResourcesContainerController = new DailyExtractionResourcesContainerController(
				model, factory );
		final boolean obtained = dailyExtractionResourcesContainerController.wants2Collaborate();
		// Assertions
		Assertions.assertTrue( obtained );
	}

	@Test
	public void buildRender() {
		// Given
		final DailyExtractionResourcesContainer model = Mockito.mock( DailyExtractionResourcesContainer.class );
		final IControllerFactory factory = Mockito.mock( IControllerFactory.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final DailyExtractionResourcesContainerController dailyExtractionResourcesContainerController = new DailyExtractionResourcesContainerController(
				model, factory );
		final IRender obtained = dailyExtractionResourcesContainerController.buildRender( context );
		// Assertions
		Assertions.assertTrue( obtained instanceof DailyExtractionResourcesContainerRender );
	}
}
