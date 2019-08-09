package org.dimensinfin.eveonline.neocom.steps;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.support.miningExtractions.CucumberTableToMiningExtractionConverter;
import org.dimensinfin.eveonline.neocom.support.miningExtractions.MiningExtractionsWorld;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.Given;

public class GivenTheNextRecordsOnTheMiningRepository {
	private MiningExtractionsWorld miningExtractionsWorld;
	private CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter;

	public GivenTheNextRecordsOnTheMiningRepository( final MiningExtractionsWorld miningExtractionsWorld,
	                                                 final CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter ) {
		this.miningExtractionsWorld = miningExtractionsWorld;
		this.cucumberTableToMiningExtractionConverter = cucumberTableToMiningExtractionConverter;
	}

	@Given("the next records on the MiningRepository")
	public void theNextRecordsOnTheMiningRepository( final List<Map<String, String>> cucumberTable ) throws SQLException {
		for (Map<String, String> row : cucumberTable) {
			final MiningExtraction extraction = this.cucumberTableToMiningExtractionConverter.convert(row);
			Assert.assertNotNull("The record is created.", extraction);
			this.miningExtractionsWorld.getMiningRepository().persist(extraction);
		}
	}
}
