package org.dimensinfin.eveonline.neocom.utilities;

import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Callable;

public class ChainTaskTest {
	private ChainTask task2Test;

	@Before
	public void setUp() throws Exception {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final Callable runcode = Mockito.mock(Callable.class);
		final Runnable callback = Mockito.mock(Runnable.class);
		this.task2Test = new ChainTask.Builder()
				                 .withTaskModel(taskModel)
				                 .withRunTask(runcode)
				                 .build();
	}

	@Test
	public void builderComplete() {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final Callable runcode = Mockito.mock(Callable.class);
		final Runnable callback = Mockito.mock(Runnable.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModel)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertNotNull(task);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureA() {
		final Callable runcode = Mockito.mock(Callable.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertNotNull(task);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureB() {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModel)
				                       .build();
		Assert.assertNotNull(task);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureD() {
		final Callable runcode = Mockito.mock(Callable.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(null)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertNotNull(task);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureE() {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModel)
				                       .withRunTask(null)
				                       .build();
		Assert.assertNotNull(task);
	}

	@Test
	public void gettersContract() {
		Assert.assertNotNull(this.task2Test.getTaskModel());
		Assert.assertNull(this.task2Test.getException());
		final Exception exception = Mockito.mock(Exception.class);
		this.task2Test.setException(exception);
		Assert.assertNotNull(this.task2Test.getException());
	}

	@Test
	public void isRetryable() {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		Mockito.when(taskModel.isRetryable()).thenReturn(true);
		final Callable runcode = Mockito.mock(Callable.class);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModel)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertNotNull(task);
		Assert.assertTrue(task.isRetryable());
	}

	@Test
	public void settersContract() {
		final Runnable callback = Mockito.mock(Runnable.class);
		final ChainTask task = this.task2Test.setCallBack(callback);
		Assert.assertNotNull(task);
	}

	@Test
	public void runCallback() {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final Callable runcode = Mockito.mock(Callable.class);
		final Runnable callback = Mockito.mock(Runnable.class);
		final Runnable callbackSpy = Mockito.spy(callback);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModel)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertNotNull(task);
		task.setCallBack(callbackSpy);

		task.runCallback();
		Mockito.verify(callbackSpy, Mockito.times(1)).run();
	}

	@Test
	public void runSuccess() throws Exception {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final InitializationTask taskModelSpy = Mockito.spy(taskModel);
		final Callable runcode = Mockito.mock(Callable.class);
		Mockito.when(runcode.call()).thenReturn(true);
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModelSpy)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertTrue(task.run());
		Mockito.when(runcode.call()).thenReturn(false);
		Assert.assertFalse(task.run());
		Mockito.verify(taskModelSpy, Mockito.times(2))
		       .setState(Mockito.any(InitializationTask.InitializationTaskState.class));
	}

	@Test
	public void runException() throws Exception {
		final InitializationTask taskModel = Mockito.mock(InitializationTask.class);
		final InitializationTask taskModelSpy = Mockito.spy(taskModel);
		final Callable runcode = Mockito.mock(Callable.class);
		Mockito.when(runcode.call()).thenThrow(new Exception());
		final ChainTask task = new ChainTask.Builder()
				                       .withTaskModel(taskModelSpy)
				                       .withRunTask(runcode)
				                       .build();
		Assert.assertFalse(task.run());
		Assert.assertNotNull(task.getException());
		Mockito.verify(taskModelSpy, Mockito.times(1))
		       .setState(Mockito.any(InitializationTask.InitializationTaskState.class));
	}
}
