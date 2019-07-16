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

public class StorageFacilityTest extends EsiDataAdapterSupportTest {
	private static final PlanetType TEST_PLANET_TYPE_BARREN = PlanetType.BARREN;
	private static final int BARREN_STORAGE_FACILITY_TYPE = 2541;
	private static final int BARREN_LAUNCHPAD_FACILITY_TYPE = 2544;
	private static final int BARREN_COMMANDCENTER_FACILITY_TYPE = 2524;

	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(StorageFacility.class);
	}

	@Test
	public void getPlanetType() {
		StorageFacility facility = this.createFacility4Storage(BARREN_STORAGE_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the testplanet type.", PlanetType.BARREN, facility.getPlanetType());
	}

	@Test
	public void getStorageCapacity() {
		// Storage facility
		StorageFacility facility = this.createFacility4Storage(BARREN_STORAGE_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check storage capacity for the facility.", 12000.0F, facility.getStorageCapacity(), 10.0F);

		// Launchpad facility
		facility = this.createFacility4Storage(BARREN_LAUNCHPAD_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check storage capacity for the facility.", 10000.0F, facility.getStorageCapacity(), 10.0F);

		// Launchpad facility
		facility = this.createFacility4Storage(BARREN_COMMANDCENTER_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check storage capacity for the facility.", 500.0F, facility.getStorageCapacity(), 10.0F);
	}

	@Test
	public void getTotalVolume() {
		final StorageFacility facility = this.createFacility4Storage(BARREN_STORAGE_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the storage volume used on the facility.", 3000 * 1.5, facility.getTotalVolume(), 0.01);
	}

	@Test
	public void getTotalValue() {
		final StorageFacility facility = this.createFacility4Storage(BARREN_STORAGE_FACILITY_TYPE);
		final EveItem item = new EveItem(2463);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check storage value on the facility.", 3000 * item.getPrice(), facility.getTotalValue(), 0.1 * 3000);
	}

	@Test
	public void getCpuUsage() {
		// Storage facility
		StorageFacility facility = this.createFacility4Storage(BARREN_STORAGE_FACILITY_TYPE);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU used.", 500, facility.getCpuUsage());

		// Launchpad facility
		facility = this.createFacility4Storage(2544);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU used.", 3600, facility.getCpuUsage());
	}

	@Test
	public void getPowerUsage() {
		// Storage facility
		StorageFacility facility = this.createFacility4Storage(2541);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power used.", 700, facility.getPowerUsage());

		// Launchpad facility
		facility = this.createFacility4Storage(2544);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power used.", 700, facility.getPowerUsage());
	}

	protected StorageFacility createFacility4Storage( final int factoryTypeId ) {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setTypeId(factoryTypeId);
		pin.setPinId(1022847372295L);
		pin.setLastCycleStart(DateTime.now());
		pin.setLatitude(2.27239886788F);
		pin.setLongitude(1.97163458582F);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2463); // Nanites
		content.setAmount(3000L);
		contents.add(content);
		pin.setContents(contents);
		final EveItem facilityItem = new EveItem(pin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(pin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.build();
		return new StorageFacility.Builder()
				.withPlanetaryFacility(facility)
				.withPlanetaryStorage(new PlanetaryStorage(facility.getContents()))
				.build();
	}
}