package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.PojoTestUtils;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ColonyPackTest {
	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(ColonyPack.class);
	}

	@Test
	public void builder() {
		final GetCharactersCharacterIdPlanets200Ok colony = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final GetUniversePlanetsPlanetIdOk planet = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final List<PlanetaryFacility> facilities = new ArrayList<>();
		final ColonyPack colonyPack = new ColonyPack.Builder()
				                              .withCapsuleerIdentifier(9222)
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
				                              .withCapsuleerIdentifier(9222)
				                              .withColony(colony)
				                              .withPlanetData(planet)
				                              .build();
		Assert.assertNotNull(colonyPack);
	}
}
