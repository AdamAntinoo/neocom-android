package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

public class VTMiningExtractionsTodayDataSourceTest {
	private static String RECORD_SOURCE = "\t  | id                                    | typeId | solarSystemId | quantity | " +
			                                      "delta | extractionDateName | extractionHour | ownerId  |\n" +
			                                      "\t  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 10             | 92223647 |\n" +
			                                      "\t  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 19276" +
			                                      "    | 0     | 2019-08-07         | 10             | 92223647 |\n";

	@Test
	public void generateDataTable() {
		final ControllerFactory factory = Mockito.mock(ControllerFactory.class);
		final Credential credential = Mockito.mock(Credential.class);
		final MiningRepository miningRepository = Mockito.mock(MiningRepository.class);
		final VTMiningExtractionsTodayDataSource datasource = new VTMiningExtractionsTodayDataSource.Builder()
				                                                      .addIdentifier("VISUAL TESTS")
				                                                      .withVariant("VISUAL TESTS")
				                                                      .withFactory(factory)
				                                                      .withCredential(credential)
				                                                      .withMiningRepository(miningRepository)
				                                                      .build();
		final List<Map<String, String>> table = datasource.generateDataTable(RECORD_SOURCE);
	}
}