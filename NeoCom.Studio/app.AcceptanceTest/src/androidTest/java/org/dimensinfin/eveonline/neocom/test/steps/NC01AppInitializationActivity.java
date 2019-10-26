package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.junit.Assert;
import org.junit.Rule;

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
	private NeoComWorld world;
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;

	public NC01AppInitializationActivity( final NeoComWorld world ) {
		super(world);
	}

	@Given("the AppInitializationActiviy activity")
	public void the_AppInitializationActiviy_activity() {
		logger.info("-- [the_AppInitializationActiviy_activity]> Storing world");
		this.world = world;
		// Create the application instance.
		final Application application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull(application);
		logger.info("-- [the_AppInitializationActiviy_activity]> Storing application");
		this.world.setApplication((NeoComApplication) application);
		// Create the required components.
		// TODO - This is probably not required for this test.
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
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
		activityScenario.moveToState(Lifecycle.State.CREATED);
	}

	@Then("we can access a LinearContainer empty of content")
	public void we_can_access_a_LinearContainer_empty_of_content() {
		final AppInitializationActivity activity = (AppInitializationActivity) this.world.getActiveActivity();
		final LinearContainer container = activity.getContainerView();
		Assert.assertEquals(0, container.size());
	}
}