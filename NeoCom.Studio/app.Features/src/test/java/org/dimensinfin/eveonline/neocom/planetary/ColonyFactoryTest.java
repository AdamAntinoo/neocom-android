package org.dimensinfin.eveonline.neocom.planetary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.test.support.CredentialSupportTest;
import org.dimensinfin.eveonline.neocom.test.support.PlanetaryRepositoryMock;

public class ColonyFactoryTest extends CredentialSupportTest {
	private ColonyFactory factory4Test;
	private Integer ownerId;
	private Integer targetPlanetId;
	private ColonyPack colony;
	private List<Schematics> schematics;
	private PlanetaryRepository planetaryRepository;

	@Override
	@Before
	public void setUp() throws IOException {
		super.setUp();
		this.planetaryRepository = new PlanetaryRepositoryMock.Builder().build().generatePlanetaryRepositoryMock();
//		this.schematics = this.generateSchematics();
//		Mockito.when(this.planetaryRepository.searchSchematics4Id(Mockito.anyInt())).thenReturn(schematics);
		ownerId = 93813310;
		targetPlanetId = 40208303;
		credential.setAccountId(ownerId);
		this.factory4Test = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
	}

	@Test
	public void builderComplete() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureA() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(null)
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureB() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withEsiDataAdapter(null)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureC() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .withPlanetaryRepository(null)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureD() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureE() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withPlanetaryRepository(this.planetaryRepository)
				                              .build();
		Assert.assertNotNull(factory);
	}
	@Test(expected = NullPointerException.class)
	public void builderFailureF() {
		final ColonyFactory factory = new ColonyFactory.Builder()
				                              .withCredential(this.credential)
				                              .withEsiDataAdapter(this.esiDataAdapter)
				                              .build();
		Assert.assertNotNull(factory);
	}

	@Test
	public void accessColonyNotCached() {
		final GetCharactersCharacterIdPlanets200Ok colonyData = Mockito.mock(GetCharactersCharacterIdPlanets200Ok.class);
		final ColonyFactory factorySpy = Mockito.spy(this.factory4Test);

		factorySpy.accessColony(colonyData);
		Mockito.verify(factorySpy, Mockito.times(1)).createColony(colonyData);
	}

	/**
	 * Cannot be used a PlanetaryRepository mocked because the schematic defines the type of factory.
	 */
	@Test
	public void build_fromData() {
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanets");
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanetsPlanetId");
		// Read the colony data and generate the support structureContainer, both for the header and for the main.
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.esiDataAdapter.getCharactersCharacterIdPlanets(
				credential.getAccountId(),
				credential.getRefreshToken(),
				credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			if (colonyEsi.getPlanetId().intValue() == this.targetPlanetId.intValue()) {
				this.colony = new ColonyFactory.Builder()
						.withEsiDataAdapter(this.esiDataAdapter)
						.withCredential(this.credential)
						.withPlanetaryRepository(this.planetaryRepository)
						.build()
						.accessColony(colonyEsi);
			}
		}
		Assert.assertNotNull("The colony should ve valid.", this.colony);
	}

	@Test
	public void calculateUsedCpu() {
		this.build_fromData();
		final int obtained = this.colony.calculateUsedCpu();
		final int expected = 8021;
		Assert.assertEquals("The used CPU should have a close value to the read Eve client value.", expected, obtained,10);
	}

	@Test
	public void calculateUsedPower() {
		this.build_fromData();
		final int obtained = this.colony.calculateUsedPower();
		final int expected = 16856;
		Assert.assertEquals("The used Power should have a close value to the read Eve client value.", expected, obtained,10);
	}

	protected List<Schematics> generateSchematics() {
		final List<Schematics> schematics = new ArrayList<>();
		schematics.add(new Schematics.Builder()
				.withSchematicId(92)
				.withSchematicName("Synthetic Synapses")
				.withCycleTime(3600)
				.withResourceTypeId(2312)
				.withQuantity(10)
				.withDirection((1 == 1) ? true : false)
				.build());
		schematics.add(new Schematics.Builder()
				.withSchematicId(92)
				.withSchematicName("Synthetic Synapses")
				.withCycleTime(3600)
				.withResourceTypeId(2319)
				.withQuantity(10)
				.withDirection((1 == 1) ? true : false)
				.build());
		schematics.add(new Schematics.Builder()
				.withSchematicId(92)
				.withSchematicName("Synthetic Synapses")
				.withCycleTime(3600)
				.withResourceTypeId(2346)
				.withQuantity(3)
				.withDirection((0 == 1) ? true : false)
				.build());
		return schematics;
	}
}