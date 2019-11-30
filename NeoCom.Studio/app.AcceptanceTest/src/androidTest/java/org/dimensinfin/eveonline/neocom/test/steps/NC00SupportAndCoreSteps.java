package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.core.SettingsActivity;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC00SupportAndCoreSteps {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private NeoComWorld world;
	private List<String> recording = new ArrayList<>();

	public NC00SupportAndCoreSteps( final NeoComWorld world ) {
		this.world = world;
		Ristretto.setWorld( this.world );
	}

	@Given("the SettingsActivity")
	public void the_SettingsActivity() {
		AcceptanceNeoComLogger.info( "the SettingsActivity..." );
	}

	@When("performing the UI thread test")
	public void performing_the_UI_thread_test() {
		AcceptanceNeoComLogger.info( "performing the test..." );
		this.recording.clear();
		this.record( "STEP 01" );
		final ActivityScenario<SettingsActivity> scenario = ActivityScenario.launch( SettingsActivity.class );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setSettingsActivityScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			this.world.setActiveActivity( activity );
			this.record( "STEP 02" );
			Ristretto.waitForCompletion( () -> this.record( "STEP 03" ) );
			this.record( "STEP 04" );
		} );
		this.record( "STEP 05" );
	}

	@When("performing the background synchronization test")
	public void performing_the_background_sunchronization_test() {
		AcceptanceNeoComLogger.info( "performing the test..." );
		this.recording.clear();
		this.record( "STEP 01" );
		final ActivityScenario<SettingsActivity> scenario = ActivityScenario.launch( SettingsActivity.class );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setSettingsActivityScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			this.world.setActiveActivity( activity );
			this.record( "STEP 02" );
			Ristretto.waitForBackground( () -> this.record( "STEP 03" ) );
			this.record( "STEP 04" );
		} );
		this.record( "STEP 05" );
	}

	@Then("we get the right order of results")
	public void we_get_the_right_order_of_results() {
		Assert.assertEquals( 5, this.recording.size() );
		Assert.assertEquals( "STEP 01", this.recording.get( 0 ) );
		Assert.assertEquals( "STEP 02", this.recording.get( 1 ) );
		Assert.assertEquals( "STEP 03", this.recording.get( 2 ) );
		Assert.assertEquals( "STEP 04", this.recording.get( 3 ) );
		Assert.assertEquals( "STEP 05", this.recording.get( 4 ) );
	}
	//	@Given("an initialized application")
	public void anInitializedApplication() {
		final Application application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull(application);
		this.world.setApplication((NeoComApplication) application);
	}
//	@And("the application access privileges")
	public void theApplicationAccessPrivileges() throws InterruptedException {
		final ActivityTestRule<SettingsActivity> activityRuleSettings = new ActivityTestRule<>(SettingsActivity.class,
				false, false);
		Assert.assertNotNull(activityRuleSettings);
		activityRuleSettings.launchActivity(null);
		final Activity activity = activityRuleSettings.getActivity();
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
					new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
					10);
			Thread.sleep( TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
					10);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.INTERNET)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
					new String[]{ Manifest.permission.INTERNET },
					20);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.ACCESS_NETWORK_STATE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
					new String[]{ Manifest.permission.ACCESS_NETWORK_STATE },
					30);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
	}
//	@And("the component factory exists")
	public void theComponentFactoryExists() {
		Assert.assertNotNull(NeoComComponentFactory.getSingleton());
	}

//	@And("the timer service is running")
	public void theTimerServiceIsRunning() {
		Assert.assertTrue(NeoComComponentFactory.getSingleton().getApplication().isSchedulerActive());
	}

	private void record( final String message ) {
		this.recording.add( message );
		NeoComLogger.info( message );
	}
//	@And("the processing spinner is visible")
	public void theProcessingSpinnerIsVisible() {
		final View progress = ((MVCPagerFragment) this.world.getFragment()).getView().findViewById(
				R.id.progressLayout);
		Assert.assertTrue(progress.isShown());
	}

	// - C O M M O N   S T E P S
	@Then("{string} panels on the {string}")
	public void panels_on_the( final String numberOfPanels, final String section ) {
		AcceptanceNeoComLogger.info( "[THEN] {string} panels on the {string}" );
		switch (section) {
			case "HeaderSection":
				Assert.assertEquals( Integer.parseInt( numberOfPanels ), Ristretto.headerContentsCount( this.world.getSelectedPage() ) );
				break;
			case "DataSection":
				Assert.assertEquals( Integer.parseInt( numberOfPanels ), Ristretto.dataContentsCount( this.world.getSelectedPage() ) );
				break;
		}
	}
	@Given("the following Credentials into my repository")
	public void the_following_Credentials_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following Credentials into my repository" );
		for (Map<String, String> row : dataTable) {
			final Credential credential = new CucumberTable2CredentialConverter().convert( row );
			this.world.getCredentialRepository().persist( credential );
		}
	}
}
