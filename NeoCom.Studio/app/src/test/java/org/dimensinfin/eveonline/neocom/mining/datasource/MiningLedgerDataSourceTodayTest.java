package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningControllerFactory;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MiningLedgerDataSourceTodayTest {
	@Test
	public void prepareModel_noitems() {

	}

	@Test
	public void prepareModel_singleLocation() throws SQLException {
		// Given
		final Credential credential = Mockito.mock(Credential.class);
		final MiningRepository miningRepository = Mockito.mock(MiningRepository.class);
		final MiningControllerFactory factory = new MiningControllerFactory("TEST-VARIANT");
		final MiningLedgerDataSourceToday dataSource = new MiningLedgerDataSourceToday.Builder()
				                                                                    .addIdentifier("TEST/MINING-TODAY")
				                                                                    .withFactory(factory)
				                                                                    .withCredential(credential)
				                                                                    .withMiningRepository(miningRepository)
				                                                                    .build();
		// When
		Mockito.when(miningRepository.accessTodayMiningExtractions4Pilot(credential)).thenReturn(this.extractionsToday_singleLocation());
		// Testing
		Assert.assertEquals(0, dataSource.getDataSectionContents().size());
		dataSource.prepareModel();
		Assert.assertEquals(0, dataSource.getDataSectionContents().size());
		dataSource.collaborate2Model();
		// Asserts
		Assert.assertEquals(2, dataSource.getDataSectionContents().size());
	}

	private List<MiningExtraction> extractionsToday_singleLocation() {
		final List<MiningExtraction> extractions = new ArrayList<>();
		MiningExtraction row = new MiningExtraction()
				                       .setOwnerId(123)
				                       .setExtractionDate(new LocalDate())
				                       .setQuantity(1000)
				                       .setTypeId(34)
				                       .setSolarSystemId(30001647);
		extractions.add(row);
		row = new MiningExtraction()
				      .setOwnerId(123)
				      .setExtractionDate(new LocalDate())
				      .setQuantity(2000)
				      .setTypeId(35)
				      .setSolarSystemId(30001647);
		extractions.add(row);
		return extractions;
	}
}
