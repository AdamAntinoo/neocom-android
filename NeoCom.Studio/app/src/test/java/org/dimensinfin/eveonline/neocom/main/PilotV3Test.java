package org.dimensinfin.eveonline.neocom.main;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.sharedcode.PilotDataSections;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.DataDownloaderService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PilotV3Test {
	private static  PilotV3 pilot;

	@Before
	public void setUp() throws Exception {
		// Given
		final Credential credential = new Credential(123);
		final DataDownloaderService downloaderService = Mockito.mock(DataDownloaderService.class);
		pilot = new PilotV3.Builder(credential, downloaderService).build();
	}

	@Test
	public void checkStaleness_Race_field () {
		final boolean obtained = pilot.checkDataStaleness(pilot.race, PilotDataSections.PILOT_RACE);
		Assert.assertTrue(obtained);
	}

}
