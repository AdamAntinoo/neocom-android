package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetStatusOk;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC02AppSplashActivity extends StepFramework {
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

	private int jobCountAtStart = 0;

	public NC02AppSplashActivity( final NeoComWorld world ) {
		super( world );
	}

	@Given("the AppSplashActivity")
	public void the_AppSplashActivity() {
		AcceptanceNeoComLogger.info( "[GIVEN] the AppSplashActivity" );
	}

	@When("the AppSplashActivity activity lifecycle completes for page {string}")
	public void the_AppSplashActivity_activity_lifecycle_completes_for_page( final String pageNumber ) {
		AcceptanceNeoComLogger.info( "[WHEN] the AppSplashActivity activity lifecycle completes for page {string}" );
		AcceptanceNeoComLogger.info( "Replacing the ESI data provider to control data values..." );
		final ESIDataProvider splashEsiDataProvider = Mockito.mock( ESIDataProvider.class );
		final GetStatusOk mockStatus = new GetStatusOk();
		mockStatus.setPlayers( 21765 );
		Mockito.when( splashEsiDataProvider.getUniverseStatus( Mockito.anyString() ) ).thenReturn( mockStatus );
		NeoComComponentFactory.getSingleton().replaceESIDataProvider( splashEsiDataProvider );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the AppSplashActivity..." );
		this.jobCountAtStart = JobScheduler.getJobScheduler().getJobCount();
		AcceptanceNeoComLogger.info( "Registering the number of schedules before activating page... {}",
				this.jobCountAtStart + "" );
		final ActivityScenario<AppSplashActivity> scenario = ActivityScenario.launch( AppSplashActivity.class );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setAppSplashActivityScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
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

	@Then("the AppSplashActivity has {string} pages")
	public void the_AppSplashActivity_has_pages( final String expectedPageCount ) {
		AcceptanceNeoComLogger.info( "[THEN] the AppSplashActivity has {string} pages" );
		Assert.assertEquals( Integer.parseInt( expectedPageCount ), Ristretto.activityPageCount() );
	}

	@Then("the list of registered scheduled tasks increases in {string}")
	public void the_list_of_registered_scheduled_tasks_increases_in( final String jobCountIncrease ) {
		final int jobCount = JobScheduler.getJobScheduler().getJobCount();
		Assert.assertEquals( this.jobCountAtStart + Integer.parseInt( jobCountIncrease ), jobCount );
	}

//	@Given("a clear Scheduler")
//	public void a_clear_Scheduler() {
//		JobScheduler.getJobScheduler().clear();
//		Assert.assertEquals( 0, JobScheduler.getJobScheduler().getJobCount() );
//	}

	//	@Then("the Credential is added to the data sources")
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

//	@When("the AppSplashActivity activity lifecycle completes")
//	public void the_AppSplashActivity_activity_lifecycle_completes() {
//		NeoComLogger.info( "The test has passed by the lifecycle completion." );
//		final Activity activity = this.world.getActiveActivity();
//		final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
//		final Stage stage = registry.getLifecycleStageOf( activity );
//		Assert.assertEquals( Stage.STARTED, stage );
//	}

	//	@Then("the header section has a {string} panel with the next information")
	public void the_header_section_has_a_panel_with_the_next_information( String string, io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@Then("the data section has a Separator panel of {string} color")
	public void the_data_section_has_a_Separator_panel_of_color( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
}
