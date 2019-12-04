package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.controller.InitializationExceptionController;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC01AppInitializationActivity extends StepFramework {
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

	// - COMPONENTS
	public NC01AppInitializationActivity( final NeoComWorld world ) {
		super( world );
	}

	@Given("the AppInitializationActivity")
	public void the_AppInitializationActivity() {
		AcceptanceNeoComLogger.info( "[GIVEN] the AppInitializationActivity" );
		Assert.assertNotNull( NeoComComponentFactory.getSingleton() );
		Assert.assertNotNull( NeoComComponentFactory.getSingleton().getApplication() );
//		logger.info("-- [the_AppInitializationActiviy_activity]> Storing application");
//		this.world.setApplication((NeoComApplication) application);
		// Create the activity scenario.
		final ActivityScenario<AppInitializationActivity> activityScenario = ActivityScenario.launch(
				AppInitializationActivity.class );
		activityScenario.onActivity( ( activity ) -> {
			logger.info( "-- [the_AppInitializationActiviy_activity]> Storing activity" );
//			this.world.setActiveActivity(activity);
		} );
//		logger.info("-- [the_AppInitializationActiviy_activity]> Storing activity scenario");
//		this.world.setActivityScenario(activityScenario);
	}

	@Given("the scheduler is disabled")
	public void the_scheduler_is_disabled() {
		AcceptanceNeoComLogger.info( "[GIVEN] the scheduler is disabled" );
		this.stopScheduler();
	}

	@Given("the network is disabled")
	public void the_network_is_disabled() {
		AcceptanceNeoComLogger.info( "[GIVEN] the network is disabled" );
	}

	@When("the AppInitializationActivity activity lifecycle completes")
	public void the_AppInitializationActivity_activity_lifecycle_completes() {
		AcceptanceNeoComLogger.info( "[WHEN] the AppInitializationActivity activity lifecycle completes for page {string}" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the AppInitializationActivity..." );
		final ActivityScenario<AppInitializationActivity> scenario = ActivityScenario.launch( AppInitializationActivity.class );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setAppInitializationActivityScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
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

	@Then("there is a action bar of type AppVersion with the next fields")
	public void there_is_a_action_bar_of_type_AppVersion_with_the_next_fields( final List<Map<String, String>> dataTable ) {
		AcceptanceNeoComLogger.info( "[THEN] there is a action bar of type AppVersion with the next fields" );
		final ActionBar actionBar = Objects.requireNonNull( this.world.getActiveActivity().getActionBar() );
		final View view = actionBar.getCustomView();
		Assert.assertTrue( this.validateNeoComActionBar( dataTable.get( 0 ), view ) );
	}

	@Then("{string} panels on the LinearContainer")
	public void panels_on_the_LinearContainer( final String panelCount ) {
		final LinearLayout linearContainer = Objects.requireNonNull( this.world.getActiveActivity().findViewById( R.id.initializationLayout ) );
		final int listPanelCount = linearContainer.getChildCount();
		Assert.assertEquals( Integer.parseInt( panelCount ), listPanelCount );
	}

	@Then("the LinearContainer has a {string} panel with the next information")
	public void the_LinearContainer_has_a_panel_with_the_next_information( final String panelType,
	                                                                       final List<Map<String, String>> dataTable ) {
		final LinearLayout linearContainer = Objects.requireNonNull( this.world.getActiveActivity().findViewById( R.id.initializationLayout ) );
		final List<View> targetpanels = new ArrayList<>();
		for (int i = 0; i < linearContainer.getChildCount(); i++) {
			final Object controller = linearContainer.getChildAt( i ).getTag();
			if (null != controller)
				if (controller instanceof InitializationExceptionController)
					targetpanels.add( linearContainer.getChildAt( i ) );
		}
		Assert.assertEquals( 1, targetpanels.size() );
		Assert.assertTrue( this.validateInitializationException( dataTable.get( 0 ), targetpanels.get( 0 ) ) );
	}
	@Then("the LinearContainer has a {string} button with the next information")
	public void the_LinearContainer_has_a_button_with_the_next_information(final String buttonType,
	                                                                       final List<Map<String, String>> dataTable) {
		final LinearLayout linearContainer = Objects.requireNonNull( this.world.getActiveActivity().findViewById( R.id.initializationLayout ) );
		final List<View> targetpanels = new ArrayList<>();
		for (int i = 0; i < linearContainer.getChildCount(); i++) {
			final Object controller = linearContainer.getChildAt( i ).getTag();
			if (null != controller)
				if (controller instanceof RetryButton)
					targetpanels.add( linearContainer.getChildAt( i ) );
		}
		Assert.assertEquals( 1, targetpanels.size() );
		Assert.assertTrue( this.validateRetryButton( dataTable.get( 0 ), targetpanels.get( 0 ) ) );
	}
	@When("completed the onCreate lifecycle phase")
	public void completed_the_onCreate_lifecycle_phase() {
		final ActivityScenario<AppInitializationActivity> activityScenario = this.world.getActivityScenario();
		logger.info( "-- [completed_the_onCreate_lifecycle_phase]> Moving to state {}", Lifecycle.State.CREATED );
		activityScenario.moveToState( Lifecycle.State.CREATED );
	}

	@Then("we have an empty view layout")
	public void we_have_an_empty_view_layout() {
		// Close any previous scenario.
		logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Starting..." );
		logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Closing scenario" );
		this.world.getActivityScenario().close();
		logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Launch new scenario" );

		try (ActivityScenario<AppInitializationActivity> scenario = ActivityScenario.launch( AppInitializationActivity.class )) {
			logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Moving to state CREATED" );
			scenario.moveToState( Lifecycle.State.CREATED );
			scenario.onActivity( ( activity ) -> {
				logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Entering onActivity" );
				final LinearLayout layout = activity.findViewById( R.id.initializationLayout );
				logger.info( "ACCEPTANCE --> [we_have_an_empty_view_layout]> Checking asserts" );
				Assert.assertNotNull( layout );
				Assert.assertEquals( 0, layout.getChildCount() );
			} );
//			scenario.close();
		}
//		final LinearLayout layout = this.world.getActiveActivity().findViewById(R.id.initializationLayout);
//		Assert.assertNotNull(layout);
//		Assert.assertEquals(0, layout.getChildCount());
//		logger.info("-- [we_have_an_empty_view_layout]> Closing scenario");
//		this.world.getActivityScenario().close();
	}

	@When("completed the onStart lifecycle phase")
	public void completed_the_onStart_lifecycle_phase() {
		final ActivityScenario<AppInitializationActivity> activityScenario = this.world.getActivityScenario();
		logger.info( "-- [completed_the_onStart_lifecycle_phase]> Moving to state {}", Lifecycle.State.STARTED );
		activityScenario.moveToState( Lifecycle.State.STARTED );
	}

	@Then("that item is the application info panel")
	public void that_item_is_the_application_info_panel() {
		// The the first element of the layout.
		final LinearLayout layout = this.world.getActiveActivity().findViewById( R.id.initializationLayout );
		Assert.assertNotNull( layout );
		final View item = layout.getChildAt( 0 );
		Assert.assertNotNull( item );
		final MVCRender tag = (MVCRender) item.getTag();
		Assert.assertNotNull( tag );
//		if (tag instanceof AppVersionController.AppVersionRender) {
//			logger.info("-- [that_item_is_the_application_info_panel]> Storing active panel");
//			this.world.setActivePanel(item);
//			logger.info("-- [that_item_is_the_application_info_panel]> Storing active render");
//			this.world.setActiveRender(tag);
//		}
	}

	//	@Then("that the application version matches {string}")
	public void that_the_application_version_matches( final String applicationVersion ) {
		final MVCRender render = this.world.getActiveRender();
//		if (render instanceof AppVersionController.AppVersionRender)
//			Assert.assertEquals(applicationVersion,
//					((AppVersionController.AppVersionRender) render).getController().getModel().getAppVersion());
//		logger.info("-- [that_the_application_version_matches]> Closing scenario");
		this.world.getActivityScenario().close();
	}

	private boolean validateNeoComActionBar( final Map<String, String> expectedData,
	                                         final View obtainedView ) {
		final String APP_NAME = "AppName";
		final String APP_VERSION = "AppVersion";
		final TextView appName = Objects.requireNonNull( obtainedView.findViewById( R.id.appName ) );
		AcceptanceNeoComLogger.info( "[THEN] appName: {}", appName.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_NAME ), appName.getText().toString() );
		final TextView appVersion = Objects.requireNonNull( obtainedView.findViewById( R.id.appVersion ) );
		AcceptanceNeoComLogger.info( "[THEN] appName: {}", appVersion.getText().toString() );
		Assert.assertEquals( expectedData.get( APP_VERSION ), appName.getText().toString() );
		return true;
	}

	private boolean validateInitializationException( final Map<String, String> expectedData,
	                                                 final View obtainedView ) {
		final String MESSAGE = "Message";
		final TextView message = Objects.requireNonNull( obtainedView.findViewById( R.id.message ) );
		AcceptanceNeoComLogger.info( "[THEN] message: {}", message.getText().toString() );
		Assert.assertEquals( expectedData.get( MESSAGE ), message.getText().toString() );
		return true;
	}
}
