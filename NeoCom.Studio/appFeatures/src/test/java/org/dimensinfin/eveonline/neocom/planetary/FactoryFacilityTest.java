package org.dimensinfin.eveonline.neocom.planetary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.market.MarketDataSet;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.support.TestAdapterReadyUp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FactoryFacilityTest extends TestAdapterReadyUp {
	private static PlanetaryRepository planetaryRepository;
	private static PlanetaryFacility facility;

	@Before
	public void setUp() throws Exception {
		final ESIDataAdapter esiDataAdapter = this.setupRealAdapter();
		EveItem.injectEsiDataAdapter(esiDataAdapter);
		MarketDataSet.injectEsiDataAdapter(esiDataAdapter);
		planetaryRepository = Mockito.mock(PlanetaryRepository.class);
		facility = Mockito.mock(PlanetaryFacility.class);
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents content = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkContents.class);
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contentsList = new ArrayList<>();
		Mockito.when(content.getTypeId()).thenReturn(2329);
		contentsList.add(content);
		contentsList.add(content);
		Mockito.when(facility.getContents()).thenReturn(contentsList);
	}

	@Test
	public void build_complete() throws IOException {
		final List<Schematics> schematicsList = new ArrayList<>();
		final Integer schematicId = 123;
		schematicsList.add(this.generateSchematic4Tier1(schematicId));
		Mockito.when(planetaryRepository.searchSchematics4Id(Mockito.anyInt())).thenReturn(schematicsList);
		final FactoryFacility factory = new FactoryFacility.Builder()
				                                .withPlanetaryRepository(planetaryRepository)
				                                .withSchematics(schematicId)
				                                .withPlanetaryFacility(facility)
				                                .build();
		Assert.assertNotNull(factory);
	}

	@Test
	public void setFactoryType() throws IOException {
		final Integer schematicId = 79;
		final List<Schematics> schematicsList = new ArrayList<>();
		schematicsList.add(this.generateSchematic4Tier2(schematicId));
		Mockito.when(planetaryRepository.searchSchematics4Id(Mockito.anyInt())).thenReturn(schematicsList);
		final FactoryFacility factory = new FactoryFacility.Builder()
				                                .withPlanetaryRepository(planetaryRepository)
				                                .withSchematics(schematicId)
				                                .withPlanetaryFacility(facility)
				                                .build();
		Assert.assertEquals(FactoryType.ADVANCED_INDUSTRY, factory.getFactoryType());
	}

	protected Schematics generateSchematic4Tier1( final Integer schematicId ) throws IOException {
		return new Schematics.Builder()
				       .withSchematicId(schematicId)
				       .withSchematicName("Electrolytes")
				       .withCycleTime(1800)
				       .withResourceTypeId(2390)
				       .withQuantity(1000)
				       .withDirection(false)
				       .build();
	}

	protected Schematics generateSchematic4Tier2( final Integer schematicId ) throws IOException {
		return new Schematics.Builder()
				       .withSchematicId(schematicId)
				       .withSchematicName("Biocells")
				       .withCycleTime(3600)
				       .withResourceTypeId(2329)
				       .withQuantity(1000)
				       .withDirection(false)
				       .build();
	}
}
