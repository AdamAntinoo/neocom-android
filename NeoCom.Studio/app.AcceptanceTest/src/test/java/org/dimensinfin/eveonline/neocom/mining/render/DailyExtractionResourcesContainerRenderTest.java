package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.mining.controller.DailyExtractionResourcesContainerController;

public class DailyExtractionResourcesContainerRenderTest {
	@Test
	public void constructorContract() {
		final DailyExtractionResourcesContainerController controller = Mockito.mock( DailyExtractionResourcesContainerController.class );
		final Context context = Mockito.mock( Context.class );
		final DailyExtractionResourcesContainerRender dailyExtractionResourcesContainerRender = new DailyExtractionResourcesContainerRender(
				controller, context );
		Assertions.assertNotNull( dailyExtractionResourcesContainerRender );
	}

	@Test
	public void getController() {
		// Given
		final DailyExtractionResourcesContainerController controller = Mockito.mock( DailyExtractionResourcesContainerController.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final DailyExtractionResourcesContainerRender dailyExtractionResourcesContainerRender = new DailyExtractionResourcesContainerRender(
				controller, context );
		final IAndroidController obtained = dailyExtractionResourcesContainerRender.getController();
		// Assertions
		Assertions.assertNotNull( obtained );
	}

	@Test
	public void accessLayoutReference() {
		// Given
		final DailyExtractionResourcesContainerController controller = Mockito.mock( DailyExtractionResourcesContainerController.class );
		final Context context = Mockito.mock( Context.class );
		// Test
		final DailyExtractionResourcesContainerRender dailyExtractionResourcesContainerRender = new DailyExtractionResourcesContainerRender(
				controller, context );
		final int obtained = dailyExtractionResourcesContainerRender.accessLayoutReference();
		// Assertions
		Assertions.assertEquals( R.layout.resource4mininglistheader, obtained );
	}
}
