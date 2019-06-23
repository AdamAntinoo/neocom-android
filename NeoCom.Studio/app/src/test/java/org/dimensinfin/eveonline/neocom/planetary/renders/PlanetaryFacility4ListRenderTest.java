package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;

import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetaryFacility4ListRenderTest {
	private static Logger logger = LoggerFactory.getLogger(PlanetaryFacility4ListRenderTest.class);

	@Test
	public void accessElapsedCycle() {
		final PlanetaryFacilityController controller = Mockito.mock(PlanetaryFacilityController.class);
		final Context context = Mockito.mock(Context.class);
		final PlanetaryFacility facility = Mockito.mock(PlanetaryFacility.class);
		Mockito.when(controller.getModel()).thenReturn(facility);
		Mockito.when(facility.getLastCycleStart()).thenReturn(new DateTime("2019-06-04T18:48:12Z"));

		final DateTime now = DateTime.now();
		final PlanetaryFacility4ListRender render = new PlanetaryFacility4ListRender(controller, context);
		final String obtained = render.accessElapsedCycle();
		logger.info("Elapsed time in format: {}", obtained);
	}
}
