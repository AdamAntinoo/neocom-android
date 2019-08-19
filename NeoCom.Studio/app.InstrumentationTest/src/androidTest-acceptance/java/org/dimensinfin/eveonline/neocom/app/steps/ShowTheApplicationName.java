package org.dimensinfin.eveonline.neocom.app.steps;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashFragment;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.support.StepFramework;
import org.dimensinfin.eveonline.neocom.app.support.worlds.AppSplashActivityWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ShowTheApplicationName extends StepFramework {
	private static Logger logger = LoggerFactory.getLogger(ShowTheApplicationName.class);

	private AppSplashActivityWorld world;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<AppSplashActivity> mActivityRule = new ActivityTestRule<>(AppSplashActivity.class);
	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);

	@Before
	public void setUp() throws Exception {
		this.world = new AppSplashActivityWorld();
		Thread.sleep(TimeUnit.SECONDS.toMillis(3));
	}

	@Test
	public void launchShowTheApplicationName() throws InterruptedException {
		the_activity_AppSplashActivity();
		the_activity_is_started();
		we_have_an_application_panel();
		that_panel_has_an_ApplicationName_field();
		that_field_shows_the_application_name_that_matches("NeoCom");
	}

	@Given("the activity AppSplashActivity")
	public void the_activity_AppSplashActivity() {
		logger.info("[ShowTheApplicationName.the_activity_AppSplashActivity]");
		final AppSplashActivity activity = this.mActivityRule.getActivity();
		Assert.assertNotNull("Activity exists.", activity);
		this.world.setActivity(activity);
		final AppSplashFragment fragment = (AppSplashFragment) activity.accessPageAdapter().getItem(0); // Get the first page
		Assert.assertNotNull("Fragment exists.", fragment);
		this.world.setFragment(fragment);
		// TODO - Temporarily set the read to the data section container
		final ListView headerContainer = fragment.accessDataSectionContainer();
		Assert.assertNotNull("Container ListView exists.", headerContainer);
		this.world.setDataSectionContainer(headerContainer);
//		this.world.setHeaderContainer(headerContainer);
//		final ViewGroup headerContainer = fragment.accessHeaderContainer();
//		Assert.assertNotNull("Container ListView exists.", headerContainer);
//		this.world.setHeaderContainer(headerContainer);
		Assert.assertTrue("The container should have elements.", headerContainer.getChildCount() > 0);
	}

	@When("the activity is started")
	public void the_activity_is_started() throws InterruptedException {
		Thread.sleep(TimeUnit.SECONDS.toMillis(2)); // Wait some time to stabilize the activity
	}

	@Then("we have an application panel")
	public void we_have_an_application_panel() {
		// TODO - Temporarily set the read to the data section container
		final View panel = this.searchPanelByClass(AppVersion.class, this.world.getDataSectionContainer());
		Assert.assertNotNull("Application version panel exists.", panel);
		this.world.setAppVersionPanel(panel);
	}

	@Then("that panel has an ApplicationName field")
	public void that_panel_has_an_ApplicationName_field() {
		final View appPanel = this.world.getAppVersionPanel();
		final TextView appNameView = appPanel.findViewById(R.id.applicationName);
		Assert.assertNotNull(appNameView);
	}

	@Then("that field shows the application name that matches {string}")
	public void that_field_shows_the_application_name_that_matches( final String string ) {
		final View appPanel = this.world.getAppVersionPanel();
		final TextView appNameView = appPanel.findViewById(R.id.applicationName);
		final TextView appVersionView = appPanel.findViewById(R.id.applicationVersion);
		Assert.assertEquals("Verify that the content of the view matches the application name.",
				this.world.getActivity().getResources().getString(R.string.appname),
				appNameView.getText());
		Assert.assertEquals("Verify that the content of the view matches the application version.",
				this.world.getActivity().getResources().getString(R.string.appversionname),
				appVersionView.getText());
	}
}
