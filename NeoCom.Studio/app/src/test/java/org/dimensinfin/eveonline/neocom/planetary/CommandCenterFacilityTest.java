package org.dimensinfin.eveonline.neocom.planetary;

import org.junit.Test;
import org.mockito.Mockito;

public class CommandCenterFacilityTest {

	@Test
	public void powerOutput() {
		final IPlanetaryFacility planetaryFacility = Mockito.mock(IPlanetaryFacility.class);
		final CommandCenterFacility facility = new CommandCenterFacility.Builder()
													   .withPlanetaryFacility(planetaryFacility)
				.build();
	}
}
