package org.dimensinfin.eveonline.neocom.test.support.validators;

import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.junit.Assert;

import java.util.Map;

public class ResourceAggregatorContainer4EsiLocationFacetPanelValidator extends PanelValidator {
	private static final String SYSTEM_NAME = "systemName";
	private static final String EXTRACTION_VALUE = "extractionValue";

	@Override
	public boolean validate() {
		Assert.assertNotNull(this.panel);
		for (Map<String, String> row : this.cucumberTable) {
			TextView field;
			switch (row.get(FIELD_IDENTIFIER)) {
				case SYSTEM_NAME:
					field = panel.findViewById(R.id.systemName);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
				case EXTRACTION_VALUE:
					field = panel.findViewById(R.id.extractionValue);
					Assert.assertEquals(row.get(FIELD_VALUE), field.getText());
					return true;
			}
		}
		return false;
	}

	// - B U I L D E R
	public static class Builder extends PanelValidator.Builder<ResourceAggregatorContainer4EsiLocationFacetPanelValidator,
			                                                          ResourceAggregatorContainer4EsiLocationFacetPanelValidator.Builder> {
		private ResourceAggregatorContainer4EsiLocationFacetPanelValidator onConstruction;

		@Override
		protected ResourceAggregatorContainer4EsiLocationFacetPanelValidator getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new ResourceAggregatorContainer4EsiLocationFacetPanelValidator();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}
	}
}