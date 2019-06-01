package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PlanetaryColonyDataSourceTest {
	private static ESIGlobalAdapter esiAdapter;
	private static Credential credential;
	private static PlanetaryControllerFactory factory;

	@Before
	public void setUp() throws Exception {
		esiAdapter = Mockito.mock(ESIGlobalAdapter.class);
		credential = new Credential()
				             .setAccountId(92223647)
				             .setRefreshToken("TEST REFRESH TOKEN")
				             .setDataSource("tranquility");
		factory = Mockito.mock(PlanetaryControllerFactory.class);
	}

	@Test
	public void builder_ok() {
		final PlanetaryColonyDataSource datasource = new PlanetaryColonyDataSource.Builder()
				                                             .addIdentifier("TEST PLANETARY DATASOURCE")
				                                             .withVariant("TEST VARIANT")
				                                             .withFactory(factory)
				                                             .withCredential(credential)
				                                             .withEsiAdapter(esiAdapter)
				                                             .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
	}

	@Test
	public void prepareModel() {
		final Bundle extras = Mockito.mock(Bundle.class);
		final GetUniversePlanetsPlanetIdOk planet = Mockito.mock(GetUniversePlanetsPlanetIdOk.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOk colonyLayout = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOk.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin1 = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin2 = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		final GetUniverseTypesTypeIdOk planetItem = Mockito.mock(GetUniverseTypesTypeIdOk.class);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pinList = new ArrayList<>();
		pinList.add(pin1);
		pinList.add(pin2);
		Mockito.when(extras.getInt(Mockito.anyString())).thenReturn(4321);
		Mockito.when(extras.getString(Mockito.anyString())).thenReturn("TEMPERATE");
		Mockito.when(esiAdapter.getUniversePlanetsPlanetId(Mockito.anyInt()
				, Mockito.anyString()
				, Mockito.anyString())).thenReturn(planet);
		Mockito.when(esiAdapter.getCharactersCharacterIdPlanetsPlanetId(Mockito.anyInt()
				, Mockito.anyInt()
				, Mockito.anyString()
				, Mockito.anyString())).thenReturn(colonyLayout);
		Mockito.when(esiAdapter.getUniverseTypeById(Mockito.anyInt())).thenReturn(planetItem);
		Mockito.when(colonyLayout.getPins()).thenReturn(pinList);
		Mockito.when(pin1.getTypeId()).thenReturn(2254);
		Mockito.when(pin1.getSchematicId()).thenReturn(null);
		final PlanetaryColonyDataSource datasource = new PlanetaryColonyDataSource.Builder()
				                                             .addIdentifier("TEST PLANETARY DATASOURCE")
				                                             .withVariant("TEST VARIANT")
				                                             .withExtras(extras)
				                                             .withFactory(factory)
				                                             .withCredential(credential)
				                                             .withEsiAdapter(esiAdapter)
				                                             .build();
		datasource.prepareModel();
		//		datasource.collaborate2Model();
		//		datasource.getDataSectionContents()
	}
}
