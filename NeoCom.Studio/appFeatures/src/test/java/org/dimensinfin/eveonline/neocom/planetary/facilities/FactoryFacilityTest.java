package org.dimensinfin.eveonline.neocom.planetary.facilities;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.FactoryType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.support.TestEsiAdapterReady;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FactoryFacilityTest extends TestEsiAdapterReady {
	private static final int SCHEMATIC4USUKOMY_TIER3 = 89;
	private static final PlanetType TEST_PLANET_TYPE_BARREN = PlanetType.BARREN;
	private static GetCharactersCharacterIdPlanetsPlanetIdOkPins advancedFactoryPin;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		advancedFactoryPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		advancedFactoryPin.setTypeId(2474);
		advancedFactoryPin.setPinId(1022847372295L);
		advancedFactoryPin.setLastCycleStart(DateTime.now());
		advancedFactoryPin.setLatitude(2.27239886788F);
		advancedFactoryPin.setLongitude(1.97163458582F);
		advancedFactoryPin.setSchematicId(SCHEMATIC4USUKOMY_TIER3);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList();
		GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(3691);
		content.setAmount(10L);
		contents.add(content);
		content.setTypeId(9838);
		content.setAmount(10L);
		contents.add(content);
		advancedFactoryPin.setContents(contents);
	}

	@Test
	public void build_complete() throws IOException {
		final EveItem facilityItem = new EveItem(advancedFactoryPin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(advancedFactoryPin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.withEsiDataAdapter(esiDataAdapter)
				.build();
		final FactoryFacility factory = new FactoryFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryRepository(planetaryRepository)
				.withSchematics(SCHEMATIC4USUKOMY_TIER3)
				.build();

		Assert.assertNotNull(factory);
	}

	@Test
	public void getFactoryType() {
		final EveItem facilityItem = new EveItem(advancedFactoryPin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(advancedFactoryPin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.withEsiDataAdapter(esiDataAdapter)
				.build();
		final FactoryFacility factory = new FactoryFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryRepository(planetaryRepository)
				.withSchematics(SCHEMATIC4USUKOMY_TIER3)
				.build();

		final FactoryType obtained = factory.getFactoryType();
		Assert.assertEquals("The factory type should be ADVANCED.", FactoryType.ADVANCED_INDUSTRY, obtained);
	}

	@Test
	public void getCycleTime() {
		final EveItem facilityItem = new EveItem(advancedFactoryPin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(advancedFactoryPin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.withEsiDataAdapter(esiDataAdapter)
				.build();
		final FactoryFacility factory = new FactoryFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryRepository(planetaryRepository)
				.withSchematics(SCHEMATIC4USUKOMY_TIER3)
				.build();

		final int obtained = factory.getCycleTime();
		Assert.assertEquals("The factory cycle time is 3600 for ADVANCED.", 3600, obtained);
	}

	@Test
	public void getRequiredQuantity() {
		final EveItem facilityItem = new EveItem(advancedFactoryPin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(advancedFactoryPin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.withEsiDataAdapter(esiDataAdapter)
				.build();
		final FactoryFacility factory = new FactoryFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryRepository(planetaryRepository)
				.withSchematics(SCHEMATIC4USUKOMY_TIER3)
				.build();

		final int obtained = factory.getRequiredQuantity();
		Assert.assertEquals("The factory input quantity for ADVANCED is 10.", 10, obtained);
	}
}
