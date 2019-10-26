package org.dimensinfin.eveonline.neocom.app.utilities;

import java.util.Objects;

public class ChainTask {
	private Runnable callBack;
	private Runnable runCode;

	public Runnable getCallBack() {
		return this.callBack;
	}

	public ChainTask setCallBack( final Runnable callBack ) {
		this.callBack = callBack;
		return this;
	}

	public void runRegistration() {

	}

	public void runException() {}

	public void runCompletion() {}

	public boolean run() {
		return true;
	}

	// - B U I L D E R
	public static class Builder {
		private ChainTask onConstruction;

		public Builder() {
			this.onConstruction = new ChainTask();
		}

		public ChainTask.Builder withCallBack( final Runnable callBack ) {
			Objects.requireNonNull(callBack);
			this.onConstruction.callBack = callBack;
			return this;
		}

		public ChainTask.Builder withRunTask( final Runnable runCode ) {
			Objects.requireNonNull(runCode);
			this.onConstruction.runCode = runCode;
			return this;
		}

		public ChainTask build() {
			Objects.requireNonNull(this.onConstruction.runCode);
			return this.onConstruction;
		}
	}
}