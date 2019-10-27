package org.dimensinfin.eveonline.neocom.app.utilities;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;

public class TaskSequencerTest {
	private static Logger logger = LoggerFactory.getLogger(AppVersion.class);
	private TaskSequencer task2test;
	private Activity activity;

	@Before
	public void setUp() throws Exception {
		this.activity = Mockito.mock(Activity.class);
		Mockito.doAnswer(( empty ) -> { return null; }).when(this.activity).runOnUiThread(Mockito.any());
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		this.task2test = new TaskSequencer.Builder()
				                 .withActivity(this.activity)
				                 .withContainer(container)
				                 .whenSuccessAction(() -> {
					                 logger.info("--> Success action.");
				                 })
				                 .whenExceptionAction(() -> {
					                 logger.info("--> Exception action.");
				                 })
				                 .build();
	}

	@Test
	public void builderComplete() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(activity)
				                                    .withContainer(container)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureA() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withContainer(container)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureB() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(activity)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureC() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(null)
				                                    .withContainer(container)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureD() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(activity)
				                                    .withContainer(null)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureE() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(activity)
				                                    .withContainer(container)
				                                    .whenSuccessAction(null)
				                                    .whenExceptionAction(() -> {
					                                    logger.info("--> Exception action.");
				                                    })
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureF() {
		final Activity activity = Mockito.mock(Activity.class);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer taskSequencer = new TaskSequencer.Builder()
				                                    .withActivity(activity)
				                                    .withContainer(container)
				                                    .whenSuccessAction(() -> {
					                                    logger.info("--> Success action.");
				                                    })
				                                    .whenExceptionAction(null)
				                                    .build();
		Assert.assertNotNull(taskSequencer);
	}

	@Test
	public void getterContract() {
		final Exception exception = Mockito.mock(Exception.class);
		Assert.assertNull(this.task2test.getException());
		this.task2test.setException(exception);
		Assert.assertNotNull(this.task2test.getException());

		Assert.assertFalse(this.task2test.isRetryable());
		this.task2test.setRetryable(true);
		Assert.assertTrue(this.task2test.isRetryable());
	}

	@Test
	public void addTask() {
		final ChainTask task1 = Mockito.mock(ChainTask.class);
		Assert.assertEquals(1, this.task2test.addTask(task1));
		final ChainTask task2 = Mockito.mock(ChainTask.class);
		Assert.assertEquals(2, this.task2test.addTask(task2));
	}

	@Test
	public void addTaskSameTask() {
		final ChainTask task = Mockito.mock(ChainTask.class);
		Assert.assertEquals(1, this.task2test.addTask(task));
		Assert.assertEquals(1, this.task2test.addTask(task));
	}

	@Test
	public void run() {
		final ChainTask task = Mockito.mock(ChainTask.class);
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		Mockito.when(taskModel.getMessage()).thenReturn("-MESSAGE-");
		Mockito.when(task.getTaskModel()).thenReturn(taskModel);
		final TaskSequencer sequencerSpy = Mockito.spy(task2test);
		Mockito.doAnswer(( empty ) -> { return null; }).when(sequencerSpy).launch(Mockito.any(ChainTask.class));
		Assert.assertEquals(1, task2test.addTask(task));

		sequencerSpy.run();

		Mockito.verify(sequencerSpy, Mockito.times(1)).launch(Mockito.any(ChainTask.class));
	}

	@Test
	public void runCompletion() {
		final ChainTask task = Mockito.mock(ChainTask.class);
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		Mockito.when(taskModel.getMessage()).thenReturn("-MESSAGE-");
		Mockito.when(task.getTaskModel()).thenReturn(taskModel);
		final TaskSequencer sequencerSpy = Mockito.spy(task2test);
		Mockito.doAnswer(( empty ) -> { return null; }).when(sequencerSpy).launch(Mockito.any(ChainTask.class));
		Assert.assertEquals(1, task2test.addTask(task));

		sequencerSpy.run();
		Mockito.verify(sequencerSpy, Mockito.times(1)).launch(Mockito.any(ChainTask.class));
		sequencerSpy.run();
		Mockito.verify(sequencerSpy, Mockito.times(1)).completeWithSuccess();
	}

	@Test
	public void completeWithException() {
		final Activity activitySpy = Mockito.spy(activity);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer sequencer = new TaskSequencer.Builder()
				                                .withActivity(activitySpy)
				                                .withContainer(container)
				                                .whenSuccessAction(() -> {
					                                logger.info("--> Success action.");
				                                })
				                                .whenExceptionAction(() -> {
					                                logger.info("--> Exception action.");
				                                })
				                                .build();
		sequencer.completeWithException();
		Mockito.verify(activitySpy, Mockito.times(1)).runOnUiThread(Mockito.any());
	}

	@Test
	public void completeWithSuccess() {
		final Activity activitySpy = Mockito.spy(activity);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer sequencer = new TaskSequencer.Builder()
				                                .withActivity(activitySpy)
				                                .withContainer(container)
				                                .whenSuccessAction(() -> {
					                                logger.info("--> Success action.");
				                                })
				                                .whenExceptionAction(() -> {
					                                logger.info("--> Exception action.");
				                                })
				                                .build();
		sequencer.completeWithSuccess();
		Mockito.verify(activitySpy, Mockito.times(1)).runOnUiThread(Mockito.any(Runnable.class));
	}

	@Test
	public void launchSuccess() throws InterruptedException {
		final Activity activitySpy = Mockito.spy(activity);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer sequencer = new TaskSequencer.Builder()
				                                .withActivity(activitySpy)
				                                .withContainer(container)
				                                .whenSuccessAction(() -> {
					                                logger.info("--> Success action.");
				                                })
				                                .whenExceptionAction(() -> {
					                                logger.info("--> Exception action.");
				                                })
				                                .build();
		final ChainTask task = Mockito.mock(ChainTask.class);
		final ChainTask taskSpy = Mockito.spy(task);
		Mockito.when(taskSpy.run()).thenReturn(true);
		sequencer.launch(taskSpy);
		Thread.sleep(1000);
		Mockito.verify(taskSpy, Mockito.times(1)).setCallBack(Mockito.any());
		Mockito.verify(taskSpy, Mockito.times(1)).run();
		Mockito.verify(taskSpy, Mockito.times(1)).runCallback();
		Mockito.verify(activitySpy, Mockito.times(2)).runOnUiThread(Mockito.any(Runnable.class));
	}
	@Test
	public void launchFailure() throws InterruptedException {
		final Activity activitySpy = Mockito.spy(activity);
		final LinearContainer container = Mockito.mock(LinearContainer.class);
		final TaskSequencer sequencer = new TaskSequencer.Builder()
				                                .withActivity(activitySpy)
				                                .withContainer(container)
				                                .whenSuccessAction(() -> {
					                                logger.info("--> Success action.");
				                                })
				                                .whenExceptionAction(() -> {
					                                logger.info("--> Exception action.");
				                                })
				                                .build();
		final ChainTask task = Mockito.mock(ChainTask.class);
		final ChainTask taskSpy = Mockito.spy(task);
		Mockito.when(taskSpy.run()).thenReturn(false);
		sequencer.launch(taskSpy);
		Thread.sleep(1000);
		Mockito.verify(taskSpy, Mockito.times(1)).setCallBack(Mockito.any());
		Mockito.verify(taskSpy, Mockito.times(1)).run();
		Mockito.verify(activitySpy, Mockito.times(3)).runOnUiThread(Mockito.any(Runnable.class));
	}
}