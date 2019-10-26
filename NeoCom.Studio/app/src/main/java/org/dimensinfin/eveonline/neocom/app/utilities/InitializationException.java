package org.dimensinfin.eveonline.neocom.app.utilities;

import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import java.util.Objects;

public class InitializationException extends NeoComNode {
	private Exception exception;
	private boolean retryable = false;

	public String getExceptionMessage() {
		return this.exception.getMessage();
	}

	public boolean isRetryable() {
		return this.retryable;
	}

	// - B U I L D E R
	public static class Builder {
		private InitializationException onConstruction;

		public Builder() {
			this.onConstruction = new InitializationException();
		}

		public InitializationException.Builder withException( final Exception exception ) {
			Objects.requireNonNull(exception);
			this.onConstruction.exception = exception;
			return this;
		}

		public InitializationException.Builder withRetryable( final boolean retryable ) {
			this.onConstruction.retryable = retryable;
			return this;
		}

		public InitializationException build() {
			Objects.requireNonNull(this.onConstruction.exception);
			return this.onConstruction;
		}
	}
}