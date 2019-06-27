package org.dimensinfin.eveonline.neocom.planetary.activity;

import android.view.View;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class PlanetaryColoniesActivityTest {
	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PlanetaryColoniesActivity> mActivityRule = new ActivityTestRule<>(PlanetaryColoniesActivity.class);

	//	@Test
	//	public void injectComponents() {
	//		mActivityRule.getActivity().injectComponents();
	//		Assert.assertNotNull("Check the injected components are not null.", mActivityRule.getActivity().configurationProvider);
	//	}

//	@Test
//	public void generateActionBar() {
//		final View actionBar = mActivityRule.getActivity().generateActionBar();
//		Assert.assertNotNull("Action bar view should not be null.", actionBar);
//	}

//	@Test
//	public void actionBarContents() {
//		final Credential credential = Mockito.mock(Credential.class);
//		final PlanetaryColoniesActivity activity = Mockito.mock(PlanetaryColoniesActivity.class);
//		Mockito.when(activity.getCredential()).thenReturn(credential);
//		Mockito.when((credential.getAccountName())).thenReturn("Beth Ripley");
//		Mockito.when(credential.getAccountId()).thenReturn(9222);
////		final View actionBarView = mActivityRule.getActivity().generateActionBar();
////		Assert.assertNotNull("Action bar view should not be null.", actionBarView);
////		final Object controller = actionBarView.getTag();
//		Assert.assertTrue(controller instanceof PlanetaryActionBarController);
//		if (controller instanceof PlanetaryActionBarController) {
//			final PlanetaryActionBar model = ((PlanetaryActionBarController) controller).getModel();
//			Assert.assertEquals("Check the title left.", "Beth Ripley", model.getTitleTopLeft());
//			Assert.assertNull("Check the title right.", model.getTitleTopLeft());
//			Assert.assertEquals("Check the subtitle.", "PLANETS LIST", model.getSubtitle());
//			Assert.assertEquals("Check the activity icon reference.", "PLANETS LIST", model.getActivityIconReference());
//			Assert.assertEquals("Check the pilot identifier.", 9222, model.getPilotIdentifier());
//		}
//	}
}
