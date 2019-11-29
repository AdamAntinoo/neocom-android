package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AuthorizationFlowActivity;
import org.dimensinfin.eveonline.neocom.auth.NeoComOauth2Flow;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

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

//	private AuthorizationFlowActivity spyActivity;
	private Instrumentation.ActivityMonitor monitor;

	public NC05AuthorizationFlowActivity( final NeoComWorld world ) {
		super( world );
	}

	public void initializeScenario() throws IOException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
		this.stopScheduler();
		NeoComComponentFactory.getSingleton().getESIDataProvider();
//		this.initializeApplicationStorage();
//		this.connectAuthorizedMockComponents();
	}

	@Given("the AuthorizationFlowActivity")
	public void the_AuthorizationFlowActivity() throws IOException {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the AuthorizationFlowActivity" );
	}

	@When("the AuthorizationFlowActivity is created with no intent")
	public void the_AuthorizationFlowActivity_is_created_with_no_intent() {
		AcceptanceNeoComLogger.info( "[WHEN] the AuthorizationFlowActivity is created with no intent" );
		final ActivityScenario<AuthorizationFlowActivity> scenario = ActivityScenario.launch( AuthorizationFlowActivity.class );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setAuthorizationFlowScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			AcceptanceNeoComLogger.info( "Verify the current activity state..." );
			final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
			final Stage stage = registry.getLifecycleStageOf( activity );
			Assert.assertEquals( Stage.RESUMED, stage );
		} );
		scenario.close();
	}

	@When("the AuthorizationFlowActivity is created with the FIRE_AUTHORIZATION_FLOW intent")
	public void the_AuthorizationFlowActivity_is_created_with_the_FIRE_AUTHORIZATION_FLOW_intent() {
		AcceptanceNeoComLogger.info( "[WHEN] the AuthorizationFlowActivity is created with the FIRE_AUTHORIZATION_FLOW intent" );
		// Setup the intent monitor.
//		this.monitor = new Instrumentation.ActivityMonitor( (IntentFilter) null, null, true );
//		final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//		instrumentation.addMonitor( this.monitor );

		final Intent startAuthorizationFlowIntent = new Intent( ApplicationProvider.getApplicationContext(), AuthorizationFlowActivity.class );
		// Tag this intent request with a flag to know we are firing it from the new login action part.
		startAuthorizationFlowIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		startAuthorizationFlowIntent.putExtra( EExtras.FIRE_AUTHORIZATION_FLOW.name(), EExtras.FIRE_AUTHORIZATION_FLOW.name() );
		final ActivityScenario<AuthorizationFlowActivity> scenario = ActivityScenario.launch( startAuthorizationFlowIntent );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setAuthorizationFlowScenario( scenario );
		this.monitor = new Instrumentation.ActivityMonitor( (IntentFilter) null, null, true );
		final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
		instrumentation.addMonitor( this.monitor );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			this.world.setActiveActivity( activity );
//			this.spyActivity = Mockito.spy( activity );
		} );
	}

	@Then("one external call is issued to the esi login server")
	public void one_external_call_is_issued_to_the_esi_login_server() {
		AcceptanceNeoComLogger.info( "[WHEN] one external call is issued to the esi login server" );
		this.world.getAuthorizationFlowScenario().onActivity( ( activity ) -> {
			final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
			instrumentation.waitForIdleSync();
			final String esiServer = "Tranquility".toLowerCase();
			final String url = new NeoComOauth2Flow.Builder()
					                   .withConfigurationProvider( NeoComComponentFactory.getSingleton().getConfigurationProvider() )
					                   .build()
					                   .generateLoginUrl( esiServer );
			Intents.intended( Matchers.allOf(
					IntentMatchers.hasAction( Matchers.equalTo( Intent.ACTION_VIEW ) ),
					IntentMatchers.hasData( Matchers.equalTo( Uri.parse( url ) ) ) ) );
			instrumentation.removeMonitor( this.monitor );
		} );

	}

//	@Then("a InformationPanel is visible with the next information")
//	public void a_InformationPanel_is_visible_with_the_next_information( DataTable dataTable ) {
//		this.world.getAuthorizationFlowScenario().onActivity( ( activity ) -> {
//			// This is a custom Activity so there are no data source renders. Exceptions are shown directly on a layout.
//			Espresso.onView( ViewMatchers.withId( R.layout.activity_loginprogress ) )
//					.check( ViewAssertions.matches( ViewMatchers.isDisplayed() ) );
//		} );
//		this.world.getAuthorizationFlowScenario().close();
//	}

//	//	@Then("one external call is issued to the esi login server")
//	public void one_external_call_is_issued_to_the_esi_login_server() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new cucumber.api.PendingException();
//	}

	//	@When("the AuthorizationFlowActivity is created with the FIRE_AUTHORIZATION_FLOW intent")
//	public void the_AuthorizationFlowActivity_is_created_with_the_FIRE_AUTHORIZATION_FLOW_intent() {
//	}
}
