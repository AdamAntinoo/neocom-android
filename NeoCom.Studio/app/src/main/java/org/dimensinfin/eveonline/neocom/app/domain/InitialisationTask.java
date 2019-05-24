package org.dimensinfin.eveonline.neocom.app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.core.EEvents;
import org.dimensinfin.eveonline.neocom.core.EventEmitter;
import org.dimensinfin.eveonline.neocom.exception.EErrorType;
import org.dimensinfin.eveonline.neocom.exception.NeoComError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class runs an initialisation task that is loaded as a lambda runnable at construction. When the right moment arrives
 * the task is run on a background thread and the execution report is the content of the model to be rendered on the Splash
 * activity.
 */
public class InitialisationTask implements ICollaboration {
	protected static Logger logger = LoggerFactory.getLogger(InitialisationTask.class);
	private static final ScheduledExecutorService initialisationExecutor = Executors.newScheduledThreadPool(2);

	public enum ETaskStatus {
		WAITING, SCHEDULED, EXECUTING, COMPLETED, EXCEPTION
	}

	private String taskName = "Initialisation Task";
	private ETaskStatus status = ETaskStatus.WAITING;
	private Callable task;
	private List<NeoComError> exceptionReport = new ArrayList<>();
	private EventEmitter eventEmitter = new EventEmitter();

	// - C O N S T R U C T O R S
	public InitialisationTask( final Callable task ) {
		//		this.taskName=taskName;
		this.task = task;
	}

	public String getTaskName() {
		return taskName + " " + this.status.name();
	}

	public ETaskStatus getStatus() {
		return status;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	public InitialisationTask schedule() {
		logger.info("[POSTING]");
		this.status = ETaskStatus.SCHEDULED;
		this.eventEmitter.sendChangeEvent(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name());
		initialisationExecutor.schedule(() -> {this.execute();}, 2, TimeUnit.SECONDS);
		return this;
	}

	/**
	 * Fires the task contained on this initialisation action to the global executor. Exceptions are reported to the caller for logging.
	 *
	 * @return this same instance so it can be inserted on the page.
	 */
	public InitialisationTask execute() {
		this.status = ETaskStatus.EXECUTING;
		this.eventEmitter.sendChangeEvent(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name());
		logger.info("[EXECUTING]");
		final ScheduledFuture<List<NeoComError>> future = initialisationExecutor.schedule(this.task, 1, TimeUnit.SECONDS);
		try {
			logger.info("[READING RESULT]");
			logger.info("[" + future.toString() + "]");
			if (null != future) {
				logger.info("[BLOCKING GET]");
				this.exceptionReport = future.get();
				logger.info("[COMPLETED GET]");
			}
			logger.info("[RESULT RECEIVED]");
			logger.info(this.exceptionReport.toString());
			logger.info("[" + this.exceptionReport.size() + "]");
			if ((null != this.exceptionReport) && (this.exceptionReport.size() > 0))
				this.status = ETaskStatus.EXCEPTION;
			else
				this.status = ETaskStatus.COMPLETED;
			this.eventEmitter.sendChangeEvent(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name());
			logger.info("[" + this.status + "]");
		} catch (InterruptedException ie) {
			this.exceptionReport.add(new NeoComError.Builder(ie)
					                         .withType(EErrorType.EXCEPTION)
					                         .withCode("neocom.error.initialization.task.interrupted.exception")
					                         .withOrigin("InitialisationTask.execute")
					                         .build());
			this.status = ETaskStatus.EXCEPTION;
			this.eventEmitter.sendChangeEvent(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name());
		} catch (ExecutionException ee) {
			this.exceptionReport.add(new NeoComError.Builder(ee)
					                         .withType(EErrorType.EXCEPTION)
					                         .withCode("neocom.error.initialization.task.execution.exception")
					                         .withOrigin("InitialisationTask.execute")
					                         .build());
			this.status = ETaskStatus.EXCEPTION;
			this.eventEmitter.sendChangeEvent(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name());
		}
		return this;
	}
}
