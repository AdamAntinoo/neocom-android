package org.dimensinfin.eveonline.neocom.app.utilities;

import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;

import java.util.Objects;
import java.util.concurrent.Callable;

public class ChainTask {
	private InitializationTask taskModel;
	private Exception exception;
	private Callable<Boolean> runCode;
	private Runnable callBack;

	public InitializationTask getTaskModel() {
		return this.taskModel;
	}

	public Exception getException() {
		return this.exception;
	}

	public boolean isRetryable() {
		return this.taskModel.isRetryable();
	}

	public ChainTask setCallBack( final Runnable callBack ) {
		this.callBack = callBack;
		return this;
	}

//	public void runException() {
//		this.taskModel.setState(InitializationTask.InitializationTaskState.EXCEPTION);
//	}
//
//	public void runCompletion() {
//		this.taskModel.setState(InitializationTask.InitializationTaskState.COMPLETED);
//	}

	public void runCallback() {
		this.callBack.run();
	}

	public boolean run() {
		try {
			final Boolean result = this.runCode.call();
			this.taskModel.setState(InitializationTask.InitializationTaskState.COMPLETED);
			return result;
		} catch (final Exception e) {
			this.exception = e;
			this.taskModel.setState(InitializationTask.InitializationTaskState.EXCEPTION);
			return false;
		}
	}

	// - B U I L D E R
	public static class Builder {
		private ChainTask onConstruction;

		public Builder() {
			this.onConstruction = new ChainTask();
		}

		public ChainTask.Builder withTaskModel( final InitializationTask taskModel ) {
			Objects.requireNonNull(taskModel);
			this.onConstruction.taskModel = taskModel;
			return this;
		}

		public ChainTask.Builder withRunTask( final Callable<Boolean> runCode ) {
			Objects.requireNonNull(runCode);
			this.onConstruction.runCode = runCode;
			return this;
		}

		public ChainTask.Builder withCallBack( final Runnable callBack ) {
			Objects.requireNonNull(callBack);
			this.onConstruction.callBack = callBack;
			return this;
		}

		public ChainTask build() {
			Objects.requireNonNull(this.onConstruction.taskModel);
			Objects.requireNonNull(this.onConstruction.runCode);
			return this.onConstruction;
		}
	}
}