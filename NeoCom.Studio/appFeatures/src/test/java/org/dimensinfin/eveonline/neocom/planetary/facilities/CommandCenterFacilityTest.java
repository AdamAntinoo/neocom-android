package org.dimensinfin.eveonline.neocom.planetary.facilities;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;
import org.dimensinfin.eveonline.neocom.support.EsiDataAdapterSupportTest;
import org.dimensinfin.eveonline.neocom.support.PojoTestUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandCenterFacilityTest extends EsiDataAdapterSupportTest {
	private static final PlanetType TEST_PLANET_TYPE_BARREN = PlanetType.BARREN;
	private static final int BARREN_COMMAND_CENTER_TYPE = 2524;

	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(CommandCenterFacility.class);
	}

	@Test
	public void getCpuCapacity() {
		final CommandCenterFacility facility = this.createFacility4CommandCenter(BARREN_COMMAND_CENTER_TYPE);
		facility.setUpgradeLevel(2);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU capacity.", 12136, facility.getCpuCapacity());
	}

	@Test
	public void getPowerOutput() {
		final CommandCenterFacility facility = this.createFacility4CommandCenter(BARREN_COMMAND_CENTER_TYPE);
		facility.setUpgradeLevel(3);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power output.", 15000, facility.getPowerOutput());
	}

	protected CommandCenterFacility createFacility4CommandCenter( final int factoryTypeId ) {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setTypeId(factoryTypeId);
		pin.setPinId(1022847372295L);
		pin.setLastCycleStart(DateTime.now());
		pin.setLatitude(2.27239886788F);
		pin.setLongitude(1.97163458582F);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2463); // Nanites
		content.setAmount(100L);
		contents.add(content);
		pin.setContents(contents);
		final EveItem facilityItem = new EveItem(pin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(pin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.build();
		return new CommandCenterFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryStorage(new PlanetaryStorage(facility.getContents()))
				.build();
	}
}