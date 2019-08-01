package org.dimensinfin.eveonline.neocom.app.steps;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ShowTheApplicationName extends StepFramework {
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
	}

	@Test
	public void launchShowTheApplicationName() throws InterruptedException {
//		final ShowTheApplicationName test = new ShowTheApplicationName();
		A_the_activity_AppSplashActivity();
		B_the_activity_is_started();
		Z_we_have_an_application_panel();
		C_that_panel_has_an_ApplicationName_field();
		D_that_field_shows_the_application_name_that_matches("NeoCom");
	}

	@Given("the activity AppSplashActivity")
	public void A_the_activity_AppSplashActivity() {
		final AppSplashActivity activity = this.mActivityRule.getActivity();
		Assert.assertNotNull("Activity exists.", activity);
		this.world.setActivity(activity);
		final AppSplashFragment fragment = (AppSplashFragment) activity.accessPageAdapter().getItem(0); // Get the first page
		Assert.assertNotNull("Fragment exists.", fragment);
		this.world.setFragment(fragment);
		final ListView dataSectionContainer = fragment.accessDataSectionContainer();
		Assert.assertNotNull("Container ListView exists.", dataSectionContainer);
		this.world.setDataSectionContainer(dataSectionContainer);
		Assert.assertTrue("The container should have elements.", dataSectionContainer.getCount() > 0);
	}

	@Test
	@When("the activity is started")
	public void B_the_activity_is_started() throws InterruptedException {
		Thread.sleep(TimeUnit.SECONDS.toMillis(2)); // Wait some time to stabilize the activity
	}

	@Test
	@Then("we have an application panel")
	public void Z_we_have_an_application_panel() {
		final View panel = this.searchPanelByClass(AppVersion.class, this.world.getDataSectionContainer());
		Assert.assertNotNull("Application version panel exists.", panel);
		this.world.setAppVersionPanel(panel);
	}

	@Test
	@Then("that panel has an ApplicationName field")
	public void C_that_panel_has_an_ApplicationName_field() {
		final View appPanel = this.world.getAppVersionPanel();
		final TextView appNameView = appPanel.findViewById(R.id.applicationName);
		Assert.assertNotNull(appNameView);
	}

	@Test
	@Then("that field shows the application name that matches {string}")
	public void D_that_field_shows_the_application_name_that_matches( final String string ) {
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
