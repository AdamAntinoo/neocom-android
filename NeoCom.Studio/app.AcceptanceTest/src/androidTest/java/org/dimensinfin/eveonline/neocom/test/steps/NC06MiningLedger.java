package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.backend.NeoComBackendService;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtractionEntity;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2MiningExtractionEntityConverter;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC06MiningLedger extends AuthorizedStep {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	@Rule
	public GrantPermissionRule permissionInternet = GrantPermissionRule.grant( Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule permissionNetworkState = GrantPermissionRule.grant( Manifest.permission.ACCESS_NETWORK_STATE );
	@Rule
	public GrantPermissionRule permissionWriteStorage = GrantPermissionRule.grant( Manifest.permission.WRITE_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule permissionReadStorage = GrantPermissionRule.grant( Manifest.permission.READ_EXTERNAL_STORAGE );

	public NC06MiningLedger( final NeoComWorld world ) {
		super( world );
	}

	public void initializeScenario() {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
	}

	@Given("the MiningLegderActivity")
	public void the_MiningLegderActivity() {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the MiningLegderActivity" );
	}

	@Given("the following MiningExtractionEntity into my repository")
	public void the_following_MiningExtractionEntity_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following MiningExtractionEntity into my repository" );
		for (Map<String, String> row : dataTable) {
			final MiningExtractionEntity miningExtraction = new CucumberTable2MiningExtractionEntityConverter().convert( row );
			this.world.getMiningRepository().persist( miningExtraction );
		}
	}
	@Given("the following MiningExtractionEntity on backend response")
	public void the_following_MiningExtractionEntity_on_backend_response( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following MiningExtractionEntity on backend response" );
		final List<MiningExtractionEntity> backendTodayMiningExtractionsResponse = new ArrayList<>(  );
		for (Map<String, String> row : dataTable) {
			final MiningExtractionEntity miningExtraction = new CucumberTable2MiningExtractionEntityConverter().convert( row );
			backendTodayMiningExtractionsResponse.add(miningExtraction);
		}
		this.world.setBackendTodayMiningExtractionsResponse(backendTodayMiningExtractionsResponse);
		// Mock the backend server response.
		final NeoComBackendService backendService = Mockito.mock(NeoComBackendService.class);
		Mockito.when( backendService.accessTodayMiningExtractions4Pilot(Mockito.any( Credential.class )))
				.thenReturn( this.world.getBackendTodayMiningExtractionsResponse());
		NeoComComponentFactory.getSingleton().replaceBackendService(backendService);
	}

	@When("the MiningLegderActivity activity lifecycle completes for page {string}")
	public void the_MiningLegderActivity_activity_lifecycle_completes_for_page( final String pageNumber ) {
		AcceptanceNeoComLogger.info( "[WHEN] the MiningLegderActivity activity lifecycle completes for page {string}" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the MiningLegderActivity..." );
		final ActivityScenario<MiningLedgerActivity> scenario = ActivityScenario.launch(
				this.world.generateAuthorizationIntent( MiningLedgerActivity.class ) );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setMiningLedgerActivityScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			AcceptanceNeoComLogger.info( "Store the activity..." + activity.getClass().getSimpleName() );
			this.world.setActiveActivity( activity );
			AcceptanceNeoComLogger.info( "Set the configured page..." + pageNumber );
			this.world.setSelectedPage( Integer.parseInt( pageNumber ) );
			AcceptanceNeoComLogger.info( "Verify the current activity state..." );
			final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
			final Stage stage = registry.getLifecycleStageOf( activity );
			Assert.assertEquals( Stage.RESUMED, stage );
		} );
		Ristretto.waitForBackground( () -> {
			Ristretto.waitForCompletion( () -> {
				AcceptanceNeoComLogger.info( "Invalidate all the display and wait termination..." );
				Ristretto.updateDisplay();
			} );
		} );
	}
	@Then("the MiningLegderActivity has {string} pages")
	public void the_MiningLegderActivity_has_pages( final String expectedPageCount ) {
		AcceptanceNeoComLogger.info( "[THEN] the MiningLegderActivity has {string} pages" );
		Assert.assertEquals( Integer.parseInt( expectedPageCount ), Ristretto.activityPageCount() );
	}
}
