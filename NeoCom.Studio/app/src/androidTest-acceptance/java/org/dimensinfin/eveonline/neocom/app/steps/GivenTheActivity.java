package org.dimensinfin.eveonline.neocom.app.steps;

import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashFragment;
import org.dimensinfin.eveonline.neocom.app.support.worlds.NeoComWorld;
import org.junit.Assert;
import org.junit.Rule;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GivenTheActivity {
	private NeoComWorld world;
	private AppSplashActivity spyActivity;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);
	@Rule
	public ActivityTestRule<AppSplashActivity> mAppSplashActivityRule = new ActivityTestRule<>(AppSplashActivity.class);

	public GivenTheActivity( final NeoComWorld world ) {
		this.world = world;
	}

	@Given("the activity {string}")
	public void the_activity( String activityName ) {
		switch (activityName) {
			case "AppSplashActivity":
				this.world.setTargetActivity(this.mAppSplashActivityRule.getActivity());
				break;
		}
	}

	@When("the activity is started")
	public void the_activity_is_started() {
		this.mAppSplashActivityRule.launchActivity(null);
	}

	@Then("we see a panel with the application name that matches {string}")
	public void we_see_a_panel_with_the_application_name_that_matches( String applicationName ) {
		this.spyActivity = this.mAppSplashActivityRule.getActivity();
		final AppSplashFragment fragment = (AppSplashFragment) this.spyActivity.accessPageAdapter().getItem(0);
		final View panel = fragment.accessHeaderContainer().getChildAt(0);
		final TextView appNameView = panel.findViewById(R.id.applicationName);
		Assert.assertEquals("Verify that the content of the view matches the application name.",
				applicationName, appNameView.getText());
	}
}
