package org.dimensinfin.eveonline.neocom.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoComLogger {
	public static final Logger logger = LoggerFactory.getLogger(NeoComLogger.class);

	private NeoComLogger() { }

	public static void enter() {
		final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		final StackTraceElement stackElement = stacktrace[3]; // This is to check if we are using Dalvik
		final String sourceMethod = stackElement.getMethodName();
		final String sourceClass = stackElement.getClassName();
		logger.info("><[{}.{}]", sourceClass, sourceMethod);
	}
}