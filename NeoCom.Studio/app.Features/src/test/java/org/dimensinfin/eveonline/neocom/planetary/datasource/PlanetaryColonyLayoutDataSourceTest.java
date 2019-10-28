package org.dimensinfin.eveonline.neocom.planetary.datasource;

import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.test.support.CredentialSupportTest;

public class PlanetaryColonyLayoutDataSourceTest extends CredentialSupportTest {
//	private static ESIDataAdapter esiAdapter;
//	private static Credential credential;
	private  ControllerFactory factory;

	@Before
	public void setUp() throws IOException {
		super.setUp();
//		esiAdapter = Mockito.mock(ESIDataAdapter.class);
//		credential = new Credential()
//				             .setAccountId(92223647)
//				             .setRefreshToken("TEST REFRESH TOKEN")
//				             .setDataSource("tranquility");
		factory = Mockito.mock(ControllerFactory.class);
	}

	@Test
	public void builder_ok() {
		final PlanetaryColonyLayoutDataSource datasource = new PlanetaryColonyLayoutDataSource.Builder()
				                                             .addIdentifier("TEST PLANETARY DATASOURCE")
				                                             .withVariant("TEST VARIANT")
				                                             .withFactory(factory)
				                                             .withCredential(credential)
				                                             .withEsiAdapter(esiDataAdapter)
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
		Mockito.when(esiDataAdapter.getUniversePlanetsPlanetId(Mockito.anyInt())).thenReturn(planet);
		Mockito.when(esiDataAdapter.getCharactersCharacterIdPlanetsPlanetId(Mockito.anyInt()
				, Mockito.anyInt()
				, Mockito.anyString()
				, Mockito.anyString())).thenReturn(colonyLayout);
		Mockito.when(esiDataAdapter.getUniverseTypeById(Mockito.anyInt())).thenReturn(planetItem);
		Mockito.when(colonyLayout.getPins()).thenReturn(pinList);
		Mockito.when(pin1.getTypeId()).thenReturn(2254);
		Mockito.when(pin1.getSchematicId()).thenReturn(null);
		final PlanetaryColonyLayoutDataSource datasource = new PlanetaryColonyLayoutDataSource.Builder()
				                                             .addIdentifier("TEST PLANETARY DATASOURCE")
				                                             .withVariant("TEST VARIANT")
				                                             .withExtras(extras)
				                                             .withFactory(factory)
				                                             .withCredential(credential)
				                                             .withEsiAdapter(esiDataAdapter)
				                                             .build();
		datasource.prepareModel();
		//		datasource.collaborate2Model();
		//		datasource.getDataSectionContents()
	}
}
