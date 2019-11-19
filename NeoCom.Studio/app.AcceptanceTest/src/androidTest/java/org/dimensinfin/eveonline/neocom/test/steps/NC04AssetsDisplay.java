package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.ActivityResolver;
import org.dimensinfin.eveonline.neocom.test.support.ActivityResolverNew;
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

	private Intent authorizationIntent;
	private ActivityResolverNew resolver;

	@Before
	public void initializeScenario() throws IOException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
		this.stopScheduler();
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
			this.resolver = new ActivityResolverNew( this.world.getActiveActivity() );
			AcceptanceNeoComLogger.info( "Verify the current activity state..." );
			final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
			final Stage stage = registry.getLifecycleStageOf( activity );
			Assert.assertEquals( Stage.RESUMED, stage );
			try {
				Thread.sleep( TimeUnit.SECONDS.toMillis( 2 ) );
			} catch (InterruptedException e) { }
		} );
	}

	@Then("there is a action bar with the next fields")
	public void there_is_a_action_bar_with_the_next_fields( final List<Map<String, String>> dataTable ) {
		final String FIELD_IDENTIFIER = "fieldIdentifier";
		final String FIELD_VALUE = "fieldValue";
		AcceptanceNeoComLogger.info( "[THEN] there is a action bar with the next fields" );
		final ActionBar actionBar = this.resolver.accessActionBar();
		final View view = actionBar.getCustomView();
		for (Map<String, String> row : dataTable) {
			switch (row.get( FIELD_IDENTIFIER )) {
				case "toolBarTitleLeft":
					final TextView accountName = Objects.requireNonNull( view.findViewById( R.id.toolBarTitleLeft ) );
					AcceptanceNeoComLogger.info( "[THEN] toolBarTitleLeft: {}", accountName.getText().toString() );
					Assert.assertEquals( this.credential.getAccountName(), accountName.getText().toString() );
					Assert.assertEquals( row.get( FIELD_VALUE ), accountName.getText().toString() );
					break;
				case "toolBarSubTitle":
					final TextView pageName = Objects.requireNonNull( view.findViewById( R.id.toolBarSubTitle ) );
					AcceptanceNeoComLogger.info( "[THEN] toolBarSubTitle: {}", pageName.getText().toString() );
					Assert.assertEquals( PageNamesType.ASSETS_BYLOCATION.getPageTitle(), pageName.getText().toString() );
					Assert.assertEquals( row.get( FIELD_VALUE ), pageName.getText().toString() );
					break;
				case "toolBarTitleRight":
					final TextView pilotIdentifier = Objects.requireNonNull( view.findViewById( R.id.toolBarTitleRight ) );
					AcceptanceNeoComLogger.info( "[THEN] toolBarTitleRight: {}", pilotIdentifier.getText().toString() );
					Assert.assertEquals( "[#" + this.credential.getAccountId() + "]", pilotIdentifier.getText().toString() );
					Assert.assertEquals( row.get( FIELD_VALUE ), pilotIdentifier.getText().toString() );
					break;
			}
		}
	}

	@Then("{string} panels on the {string}")
	public void panels_on_the( final String numberOfPanels, final String section ) {
		AcceptanceNeoComLogger.info( "[THEN] {string} panels on the {string}" );
//		this.wait4Seconds( 2 );
		switch (section) {
			case "HeaderSection":
				Assert.assertEquals( Integer.parseInt( numberOfPanels ), this.resolver.headerContentsCount() );
				break;
			case "DataSection":
				Assert.assertEquals( Integer.parseInt( numberOfPanels ), this.resolver.dataContentsCount() );
				break;
		}
	}

	@Then("a type {string} panel on the {string} with the next data")
	public void a_type_panel_on_the_with_the_next_data( final String panelType, final String section,
	                                                    final List<Map<String, String>> dataTable ) {
		AcceptanceNeoComLogger.info( "[THEN] a type {string} panel on the {string} with the next data" );
		this.wait4Seconds( 3 );
		switch (section) {
			case "HeaderSection":
				break;
			case "DataSection":
				final List<View> panels = this.resolver.matchDataPanel( panelType );
				Assert.assertTrue( panels.size() > 0 );
				Assert.assertTrue( this.validateRegionPanel( dataTable.get( 0 ), panels.get( 0 ) ) );
				break;
		}
	}

	@Then("a type {string} panel on the {string} with expansion enabled")
	public void a_type_panel_on_the_with_expansion_enabled( final String panelType, final String section ) {
		AcceptanceNeoComLogger.info( "[THEN] a type {string} panel on the {string} with expansion enabled" );
		switch (section) {
			case "HeaderSection":
				break;
			case "DataSection":
//				this.world.getAssetDisplayScenario().onActivity( activity -> {
				final List<IAndroidController> panels = this.resolver.matchDataController( panelType );
				Assert.assertTrue( panels.size() > 0 );
				// This check are not enough to validate the arrow is visible.
				Assert.assertTrue( panels.get( 0 ) instanceof RegionController );
				Assert.assertEquals( View.VISIBLE,
						this.resolver.matchDataPanel( panelType ).get( 0 ).findViewById( R.id.actuatorsBlock ).getVisibility() );
				Assert.assertEquals( View.VISIBLE,
						this.resolver.matchDataPanel( panelType ).get( 0 ).findViewById( R.id.rightArrow ).getVisibility() );
//				} );
				break;
		}
		this.world.getAssetDisplayScenario().close();
	}

	@Then("when the region is expanded then there are {string} panels on the {string}")
	public void when_the_region_is_expanded_then_there_are_panels_on_the( final String numberOfPanels, final String section ) {
		AcceptanceNeoComLogger.info( "[THEN] when the region is expanded then there are {string} panels on the {string}" );
//		final ActivityScenario<AssetDisplayActivity> scenario = ActivityScenario.launch( this.authorizationIntent );
//		Assert.assertNotNull( scenario );
////		AcceptanceNeoComLogger.info( "Store the scenario..." );
////		this.world.setAssetDisplayScenario( scenario );
//		scenario.onActivity( ( activity ) -> {
		// Expand one of the Regions.
		final List<IAndroidController> matchRegions = this.resolver.matchDataController( "Region" );
		final IAndroidController region = matchRegions.get( 0 );
		if (region instanceof View.OnClickListener) {
			AcceptanceNeoComLogger.info( "[THEN] click on the Region" );
			((View.OnClickListener) region).onClick( region.getViewCache() );
			this.world.getActiveActivity().runOnUiThread( () -> {
				switch (section) {
					case "HeaderSection":
						Assert.assertEquals( Integer.parseInt( numberOfPanels ), this.resolver.headerContentsCount() );
						break;
					case "DataSection":
						AcceptanceNeoComLogger.info( "[THEN] new panel count test" );
						Assert.assertEquals( Integer.parseInt( numberOfPanels ), this.resolver.dataContentsCount() );
						break;
				}
			} );
		}
//		} );
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

	private boolean validateRegionPanel( final Map<String, String> expectedData,
	                                     final View obtainedView ) {
		final String REGION_NAME = "regionName";
		final String STATION_COUNT = "stationCount";
		Assert.assertEquals( expectedData.get( REGION_NAME ), ((TextView) obtainedView.findViewById( R.id.regionName )).getText() );
		Assert.assertEquals( expectedData.get( STATION_COUNT ), ((TextView) obtainedView.findViewById( R.id.stationCount )).getText() );
		return true;
	}
}
