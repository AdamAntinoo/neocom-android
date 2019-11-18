package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.ActivityResolver;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.ControllerMatcher;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportAssetRepository;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2NeoAssetConverter;


import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC04AssetsDisplay extends AuthorizedStep {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	public NC04AssetsDisplay( final NeoComWorld world ) {
		super( world );
	}

	@Rule
	public GrantPermissionRule permissionInternet = GrantPermissionRule.grant( Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule permissionNetworkState = GrantPermissionRule.grant( Manifest.permission.ACCESS_NETWORK_STATE );
	@Rule
	public GrantPermissionRule permissionWriteStorage = GrantPermissionRule.grant( Manifest.permission.WRITE_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule permissionReadStorage = GrantPermissionRule.grant( Manifest.permission.READ_EXTERNAL_STORAGE );
//	@Rule
//	public GrantPermissionRule permissionReadStorage2 = GrantPermissionRule.grant(READ_EXTERNAL );

	private Intent authorizationIntent;

	public void grantPermissions() {
		AcceptanceNeoComLogger.info( "[BEFORE] grantPermissions" );
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
				.executeShellCommand("pm grant ${getTargetContext().packageName} android.permission.WRITE_EXTERNAL_STORAGE");
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
				.executeShellCommand("pm grant ${getTargetContext().packageName} android.permission.READ_EXTERNAL_STORAGE");
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
				.executeShellCommand("pm grant ${getTargetContext().packageName} android.permission.INTERNET");
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
				.executeShellCommand("pm grant ${getTargetContext().packageName} android.permission.ACCESS_NETWORK_STATE");
	}
	@Before
	public void initializeScenario() throws IOException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
//		this.grantPermissions();
		this.initializeApplicationStorage();
		this.connectAuthorizedMockComponents();
	}

	@Given("the AssetDisplayActivity")
	public void the_AssetDisplayActivity() {
		AcceptanceNeoComLogger.info( "[GIVEN] the AssetDisplayActivity" );
		this.authorizationIntent = this.authorizationIntent( AssetDisplayActivity.class );
	}

	@Given("the next list of assets persisted for character {string}")
	public void the_next_list_of_assets_persisted_for_character( final String characterId,
	                                                             final List<Map<String, String>> dataTable ) {
		AcceptanceNeoComLogger.info( "[GIVEN] the next list of assets persisted for character {string}" );
		List<NeoAsset> assetList = new CucumberTable2NeoAssetConverter().convert( dataTable );
		final SupportAssetRepository assetRepository = Mockito.mock( SupportAssetRepository.class );
		Mockito.when( assetRepository.findAllByOwnerId( Mockito.anyInt() ) ).thenReturn( assetList );
		NeoComComponentFactory.getSingleton().replaceAssetRepository( assetRepository );
	}

	@When("the AssetDisplayActivity activity lifecycle completes")
	public void the_activity_lifecycle_completes() {
		AcceptanceNeoComLogger.info( "[WHEN] the AssetDisplayActivity activity lifecycle completes" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the AssetDisplayActivity..." );
		final ActivityScenario<AssetDisplayActivity> scenario = ActivityScenario.launch( this.authorizationIntent );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setAssetDisplayScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			Assert.assertTrue( activity instanceof AssetDisplayActivity );
			AcceptanceNeoComLogger.info( "Store the activity..." + activity.getClass().getSimpleName() );
			this.world.setActiveActivity( activity );
			AcceptanceNeoComLogger.info( "Verify the current activity state..." );
			final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
			final Stage stage = registry.getLifecycleStageOf( activity );
			Assert.assertEquals( Stage.RESUMED, stage );
		} );
	}

	@Then("there is a action bar with the next fields")
	public void there_is_a_action_bar_with_the_next_fields( final List<Map<String, String>> dataTable ) {

	}

	@When("the pilot activates the Assets information panel")
	public void the_pilot_activates_the_Assets_information_panel() {
		final ActivityScenario<PilotDashboardActivity> scenario = this.world.getPilotDashboardScenario();
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Searching assets panel..." );
			final PilotDashboardFragment resolver = new ActivityResolver<PilotDashboardActivity, PilotDashboardFragment>( activity )
					                                        .fragmentResolver( activity, 0 );
			final ListView dataSectionContainer = resolver.accessDataSectionContainer();

			final List<PilotSectionController> controllers = new ControllerMatcher<PilotSectionController>( dataSectionContainer )
					                                                 .withType( PilotSectionController.class );
			PilotSectionController target = null;
			for (PilotSectionController controller : controllers) {
				if (controller.getModel().getTitle().equalsIgnoreCase( "ASSETS" )) {
					target = controller;
					NeoComLogger.info( "Panel found." );
				}
			}
			Assert.assertNotNull( target );
			Assert.assertNotNull( target.getModel().getClickTarget() );
			NeoComLogger.info( "Panel is clickable." );
			// TODO - Write the code to get the view from the data source adapter.
			final View panelView = target.getViewCache();
			target.onClick( panelView );
		} );
	}

	@Then("the application moves to the AssetDisplayActivity.")
	public void the_application_moves_to_the_AssetDisplayActivity() {
		// TODO - check how to validate I have moved to another activity
		NeoComLogger.info( "This step has to be implemented." );
	}


	//	@Then("{string} panels on the {string}")
	public void panels_on_the( String string, String string2 ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("{string} {string} type panels on the {string}")
	public void type_panels_on_the( String string, String string2, String string3 ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("a type {string} panel with the next data")
	public void a_type_panel_with_the_next_data( String string, io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}
}
