package org.dimensinfin.eveonline.neocom.app.steps;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.app.support.StepFramework;
import org.dimensinfin.eveonline.neocom.app.support.worlds.PilotDashboardActivityWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class N04PANELMiningDatAccessInformationPanel extends StepFramework {
	private static Logger logger = LoggerFactory.getLogger(ShowTheApplicationName.class);

	private PilotDashboardActivityWorld world;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<PilotDashboardActivity> mActivityRule =
			new ActivityTestRule<PilotDashboardActivity>(PilotDashboardActivity.class) {
				@Override
				protected Intent getActivityIntent() {
					Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
					Intent intent = new Intent(targetContext, PilotDashboardActivity.class);
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
							, 93813310); // Credential account identifier
					return intent;
				}
			};
	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);

	@Before
	public void setUp() throws Exception {
		this.world = new PilotDashboardActivityWorld();
		Thread.sleep(TimeUnit.SECONDS.toMillis(3));
	}
	@Test
	public void launchN04PANELMiningDatAccessInformationPanel() throws InterruptedException {
		logger.info(">> [N04PANELMiningDatAccessInformationPanel.launchN04PANELMiningDatAccessInformationPanel]");
		the_activity_PilotDashboardActivity();
		the_activity_PilotDashboardActivity_is_started();
		there_is_a_data_panel_of_type_MiningData();
		that_panel_has_an_icon();
		that_panel_has_a_title_with_the_literal("MINING DATA");
		that_panel_has_a_label_with_the_literal("Mining Resources market value");
		that_panel_has_a_data_field_with_contents(".00 ISK");
		that_panel_has_an_activator_visible();
	}

	@Given("the activity PilotDashboardActivity")
	public void the_activity_PilotDashboardActivity() throws InterruptedException {
		logger.info(">> [N04PANELMiningDatAccessInformationPanel.the_activity_PilotDashboardActivity]");
		final PilotDashboardActivity activity = this.mActivityRule.getActivity();
		Assert.assertNotNull("Activity exists.", activity);
		this.world.setActivity(activity);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
	}

	@When("the activity PilotDashboardActivity is started")
	public void the_activity_PilotDashboardActivity_is_started() {
		final PilotDashboardActivity activity = this.world.getActivity();
		final PilotDashboardFragment fragment = (PilotDashboardFragment) activity.accessPageAdapter().getItem(0); // Get the first page
		Assert.assertNotNull("Fragment exists.", fragment);
		this.world.setFragment(fragment);
		this.accessDataContainers(fragment, this.world);
		Assert.assertTrue("The container should have elements.", this.world.getDataSectionContainer().getChildCount() > 0);
	}

	@Then("there is a data panel of type MiningData")
	public void there_is_a_data_panel_of_type_MiningData() {
		final List<View> sections = this.accessPanelsOfClass(PilotSection.class, this.world.getDataSectionContainer());
		final List<View> panels = Stream.of(sections)
		                                .filter(section -> this.pilotSectionTitleFilter(section, "MINING DATA"))
		                                .collect(Collectors.toList());
		Assert.assertEquals("MiningData panel exists.", 1, panels.size());
		this.world.setMiningDataPanel(panels.get(0));
	}

	@Then("that panel has an icon")
	public void that_panel_has_an_icon() {
		final View miningDataPanel = this.world.getMiningDataPanel();
		final ImageView icon = miningDataPanel.findViewById(R.id.sectionIcon);
		Assert.assertNotNull(icon);
		Assert.assertNotNull(icon.getDrawable());
	}

	@Then("that panel has a title with the literal {string}")
	public void that_panel_has_a_title_with_the_literal( final String expected ) {
		final View miningDataPanel = this.world.getMiningDataPanel();
		final TextView field = miningDataPanel.findViewById(R.id.sectionTitle);
		Assert.assertNotNull(field);
		Assert.assertEquals(expected, field.getText());
	}

	@Then("that panel has a label with the literal {string}")
	public void that_panel_has_a_label_with_the_literal( final String expected ) {
		final View miningDataPanel = this.world.getMiningDataPanel();
		final TextView field = miningDataPanel.findViewById(R.id.sectionLabel);
		Assert.assertNotNull(field);
		Assert.assertEquals(expected, field.getText());
	}

	@Then("that panel has a data field with contents {string}")
	public void that_panel_has_a_data_field_with_contents( final String expected ) {
		final View miningDataPanel = this.world.getMiningDataPanel();
		final TextView field = miningDataPanel.findViewById(R.id.sectionData);
		Assert.assertNotNull(field);
		Assert.assertEquals(expected, field.getText());
	}

	@Then("that panel has an activator visible")
	public void that_panel_has_an_activator_visible() {
		final View miningDataPanel = this.world.getMiningDataPanel();
		final ViewGroup activator = miningDataPanel.findViewById(R.id.clickBlock);
		Assert.assertNotNull(activator);
		Assert.assertTrue(activator.isEnabled());
	}

	private List<View> accessPanelsOfClass( final Class filterClass, final ListView container ) {
		final List<View> panels = new ArrayList<>();
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition(i, container);
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) panels.add(target);
		}
		return panels;
	}

	private boolean pilotSectionTitleFilter( final View target, final String title ) {
		final IAndroidController controller = (IAndroidController) target.getTag();
		final PilotSection model = (PilotSection) controller.getModel();
		if (model.getTitle().equalsIgnoreCase(title)) return true;
		return false;
	}
}
