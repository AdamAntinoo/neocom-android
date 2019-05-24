package org.dimensinfin.eveonline.neocom.app.render;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.SettingsActivity;
import org.dimensinfin.eveonline.neocom.app.controller.PanelTitleController;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PanelTitleRenderTest {
	@Rule
	public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(SettingsActivity.class);

	@Test
	public void accessLayoutReference() {
		final PanelTitleController controller = Mockito.mock(PanelTitleController.class);
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		final PanelTitleRender render = new PanelTitleRender(controller, mActivityRule.getActivity());
		Assert.assertNotNull("Render is not null.", render);
		final int id = render.accessLayoutReference();
		Assert.assertTrue(id > 0);
	}

	@Test
	public void initializeViews() {
		final PanelTitleController controller = Mockito.mock(PanelTitleController.class);
		final PanelTitleRender render = new PanelTitleRender(controller, mActivityRule.getActivity());
		render.initializeViews();
		Assert.assertNotNull(render);
	}

	@Test
	public void updateContent() {
		final PanelTitleController controller = Mockito.mock(PanelTitleController.class);
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		final PanelTitleRender render = new PanelTitleRender(controller, mActivityRule.getActivity());
		final String expected = "TEST TITLE";
		Mockito.when(controller.getModel()).thenReturn(model);
		Mockito.when(model.getTitle()).thenReturn(expected);
		render.updateContent();
		onView(withId(R.id.title)).check(matches(withText(expected)));
	}
}
