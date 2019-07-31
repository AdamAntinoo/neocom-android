package org.dimensinfin.eveonline.neocom.app.steps;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.app.support.worlds.NeoComWorld;
import org.junit.Rule;

import cucumber.api.java.en.Given;

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

	public GivenTheActivity( final NeoComWorld world) {
		this.world=world;
	}

	@Given("the activity {string}")
	public void the_activity(String activityName) {
		switch ( activityName){
			case "AppSplashActivity":
				this.world.setTargetActivity( this.mAppSplashActivityRule.getActivity());
				break;
		}
	}
}
