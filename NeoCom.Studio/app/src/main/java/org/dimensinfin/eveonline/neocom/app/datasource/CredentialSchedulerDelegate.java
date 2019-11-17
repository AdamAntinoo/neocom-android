package org.dimensinfin.eveonline.neocom.app.datasource;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;

@FunctionalInterface
public interface CredentialSchedulerDelegate {
	void registerScheduledJobs( final Credential credential );
}
