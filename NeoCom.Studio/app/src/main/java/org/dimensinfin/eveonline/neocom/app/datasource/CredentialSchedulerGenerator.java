package org.dimensinfin.eveonline.neocom.app.datasource;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class CredentialSchedulerGenerator {
	private Credential credential;

	public CredentialSchedulerGenerator( final Credential credential ) {
		this.credential = credential;
	}

	public void defineJobs(final CredentialSchedulerDelegate delegate) {
		delegate.registerScheduledJobs( this.credential );
//		new JobScheduler.Builder().withCronScheduleGenerator( new HourlyCronScheduleGenerator ())
//		                          .build()
//		                          .registerJob( new AssetDownloadProcessor.Builder()
//		                          .withCredential( credential )
//		                          .withAssetRepository( assetRepository )
//		                          .withEsiDataProvider( esiDataProvider )
//		                          .withLocationCatalogService( locationCatalogService )
//		                          .withNeoAssetConverter( neocomAssetConverter ))
	}
}
