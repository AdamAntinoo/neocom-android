package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Application;
import android.content.Context;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.exception.NeoComError;

/**
 * This class runs an initialisation task that is loaded as a lambda runnable at construction. When the right moment arrives
 * the task is run on a background thread and the execution report is the content of the model to be rendered on the Splash
 * activity.
 */
public class InitialisationTask implements ICollaboration {
	private Callable task;
	private Context applicationContext;
	private List<NeoComError> exceptionReport = new ArrayList<>();
	ExecutorService service =  Executors.newSingleThreadExecutor();

//	public InitialisationTask( final Runnable task ) {
//		this.task = task;
//	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	public void execute () {
		Future<Integer> future = service.submit(task);
		Integer result=0;
		try {
			result = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	// - B U I L D E R
	public static class Builder {
		private InitialisationTask onConstruction;

		public Builder() {
			this.onConstruction = new InitialisationTask();
		}

		public Builder withApplicationContext( final Context applicationContext ) {
			this.onConstruction.applicationContext = applicationContext;
			return this;
		}

		public Builder withTask( final Callable task ) {
			this.onConstruction.task = task;
			return this;
		}

		public InitialisationTask build() {
//			Objects.requireNonNull(this.onConstruction.applicationContext);
			Objects.requireNonNull(this.onConstruction.task);
			return this.onConstruction;
		}
	}
}
