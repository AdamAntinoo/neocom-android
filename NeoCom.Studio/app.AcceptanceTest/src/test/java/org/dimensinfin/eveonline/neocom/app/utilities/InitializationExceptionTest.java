package org.dimensinfin.eveonline.neocom.app.utilities;

import org.junit.Assert;
import org.junit.Test;

public class InitializationExceptionTest {
	@Test
	public void builderComplete() {
		final InitializationException exception = new InitializationException.Builder()
				                                          .withException(new Exception())
				                                          .withRetryable(true)
				                                          .build();
		Assert.assertNotNull(exception);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureA() {
		final InitializationException exception = new InitializationException.Builder()
				                                          .withRetryable(true)
				                                          .build();
		Assert.assertNotNull(exception);
	}

	@Test(expected = NullPointerException.class)
	public void builderFailureC() {
		final InitializationException exception = new InitializationException.Builder()
				                                          .withException(null)
				                                          .withRetryable(true)
				                                          .build();
		Assert.assertNotNull(exception);
	}

	@Test
	public void gettersContract() {
		final InitializationException exception = new InitializationException.Builder()
				                                          .withException(new Exception("-MESSAGE-"))
				                                          .withRetryable(true)
				                                          .build();
		Assert.assertNotNull(exception);
		Assert.assertNotNull(exception.getExceptionMessage());
		Assert.assertEquals("-MESSAGE-", exception.getExceptionMessage());
		Assert.assertTrue(exception.isRetryable());
	}
}