package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.view.View;

import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.core.NeoComFragment;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.junit.Assert;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class GivenTheActivity extends StepFramework {
	private static final int FIRST_PAGE = 0;

	public GivenTheActivity( final NeoComWorld world ) {
		super(world);
	}

	@When("selected the page {string} of the activity")
	public void selectedThePageOfTheActivity( final String pageNumber ) {
		// TODO - Check first if the current page al already the selected page
//		((MVCMultiPageActivity) this.world.getActiveActivity()).accessPageAdapter().

//		this.world.setFragment(((MVCMultiPageActivity) this.world.getActiveActivity()).setPage(FIRST_PAGE));
		this.world.setFragment((NeoComFragment) ((MVCMultiPageActivity) this.world.getActiveActivity()).accessPageAdapter()
				                                        .getItem(Integer.parseInt(pageNumber)));
		Assert.assertNotNull(this.world.getFragment());
	}

	@And("the component factory exists")
	public void theComponentFactoryExists() {
		Assert.assertNotNull(NeoComComponentFactory.getSingleton());
	}

	@And("the timer service is running")
	public void theTimerServiceIsRunning() {
		Assert.assertTrue(NeoComComponentFactory.getSingleton().getApplication().isSchedulerActive());
	}

	@And("the processing spinner is visible")
	public void theProcessingSpinnerIsVisible() {
		final View progress = ((MVCPagerFragment) this.world.getFragment()).getView().findViewById(
				R.id.progressLayout);
		Assert.assertTrue(progress.isShown());
//		onView(progress).check(matches(isDisplayed()));
//		this.accessDataContainers((MVCPagerFragment) this.world.getFragment());
//		final View panel = this.searchFirstPanelByClass(AppVersion.class, this.world.getHeaderContainer());
//		Assert.assertNotNull(panel);
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
