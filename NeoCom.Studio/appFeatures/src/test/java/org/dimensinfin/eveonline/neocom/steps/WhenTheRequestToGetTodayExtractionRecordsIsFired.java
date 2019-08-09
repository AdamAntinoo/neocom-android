package org.dimensinfin.eveonline.neocom.steps;

import android.os.Bundle;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;
import org.dimensinfin.eveonline.neocom.support.miningExtractions.MiningExtractionsWorld;
import org.mockito.Mockito;

public class WhenTheRequestToGetTodayExtractionRecordsIsFired {
	private MiningExtractionsWorld miningExtractionsWorld;

	public WhenTheRequestToGetTodayExtractionRecordsIsFired( final MiningExtractionsWorld miningExtractionsWorld ) {
		this.miningExtractionsWorld = miningExtractionsWorld;
	}

	//	@When("the request to get today extraction records is fired")
	public void theRequestToGetTodayExtractionRecordsIsFired() {
		final MiningLedgerDataSourceToday dataSource = new MiningLedgerDataSourceToday.Builder()
				                                               .addIdentifier(this.getVariant())
				                                               .addIdentifier(this.getCredential().getAccountId())
				                                               .withVariant(this.getVariant())
				                                               .withExtras(this.getExtras())
				                                               .withFactory(this.createFactory())
				                                               .withCredential(this.getCredential())
				                                               .withMiningRepository(this.miningExtractionsWorld.getMiningRepository())
				                                               .build();
		dataSource.prepareModel();
	}

	private Credential getCredential() {
		return this.miningExtractionsWorld.getCredential();
	}

	private String getVariant() {
		return PageNamesType.MINING_EXTRACTIONS_TODAY.name();
	}

	private Bundle getExtras() {
		return null;
	}

	private ControllerFactory createFactory() {
		return Mockito.mock(ControllerFactory.class);
	}
}
