package org.dimensinfin.eveonline.neocom.test.steps;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportMiningRepository;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTableToMiningExtractionConverter;
import org.joda.time.LocalDate;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC20P0MiningLedgerActivityDailyExtractions extends StepFramework {
	private static final String EXTRA_TYPE = "extraType";
	private static final String EXTRA_NAME = "extraName";
	private static final String EXTRA_VALUE = "extraValue";
	private CucumberTable2CredentialConverter cucumberTable2CredentialConverter;
	private CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter;

	public NC20P0MiningLedgerActivityDailyExtractions( final NeoComWorld world,
	                                                   final CucumberTable2CredentialConverter cucumberTable2CredentialConverter,
	                                                   final CucumberTableToMiningExtractionConverter cucumberTableToMiningExtractionConverter ) {
		super(world);
		this.cucumberTable2CredentialConverter = cucumberTable2CredentialConverter;
		this.cucumberTableToMiningExtractionConverter = cucumberTableToMiningExtractionConverter;
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
		final SupportMiningRepository miningRepository = (SupportMiningRepository) NeoComComponentFactory.getSingleton().getMiningRepository();
		miningRepository.deleteAll();
		for (Map<String, String> row : cucumberTable) {
			final MiningExtraction extraction = this.cucumberTableToMiningExtractionConverter.convert(row);
			Assert.assertNotNull("The record is created.", extraction);
			miningRepository.persist(extraction);
		}
	}

	@And("the activity {string} with the next extras")
	public void theActivityWithTheNextExtras( final String activityClassName, final List<Map<String, String>> cucumberTable ) {
		this.createActivity(activityClassName);
		Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		switch (activityClassName) {
			case "MiningLedgerActivity":
				Intent intent = new Intent(targetContext, MiningLedgerActivity.class);
				intent = this.setIntentExtras(intent, cucumberTable);
				this.world.getActivityRule().launchActivity(intent);
				this.world.setActiveActivity((FragmentActivity) this.world.getActivityRule().getActivity());
				Assert.assertNotNull(this.world.getActiveActivity());
				break;
		}
	}

	private Intent setIntentExtras( final Intent intent, final List<Map<String, String>> cucumberTable ) {
		for (Map<String, String> row : cucumberTable) {
			switch (row.get(EXTRA_TYPE)) {
				case "Integer":
					intent.putExtra(row.get(EXTRA_NAME), Integer.parseInt(row.get(EXTRA_VALUE)));
					break;
			}
		}
		return intent;
	}

	@And("the page variant set to {string}")
	public void thePageVariantSetTo( final String variant ) {
		Assert.assertEquals(variant, this.world.getFragment().getVariant());
	}

	@And("today date being {string}")
	public void theTodayDateBeing( final String todayDate ) {
		if (todayDate.equalsIgnoreCase("<today>")) this.world.setTodayDate(LocalDate.now());
		else this.world.setTodayDate(new LocalDate(todayDate));
	}

	@Then("check that the custom action bar is present")
	public void checkThatTheCustomActionBarIsPresent() {
		// TODO - some elements should be created on another thread.
//		this.world.getExecutor().submit(() -> {
//			this.world.setActionBar(this.world.getFragment().generateActionBarView());
//		});
//		Awaitility.await().atMost(1, MINUTES).until(() -> null != this.world.getActionBar());
		this.world.setActionBar(this.world.getFragment().generateActionBarView());
		Assert.assertNotNull(this.world.getActionBar());
	}

	@When("complete the lifecycle without exceptions")
	public void completeTheLifecycleWithoutExceptions() throws InterruptedException {
		Thread.sleep(TimeUnit.SECONDS.toMillis(3));
		Assert.assertNull(this.world.getFragment().getLastException());
	}
}