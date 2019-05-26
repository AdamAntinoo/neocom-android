package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComActionBarV2;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class NeoComActionBarControllerTest {
	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PilotDashboardActivity> mActivityRule = new ActivityTestRule<PilotDashboardActivity>(PilotDashboardActivity.class) {
		@Override
		protected Intent getActivityIntent() {
			Context targetContext = InstrumentationRegistry.getInstrumentation()
					                        .getTargetContext();
			Intent intent = new Intent(targetContext, PilotDashboardActivity.class);
			intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
					, 92223647); // Credential account identifier
			return intent;
		}
	};

	@Test
	public void accessLayoutReference() {
		final NeoComActionBarController controller = Mockito.mock(NeoComActionBarController.class);
		final NeoComActionBarV2 model = new NeoComActionBarV2.Builder()
				                                .withTitleTopLeft("TEST ACTION BAR").build();
		final NeoComActionBarController.NeoComActionBarRender render = new NeoComActionBarController.NeoComActionBarRender(controller, mActivityRule.getActivity());
		Assert.assertNotNull("Render is not null.", render);
		final int id = render.accessLayoutReference();
		Assert.assertTrue(id > 0);
	}

	@Test
	public void updateContent_onlyTopLeft() {
		final String expected = "TEST ACTION BAR";
		final NeoComActionBarV2 model = new NeoComActionBarV2.Builder()
				                                .withTitleTopLeft(expected).build();
		final AppControllerFactory factory = new AppControllerFactory("TEST VARIANT");
		final NeoComActionBarController controller = (NeoComActionBarController) factory.createController(model);
		final NeoComActionBarController.NeoComActionBarRender render = new NeoComActionBarController.NeoComActionBarRender(controller, mActivityRule.getActivity());
		Assert.assertNotNull("Render is not null.", render);
		final View view = render.getView();
		Assert.assertNotNull("View is not null.", view);

		render.updateContent();
		final TextView titleView = view.findViewById(R.id.toolBarTitleLeft);
		Assert.assertNotNull("Check view should have been found.", titleView);
		final String obtained = titleView.getText().toString();
		Assert.assertEquals("The text on the title should match.", expected, obtained);
		// TODO - THis type of tests should be verified
//		Assert.assertTrue("Set field should be visible", view.findViewById(R.id.toolBarTitleLeft).isShown());
//		Assert.assertFalse("Other fields should not be visible", view.findViewById(R.id.toolBarTitleRight).isShown());
//		Assert.assertFalse("Other fields should not be visible", view.findViewById(R.id.toolBarSubTitle).isShown());
	}

	@Test
	public void updateContent_allfields() {
		final String expectedTopLeft = "TEST ACTION BAR TOP LEFT";
		final String expectedTopRigth = "TEST ACTION BAR TOP RIGHT";
		final String expectedSubtitle = "TEST ACTION BAR SUBTITLE";
		final NeoComActionBarV2 model = new NeoComActionBarV2.Builder()
				                                .withTitleTopLeft(expectedTopLeft)
				                                .withTitleTopRight(expectedTopRigth)
				                                .withSubtitle(expectedSubtitle)
				                                .build();
		final AppControllerFactory factory = new AppControllerFactory("TEST VARIANT");
		final NeoComActionBarController controller = (NeoComActionBarController) factory.createController(model);
		final NeoComActionBarController.NeoComActionBarRender render = new NeoComActionBarController.NeoComActionBarRender(controller, mActivityRule.getActivity());
		Assert.assertNotNull("Render is not null.", render);
		final View view = render.getView();
		Assert.assertNotNull("View is not null.", view);

		render.updateContent();
		TextView titleView = view.findViewById(R.id.toolBarTitleLeft);
		Assert.assertNotNull("Check view should have been found.", titleView);
		String obtained = titleView.getText().toString();
		Assert.assertEquals("The text on the title should match.", expectedTopLeft, obtained);
		titleView = view.findViewById(R.id.toolBarTitleRight);
		Assert.assertNotNull("Check view should have been found.", titleView);
		obtained = titleView.getText().toString();
		Assert.assertEquals("The text on the title should match.", expectedTopRigth, obtained);
		titleView = view.findViewById(R.id.toolBarSubTitle);
		Assert.assertNotNull("Check view should have been found.", titleView);
		obtained = titleView.getText().toString();
		Assert.assertEquals("The text on the title should match.", expectedSubtitle, obtained);

		// TODO - THis type of tests should be verified
//		Assert.assertTrue("Set field should be visible", view.findViewById(R.id.toolBarTitleLeft).isShown());
//		Assert.assertTrue("Set field should be visible", view.findViewById(R.id.toolBarTitleRight).isShown());
//		Assert.assertTrue("Set field should be visible", view.findViewById(R.id.toolBarSubTitle).isShown());
	}
}
