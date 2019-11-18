package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportCredentialRepository;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC02AppSplashActivity extends StepFramework {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	@Rule
	public GrantPermissionRule mPermissionReadExternalStorageRule = GrantPermissionRule.grant(
			Manifest.permission.READ_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule mPermissionWriteExternalStorageRule = GrantPermissionRule.grant(
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant( android.Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(
			android.Manifest.permission.ACCESS_NETWORK_STATE );

	// -  C O M P O N E N T S
	private SupportCredentialRepository credentialRepository;

	public NC02AppSplashActivity( final NeoComWorld world ) {
		super( world );
		AcceptanceNeoComLogger.info( "Storing world..." );
	}

	@Given("the AppSplashActivity")
	public void the_AppSplashActivity() {
		final Application application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull( application );
		AcceptanceNeoComLogger.info( "Storing application..." );
		this.world.setApplication( (NeoComApplication) application );
		// Create the activity scenario.
		final ActivityScenario<AppSplashActivity> activityScenario = ActivityScenario.launch( AppSplashActivity.class );
		activityScenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Validating activity existence activity..." );
			Assert.assertNotNull( activity );
//			Assert.assertEquals( PageNamesType.SPLASH.name().equalsIgnoreCase( activity.getVariant() ) );
			AcceptanceNeoComLogger.info( "Storing activity..." );
			this.world.setActiveActivity( activity );
		} );
		AcceptanceNeoComLogger.info( "Storing activity scenario..." );
		this.world.setActivityScenario( activityScenario );
	}

	@Given("a clear Scheduler")
	public void a_clear_Scheduler() {
//		final JobScheduler scheduler = new JobScheduler.Builder()
//				                               .withCronScheduleGenerator( new HourlyCronScheduleGenerator() )
//				                               .build();
//		Assert.assertNotNull( scheduler );
		JobScheduler.getJobScheduler().clear();
		Assert.assertEquals( 0, JobScheduler.getJobScheduler().getJobCount() );
	}

	@Then("the Credential is added to the data sources")
	public void the_Credential_is_added_to_the_data_sources() {
		final ActivityScenario<AppSplashActivity> scenario = this.world.getAppSplashActivityScenario();
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Check there are credentials on the data section..." );
//		dataModels=	new ActivityResolver<AppSplashActivity, AppSplashFragment>(activity).getDataContainer();
		} );
	}

//	@Then("the Credential adds the next jobs to the Scheduler")
	public void the_Credential_adds_the_next_jobs_to_the_Scheduler( final List<Map<String, String>> dataTable ) {
//		final JobScheduler scheduler = new JobScheduler.Builder()
//				                               .withCronScheduleGenerator( new HourlyCronScheduleGenerator() )
//				                               .build();
		Assert.assertEquals( 1, JobScheduler.getJobScheduler().getJobCount() );
	}

	@Given("the next Credentials registered")
	public void the_next_Credentials_registered( final List<Map<String, String>> dataTable ) throws SQLException {
		final Map<String, String> row = dataTable.get( 0 );
		Objects.requireNonNull( row );
		final Credential credential = new CucumberTable2CredentialConverter().convert( row );
		Objects.requireNonNull( credential );
		// Clear database records.
		this.credentialRepository = new SupportCredentialRepository.Builder()
				                            .withCredentialDao( NeoComComponentFactory.getSingleton().getNeoComDBHelper().getCredentialDao() )
				                            .build();
		this.credentialRepository.deleteAll();
		this.credentialRepository.persist( credential );
	}

	@When("the AppSplashActivity activity lifecycle completes")
	public void the_AppSplashActivity_activity_lifecycle_completes() {
		NeoComLogger.info( "The test has passed by the lifecycle completion." );
		final Activity activity = this.world.getActiveActivity();
		final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
		final Stage stage = registry.getLifecycleStageOf( activity );
		Assert.assertEquals( Stage.STARTED, stage );
	}

		@Then("the AppSplashActivity has {string} pages")
	public void the_AppSplashActivity_has_pages( String pageCount ) {
//		new ActivityResolver<AppSplashActivity, AppSplashFragment>( this.world.getActiveActivity() )
	}

	//	@Then("the activity has an action bar of type {string}")
	public void the_activity_has_an_action_bar_of_type( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the SplashActionBar action bar contains the next fields")
	public void the_SplashActionBar_action_bar_contains_the_next_fields( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@Then("the activity has {string} on the header section")
	public void the_activity_has_on_the_header_section( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("AppInfoPanel has the next data")
	public void appinfopanel_has_the_next_data( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@Then("the activity has {string} Credential on the data section")
	public void the_activity_has_Credential_on_the_data_section( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the Credential panel has the next rendered data")
	public void the_Credential_panel_has_the_next_rendered_data( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@Then("the activity has a {string} on the {string} section")
	public void the_activity_has_a_on_the_section( String string, String string2 ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the NewLoginPanel panel has the next label")
	public void the_NewLoginPanel_panel_has_the_next_label( io.cucumber.datatable.DataTable dataTable ) {
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
