package org.dimensinfin.eveonline.neocom.steps;

import android.os.Bundle;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;
import org.dimensinfin.eveonline.neocom.support.miningExtractions.FeaturesMiningExtractionsWorld;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NF01ProcessMiningLedgerDaily {
	private static final String SOLAR_SYSTEM_ID = "solarSystemId";
	private static final String SOLAR_SYSTEM_NAME = "solarSystemName";
	private static final String TYPE_ID = "typeId";
	private static final String QUANTITY = "quantity";
	private static final String HOUR = "hour";
	private FeaturesMiningExtractionsWorld miningExtractionsWorld;

	public NF01ProcessMiningLedgerDaily( final FeaturesMiningExtractionsWorld miningExtractionsWorld ) {
		this.miningExtractionsWorld = miningExtractionsWorld;
	}

	@When("the request to get today extraction records is fired")
	public void theRequestToGetTodayExtractionRecordsIsFired() throws SQLException {
		this.miningExtractionsWorld.setTodayMiningDataSource(new MiningLedgerDataSourceToday.Builder()
				                                                     .addIdentifier(this.getVariant())
				                                                     .addIdentifier(this.getCredential().getAccountId())
				                                                     .withVariant(this.getVariant())
				                                                     .withExtras(this.getExtras())
				                                                     .withFactory(this.createFactory())
				                                                     .withCredential(this.getCredential())
				                                                     .withEsiDataAdapter(
						                                                     this.miningExtractionsWorld.getEsiDataAdapter())
				                                                     .withMiningRepository(
						                                                     this.miningExtractionsWorld.getMiningRepository())
				                                                     .build());
		Assert.assertNotNull(this.miningExtractionsWorld.getTodayMiningDataSource());
		this.miningExtractionsWorld.getMiningRepository().remapTodayDate(LocalDate.now());
		this.miningExtractionsWorld.getTodayMiningDataSource().prepareModel();
	}

	@Then("there are the next system aggregators")
	public void thereAreTheNextSystemAggregators( final List<Map<String, String>> cucumberTable ) {
		final List<ResourceAggregatorContainer<EsiLocation>> systems = this.miningExtractionsWorld
				                                                               .getTodayMiningDataSource().getExtractionSystems();
		int systemIndex = 0;
		for (Map<String, String> row : cucumberTable) {
			final EsiLocation system = systems.get(systemIndex).getFacetDelegate();
			Assert.assertEquals(Integer.parseInt(row.get(SOLAR_SYSTEM_ID)), system.getSystemId());
			Assert.assertEquals(row.get(SOLAR_SYSTEM_NAME), system.getSystemName());
			systemIndex++;
		}
	}

	@And("system aggregator {string} having the next hourly records")
	public void systemAggregatorHavingTheNextHourlyRecords( final String locationId, final List<Map<String, String>> cucumberTable ) {
		final List<ResourceAggregatorContainer<EsiLocation>> systems = this.miningExtractionsWorld
				                                                               .getTodayMiningDataSource().getExtractionSystems();
		for (ResourceAggregatorContainer<EsiLocation> container : systems) {
			if (container.getFacetDelegate().getId() == Integer.parseInt(locationId)) {
				final List<MiningExtraction> extractions = container.getContentList();
				int extractionIndex = 0;
				for (Map<String, String> row : cucumberTable) {
					Assert.assertEquals(Integer.parseInt(row.get(TYPE_ID)), extractions.get(extractionIndex).getTypeId());
					Assert.assertEquals(Integer.parseInt(row.get(SOLAR_SYSTEM_ID)),
					                    extractions.get(extractionIndex).getSolarSystemId());
					Assert.assertEquals(Integer.parseInt(row.get(QUANTITY)), extractions.get(extractionIndex).getQuantity());
					Assert.assertEquals(Integer.parseInt(row.get(HOUR)), extractions.get(extractionIndex).getExtractionHour());
					extractionIndex++;
				}
			}
		}
	}

	private Credential getCredential() {
		return this.miningExtractionsWorld.getCredential();
	}

	private String getVariant() {
		return PageNamesType.MINING_EXTRACTIONS_TODAY.name();
	}

	private Bundle getExtras() {
		return null;
	}

	private ControllerFactory createFactory() {
		return Mockito.mock(ControllerFactory.class);
	}
}