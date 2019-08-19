package org.dimensinfin.eveonline.neocom.test.supersteps;

import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class GivenTheActivity {
	private NeoComWorld world;

	public GivenTheActivity( final NeoComWorld world ) {
		this.world = world;
	}

	@Given("the activity {string}")
	public void theActivity( final String activityClass ) {
		switch (activityClass) {
			case "AppSplashActivity":
				final ActivityTestRule<AppSplashActivity> activityRuleSplash = new ActivityTestRule<>(AppSplashActivity.class,
				                                                                                      false, false);
				Assert.assertNotNull(activityRuleSplash);
				this.world.setActivityRule(activityRuleSplash);
				break;
//			case "AcceptanceActivity04":
//				final ActivityTestRule<AcceptanceActivity04> activityRule04 = new ActivityTestRule<>(AcceptanceActivity04.class,
//				                                                                                   false, false);
//
//				Assert.assertNotNull(activityRule04);
//				this.world.setActivityRule(activityRule04);
//				break;
		}
//		Assert.assertEquals(activityClass, this.world.getActiveActivity().getClass().getSimpleName());
	}

	@When("launching the activity to the onResume phase")
	public void launchingTheActivityToTheOnResumePhase() {
		this.world.getActivityRule().launchActivity(null);
		this.world.setActiveActivity(this.world.getActivityRule().getActivity());
		Assert.assertNotNull(this.world.getActiveActivity());
	}
}
