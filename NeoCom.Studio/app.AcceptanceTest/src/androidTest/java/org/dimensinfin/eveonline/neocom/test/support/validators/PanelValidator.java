package org.dimensinfin.eveonline.neocom.test.support.validators;

import android.view.View;

import java.util.Map;
import java.util.Objects;

public abstract class PanelValidator implements IValidator {
	protected Map<String, String> rowData;
	protected View targetView;

	// - B U I L D E R
	protected static abstract class Builder<T extends PanelValidator, B extends PanelValidator.Builder> {
		protected B actualClassBuilder;

		public Builder() {
			this.actualClassBuilder = getActualBuilder();
		}

		protected abstract T getActual();

		protected abstract B getActualBuilder();

		public B withExpectedRowData( final Map<String, String> rowData ) {
			Objects.requireNonNull( rowData );
			this.getActual().rowData = rowData;
			return this.getActualBuilder();
		}

		public B withTargetView( final View targetView ) {
			Objects.requireNonNull( targetView );
			this.getActual().targetView = targetView;
			return this.getActualBuilder();
		}

		public T build() {
			Objects.requireNonNull( this.getActual().rowData );
			Objects.requireNonNull( this.getActual().targetView );
			return this.getActual();
		}
	}
}
