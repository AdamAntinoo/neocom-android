package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.PojoTestUtils;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class PlanetaryFacilityTest {
	private static PlanetaryFacility globalFacility;

	@Before
	public void setUp() throws Exception {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final ESIGlobalAdapter esiAdapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetUniverseSchematicsSchematicIdOk schematic = Mockito.mock(GetUniverseSchematicsSchematicIdOk.class);
		final GetUniverseTypesTypeIdOk planetItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById("tranquility", 111)).thenReturn(schematic);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		globalFacility = new PlanetaryFacility.Builder()
				                 .withPin(pin)
				                 .withPlanetItem(planetItem)
				                 .withEsiAdapter(esiAdapter)
				                 .withPlanetType(PlanetType.GAS)
				                 .build();
	}

	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(PlanetaryFacility.class);
	}

	@Test
	public void builder_allfields() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final ESIGlobalAdapter esiAdapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetUniverseSchematicsSchematicIdOk schematic = Mockito.mock(GetUniverseSchematicsSchematicIdOk.class);
		final GetUniverseTypesTypeIdOk planetItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById("tranquility", 111)).thenReturn(schematic);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withPlanetItem(planetItem)
				                                   .withEsiAdapter(esiAdapter)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		Assert.assertNotNull(facility);
	}

	@Test(expected = NullPointerException.class)
	public void builder_exception() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final GetUniverseTypesTypeIdOk planetItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withPlanetItem(planetItem)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
	}

	@Test
	public void getContentsVolume() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final ESIGlobalAdapter esiAdapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetUniverseSchematicsSchematicIdOk schematic = Mockito.mock(GetUniverseSchematicsSchematicIdOk.class);
		final GetUniverseTypesTypeIdOk resourceItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents planetContent = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkContents.class);
		Mockito.when(pin.getSchematicId()).thenReturn(null);
		Mockito.when(esiAdapter.getUniversePlanetarySchematicsById("tranquility", 111)).thenReturn(schematic);
		Mockito.when(esiAdapter.getUniverseTypeById(any(Integer.class))).thenReturn(resourceItem);
		Mockito.when(resourceItem.getVolume()).thenReturn((float) 0.38);
		Mockito.when(schematic.getSchematicName()).thenReturn("TEST SCHEMATIC");
		Mockito.when(schematic.getCycleTime()).thenReturn(3600);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		Mockito.when(planetContent.getTypeId()).thenReturn(2395);
		Mockito.when(planetContent.getAmount()).thenReturn(new Long(1000));
		contents.add(planetContent);
		Mockito.when(pin.getContents()).thenReturn(contents);
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				                                   .withPin(pin)
				                                   .withPlanetItem(resourceItem)
				                                   .withEsiAdapter(esiAdapter)
				                                   .withPlanetType(PlanetType.GAS)
				                                   .build();
		final double obtained = facility.getContentsVolume();
		Assert.assertEquals("The content volume matches.", 1000.0 * 0.38, obtained, 0.01);
	}

	@Test
	public void getStructureType() {
		final PlanetaryFacilityType type = globalFacility.getStructureType();
		Assert.assertNotNull(type);
	}

	@Test
	public void getGeoPosition() {
		final FacilityGeoPosition position = globalFacility.getGeoPosition();
		Assert.assertNotNull(position);
	}

	@Test
	public void getBackgroundColor() {
		final int color = globalFacility.getBackgroundColor();
		Assert.assertEquals("Check the color identifier.", 2131099715, color);
	}

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

	@Test
	public void getStructureColorReference() {
		final int reference = globalFacility.getStructureColorReference();
		Assert.assertEquals("Check the color identifier.", 2131099718, reference);
	}
}
