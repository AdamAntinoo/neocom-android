package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.activity.AuthorizationFlowActivity;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class NC05AuthorizationFlowActivity extends StepFramework {
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

	public NC05AuthorizationFlowActivity( final NeoComWorld world ) {
		super( world );
	}

	@Given("the AuthorizationFlowActivity")
	public void the_AuthorizationFlowActivity() {
		AcceptanceNeoComLogger.info( "Launch the scenario with no intents..." );
//		final ActivityScenario<AuthorizationFlowActivity> scenario = ActivityScenario.launch( AuthorizationFlowActivity.class );
//		this.world.setAuthorizationFlowScenario( scenario );
//		Assert.assertNotNull( scenario );
//		scenario.close();
	}

	@When("the AuthorizationFlowActivity is created with no intent")
	public void the_AuthorizationFlowActivity_is_created_with_no_intent() {
		AcceptanceNeoComLogger.info( "Launch the activity with no intents..." );
		final ActivityScenario<AuthorizationFlowActivity> scenario = ActivityScenario.launch( AuthorizationFlowActivity.class );
		this.world.setAuthorizationFlowScenario( scenario );
		Assert.assertNotNull( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the launch completes successfully..." );
			Assert.assertNotNull( activity );
		} );
	}

	@Then("a InformationPanel is visible with the next information")
	public void a_InformationPanel_is_visible_with_the_next_information( DataTable dataTable ) {
		this.world.getAuthorizationFlowScenario().onActivity( ( activity ) -> {
			// This is a custom Activity so there are no data source renders. Exceptions are shown directly on a layout.
			Espresso.onView( ViewMatchers.withId( R.layout.activity_loginprogress ) )
			        .check( ViewAssertions.matches(ViewMatchers.isDisplayed()));
		} );
		this.world.getAuthorizationFlowScenario().close();
	}

	//	@Then("one external call is issued to the esi login server")
	public void one_external_call_is_issued_to_the_esi_login_server() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@When("the AuthorizationFlowActivity is created with the FIRE_AUTHORIZATION_FLOW intent")
	public void the_AuthorizationFlowActivity_is_created_with_the_FIRE_AUTHORIZATION_FLOW_intent() {
		final Intent startAuthorizationFlowIntent = new Intent( ApplicationProvider.getApplicationContext(), AuthorizationFlowActivity.class );
		// Tag this intent request with a flag to know we are firing it from the new login action part.
		startAuthorizationFlowIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		startAuthorizationFlowIntent.putExtra( EExtras.FIRE_AUTHORIZATION_FLOW.name(), EExtras.FIRE_AUTHORIZATION_FLOW.name() );
		final ActivityScenario<AuthorizationFlowActivity> scenario = ActivityScenario.launch( startAuthorizationFlowIntent );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}
}