package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC03PilotDashboard extends AuthorizedStep {
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

	private Intent authorizationIntent;
	private Integer selectedPage=0;

	public NC03PilotDashboard( final NeoComWorld world ) {
		super( world );
	}

	public void initializeScenario() throws IOException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
		this.stopScheduler();
		this.initializeApplicationStorage();
		this.connectAuthorizedMockComponents();
	}

	@Given("the PilotDashboardActivity")
	public void the_PilotDashboardActivity() throws IOException {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the PilotDashboardActivity" );
		this.authorizationIntent = this.authorizationIntent( PilotDashboardActivity.class );
	}

	@When("the PilotDashboardActivity activity lifecycle completes")
	public void the_PilotDashboardActivity_activity_lifecycle_completes() {
		AcceptanceNeoComLogger.info( "[WHEN] the PilotDashboardActivity activity lifecycle completes" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the PilotDashboardActivity..." );
		final ActivityScenario<PilotDashboardActivity> scenario = ActivityScenario.launch( this.authorizationIntent );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setPilotDashboardScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			Assert.assertTrue( activity instanceof PilotDashboardActivity );
			AcceptanceNeoComLogger.info( "Store the activity..." + activity.getClass().getSimpleName() );
			this.world.setActiveActivity( activity );
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

	@Then("the PilotDashboardActivity has {string} pages")
	public void the_PilotDashboardActivity_has_pages( final String expectedPageCount ) {
		AcceptanceNeoComLogger.info( "[THEN] the PilotDashboardActivity has {string} pages" );
//		this.world.getPilotDashboardScenario().onActivity( ( activity ) -> {
//			AcceptanceNeoComLogger.info( "Check the number of pages created..." );
		Assert.assertEquals( Integer.parseInt( expectedPageCount ), Ristretto.activityPageCount() );
//		Assert.assertNotNull( activity );
//		} );
	}
//	@Given("the next Credential")
	public void the_next_Credential(io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

//	@When("the PilotDashboardActivity activity lifecycle completes for page {string}")
	public void the_PilotDashboardActivity_activity_lifecycle_completes_for_page(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

//	@Then("the activity has an action bar with title {string}")
	public void the_activity_has_an_action_bar_with_title(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

//	@Then("the header has a {string} panel with the next information")
	public void the_header_has_a_panel_with_the_next_information(String string, io.cucumber.datatable.DataTable dataTable) {
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
