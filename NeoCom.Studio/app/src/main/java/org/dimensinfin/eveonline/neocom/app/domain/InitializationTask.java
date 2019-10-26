package org.dimensinfin.eveonline.neocom.app.domain;

import org.dimensinfin.eveonline.neocom.model.NeoComNode;

public class InitializationTask extends NeoComNode {
	public enum InitializationTaskState {
		RUNNING, COMPLETED, EXCEPTION;
	}

	private String message;
	private InitializationTaskState state = InitializationTaskState.RUNNING;
	private boolean retryable = true;
	private Exception exception;

	public InitializationTaskState getState() {
		return this.state;
	}

	public String getMessage() {
		return this.message;
	}

	public InitializationTask setState( final InitializationTaskState state ) {
		this.state = state;
		return this;
	}

	// - B U I L D E R
	public static class Builder {
		private InitializationTask onConstruction;

		public Builder() {
			this.onConstruction = new InitializationTask();
		}

		public InitializationTask.Builder withMessage( final String message ) {
			this.onConstruction.message = message;
			return this;
		}

		public InitializationTask.Builder withState( final InitializationTaskState state ) {
			this.onConstruction.state = state;
			return this;
		}

		public InitializationTask.Builder withRetryable( final boolean retryable ) {
			this.onConstruction.retryable = retryable;
			return this;
		}

		public InitializationTask.Builder optionalException( final Exception exception ) {
			this.onConstruction.exception = exception;
			return this;
		}

		public InitializationTask build() {
			return this.onConstruction;
		}
	}
}