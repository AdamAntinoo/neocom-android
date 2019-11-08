package org.dimensinfin.eveonline.neocom.app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.EventEmitter;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IEventReceiver;

/**
 * This class runs an initialisation task that is loaded as a lambda runnable at construction. When the right moment arrives
 * the task is run on a background thread and the execution report is the content of the model to be rendered on the Splash
 * activity.
 */
public class InitialisationTask implements ICollaboration {
	protected static Logger logger = LoggerFactory.getLogger(InitialisationTask.class);
	private static final ScheduledExecutorService initialisationExecutor = Executors.newScheduledThreadPool(1);

	public enum ETaskStatus {
		WAITING, SCHEDULED, EXECUTING, COMPLETED, EXCEPTION
	}

	private transient ExecutorService executor;
	private String taskName = "Initialisation Task";
	private ETaskStatus status = ETaskStatus.WAITING;
	private transient Callable task;
	private Instant startInstant = Instant.now();
	private List<NeoComError> exceptionReport = new ArrayList<>();
	private EventEmitter eventEmitter = new EventEmitter();

	// - C O N S T R U C T O R S
	private InitialisationTask() {}

	public String getTaskName() {
		return taskName + " " + this.status.name();
	}

	public ETaskStatus getStatus() {
		return status;
	}

	public Instant getStartInstant() {
		return startInstant;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	public InitialisationTask schedule( final int delay ) {
		logger.info("[POSTING SCHEDULE]");
		this.setStatus(ETaskStatus.SCHEDULED);
		initialisationExecutor.schedule(() -> {
			logger.info("[STARTING SCHEDULE]");
			this.setStatus(ETaskStatus.SCHEDULED);
			//			try {
			//				Thread.sleep((TimeUnit.SECONDS.toMillis(1)));
			//			} catch (InterruptedException ie) {
			//			}
			this.execute();
		}, 1, TimeUnit.SECONDS);
		return this;
	}

	/**
	 * Fires the task contained on this initialisation action to the global executor. Exceptions are reported to the caller for logging.
	 *
	 * @return this same instance so it can be inserted on the page.
	 */
	public InitialisationTask execute() {
		this.startInstant = Instant.now();
		logger.info("[EXECUTING]");
		this.setStatus(ETaskStatus.EXECUTING);
		final Future<List<NeoComError>> future = this.executor.submit(this.task);
		try {
			//			logger.info("[READING RESULT]");
			//			logger.info("[" + future.toString() + "]");
			//			if (null != future) {
			logger.info("[BLOCKING GET]");
			this.exceptionReport = future.get();
			//			logger.info("[COMPLETED GET]");
			//			}
			logger.info("[RESULT RECEIVED]");
			logger.info(this.exceptionReport.toString());
			logger.info("[" + this.exceptionReport.size() + "]");
			if ((null != this.exceptionReport) && (this.exceptionReport.size() > 0))
				this.status = ETaskStatus.EXCEPTION;
			else
				this.status = ETaskStatus.COMPLETED;
			this.eventEmitter.sendChangeEvent(EEvents.EVENT_REFRESHDATA.name());
			logger.info("[" + this.status + "]");
		} catch (InterruptedException ie) {
			this.exceptionReport.add(new NeoComError.Builder(ie)
					                         .withType(EErrorType.EXCEPTION)
					                         .withCode("neocom.error.initialization.task.interrupted.exception")
//					                         .withOrigin("InitialisationTask.execute")
					                         .build());
			this.status = ETaskStatus.EXCEPTION;
			this.eventEmitter.sendChangeEvent(EEvents.EVENT_REFRESHDATA.name());
		} catch (ExecutionException ee) {
			this.exceptionReport.add(new NeoComError.Builder(ee)
					                         .withType(EErrorType.EXCEPTION)
					                         .withCode("neocom.error.initialization.task.execution.exception")
//					                         .withOrigin("InitialisationTask.execute")
					                         .build());
			this.status = ETaskStatus.EXCEPTION;
			this.eventEmitter.sendChangeEvent(EEvents.EVENT_REFRESHDATA.name());
		}
		return this;
	}

	protected void setStatus( final ETaskStatus newStatus ) {
		this.status = newStatus;
		this.eventEmitter.sendChangeEvent(EEvents.EVENT_REFRESHDATA.name());
	}

//	public void addPropertyChangeListener( final PropertyChangeListener listener ) {
//		eventEmitter.addPropertyChangeListener(listener);
//	}

	public void addEventListener( final IEventReceiver listener ) {
		eventEmitter.addEventListener(listener);
	}

	// - B U I L D E R
	public static class Builder {
		private InitialisationTask onConstruction;

		public Builder() {
			this.onConstruction = new InitialisationTask();
		}

		public Builder withTaskName( final String taskName ) {
			this.onConstruction.taskName = taskName;
			return this;
		}

		public Builder withTask( final Callable task ) {
			this.onConstruction.task = task;
			return this;
		}

		public Builder withExecutor( final ExecutorService executor ) {
			this.onConstruction.executor = executor;
			return this;
		}

		public InitialisationTask build( final int delay ) {
			//			logger.info("[POSTING SCHEDULE]");
			//			initialisationExecutor.build(() -> {
			//				logger.info("[STARTING SCHEDULE]");
			//				this.onConstruction.setStatus(ETaskStatus.SCHEDULED);
			//				this.onConstruction.execute();
			//			}, Math.min(1, delay), TimeUnit.SECONDS);
			return this.onConstruction;
		}
	}
}
