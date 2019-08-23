package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.view.View;
import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.StepFramework;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;

public class PanelVerificationSteps extends StepFramework {
	private static final String FIELD_IDENTIFIER = "fieldIdentifier";
	private static final String FIELD_VALUE = "fieldValue";

	private static final String MININGRESOURCE_RESOURCENAME = "resourceName";
	private static final String MININGRESOURCE_RESOURCEIDENTIFIER = "resourceIdentifier";
	private static final String MININGRESOURCE_QUANTITY = "quantity";
	private static final String MININGRESOURCE_VALUE = "value";

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
		switch (panelTypeName) {
			case "MiningResource":
				final View panel = this.searchFirstPanelByClass(Resource.class, this.world.getHeaderContainer());
				Assert.assertNotNull(panel);
				Assert.assertTrue(this.validateMiningResourcePanel(panel, cucumberTable));
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
}