package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.app.activity.AuthorizationFlowActivity;
import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
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
	private Integer selectedPage = 0;

	public NC05AuthorizationFlowActivity( final NeoComWorld world ) {
		super( world );
	}

//	public void initializeScenario() throws IOException, SQLException {
//		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
////		this.stopScheduler();
////		this.initializeApplicationStorage();
////		this.initializeApplicationEsi();
////		this.deleteDatabaseRecords();
//		NeoComComponentFactory.getSingleton().getESIDataProvider();
//	}

	@Given("the AuthorizationFlowActivity")
	public void the_AuthorizationFlowActivity() throws IOException, SQLException {
//		this.initializeScenario();
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
		Ristretto.waitForBackground( () -> {
			Ristretto.waitForCompletion( () -> {
				AcceptanceNeoComLogger.info( "Invalidate all the display and wait termination..." );
				Ristretto.updateDisplay();
			} );
		} );
		try {
			Thread.sleep( TimeUnit.SECONDS.toMillis( 3 ) );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
//		this.monitor = new Instrumentation.ActivityMonitor( (IntentFilter) null, null, true );
//		final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//		instrumentation.addMonitor( this.monitor );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			this.world.setActiveActivity( activity );
			// The activity should call the external service and on return it should complete the authentication
			this.wait4Seconds( 18 );
			Ristretto.waitForBackground( () -> {
				Ristretto.waitForCompletion( () -> {
					AcceptanceNeoComLogger.info( "Invalidate all the display and wait termination..." );
					Ristretto.updateDisplay();
				} );
			} );
		} );
	}

	@Then("one external call is issued to the esi login server")
	public void one_external_call_is_issued_to_the_esi_login_server() {
		AcceptanceNeoComLogger.info( "[WHEN] one external call is issued to the esi login server" );
//		this.world.getAuthorizationFlowScenario().onActivity( ( activity ) -> {
//			final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//			instrumentation.waitForIdleSync();
//			final String esiServer = "Tranquility".toLowerCase();
//			final String url = new NeoComOauth2Flow.Builder()
//					                   .withConfigurationProvider( NeoComComponentFactory.getSingleton().getConfigurationProvider() )
//					                   .build()
//					                   .generateLoginUrl( esiServer );
//			Intents.intended( Matchers.allOf(
//					IntentMatchers.hasAction( Matchers.equalTo( Intent.ACTION_VIEW ) ),
//					IntentMatchers.hasData( Matchers.equalTo( Uri.parse( url ) ) ) ) );
//			instrumentation.removeMonitor( this.monitor );
//		} );
	}

	//	@Then("a InformationPanel is visible with the next information")
	public void a_InformationPanel_is_visible_with_the_next_information( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	@Then("when the authorization completes then a new Credential is created with the next data")
	public void when_the_authorization_completes_then_a_new_Credential_is_created_with_the_next_data( final List<Map<String, String>> dataTable ) {
		final String UNIQUE_ID = "UniqueId";
		final String ACCOUNT_ID = "AccountId";
		final String ACCOUNT_NAME = "AccountName";
		final String RACE_NAME = "RaceName";
//		this.world.getAuthorizationFlowScenario().onActivity( ( activity ) -> {
			// The activity should call the external service and on return it should complete the authentication
			this.wait4Seconds( 5 );
			Ristretto.waitForBackground( () -> {
				Ristretto.waitForCompletion( () -> {
					AcceptanceNeoComLogger.info( "Invalidate all the display and wait termination..." );
					Ristretto.updateDisplay();
				} );
			} );
			// Check the Credential is created.
			Assert.assertEquals( 1, this.world.getCredentialRepository().countRecords().intValue() );
			try {
				final Credential credential = this.world.getCredentialRepository().findCredentialById(
						dataTable.get( 0 ).get( UNIQUE_ID )
				);
				Assert.assertNotNull( credential );
				Assert.assertEquals( dataTable.get( 0 ).get( ACCOUNT_ID ), credential.getAccountId().toString() );
				Assert.assertEquals( dataTable.get( 0 ).get( ACCOUNT_NAME ), credential.getAccountName() );
				Assert.assertEquals( dataTable.get( 0 ).get( RACE_NAME ), credential.getRaceName() );
			} catch (final SQLException sqle) {
				sqle.printStackTrace();
			}
//		} );
	}
}
