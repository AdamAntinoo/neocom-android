package org.dimensinfin.eveonline.neocom.app.utilities;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;

public class ChainTask {
	protected static Logger logger = LoggerFactory.getLogger(ChainTask.class);
	private InitializationTask taskModel;
	private Exception exception;
	private Callable<Boolean> runCode;
	private Runnable callBack;
	private int sleepTime = 0;

	private ChainTask() {}

	public InitializationTask getTaskModel() {
		return this.taskModel;
	}

	public Exception getException() {
		return this.exception;
	}

	public boolean isRetryable() {
		return this.taskModel.isRetryable();
	}

	public ChainTask setException( final Exception exception ) {
		this.exception = exception;
		return this;
	}

	public ChainTask setCallBack( final Runnable callBack ) {
		this.callBack = callBack;
		return this;
	}

	public void runCallback() {
		this.callBack.run();
	}

	public boolean run() {
		try {
			final Boolean result = this.runCode.call();
			logger.info("--[ChainTask.run]> run lambda result: {}", result);
			this.taskModel.setState(InitializationTask.InitializationTaskState.COMPLETED);
			Thread.sleep(TimeUnit.SECONDS.toMillis(Math.min(this.sleepTime, 1)));
			return result;
		} catch (final Exception e) {
			logger.info("--[ChainTask.run]> run lambda exception: {}", e.getMessage());
			this.setException(e);
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

		public ChainTask.Builder optionalSleepTime( final int sleepTime ) {
			this.onConstruction.sleepTime = sleepTime;
			return this;
		}

		public ChainTask build() {
			Objects.requireNonNull(this.onConstruction.taskModel);
			Objects.requireNonNull(this.onConstruction.runCode);
			return this.onConstruction;
		}
	}
}
