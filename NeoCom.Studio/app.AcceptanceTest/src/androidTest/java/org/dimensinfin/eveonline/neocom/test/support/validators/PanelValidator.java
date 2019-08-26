package org.dimensinfin.eveonline.neocom.test.support.validators;

import android.view.View;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class PanelValidator implements IValidator {
	protected static final String FIELD_IDENTIFIER = "fieldIdentifier";
	protected static final String FIELD_VALUE = "fieldValue";
	protected View panel;
	protected List<Map<String, String>> cucumberTable;

	// - B U I L D E R
	public abstract static class Builder<T extends PanelValidator, B extends PanelValidator.Builder> {
		protected B actualClassBuilder;

		public Builder() {
			this.actualClassBuilder = getActualBuilder();
		}

		protected abstract T getActual();

		protected abstract B getActualBuilder();

		public T.Builder withPanel( final View panel ) {
			this.getActual().panel = panel;
			return this;
		}

		public T.Builder withCucumberTable( final List<Map<String, String>> cucumberTable ) {
			this.getActual().cucumberTable = cucumberTable;
			return this;
		}

		public T build() {
			Objects.requireNonNull(this.getActual().panel);
			return this.getActual();
		}
	}
}