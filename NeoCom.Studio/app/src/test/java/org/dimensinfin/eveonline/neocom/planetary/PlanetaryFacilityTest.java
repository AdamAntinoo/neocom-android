package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.support.PojoTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PlanetaryFacilityTest {
	private static IPlanetaryFacility globalFacility;
	private static final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
	private static final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
	private static final GetUniverseSchematicsSchematicIdOk schematic=Mockito.mock(GetUniverseSchematicsSchematicIdOk.class);
	private static final EveItem planetItem = Mockito.mock(EveItem.class);

	@Before
	public void setUp()  {
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById(111)).thenReturn(schematic);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		globalFacility = new PlanetaryFacility.Builder()
				                 .withPin(pin)
				                 .withFacilityItem(planetItem)
//				                 .withEsiDataAdapter(esiAdapter)
				                 .withPlanetType(PlanetType.GAS)
				                 .build();
	}

	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(IPlanetaryFacility.class);
	}

//	@Test
	public void builder_allfields() {
		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(planetItem)
//				                                   .withEsiDataAdapter(esiAdapter)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		Assert.assertNotNull(facility);
	}

	@Test(expected = NullPointerException.class)
	public void builder_exception() {
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(planetItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
	}

	@Test
	public void getFacilityType() {
		final PlanetaryFacilityType type = globalFacility.getFacilityType();
		Assert.assertNotNull(type);
	}

	@Test
	public void getName() {
		final EveItem resourceItem = Mockito.mock(EveItem.class);
		Mockito.when(resourceItem.getName()).thenReturn("TEST FACILITY - NOT USABLE");
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(resourceItem)
//				                                   .withEsiDataAdapter(esiAdapter)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final String name = facility.getName();
		Assert.assertNotNull(name);
		Assert.assertEquals("The name should match the one initialised.", "TEST FACILITY - NOT USABLE", name);
	}

	@Test
	public void getStorageCapacity() {
		final EveItem resourceItem = Mockito.mock(EveItem.class);
		Mockito.when(resourceItem.getCapacity()).thenReturn((float) 100.0);
		final PlanetaryFacility facility = (PlanetaryFacility) new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(resourceItem)
//				                                   .withEsiDataAdapter(esiAdapter)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final Float capacity = facility.getStorageCapacity();
		Assert.assertNotNull(capacity);
		Assert.assertEquals(100.0, new Double(capacity).doubleValue(), 0.01);
	}

	@Test(expected = NullPointerException.class)
	public void getSchematicId_nonfactory() {
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(planetItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final Integer schematic = facility.getSchematicId();
		Assert.assertNull(schematic);
	}

	@Test
	public void getSchematicId_factory() {
//		final GetUniverseTypesTypeIdOk resourceItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		final PlanetarySchematic planetSchematic = Mockito.mock(PlanetarySchematic.class);
		Mockito.when(pin.getSchematicId()).thenReturn(100);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById(100)).thenReturn(schematic);
		Mockito.when(planetSchematic.getSchematicId()).thenReturn(100);
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(planetItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final Integer schematicId = facility.getSchematicId();
		Assert.assertNotNull(schematicId);
		Assert.assertEquals(100, schematicId.intValue());
	}

	@Test
	public void getUniqueModelIdentifier() {
//		final GetUniverseTypesTypeIdOk resourceItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
//		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
		Mockito.when(pin.getPinId()).thenReturn(200L);
		final PlanetaryFacility facility = (PlanetaryFacility) new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(planetItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final long id = facility.getUniqueModelIdentifier();
		Assert.assertEquals(200L, id);
	}

//	@Test
	public void getContentsVolume() {
		final EveItem resourceItem = Mockito.mock(EveItem.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents planetContent = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkContents.class);
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById(111)).thenReturn(schematic);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		Mockito.when(planetContent.getTypeId()).thenReturn(2395);
		Mockito.when(planetContent.getAmount()).thenReturn(new Long(1000));
		contents.add(planetContent);
		Mockito.when(pin.getContents()).thenReturn(contents);
		final IPlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withFacilityItem(resourceItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
//		final double obtained = facility.getContentsVolume();
//		Assert.assertEquals("The content volume matches.", 1000.0 * 0.38, obtained, 0.01);
	}

	@Test
	public void getGeoPosition() {
		final FacilityGeoPosition position = globalFacility.getGeoPosition();
		Assert.assertNotNull(position);
	}

//	@Test
//	public void getBackgroundColor() {
//		final int color = globalFacility.getBackgroundColor();
//		Assert.assertEquals("Check the color identifier.", 2131099715, color);
//	}

	@Test
	public void getIconReferenceId() {
		final int reference = globalFacility.getIconReferenceId();
		Assert.assertEquals("Check the color identifier.", 2131230800, reference);
	}

	@Test
	public void getIconColorReference() {
		final int reference = globalFacility.getIconColorReference();
		Assert.assertEquals("Check the color identifier.", 2131099717, reference);
	}

//	@Test
//	public void getStructureColorReference() {
//		final int reference = globalFacility.getStructureColorReference();
//		Assert.assertEquals("Check the color identifier.", 2131099718, reference);
//	}

	@Test
	public void compareTo_less() {
		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
		final EveItem resourceItemA = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemA.getName()).thenReturn("TEST FACILITY - A");
		final EveItem resourceItemB = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemB.getName()).thenReturn("TEST FACILITY - B");
		final IPlanetaryFacility facilityA = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemA)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final IPlanetaryFacility facilityB = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemB)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final int comparison = facilityA.compareTo(facilityB);
		Assert.assertEquals(-1, comparison);
	}

	@Test
	public void compareTo_equal() {
		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
		final EveItem resourceItemA = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemA.getName()).thenReturn("TEST FACILITY - A");
		final EveItem resourceItemB = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemB.getName()).thenReturn("TEST FACILITY - A");
		final IPlanetaryFacility facilityA = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemA)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final IPlanetaryFacility facilityB = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemB)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final int comparison = facilityA.compareTo(facilityB);
		Assert.assertEquals(0, comparison);
	}

	@Test
	public void compareTo_greater() {
		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
		final EveItem resourceItemA = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemA.getName()).thenReturn("TEST FACILITY - B");
		final EveItem resourceItemB = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemB.getName()).thenReturn("TEST FACILITY - A");
		final IPlanetaryFacility facilityA = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemA)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final IPlanetaryFacility facilityB = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemB)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final int comparison = facilityA.compareTo(facilityB);
		Assert.assertEquals(1, comparison);
	}

	@Test
	public void compareTo_other() {
		final EveItem resourceItemA = Mockito.mock(EveItem.class);
		Mockito.when(resourceItemA.getName()).thenReturn("TEST FACILITY - B");
		final IPlanetaryFacility facilityA = new PlanetaryFacility.Builder()
				                                    .withPin(pin)
				                                    .withFacilityItem(resourceItemA)
				                                    .withPlanetType(PlanetType.GAS)
				                                    .build();
		final int comparison = facilityA.compareTo("NOT PLANETARY FACILITY");
		Assert.assertEquals(0, comparison);
	}
}
