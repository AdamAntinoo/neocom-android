package org.dimensinfin.eveonline.neocom.app.render;

import android.view.View;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PanelTitleController;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;

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
	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<MiningLedgerActivity> mActivityRule = new ActivityTestRule<>(MiningLedgerActivity.class);

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
		final Credential credential = new Credential.Builder(123).build()
				                              .setAccountName("TEST ACCOUNT");
		final MiningLedgerFragment fragment = Mockito.mock(MiningLedgerFragment.class);
		final MiningLedgerDataSourceToday dataSource = Mockito.mock(MiningLedgerDataSourceToday.class);
		Mockito.when(fragment.createDS()).thenReturn(dataSource);
		Mockito.when(fragment.getCredential()).thenReturn(credential);
		Mockito.doAnswer(( call ) -> {
			return null;
		}).when(dataSource).collaborate2Model();
		final PanelTitleController controller = Mockito.mock(PanelTitleController.class);
		final PanelTitle model = Mockito.mock(PanelTitle.class);
		final PanelTitleRender render = new PanelTitleRender(controller, mActivityRule.getActivity());
		final String expected = "TEST TITLE";
		Mockito.when(controller.getModel()).thenReturn(model);
		Mockito.when(model.getTitle()).thenReturn(expected);
		render.updateContent();
		final View view = render.getView();
		Assert.assertNotNull(view);
		final TextView titleView = view.findViewById(R.id.title);
		Assert.assertNotNull(titleView);
		final String obtained = titleView.getText().toString();
		Assert.assertEquals("The text on the title should match.", expected, obtained);
	}
}
