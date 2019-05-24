package org.dimensinfin.eveonline.neocom.app.controller;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;

import org.junit.Assert;
import org.junit.Before;
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
public class InitialisationTaskRenderTest {
	private static MiningLedgerActivity context;
	private static InitialisationTaskController controller;
	private static InitialisationTask model;

	@Rule
	public ActivityTestRule<MiningLedgerActivity> mActivityRule = new ActivityTestRule<>(MiningLedgerActivity.class);

	@Before
	public void setUp() throws Exception {
		context = mActivityRule.getActivity();
		controller = Mockito.mock(InitialisationTaskController.class);
		model = Mockito.mock(InitialisationTask.class);
	}

	@Test
	public void accessLayoutReference() {
		final InitialisationTaskController.InitialisationTaskRender render = new InitialisationTaskController.InitialisationTaskRender(controller, context);
		Assert.assertNotNull("Render is not null.", render);
		final int id = render.accessLayoutReference();
		Assert.assertTrue(id > 0);
	}

	@Test
	public void initializeViews() {
		final InitialisationTaskController.InitialisationTaskRender render = new InitialisationTaskController.InitialisationTaskRender(controller, context);
		render.initializeViews();
	}

	@Test
	public void updateContent() {
		final String expected = "TEST-TITLE";
		final MVCRender mvcrender = Mockito.mock(MVCRender.class);
		Mockito.when(controller.getModel()).thenReturn(model);
		Mockito.when(model.getTaskName()).thenReturn(expected);
		Mockito.doAnswer(( call ) -> {
			final MiningExtraction parameter = call.getArgument(0);
			Assert.assertNotNull(parameter);
			return null;
		}).when(mvcrender).createView();
		final InitialisationTaskController.InitialisationTaskRender render = new InitialisationTaskController.InitialisationTaskRender(controller, context);
		//		render.initializeViews();
		render.updateContent();
		onView(withId(R.id.title)).check(matches(withText(expected)));
	}
	//
	//	@Test
	//	public void buildRender() {
	//		final SettingsActivity context = mActivityRule.getActivity();
	//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	//		final boolean obtained = preferences.getBoolean("prefkey_BlockDownloads", true);
	//
	//		assertFalse(obtained);
	//	}
	//
	//	@Test
	//	public void getModelId() {
	//	}
}
