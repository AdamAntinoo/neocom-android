package org.dimensinfin.eveonline.neocom.app.domain;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.domain.NeoComNode;

public class RetryButton extends NeoComNode {
	private String message;

	private RetryButton() {}

	public String getMessage() {
		return this.message;
	}

	// - B U I L D E R
	public static class Builder {
		private RetryButton onConstruction;

		public Builder() {
			this.onConstruction = new RetryButton();
		}

		public RetryButton.Builder withMessage( final String message ) {
			Objects.requireNonNull( message );
			this.onConstruction.message = message;
			return this;
		}

		public RetryButton build() {
			return this.onConstruction;
		}
	}
}
