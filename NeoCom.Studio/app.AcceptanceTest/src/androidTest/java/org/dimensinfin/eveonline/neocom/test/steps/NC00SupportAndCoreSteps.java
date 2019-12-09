package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.core.activity.SettingsActivity;
import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;
import org.dimensinfin.eveonline.neocom.ui.PreferenceKeys;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC00SupportAndCoreSteps {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private NeoComWorld world;
	private List<String> recording = new ArrayList<>();
	// -  C O M P O N E N T S
	private Context applicationContext = NeoComComponentFactory.getSingleton().getApplication().getApplicationContext();
	private IFileSystem fileSystemAdapter = NeoComComponentFactory.getSingleton().getFileSystemAdapter();

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
		Assert.assertNotNull( application );
		this.world.setApplication( (NeoComApplication) application );
	}

	//	@And("the application access privileges")
	public void theApplicationAccessPrivileges() throws InterruptedException {
		final ActivityTestRule<SettingsActivity> activityRuleSettings = new ActivityTestRule<>( SettingsActivity.class,
				false, false );
		Assert.assertNotNull( activityRuleSettings );
		activityRuleSettings.launchActivity( null );
		final Activity activity = activityRuleSettings.getActivity();
		if (ContextCompat.checkSelfPermission( this.world.getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE )
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions( activity,
					new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
					10 );
			Thread.sleep( TimeUnit.SECONDS.toMillis( 2 ) );
		}
		if (ContextCompat.checkSelfPermission( this.world.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE )
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions( activity,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
					10 );
			Thread.sleep( TimeUnit.SECONDS.toMillis( 2 ) );
		}
		if (ContextCompat.checkSelfPermission( this.world.getApplication(), Manifest.permission.INTERNET )
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions( activity,
					new String[]{ Manifest.permission.INTERNET },
					20 );
			Thread.sleep( TimeUnit.SECONDS.toMillis( 2 ) );
		}
		if (ContextCompat.checkSelfPermission( this.world.getApplication(), Manifest.permission.ACCESS_NETWORK_STATE )
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions( activity,
					new String[]{ Manifest.permission.ACCESS_NETWORK_STATE },
					30 );
			Thread.sleep( TimeUnit.SECONDS.toMillis( 2 ) );
		}
	}

	//	@And("the component factory exists")
	public void theComponentFactoryExists() {
		Assert.assertNotNull( NeoComComponentFactory.getSingleton() );
	}

	//	@And("the timer service is running")
	public void theTimerServiceIsRunning() {
		Assert.assertTrue( NeoComComponentFactory.getSingleton().getApplication().isSchedulerActive() );
	}

	private void record( final String message ) {
		this.recording.add( message );
		NeoComLogger.info( message );
	}

	//	@And("the processing spinner is visible")
//	public void theProcessingSpinnerIsVisible() {
//		final View progress = ((MVCPagerFragment) this.world.getFragment()).getView().findViewById(
//				R.id.progressLayout );
//		Assert.assertTrue( progress.isShown() );
//	}

	public Intent authorizationIntent( final Class destinationActivity ) {
		final Intent authorizationIntent = new Intent( ApplicationProvider.getApplicationContext(), destinationActivity );
		Bundle bundle = new Bundle();
		bundle.putInt( EExtras.CAPSULEER_IDENTIFIER.name(), this.world.getCapsuleerIdentifier() );
		authorizationIntent.putExtras( bundle );
		return authorizationIntent;
	}

	// - C O M M O N   S T E P S
	@Given("the application storage is initialized")
	public void the_application_storage_is_initialized() {
		this.initializeApplicationStorage();
		this.initializeApplicationEsi();
	}

	@Given("the scheduler is disabled")
	public void the_scheduler_is_disabled() {
		AcceptanceNeoComLogger.info( "[GIVEN] the scheduler is disabled" );
		this.stopScheduler();
	}

	@Given("the NeoCom database is initialized empty")
	public void the_NeoCom_database_is_initialized_empty() {
		this.deleteDatabaseRecords();
	}

	@Given("the following Credentials into my repository")
	public void the_following_Credentials_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following Credentials into my repository" );
		for (Map<String, String> row : dataTable) {
			final Credential credential = new CucumberTable2CredentialConverter().convert( row );
			this.world.getCredentialRepository().persist( credential );
		}
	}

	@Then("there is a action bar of type {string} with the next fields")
	public void there_is_a_action_bar_of_type_with_the_next_fields( final String actionBarType,
	                                                                final List<Map<String, String>> dataTable ) {
		AcceptanceNeoComLogger.info( "[THEN] there is a action bar of type {string} with the next fields" );
		final ActionBar actionBar = Objects.requireNonNull( this.world.getActiveActivity().getActionBar() );
		final View view = actionBar.getCustomView();
		Assert.assertNotNull( view );
		switch (actionBarType) {
			case "AppVersion":
				Assert.assertTrue( this.validateNeoComActionBar( dataTable.get( 0 ), view ) );
				break;
			case "SplashActionBar":
				Assert.assertTrue( this.validateSplashActionBar( dataTable.get( 0 ), view ) );
				break;
			case "DashboardActionBar":
				Assert.assertTrue( this.validateDashboardActionBar( dataTable.get( 0 ), view ) );
				break;
		}
	}

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

	@Given("the next {string} identifier for the CAPSULEER_IDENTIFIER")
	public void the_next_identifier_for_the_CAPSULEER_IDENTIFIER( String capsuleerIdentifier ) {
		this.world.setCapsuleerIdentifier( Integer.parseInt( capsuleerIdentifier ) );
	}

	// - M E T H O D S   F O R   I N I T I A L I Z A T I O N
	private void initializeApplicationStorage() {
		AcceptanceNeoComLogger.enter();
		final boolean createAppResult = NeoComApplication.createAppDir();
		Assert.assertTrue( createAppResult );
		final boolean createPublicAppResult = NeoComApplication.createPublicAppDir();
//		Assert.assertTrue( createPublicAppResult );
		final boolean createCacheResult = NeoComApplication.createCacheDirectories();
		Assert.assertTrue( createCacheResult );
		if (!NeoComApplication.sdeDatabaseExists()) {
			this.fileSystemAdapter.copyFromAssets(
					this.applicationContext.getResources().getString( R.string.sdedatabasefilename ), null );
		}
		AcceptanceNeoComLogger.exit();
	}
	private void initializeApplicationEsi() {
		NeoComComponentFactory.getSingleton().getESIDataProvider();
	}

	private void stopScheduler() {
		final SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(
				InstrumentationRegistry.getInstrumentation().getContext()
		).edit();
		preferencesEditor.putBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), false );
		preferencesEditor.commit();
	}

	private void deleteDatabaseRecords() {
		this.world.getCredentialRepository().deleteAll();
	}

	private boolean validateNeoComActionBar( final Map<String, String> expectedData,
	                                         final View obtainedView ) {
		final String APP_NAME = "AppName";
		final String APP_VERSION = "AppVersion";

		final TextView appName = Objects.requireNonNull( obtainedView.findViewById( R.id.appName ) );
		AcceptanceNeoComLogger.info( "[THEN] appName: {}", appName.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_NAME ), appName.getText().toString() );
		final TextView appVersion = Objects.requireNonNull( obtainedView.findViewById( R.id.appVersion ) );
		AcceptanceNeoComLogger.info( "[THEN] appVersion: {}", appVersion.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_VERSION ), appVersion.getText().toString() );
		return true;
	}

	private boolean validateSplashActionBar( final Map<String, String> expectedData,
	                                     final View obtainedView ) {
		final String APP_NAME = "AppName";
		final String APP_VERSION = "AppVersion";
		final String SERVER_NAME = "ServerName";
		final String SERVER_STATE = "ServerState";
		final String PILOT_COUNT = "PilotCount";

		final TextView appName = Objects.requireNonNull( obtainedView.findViewById( R.id.appName ) );
		AcceptanceNeoComLogger.info( "[THEN] appName: {}", appName.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_NAME ), appName.getText().toString() );
		final TextView appVersion = Objects.requireNonNull( obtainedView.findViewById( R.id.appVersion ) );
		AcceptanceNeoComLogger.info( "[THEN] appVersion: {}", appVersion.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_VERSION ), appVersion.getText().toString() );
		final TextView serverName = Objects.requireNonNull( obtainedView.findViewById( R.id.serverName ) );
		AcceptanceNeoComLogger.info( "[THEN] serverName: {}", serverName.getText().toString() );
		Assert.assertEquals( expectedData.get( SERVER_NAME ), serverName.getText().toString() );
		final TextView serverStatus = Objects.requireNonNull( obtainedView.findViewById( R.id.serverStatus ) );
		AcceptanceNeoComLogger.info( "[THEN] serverStatus: {}", serverStatus.getText().toString() );
		Assert.assertEquals( expectedData.get( SERVER_STATE ), serverStatus.getText().toString() );
		final TextView capsuleersNumber = Objects.requireNonNull( obtainedView.findViewById( R.id.capsuleersNumber ) );
		AcceptanceNeoComLogger.info( "[THEN] capsuleersNumber: {}", capsuleersNumber.getText().toString() );
		Assert.assertEquals( expectedData.get( PILOT_COUNT ), capsuleersNumber.getText().toString() );
		return true;
	}

	private boolean validateDashboardActionBar( final Map<String, String> expectedData,
	                                        final View obtainedView ) {
		final String APP_NAME = "AppName";
		final String APP_VERSION = "AppVersion";
		final String PILOT_NAME = "PilotName";

		final TextView appName = Objects.requireNonNull( obtainedView.findViewById( R.id.toolBarTitleLeft ) );
		AcceptanceNeoComLogger.info( "[THEN] toolBarTitleLeft: {}", appName.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_NAME ), appName.getText().toString() );
		final TextView appVersion = Objects.requireNonNull( obtainedView.findViewById( R.id.toolBarTitleRight ) );
		AcceptanceNeoComLogger.info( "[THEN] toolBarTitleRight: {}", appVersion.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_VERSION ), appVersion.getText().toString() );
		final TextView pilotName = Objects.requireNonNull( obtainedView.findViewById( R.id.toolBarSubTitle ) );
		AcceptanceNeoComLogger.info( "[THEN] toolBarSubTitle: {}", pilotName.getText().toString() );
		Assert.assertEquals( expectedData.get( PILOT_NAME ), pilotName.getText().toString() );
		return true;
	}

}
