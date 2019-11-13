package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.view.View;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.ActivityResolver;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.ControllerMatcher;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;


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

	@Before
	public void initializeScenario() throws IOException {
		this.initializeApplicationStorage();
//		this.createIntent();
		this.connectAuthorizedMockComponents();
	}

	@When("the pilot activates the Assets information panel")
	public void the_pilot_activates_the_Assets_information_panel() {
		final ActivityScenario<PilotDashboardActivity> scenario = this.world.getPilotDashboardScenario();
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Searching assets panel..." );
			final PilotDashboardFragment resolver = new ActivityResolver<PilotDashboardActivity, PilotDashboardFragment>()
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

	@Given("the AssetDisplayActivity")
	public void the_AssetDisplayActivity() {
		final ActivityScenario<AssetDisplayActivity> scenario = ActivityScenario.launch( this.authorizationIntent( AssetDisplayActivity.class ) );
		this.world.setAssetDisplayScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}

	@When("the AssetDisplayActivity activity lifecycle completes")
	public void the_activity_lifecycle_completes() {
		this.world.getAssetDisplayScenario().onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}

	//	@Then("there is a single page activity with title {string}")
	public void there_is_a_single_page_activity_with_title( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("there is a {string} panel with the next contents")
	public void there_is_a_panel_with_the_next_contents( String string, io.cucumber.datatable.DataTable dataTable ) {
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