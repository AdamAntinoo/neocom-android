package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.test.support.PojoTestUtils;

public class ColonyPackTest {
	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(ColonyPack.class);
	}

	@Test
	public void builder() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planet = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		final ColonyPack colonyPack = new ColonyPack.Builder()
				.withPilotIdentifier(9222)
				.withColony(colony)
				.withPlanetData(planet)
				.withFacilities(facilities)
				.build();
		Assert.assertNotNull(colonyPack);
	}

	@Test(expected = NullPointerException.class)
	public void builder_incomplete() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planet = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final ColonyPack colonyPack = new ColonyPack.Builder()
				.withPilotIdentifier(9222)
				.withColony(colony)
				.withPlanetData(planet)
				.build();
		Assert.assertNotNull(colonyPack);
	}

//	@Test
	public void accessCommandCenter() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planetData = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final CommandCenterFacility facility = Mockito.mock(CommandCenterFacility.class);
		Mockito.when(facility.getFacilityType()).thenReturn(PlanetaryFacilityType.COMMAND_CENTER);
		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		facilities.add(facility);
		final ColonyPack colonyPack = new ColonyPack.Builder()
				.withColony(colony)
				.withPlanetData(planetData)
				.withPilotIdentifier(123)
				.withFacilities(facilities)
				.build();

		final ICommandCenterFacility commandCenter = colonyPack.accessCommandCenter();
		Assert.assertNotNull(commandCenter);
		Assert.assertTrue(commandCenter instanceof CommandCenterFacility);
	}

	@Test(expected = NeoComRuntimeException.class)
	public void accessCommandCenter_failure() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planetData = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final CommandCenterFacility facility = Mockito.mock(CommandCenterFacility.class);
		Mockito.when(facility.getFacilityType()).thenReturn(PlanetaryFacilityType.PLANETARY_FACTORY);
		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		facilities.add(facility);
		final ColonyPack colonyPack = new ColonyPack.Builder()
				.withColony(colony)
				.withPlanetData(planetData)
				.withPilotIdentifier(123)
				.withFacilities(facilities)
				.build();

		final ICommandCenterFacility commandCenter = colonyPack.accessCommandCenter();
		Assert.assertNotNull(commandCenter);
		Assert.assertTrue(commandCenter instanceof CommandCenterFacility);
	}

	@Test
	public void calculateUsedCpu() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planetData = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final CommandCenterFacility commandCenteracility = Mockito.mock(CommandCenterFacility.class);
		Mockito.when(commandCenteracility.getFacilityType()).thenReturn(PlanetaryFacilityType.COMMAND_CENTER);
		final PlanetaryFacility facility = Mockito.mock(PlanetaryFacility.class);
		Mockito.when(facility.getCpuUsage()).thenReturn(1000);
		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		facilities.add(commandCenteracility);
		facilities.add(facility);
		facilities.add(facility);
		final ColonyPack colonyPack = new ColonyPack.Builder()
				.withColony(colony)
				.withPlanetData(planetData)
				.withPilotIdentifier(123)
				.withFacilities(facilities)
				.build();

		final int cpu = colonyPack.calculateUsedCpu();
		Assert.assertEquals(2000, cpu);
	}

}