package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;

import org.junit.Test;
import org.mockito.Mockito;

public class CommandCenterFacilityTest {

	@Test
	public void powerOutput() {
		final PlanetaryFacility planetaryFacility = Mockito.mock(PlanetaryFacility.class);
		final CommandCenterFacility facility = new CommandCenterFacility.Builder()
													   .withPlanetaryFacility(planetaryFacility)
				.build();
	}

	@Test
	public void getCpuCapacity() {
	}

	@Test
	public void getPowerOutput() {
	}

	@Test
	public void cpuInUse() {
	}

	@Test
	public void powerInUse() {
	}
}
