package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.exception.NeoComError;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

/**
 * This class runs an initialisation task that is loaded as a lambda runnable at construction. When the right moment arrives
 * the task is run on a background thread and the execution report is the content of the model to be rendered on the Splash
 * activity.
 */
public class InitialisationTask implements ICollaboration {
	private Callable task;
	private List<NeoComError> exceptionReport = new ArrayList<>();

	// - C O N S T R U C T O R S
	public InitialisationTask( final Callable task ) {
		this.task = task;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	public InitialisationTask execute() {
		Future<List<NeoComError>> future = AndroidGlobalDataManager.getSingleton().submitCall(task);
		Integer result = 0;
		try {
			this.exceptionReport = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return this;
	}
}
