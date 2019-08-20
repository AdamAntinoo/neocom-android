package org.dimensinfin.eveonline.neocom.test.supersteps;

import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.junit.Assert;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GivenTheActivity {
	private static final int FIRST_PAGE = 0;
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
		this.world.getActivityRule().launchActivity(null);
		this.world.setActiveActivity((MVCMultiPageActivity) this.world.getActivityRule().getActivity());
		Assert.assertNotNull(this.world.getActiveActivity());
	}

//	@When("launching the activity to the onResume phase")
//	public void launchingTheActivityToTheOnResumePhase() {
//		this.world.getActivityRule().launchActivity(null);
//		this.world.setActiveActivity((MVCMultiPageActivity) this.world.getActivityRule().getActivity());
//		Assert.assertNotNull(this.world.getActiveActivity());
//	}

	@When("selected the page {string} of the activity")
	public void selectedThePageOfTheActivity( final String pageNumber ) {
		this.world.setFragment(this.world.getActiveActivity().setPage(FIRST_PAGE));
		Assert.assertNotNull(this.world.getFragment());
	}

	@Then("complete the lifecycle without exceptions")
	public void completeTheLifecycleWithoutExceptions() {
		Assert.assertNull(((AppSplashActivity) this.world.getActiveActivity()).getLatestException());
	}

	@And("the component factory exists")
	public void theComponentFactoryExists() {
		Assert.assertNotNull(NeoComComponentFactory.getSingleton());
	}

	@And("the timer service is running")
	public void theTimerServiceIsRunning() {
		Assert.assertTrue(NeoComComponentFactory.getSingleton().getApplication().isSchedulerActive());
	}

//	@Then("complete the {string} phase with no exceptions")
//	public void completeThePhaseWithNoExceptions( final String phase ) {
//		switch (phase) {
//			case "onCreate":
//				Assert.assertTrue(((AppSplashActivity) this.world.getActiveActivity()).isPhaseOnCreateCompleted());
//				break;
//			case "onStart":
//				Assert.assertTrue(((AppSplashActivity) this.world.getActiveActivity()).isPhaseOnStartCompleted());
//				break;
//		}
//	}

}
