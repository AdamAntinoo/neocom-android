package org.dimensinfin.eveonline.neocom.planetary.datasource;

public class PlanetaryCommonDataSourceTest {

//	@Test
//	public void convertPin2Facility_default() {
//		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
//		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins = new ArrayList<>();
//		final GetCharactersCharacterIdPlanetsPlanetIdOkPins facility = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
//		final GetUniverseTypesTypeIdOk item = Mockito.mock(GetUniverseTypesTypeIdOk.class);
//		pins.add(facility);
//		Mockito.when(facility.getTypeId()).thenReturn(2550);
//		Mockito.when(esiAdapter.getUniverseTypeById(2550)).thenReturn(item);
//		Mockito.when(item.getGroupId()).thenReturn(1028);
//		final PlanetaryCommonDataSource ds = Mockito.mock(PlanetaryCommonDataSource.class, Mockito.CALLS_REAL_METHODS);
//		Mockito.when(ds.getEsiDataAdapter()).thenReturn(esiAdapter);
//
//		final List<IPlanetaryFacility> facilities = ds.convertPin2Facility(pins, PlanetType.STORM);
//		Assert.assertTrue(facilities.size() > 0);
//		Assert.assertTrue(facilities.get(0) instanceof PlanetaryFacility);
//	}
//
//	@Test
//	public void convertPin2Facility_commandcenter() {
//		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
//		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins = new ArrayList<>();
//		final GetCharactersCharacterIdPlanetsPlanetIdOkPins facility = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
//		final GetUniverseTypesTypeIdOk item = Mockito.mock(GetUniverseTypesTypeIdOk.class);
//		pins.add(facility);
//		Mockito.when(facility.getTypeId()).thenReturn(2550);
//		Mockito.when(esiAdapter.getUniverseTypeById(2550)).thenReturn(item);
//		Mockito.when(item.getGroupId()).thenReturn(1027);
//		final PlanetaryCommonDataSource ds = Mockito.mock(PlanetaryCommonDataSource.class, Mockito.CALLS_REAL_METHODS);
//		Mockito.when(ds.getEsiDataAdapter()).thenReturn(esiAdapter);
//
//		final List<IPlanetaryFacility> facilities = ds.convertPin2Facility(pins, PlanetType.STORM);
//		Assert.assertTrue(facilities.size() > 0);
//		Assert.assertTrue(facilities.get(0) instanceof CommandCenterFacility);
//	}
//
//	@Test
//	public void convertPin2Facility_storage() {
//		final ESIDataAdapter esiAdapter = Mockito.mock(ESIDataAdapter.class);
//		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins = new ArrayList<>();
//		final GetCharactersCharacterIdPlanetsPlanetIdOkPins facility = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
//		final GetUniverseTypesTypeIdOk item = Mockito.mock(GetUniverseTypesTypeIdOk.class);
//		pins.add(facility);
//		Mockito.when(facility.getTypeId()).thenReturn(2550);
//		Mockito.when(esiAdapter.getUniverseTypeById(2550)).thenReturn(item);
//		Mockito.when(item.getGroupId()).thenReturn(1029);
//		final PlanetaryCommonDataSource ds = Mockito.mock(PlanetaryCommonDataSource.class, Mockito.CALLS_REAL_METHODS);
//		Mockito.when(ds.getEsiDataAdapter()).thenReturn(esiAdapter);
//
//		final List<IPlanetaryFacility> facilities = ds.convertPin2Facility(pins, PlanetType.STORM);
//		Assert.assertTrue(facilities.size() > 0);
//		Assert.assertTrue(facilities.get(0) instanceof StorageFacility);
//	}
}
