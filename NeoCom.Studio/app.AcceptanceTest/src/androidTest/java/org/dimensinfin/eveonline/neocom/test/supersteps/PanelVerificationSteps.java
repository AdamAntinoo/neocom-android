package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.dimensinfin.eveonline.neocom.test.support.validators.ResourceAggregatorContainer4EsiLocationFacetPanelValidator;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class PanelVerificationSteps extends StepFramework {
	private static final String FIELD_IDENTIFIER = "fieldIdentifier";
	private static final String FIELD_VALUE = "fieldValue";

	private static final String MININGRESOURCE_RESOURCENAME = "resourceName";
	private static final String MININGRESOURCE_RESOURCEIDENTIFIER = "resourceIdentifier";
	private static final String MININGRESOURCE_QUANTITY = "quantity";
	private static final String MININGRESOURCE_VALUE = "value";

	private static final String DAILYEXTRACTIONRESOURCESCONTAINER_NAMETYPE_LABEL = "nameTypeLabel";
	private static final String DAILYEXTRACTIONRESOURCESCONTAINER_QUANTITY_LABEL = "quantityLabel";
	private static final String DAILYEXTRACTIONRESOURCESCONTAINER_VALUE_LABEL = "valueLabel";

	public PanelVerificationSteps( final NeoComWorld world ) {
		super(world);
	}

	@And("check a action bar {string} with the next contents")
	public void checkAActionBarWithNextContents( final String actionBarType, final List<Map<String, String>> cucumberTable ) {
		switch (actionBarType) {
			case "MiningActionBar":
				Assert.assertTrue(this.validateMiningActionBar(this.world.getActionBar(), cucumberTable));
				break;
			default:
				throw new NullPointerException("The panel type is not defined on the list of registered verifiers.");
		}
	}

	@And("check a {string} panel with the next contents")
	public void checkAPanelWithTheNextContents( final String panelTypeName, final List<Map<String, String>> cucumberTable ) {
		View panel;
		switch (panelTypeName) {
			case "MiningResource":
				panel = this.searchFirstPanelByClass(Resource.class, this.world.getHeaderContainer());
				Assert.assertNotNull(panel);
				Assert.assertTrue(this.validateMiningResourcePanel(panel, cucumberTable));
				break;
			case "DailyExtractionResourcesContainer":
				panel = this.searchFirstPanelByClass(DailyExtractionResourcesContainer.class, this.world.getHeaderContainer());
				Assert.assertNotNull(panel);
				Assert.assertTrue(this.validateDailyExtractionResourcesContainerPanel(panel, cucumberTable));
				break;
			default:
				throw new NullPointerException("The panel type is not defined on the list of registered verifiers.");
		}
	}

	private boolean validateMiningActionBar( final View actionBar, final List<Map<String, String>> cucumberTable ) {
		Assert.assertNotNull(actionBar);
		for (Map<String, String> row : cucumberTable) {
			TextView field;
			switch (row.get(FIELD_IDENTIFIER)) {
				case "titleTopLeft":
					field = actionBar.findViewById(R.id.toolBarTitleLeft);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case "subtitle":
					field = actionBar.findViewById(R.id.toolBarSubTitle);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
			}
		}
		return false;
	}

	private boolean validateMiningResourcePanel( final View panel, final List<Map<String, String>> cucumberTable ) {
		Assert.assertNotNull(panel);
		for (Map<String, String> row : cucumberTable) {
			TextView field;
			switch (row.get(FIELD_IDENTIFIER)) {
				case MININGRESOURCE_RESOURCENAME:
					field = panel.findViewById(R.id.resourceName);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case MININGRESOURCE_RESOURCEIDENTIFIER:
					field = panel.findViewById(R.id.resourceIdentifier);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case MININGRESOURCE_QUANTITY:
					field = panel.findViewById(R.id.quantity);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case MININGRESOURCE_VALUE:
					field = panel.findViewById(R.id.value);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
			}
		}
		return false;
	}

	private boolean validateDailyExtractionResourcesContainerPanel( final View panel,
	                                                                final List<Map<String, String>> cucumberTable ) {
		Assert.assertNotNull(panel);
		for (Map<String, String> row : cucumberTable) {
			TextView field;
			switch (row.get(FIELD_IDENTIFIER)) {
				case DAILYEXTRACTIONRESOURCESCONTAINER_NAMETYPE_LABEL:
					field = panel.findViewById(R.id.nameTypeLabel);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case DAILYEXTRACTIONRESOURCESCONTAINER_QUANTITY_LABEL:
					field = panel.findViewById(R.id.quantityLabel);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case DAILYEXTRACTIONRESOURCESCONTAINER_VALUE_LABEL:
					field = panel.findViewById(R.id.valueLabel);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
			}
		}
		return false;
	}

	@And("render a panel with {string} and the next contents")
	public void renderAPanelWithAndTheNextContents( final String renderClassName,
	                                                final List<Map<String, String>> cucumberTable ) {
		View panel;
		switch (renderClassName) {
			case "ResourceAggregatorContainer4EsiLocationFacetRender":
			case "ResourceAggregatorContainer4LocalTimeFacetRender":
				panel = this.searchFirstPanelByRenderClass(renderClassName, this.world.getDataSectionContainer());
				Assert.assertNotNull(panel);
				Assert.assertTrue(new ResourceAggregatorContainer4EsiLocationFacetPanelValidator.Builder()
						                  .withPanel(panel)
						                  .withCucumberTable(cucumberTable)
						                  .build()
						                  .validate());
				break;
			default:
				throw new NullPointerException("The panel type is not defined on the list of registered verifiers.");
		}
	}

	@Then("the header container has {string} panels")
	public void theHeaderContainerHasPanels( final String panelCount ) {
		final ViewGroup header = this.world.getHeaderContainer();
		Assert.assertEquals(Integer.parseInt(panelCount), header.getChildCount());
	}

	@Then("the data container has {string} panels")
	public void theDataContainerHasPanels( final String panelCount ) {
		final ListView data = this.world.getDataSectionContainer();
		Assert.assertEquals(Integer.parseInt(panelCount), data.getChildCount());
	}
}