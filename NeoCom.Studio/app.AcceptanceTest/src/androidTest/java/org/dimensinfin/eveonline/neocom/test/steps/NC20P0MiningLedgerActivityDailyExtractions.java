package org.dimensinfin.eveonline.neocom.test.steps;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportCredentialRepository;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportMiningRepository;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTableToMiningExtractionConverter;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

public class NC20P0MiningLedgerActivityDailyExtractions {
	private CucumberTable2CredentialConverter cucumberTable2CredentialConverter;
	private CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter;

	public NC20P0MiningLedgerActivityDailyExtractions( final CucumberTable2CredentialConverter cucumberTable2CredentialConverter,
	                                                   final CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter ) {
		this.cucumberTable2CredentialConverter = cucumberTable2CredentialConverter;
		this.cucumberTableToMiningExtractionConverter = cucumberTableToMiningExtractionConverter;
	}

	/**
	 * Replace the NeoCom components by the mocked and support components that should e used for
	 * instrumentation testing.
	 */
	@Given("the reconfigured neocom component factory")
	public void theReconfiguredNeocomComponentFactory() throws SQLException {
		final SupportCredentialRepository credentialRepository = new SupportCredentialRepository.Builder()
				                                                         .withCredentialDao(NeoComComponentFactory.getSingleton()
						                                                                            .getNeoComDBHelper()
						                                                                            .getCredentialDao())
				                                                         .build();
		NeoComComponentFactory.getSingleton().setCredentialRepository(credentialRepository);
		final SupportMiningRepository miningRepository = new SupportMiningRepository.Builder()
				                                                 .withMiningExtractionDao(NeoComComponentFactory.getSingleton()
						                                                                          .getNeoComDBHelper()
						                                                                          .getMiningExtractionDao())
				                                                 .build();
		NeoComComponentFactory.getSingleton().setMiningRepository(miningRepository);
	}

	/**
	 * Cleanup the repository from all real records before setting up the new test records.
	 */
	@And("a new application repository")
	public void aNewApplicationRepository() {
		((SupportCredentialRepository) NeoComComponentFactory.getSingleton().getCredentialRepository()).deleteAll();
		((SupportMiningRepository) NeoComComponentFactory.getSingleton().getMiningRepository()).deleteAll();
	}

	@And("the next records on the CredentialRepository")
	public void theNextRecordsOnTheCredentialRepository( final List<Map<String, String>> cucumberTable ) throws SQLException {
		final CredentialRepository credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
		for (Map<String, String> row : cucumberTable) {
			final Credential credential = this.cucumberTable2CredentialConverter.convert(row);
			Assert.assertNotNull("The record is created.", credential);
			credentialRepository.persist(credential);
		}
	}

	@And("the next records on the MiningRepository")
	public void theNextRecordsOnTheMiningRepository( final List<Map<String, String>> cucumberTable ) throws SQLException {
		final MiningRepository miningRepository = NeoComComponentFactory.getSingleton().getMiningRepository();
		for (Map<String, String> row : cucumberTable) {
			final MiningExtraction extraction = this.cucumberTableToMiningExtractionConverter.convert(row);
			Assert.assertNotNull("The record is created.", extraction);
			miningRepository.persist(extraction);
		}
	}

}