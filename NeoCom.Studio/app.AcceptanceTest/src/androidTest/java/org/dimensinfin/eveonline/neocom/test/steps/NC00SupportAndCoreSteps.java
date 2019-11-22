package org.dimensinfin.eveonline.neocom.test.steps;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.app.activity.core.SettingsActivity;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.neocom.NeoComLogger;


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

	private void record( final String message ) {
		this.recording.add( message );
		NeoComLogger.info( message );
	}
}
