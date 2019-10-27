package org.dimensinfin.eveonline.neocom.app.utilities;

import android.app.Activity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;

public class TaskSequencer {
	protected static Logger logger = LoggerFactory.getLogger(TaskSequencer.class);
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
	private Map<ChainTask, Boolean> taskList = new LinkedHashMap<ChainTask, Boolean>();
	private Activity activity;
	private LinearContainer container;
	private Runnable whenExceptionAction;
	private Runnable whenSuccessAction;
	private Exception exception;
	private boolean retryable;

	private TaskSequencer() { }

	public Exception getException() {
		return this.exception;
	}

	public boolean isRetryable() {
		return this.retryable;
	}

	public TaskSequencer setException( final Exception exception ) {
		this.exception = exception;
		return this;
	}

	public TaskSequencer setRetryable( final boolean retryable ) {
		this.retryable = retryable;
		return this;
	}

	public int addTask( final ChainTask task ) {
		this.taskList.put(task, false);
		return this.taskList.size();
	}

	/**
	 * Search for the next task that has a false on their execution flag. Run only until the first match and then until all
	 * tasks have completed.
	 */
	public void run() {
		logger.info("--[TaskSequencer.run]> Sequencer run iteration: tasks ", this.taskList.size());
		for (ChainTask task : this.taskList.keySet()) {
			if (!this.taskList.get(task)) {
				logger.info("--[TaskSequencer.run]> launching task: {}", task.getTaskModel().getMessage());
				this.launch(task);
				this.taskList.put(task, true);
				return; // Completes this run sequence.
			}
		}
		this.completeWithSuccess(); // Only called when the list is completed successfully.
	}

	public void stop() {
		this.taskList.clear(); // Clear the list to stoop processing more actions.
	}

	/**
	 * This is the method that executes the different Task steps. If the final step completes successfully then the task should
	 * be marked as completed and the execution completes.
	 * During the execution there are other actions to be executed on the UI thread to change the render state.
	 * The first step is to register on the task the callback to use when completed.
	 */
	protected void launch( final ChainTask task ) {
		task.setCallBack(() -> this.run()); // The callback to fire the execution of the next task.
		this.activity.runOnUiThread(() -> this.container.addModel(task.getTaskModel())); // Register the model on the view.
		this.backgroundExecutor.submit(() -> { // Rin the task code in another thread.
			final boolean result = task.run();
			if (!result) {
				this.setException(task.getException());
				this.setRetryable(task.isRetryable());
				this.activity.runOnUiThread(
						() -> this.container.needsUpdate()); // Update the render model to the exception result.
				this.completeWithException();
				return;
			}
			this.activity.runOnUiThread(() -> this.container.needsUpdate()); // Update the render model to the completion result.
			task.runCallback();
		});
	}

	public void completeWithException() {
		this.activity.runOnUiThread(() -> this.whenExceptionAction.run());
	}

	public void completeWithSuccess() {
		this.activity.runOnUiThread(() -> this.whenSuccessAction.run());
	}

	// - B U I L D E R
	public static class Builder {
		private TaskSequencer onConstruction;

		public Builder() {
			this.onConstruction = new TaskSequencer();
		}

		public TaskSequencer.Builder withActivity( final Activity activity ) {
			Objects.requireNonNull(activity);
			this.onConstruction.activity = activity;
			return this;
		}

		public TaskSequencer.Builder withContainer( final LinearContainer container ) {
			Objects.requireNonNull(container);
			this.onConstruction.container = container;
			return this;
		}

		public TaskSequencer.Builder whenExceptionAction( final Runnable whenExceptionAction ) {
			Objects.requireNonNull(whenExceptionAction);
			this.onConstruction.whenExceptionAction = whenExceptionAction;
			return this;
		}

		public TaskSequencer.Builder whenSuccessAction( final Runnable whenSuccessAction ) {
			Objects.requireNonNull(whenSuccessAction);
			this.onConstruction.whenSuccessAction = whenSuccessAction;
			return this;
		}

		public TaskSequencer build() {
			Objects.requireNonNull(this.onConstruction.activity);
			Objects.requireNonNull(this.onConstruction.container);
			return this.onConstruction;
		}
	}
}