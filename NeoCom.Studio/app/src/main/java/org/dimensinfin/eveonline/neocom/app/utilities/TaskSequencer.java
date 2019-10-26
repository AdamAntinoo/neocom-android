package org.dimensinfin.eveonline.neocom.app.utilities;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskSequencer {
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
	private Map<ChainTask, Boolean> taskList = new HashMap<ChainTask, Boolean>();
	private Activity activity;
	private Runnable whenExceptionAction;
	private Runnable whenSuccessAction;
	private

	public int addTask( final ChainTask task ) {
		this.taskList.put(task, false);
		return this.taskList.size();
	}

	public void run() {
		for (ChainTask task : this.taskList.keySet()) {
			if (!this.taskList.get(task)) {
				this.launch(task);
				return;
			}
		}
		this.completeWithSuccess();
	}

	private void launch( final ChainTask task ) {
		task.setCallBack(() -> this.run());
		this.activity.runOnUiThread(() -> task.runRegistration());
		this.backgroundExecutor.submit(() -> {
			final boolean result = task.run();
			if (!result) {
				this.activity.runOnUiThread(() -> task.runException());
				this.completeWithException();
			}
			this.activity.runOnUiThread(() -> task.runCompletion());
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
			return this.onConstruction;
		}
	}
}