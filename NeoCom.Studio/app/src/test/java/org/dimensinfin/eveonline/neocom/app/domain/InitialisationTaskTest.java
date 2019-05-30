package org.dimensinfin.eveonline.neocom.app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.exception.EErrorType;
import org.dimensinfin.eveonline.neocom.exception.NeoComError;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitialisationTaskTest {
	protected static Logger logger = LoggerFactory.getLogger(InitialisationTask.class);
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private static final Callable<List<NeoComError>> runOkTask = () -> {
		logger.info("[R-RUNNING TASK]");
		logger.info("[R-WAITING]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[R-END WAIT]");
		return new ArrayList<>();
	};
	private static final Callable<List<NeoComError>> runExceptionTask = () -> {
		logger.info("[R-RUNNING TASK]");
		logger.info("[R-WAITING]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[R-END WAIT]");
		final List<NeoComError> exceptionList = new ArrayList<>();
		exceptionList.add(new NeoComError.Builder(new NeoComRuntimeException("Test Exception Text."))
				                  .withType(EErrorType.RUNTIME)
				                  .withCode("neocom.error.initialization.runtime.exception.component.instantiation")
//				                  .withOrigin("SplashActionsDataSource.collaborate2Model")
				                  .build());
		return exceptionList;
	};

	@Test
	public void execute_emptyTaskOk() throws InterruptedException {
		logger.info("[T-STARTING TEST]");
		final InitialisationTask task = new InitialisationTask.Builder()
				                                .withTask(runOkTask)
				                                .withTaskName("TEST EMPTY")
				                                .withExecutor(executor)
				                                .build(1);
		logger.info("[T-AWAITING WAITING]");
		Assert.assertEquals("The initial status should be WAITING.", InitialisationTask.ETaskStatus.WAITING, task.getStatus());
		task.schedule(1);
		logger.info("[T-AWAITING SCHEDULE]");
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
		Assert.assertEquals("The status should be SCHEDULED.", InitialisationTask.ETaskStatus.SCHEDULED, task.getStatus());
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[T-AWAITING EXECUTION]");
		Assert.assertEquals("The status should be EXECUTING.", InitialisationTask.ETaskStatus.EXECUTING, task.getStatus());
		logger.info("[T-AWAITING COMPLETION]");
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
		Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
		logger.info("[T-COMPLETED WAIT]");
		Assert.assertEquals("The status should be COMPLETED.", InitialisationTask.ETaskStatus.COMPLETED, task.getStatus());
	}

	@Test
	public void execute_exceptionTask() throws InterruptedException {
		final InitialisationTask task = new InitialisationTask.Builder()
				                                .withTask(runExceptionTask)
				                                .withTaskName("TEST EMPTY")
				                                .withExecutor(executor)
				                                .build(1);

		Assert.assertEquals("The initial status should be WAITING.", InitialisationTask.ETaskStatus.WAITING, task.getStatus());
		//		task.build();
		Assert.assertEquals("The status should be SCHEDULED.", InitialisationTask.ETaskStatus.SCHEDULED, task.getStatus());
		logger.info("[AWAITING EXECUTION]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[COMPLETED WAIT]");
		Assert.assertEquals("The status should be EXECUTING.", InitialisationTask.ETaskStatus.EXECUTING, task.getStatus());
		logger.info("[AWAITING COMPLETION]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[COMPLETED WAIT]");
		Assert.assertEquals("The status should be EXCEPTION.", InitialisationTask.ETaskStatus.EXCEPTION, task.getStatus());
	}

	@Test
	public void execute_emptyTask_interrupted() throws InterruptedException, ExecutionException {
		final ScheduledFuture future = Mockito.mock(ScheduledFuture.class);
		Mockito.when(future.get()).thenThrow(InterruptedException.class);
		final InitialisationTask task = new InitialisationTask.Builder()
				                                .withTask(runOkTask)
				                                .withTaskName("TEST EMPTY")
				                                .withExecutor(executor)
				                                .build(1);

		Assert.assertEquals("The initial status should be WAITING.", InitialisationTask.ETaskStatus.WAITING, task.getStatus());
		//		task.build();
		Assert.assertEquals("The status should be SCHEDULED.", InitialisationTask.ETaskStatus.SCHEDULED, task.getStatus());
		logger.info("[AWAITING EXECUTION]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[COMPLETED WAIT]");
		Assert.assertEquals("The status should be EXECUTING.", InitialisationTask.ETaskStatus.EXECUTING, task.getStatus());
		logger.info("[AWAITING COMPLETION]");
		Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		logger.info("[COMPLETED WAIT]");
		Assert.assertEquals("The status should be COMPLETED.", InitialisationTask.ETaskStatus.COMPLETED, task.getStatus());
	}
}
