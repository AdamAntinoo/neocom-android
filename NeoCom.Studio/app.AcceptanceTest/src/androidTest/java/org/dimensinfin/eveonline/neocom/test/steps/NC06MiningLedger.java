package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtractionEntity;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2MiningExtractionEntityConverter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.Given;
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
//		NeoComComponentFactory.getSingleton().getESIDataProvider(); // Probably to run the initialization code for family data. N/A now.
	}

	@Given("the MiningLegderActivity")
	public void the_MiningLegderActivity()  {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the MiningLegderActivity" );
//		this.authorizationIntent = this.authorizationIntent( PilotDashboardActivity.class );
	}
	@Given("the following MiningExtractionEntity into my repository")
	public void the_following_MiningExtractionEntity_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following MiningExtractionEntity into my repository" );
		for (Map<String, String> row : dataTable) {
			final MiningExtractionEntity miningExtraction = new CucumberTable2MiningExtractionEntityConverter().convert( row );
			this.world.getMiningRepository().persist( miningExtraction );
		}
	}
//	@When("the PilotDashboardActivity activity lifecycle completes for page {string}")
	public void the_PilotDashboardActivity_activity_lifecycle_completes_for_page( final String pageNumber ) {
		AcceptanceNeoComLogger.info( "[WHEN] the PilotDashboardActivity activity lifecycle completes for page {string}" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the PilotDashboardActivity..." );
		final ActivityScenario<PilotDashboardActivity> scenario = ActivityScenario.launch(
				this.world.generateAuthorizationIntent(PilotDashboardActivity.class) );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setPilotDashboardScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
//			Assert.assertTrue( activity instanceof PilotDashboardActivity );
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

}
