package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.support.PlanetaryRepositorySupportTest;
import org.dimensinfin.eveonline.neocom.support.TestFeaturesControllerFactory;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * To test this data source I have to mock the data downloaded from CCP using a mock data generator loaded with a static
 * copy of the planet and structures. The problem is that the ESI Adapter should be properly initialised and thus all their
 * dependency components.
 *
 * Create a task to do the initialisation before starting each test.
 * WARNING: This cannot be done since the Application is a requirement and cannot be mocked properly. I should replace the output
 * by programmatically generated GetCharactersCharacterIdPlanets200Ok and GetCharactersCharacterIdPlanetsPlanetIdOk.
 */
public class PlanetaryColoniesDataSourceTest extends PlanetaryRepositorySupportTest {
	private Integer ownerId;
	private Integer targetPlanetId;


	//	private static ESIDataAdapter esiAdapter;
	private static List<GetCharactersCharacterIdPlanets200Ok> mockColoniesData = new ArrayList<>();
//	private Credential credential;
private  TestFeaturesControllerFactory factory;

	@Before
	public void setUp()  {
		super.setUp();
		ownerId = 93813310;
		targetPlanetId = 40208303;
		credential.setAccountId(ownerId);

//		esiAdapter = Mockito.mock(ESIDataAdapter.class);
//		credential = new Credential()
//				             .setAccountId(92223647)
//				             .setRefreshToken("TEST REFRESH TOKEN")
//				             .setDataSource("tranquility");
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
		factory = new TestFeaturesControllerFactory("TEST-VARIANT");
	}

	@Test
	public void builder_ok() {
		final PlanetaryRepository planetaryRepository = Mockito.mock(PlanetaryRepository.class);
		final PlanetaryColoniesDataSource datasource = new PlanetaryColoniesDataSource.Builder()
				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
				                                       .withVariant("TEST VARIANT")
				                                       .withFactory(factory)
				                                       .withCredential(credential)
				                                       .withEsiAdapter(esiDataAdapter)
				                                       .withPlanetaryRepository(planetaryRepository)
				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
	}

	@Test(expected = NullPointerException.class)
	public void builder_missingMandatory() {
		final PlanetaryColoniesDataSource datasource = new PlanetaryColoniesDataSource.Builder()
				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
				                                       .withVariant("TEST VARIANT")
				                                       .withFactory(factory)
				                                       .withCredential(null)
				                                       .withEsiAdapter(esiDataAdapter)
				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
	}

	@Test
	public void prepareModel_onlyColony() {
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanets");
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanetsPlanetId");
		final PlanetaryColoniesDataSource datasource = new PlanetaryColoniesDataSource.Builder()
				.addIdentifier("TEST PLANETARY DATASOURCE")
				.withVariant("TEST VARIANT")
				.withFactory(factory)
				.withCredential(credential)
				.withEsiAdapter(esiDataAdapter)
				.withPlanetaryRepository(planetaryRepository)
				.build();

		//		final GetCharactersCharacterIdPlanetsPlanetIdOk structure = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOk.class);
//		esiDataAdapter = Mockito.mock(ESIDataAdapter.class);
//		Mockito.when(esiDataAdapter.getCharactersCharacterIdPlanets(
//				any(Integer.class)
//				, any(String.class)
//				, any(String.class)
//		)).thenReturn(mockColoniesData);
//		Mockito.when(esiDataAdapter.getCharactersCharacterIdPlanetsPlanetId(
//				any(Integer.class)
//				, any(Integer.class)
//				, any(String.class)
//				, any(String.class)
//		)).thenReturn(structure);
//		final PlanetaryColoniesDataSource datasource = new PlanetaryColoniesDataSource.Builder()
//				                                       .addIdentifier("TEST PLANETARY DATASOURCE")
//				                                       .withVariant("TEST VARIANT")
//				                                       .withFactory(factory)
//				                                       .withCredential(credential)
//				                                       .withEsiAdapter(esiDataAdapter)
//				                                       .build();
		Assert.assertNotNull("Check the data source is properly created.", datasource);
		datasource.prepareModel();
		datasource.collaborate2Model();
		final int obtained = datasource.getDataSectionContents().size();
		Assert.assertEquals("The number of colonies should be 1.", 1, obtained);
	}
}
