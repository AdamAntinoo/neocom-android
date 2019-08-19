package org.dimensinfin.eveonline.neocom.app.steps;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.support.StepFramework;
import org.dimensinfin.eveonline.neocom.app.support.worlds.MiningLedgerActivityWorld;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.TableHeader;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerFragment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class N07P0HEADERListTodayExtractionsResources extends StepFramework {
	private MiningLedgerActivityWorld world;

	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<MiningLedgerActivity> mActivityRule =
			new ActivityTestRule<MiningLedgerActivity>(MiningLedgerActivity.class) {
				@Override
				protected Intent getActivityIntent() {
					Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
					Intent intent = new Intent(targetContext, MiningLedgerActivity.class);
					intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
							, 93813310); // Credential account identifier
					return intent;
				}
			};
	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(
			android.Manifest.permission.ACCESS_NETWORK_STATE);

	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.world = new MiningLedgerActivityWorld();
		Thread.sleep(TimeUnit.SECONDS.toMillis(3));
	}

	@Test
	public void launchN07P0HEADERListTodayExtractionsResourcesTableHeader() throws InterruptedException {
		logger.info(">> [N07P0HEADERListTodayExtractionsResources.launchN07P0HEADERListTodayExtractionsResourcesTableHeader]");
		the_activity_MiningLedgerActivity();
		the_activity_MiningLedgerActivity_is_started();
		there_is_a_header_panel_of_type_TableHeader();
		that_panel_has_labels(3);
		the_label_contains_the_literal("1", "RESOURCE NAME [#TYPEID]");
		the_label_contains_the_literal("2", "QUANTITY");
		the_label_contains_the_literal("3", "VALUE");
		logger.info("<< [N07P0HEADERListTodayExtractionsResources.launchN07P0HEADERListTodayExtractionsResourcesTableHeader]");
	}

	@Test
	public void launchN07P0HEADERListTodayExtractionsResourcesResourceList() throws InterruptedException {
		logger.info(">> [N07P0HEADERListTodayExtractionsResources.launchN07P0HEADERListTodayExtractionsResourcesResourceList]");
		the_activity_MiningLedgerActivity();
		the_activity_MiningLedgerActivity_is_started();
		there_is_a_list_of_header_panels_of_type_Resource(2);
		that_each_panel_has_an_icon_representing_the_resource_icon();
		final List<Resource> resourceList = new ArrayList<>();
		resourceList.add(new Resource(34, 1200));
		resourceList.add(new Resource(18, 400));
		i_get_the_next_list_of_resource_data(resourceList);
		logger.info("<< [N07P0HEADERListTodayExtractionsResources.launchN07P0HEADERListTodayExtractionsResourcesResourceList]");
	}

	@Given("the activity MiningLedgerActivity")
	public void the_activity_MiningLedgerActivity() throws InterruptedException {
		logger.info(">> [N07P0HEADERListTodayExtractionsResources.the_activity_MiningLedgerActivity]");
		final MiningLedgerActivity activity = this.mActivityRule.getActivity();
		Assert.assertNotNull("Activity exists.", activity);
		this.world.setActivity(activity);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
	}

	@When("the activity MiningLedgerActivity is started")
	public void the_activity_MiningLedgerActivity_is_started() {
		logger.info(">> [N07P0HEADERListTodayExtractionsResources.the_activity_MiningLedgerActivity_is_started]");
		final MiningLedgerActivity activity = this.world.getActivity();
		final MiningLedgerFragment fragment = (MiningLedgerFragment) activity.accessPageAdapter().getItem(0); // Get the first
		// page
		Assert.assertNotNull("Fragment exists.", fragment);
		this.world.setFragment(fragment);
		this.accessDataContainers(fragment, this.world);
		Assert.assertTrue("The container should have elements.", this.world.getDataSectionContainer().getChildCount() > 0);
	}

	@Then("there is a header panel of type TableHeader")
	public void there_is_a_header_panel_of_type_TableHeader() {
		final View header = this.searchPanelByClass(TableHeader.class, this.world.getHeaderContainer());
		Assert.assertNotNull("There should be only one header", header);
		this.world.setTableHeaderPanel(header);
	}

	@Then("that panel has {int} labels")
	public void that_panel_has_labels( final Integer labelCount ) {
		final View headerPanel = this.world.getTableHeaderPanel();
		TextView field = headerPanel.findViewById(R.id.nameTypeLabel);
		Assert.assertNotNull(field);
		field = headerPanel.findViewById(R.id.quantityLabel);
		Assert.assertNotNull(field);
		field = headerPanel.findViewById(R.id.valueLabel);
		Assert.assertNotNull(field);
	}

	@Then("the label {string} contains the literal {string}")
	public void the_label_contains_the_literal( final String labelPosition, final String labelContent ) {
		final View headerPanel = this.world.getTableHeaderPanel();
		TextView field;
		switch (labelPosition) {
			case "1":
				field = headerPanel.findViewById(R.id.nameTypeLabel);
				Assert.assertEquals(labelContent, field.getText());
				break;
			case "2":
				field = headerPanel.findViewById(R.id.quantityLabel);
				Assert.assertEquals(labelContent, field.getText());
				break;
			case "3":
				field = headerPanel.findViewById(R.id.valueLabel);
				Assert.assertEquals(labelContent, field.getText());
				break;
		}
	}

	@Then("there is a list of header panels of type Resource")
	public void there_is_a_list_of_header_panels_of_type_Resource( final int resourceCount ) {
		final List<View> resources = this.accessPanelsOfClass(Resource.class, this.world.getHeaderContainer());
		Assert.assertEquals(resourceCount, resources.size());
	}

	@Then("that each panel has an icon representing the resource icon")
	public void that_each_panel_has_an_icon_representing_the_resource_icon() {
		final List<View> resources = this.accessPanelsOfClass(Resource.class, this.world.getHeaderContainer());

	}

	@Then("i get the next list of resource data")
	public void i_get_the_next_list_of_resource_data( final List<Resource> resourceList ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new PendingException();
	}
}
