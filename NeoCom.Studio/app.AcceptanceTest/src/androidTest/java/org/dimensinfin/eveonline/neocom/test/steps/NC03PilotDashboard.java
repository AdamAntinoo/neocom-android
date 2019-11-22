package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.ActivityResolver;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;


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

	public NC03PilotDashboard( final NeoComWorld world ) {
		super( world );
	}

//	@Before
	public void initializeScenario() throws IOException {
		this.initializeApplicationStorage();
//		this.createIntent();
		this.connectAuthorizedMockComponents();
	}

	@Given("the PilotDashboardActivity")
	public void the_PilotDashboardActivity() throws IOException {
		final ActivityScenario<PilotDashboardActivity> scenario = ActivityScenario
				                                                          .launch( this.authorizationIntent( PilotDashboardActivity.class ) );
		this.world.setPilotDashboardScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}

	@When("the PilotDashboardActivity activity lifecycle completes")
	public void the_PilotDashboardActivity_activity_lifecycle_completes() {
		this.world.getPilotDashboardScenario().onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}

	@Then("the PilotDashboardActivity has {string} pages")
	public void the_PilotDashboardActivity_has_pages( String expectedPageCount ) {
		this.world.getPilotDashboardScenario().onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Check the number of pages created..." );
			Assert.assertEquals( Integer.parseInt( expectedPageCount ),
					new ActivityResolver<PilotDashboardActivity, PilotDashboardFragment>(activity).pagesCreated( activity ) );
			Assert.assertNotNull( activity );
		} );
	}

	//	@Then("the activity has an action bar with title {string}")
	public void the_activity_has_an_action_bar_with_title( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the header has a {string} panel with the next information")
	public void the_header_has_a_panel_with_the_next_information( String string, io.cucumber.datatable.DataTable dataTable ) {
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
