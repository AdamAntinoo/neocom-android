package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Application;
import android.view.View;
import android.widget.LinearLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC01AppInitializationActivity extends StepFramework {
	@Rule
	public GrantPermissionRule mPermissionReadExternalStorageRule = GrantPermissionRule.grant(
			Manifest.permission.READ_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionWriteExternalStorageRule = GrantPermissionRule.grant(
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(
			android.Manifest.permission.ACCESS_NETWORK_STATE);

	// - COMPONENTS
	public NC01AppInitializationActivity( final NeoComWorld world ) {
		super(world);
		logger.info("-- [NC01AppInitializationActivity]> Storing world");
	}

	@Given("the AppInitializationActiviy activity")
	public void the_AppInitializationActiviy_activity() {
		final Application application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull(application);
		logger.info("-- [the_AppInitializationActiviy_activity]> Storing application");
		this.world.setApplication((NeoComApplication) application);
		// Create the activity scenario.
		final ActivityScenario<AppInitializationActivity> activityScenario = ActivityScenario.launch(
				AppInitializationActivity.class);
		activityScenario.onActivity(( activity ) -> {
			logger.info("-- [the_AppInitializationActiviy_activity]> Storing activity");
			this.world.setActiveActivity(activity);
		});
		logger.info("-- [the_AppInitializationActiviy_activity]> Storing activity scenario");
		this.world.setActivityScenario(activityScenario);
	}

	@When("completed the onCreate lifecycle phase")
	public void completed_the_onCreate_lifecycle_phase() {
		final ActivityScenario<AppInitializationActivity> activityScenario = this.world.getActivityScenario();
		logger.info("-- [completed_the_onCreate_lifecycle_phase]> Moving to state {}", Lifecycle.State.CREATED);
		activityScenario.moveToState(Lifecycle.State.CREATED);
	}

	@Then("we have an empty view layout")
	public void we_have_an_empty_view_layout() {
		// Close any previous scenario.
		logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Starting...");
		logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Closing scenario");
		this.world.getActivityScenario().close();
		logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Launch new scenario");

		try(ActivityScenario<AppInitializationActivity> scenario = ActivityScenario.launch(AppInitializationActivity.class)) {
			logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Moving to state CREATED");
			scenario.moveToState(Lifecycle.State.CREATED);
			scenario.onActivity(( activity ) -> {
				logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Entering onActivity");
				final LinearLayout layout =activity.findViewById(R.id.initializationLayout);
				logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Checking asserts");
				Assert.assertNotNull(layout);
				Assert.assertEquals(0, layout.getChildCount());
			});
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
		logger.info("-- [completed_the_onStart_lifecycle_phase]> Moving to state {}", Lifecycle.State.STARTED);
		activityScenario.moveToState(Lifecycle.State.STARTED);
	}

	@Then("we have layout with a single item")
	public void we_have_layout_with_a_single_item() {
		final AppInitializationActivity activity = (AppInitializationActivity) this.world.getActiveActivity();
		final LinearContainer container = activity.getContainerView();
		Assert.assertEquals(1, container.size());
	}

	@Then("that item is the application info panel")
	public void that_item_is_the_application_info_panel() {
		// The the first element of the layout.
		final LinearLayout layout = this.world.getActiveActivity().findViewById(R.id.initializationLayout);
		Assert.assertNotNull(layout);
		final View item = layout.getChildAt(0);
		Assert.assertNotNull(item);
		final MVCRender tag = (MVCRender) item.getTag();
		Assert.assertNotNull(tag);
		if (tag instanceof AppVersionController.AppVersionRender) {
			logger.info("-- [that_item_is_the_application_info_panel]> Storing active panel");
			this.world.setActivePanel(item);
			logger.info("-- [that_item_is_the_application_info_panel]> Storing active render");
			this.world.setActiveRender(tag);
		}
	}

	@Then("that the application version matches {string}")
	public void that_the_application_version_matches( final String applicationVersion ) {
		final MVCRender render = this.world.getActiveRender();
		if (render instanceof AppVersionController.AppVersionRender)
			Assert.assertEquals(applicationVersion,
					((AppVersionController.AppVersionRender) render).getController().getModel().getAppVersion());
		logger.info("-- [that_the_application_version_matches]> Closing scenario");
		this.world.getActivityScenario().close();
	}
}