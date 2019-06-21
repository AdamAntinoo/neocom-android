package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryControllerFactory;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

/**
 * To test this data source I have to mock the data downloaded from CCP using a mock data generator loaded with a static
 * copy of the planet and structures. The problem is that the ESI Adapter should be properly initialised and thus all their
 * dependency components.
 *
 * Create a task to do the initialisation before starting each test.
 * WARNING: This cannot be done since the Applciation is a requirement and cannot be mocked properly. I should replace the output
 * by programmatically generated GetCharactersCharacterIdPlanets200Ok and GetCharactersCharacterIdPlanetsPlanetIdOk.
 */
public class PlanetaryDataSourceTest {
	private static ESIDataAdapter esiAdapter;
	private static List<GetCharactersCharacterIdPlanets200Ok> mockColoniesData = new ArrayList<>();
	private Credential credential;

	@Before
	public void setUp() throws Exception {
		esiAdapter = Mockito.mock(ESIDataAdapter.class);
		credential = new Credential()
				             .setAccountId(92223647)
				             .setRefreshToken("TEST REFRESH TOKEN")
				             .setDataSource("tranquility");
		// Create the colony data from manual object creation.
		final GetCharactersCharacterIdPlanets200Ok colony = new GetCharactersCharacterIdPlanets200Ok();
		colony.setOwnerId(92223647);
		colony.setPlanetId(40208073);
		colony.setPlanetType(GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum.BARREN);
		colony.setSolarSystemId(30003280);
		colony.setUpgradeLevel(4);
		colony.setNumPins(11);
		colony.setLastUpdate(DateTime.parse("2019-04-20T23:56:39Z"));
		mockColoniesData.add(colony);
	}

	@Test
	public void builder_ok() {
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryDataSource datasource = new PlanetaryDataSource.Builder()
				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
				                                       .withVariant("TEST VARIANT")
				                                       .withFactory(factory)
				                                       .withCredential(credential)
				                                       .withEsiAdapter(esiAdapter)
				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
	}

	@Test(expected = NullPointerException.class)
	public void builder_missingMandatory() {
		final PlanetaryControllerFactory factory = Mockito.mock(PlanetaryControllerFactory.class);
		final PlanetaryDataSource datasource = new PlanetaryDataSource.Builder()
				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
				                                       .withVariant("TEST VARIANT")
				                                       .withFactory(factory)
				                                       .withCredential(null)
				                                       .withEsiAdapter(esiAdapter)
				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
	}

	@Test
	public void prepareModel_onlyColony() {
		final GetCharactersCharacterIdPlanetsPlanetIdOk structure = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOk.class);
		Mockito.when(esiAdapter.getCharactersCharacterIdPlanets(
				any(Integer.class)
				, any(String.class)
				, any(String.class)
		)).thenReturn(mockColoniesData);
		Mockito.when(esiAdapter.getCharactersCharacterIdPlanetsPlanetId(
				any(Integer.class)
				, any(Integer.class)
				, any(String.class)
				, any(String.class)
		)).thenReturn(structure);
		final PlanetaryDataSource datasource = new PlanetaryDataSource.Builder()
				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
				                                       .withVariant("TEST VARIANT")
				                                       .withFactory(new PlanetaryControllerFactory("TESTING"))
				                                       .withCredential(credential)
				                                       .withEsiAdapter(esiAdapter)
				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
		datasource.prepareModel();
		datasource.collaborate2Model();
		final int obtained = datasource.getDataSectionContents().size();
		Assert.assertEquals("The numbr of colonies should be 1.", 1, obtained);
	}
}
