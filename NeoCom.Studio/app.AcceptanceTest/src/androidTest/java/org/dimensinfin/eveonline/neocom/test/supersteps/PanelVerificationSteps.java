package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.view.View;
import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;

public class PanelVerificationSteps {
	private static final String FIELD_IDENTIFIER = "fieldIdentifier";
	private static final String FIELD_VALUE = "fieldValue";

	protected NeoComWorld world;

	public PanelVerificationSteps( final NeoComWorld world ) {
		this.world = world;
	}

	@And("check a {string} with next contents")
	public void checkAWithNextContents( final String panelTypeName, final List<Map<String, String>> cucumberTable ) {
		switch (panelTypeName) {
			case "MiningActionBar":
				Assert.assertTrue(this.validateMiningActionBar(this.world.getFragment().generateActionBarView(),
				                                               cucumberTable));
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
}