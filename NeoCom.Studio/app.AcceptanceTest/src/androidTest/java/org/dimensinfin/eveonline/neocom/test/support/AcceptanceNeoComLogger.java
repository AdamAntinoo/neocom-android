package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class AcceptanceNeoComLogger extends NeoComLogger {
	protected AcceptanceNeoComLogger() {}

	public static void info( final String message ) {
		NeoComLogger.info( "ACCEPTANCE-->" + message );
	}
}
