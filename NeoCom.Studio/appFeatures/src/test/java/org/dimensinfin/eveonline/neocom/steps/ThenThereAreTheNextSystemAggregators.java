package org.dimensinfin.eveonline.neocom.steps;

import org.dimensinfin.eveonline.neocom.mining.AggregatorContainer;
import org.dimensinfin.eveonline.neocom.mining.ExtractionSystem;
import org.dimensinfin.eveonline.neocom.support.miningExtractions.FeaturesMiningExtractionsWorld;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import cucumber.api.java.en.Then;

public class ThenThereAreTheNextSystemAggregators {
	private static final String SOLAR_SYSTEM_ID = "solarSystemId";
	private static final String SOLAR_SYSTEM_NAME = "solarSystemName";
	private FeaturesMiningExtractionsWorld miningExtractionsWorld;

	public ThenThereAreTheNextSystemAggregators( final FeaturesMiningExtractionsWorld miningExtractionsWorld ) {
		this.miningExtractionsWorld = miningExtractionsWorld;
	}

	@Then("there are the next system aggregators")
	public void thereAreTheNextSystemAggregators( final List<Map<String, String>> cucumberTable ) {
		final List<AggregatorContainer> systems = this.miningExtractionsWorld
				                                          .getTodayMiningDataSource().getExtractionSystems();
		int systemIndex = 0;
		for (Map<String, String> row : cucumberTable) {
			final ExtractionSystem system = systems.get(systemIndex).getFacetDelegate();
			Assert.assertEquals(Integer.parseInt(row.get(SOLAR_SYSTEM_ID)), system.getSystemId());
			Assert.assertEquals(row.get(SOLAR_SYSTEM_NAME), system.getSystemName());
		}
	}
}
