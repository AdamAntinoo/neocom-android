package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.controller.PanelTitleController;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.database.INeoComDBHelper;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningControllerFactory;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionAggregatorController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionController;
import org.dimensinfin.eveonline.neocom.model.ANeoComEntity;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.j256.ormlite.dao.Dao;

public class MiningLedgerDataSourceTodayTest {
	private List<MiningExtraction> extractions4Test = new ArrayList<>();
	private Credential credential;
	private MiningRepository miningRepository;
	private ControllerFactory factory;
	private AAndroidController controller;

	@Before
	public void setUp() throws Exception {
		final GlobalDataManager global = Mockito.mock(GlobalDataManager.class);
		final INeoComDBHelper helper = Mockito.mock(INeoComDBHelper.class);
		final Dao dao = Mockito.mock(Dao.class);
		ANeoComEntity.connectGlobal(global);
		final DataDownloaderService downloader = Mockito.mock(DataDownloaderService.class);
//		EsiItemV2.injectDownloaderService(downloader);
		//		Mockito.when(ANeoComEntity.accessGlobal()).thenReturn(global);
		Mockito.when(global.getNeocomDBHelper()).thenReturn(helper);
		Mockito.when(helper.getMiningExtractionDao()).thenReturn(dao);

		miningRepository = Mockito.mock(MiningRepository.class);
		credential = Mockito.mock(Credential.class);
	}

	@Test
	public void dataSource_noextractions() throws SQLException {
		extractions4Test.clear();
		Mockito.when(miningRepository.accessTodayMiningExtractions4Pilot(credential)).thenReturn(extractions4Test);
		final MiningLedgerDataSourceToday dataSource = new MiningLedgerDataSourceToday.Builder()
				                                               .addIdentifier("TEST/DATASOURCE")
				                                               .withFactory(new MiningControllerFactory("TESTING"))
				                                               .withCredential(credential)
				                                               .withMiningRepository(miningRepository)
				                                               .build();
		dataSource.prepareModel();
		dataSource.collaborate2Model();
		// Asserts
		final List<IAndroidController> controllers = dataSource.getDataSectionContents();
		Assert.assertEquals(1, controllers.size());
		// Test contents details.
		IAndroidController testSlot = controllers.get(0);
		Assert.assertTrue(testSlot instanceof PanelTitleController);
		final int obtained = dataSource.getExtractionsProcessed();
		Assert.assertEquals("Number of extractions processed.", 0, obtained);
	}

	@Test
	public void testExtractions5Hours() throws SQLException {
		extractions4Test.clear();
		this.generateExtractionsT1();
		Mockito.when(miningRepository.accessTodayMiningExtractions4Pilot(credential)).thenReturn(extractions4Test);
		Mockito.when(miningRepository.accessTodayMiningExtractions4PilotMock(credential)).thenReturn(extractions4Test);
		final MiningLedgerDataSourceToday dataSource = new MiningLedgerDataSourceToday.Builder()
				                                               .addIdentifier("TEST/DATASOURCE")
				                                               .withFactory(new MiningControllerFactory("TESTING"))
				                                               .withCredential(credential)
				                                               .withMiningRepository(miningRepository)
				                                               .build();
		dataSource.prepareModel();
		dataSource.collaborate2Model();
		// Asserts
		final List<IAndroidController> controllers = dataSource.getDataSectionContents();
		Assert.assertEquals("Number of controllers on Data Section:", 12, controllers.size());
		int obtained = dataSource.getExtractionsProcessed();
		Assert.assertEquals("Number of extractions processed.", 10, obtained);
		obtained = dataSource.getResources().get(34).getQuantity();
		Assert.assertEquals("Number of Tritanium units.", 10000 * 5 + (15 + 16 + 17 + 18 + 19) * 100, obtained);
		obtained = dataSource.getResources().get(45498).getQuantity();
		Assert.assertEquals("Number of Other units.", 10000 * 5 + (15 + 16 + 17 + 18 + 19) * 100, obtained);
		// Test contents details.
		IAndroidController testSlot = controllers.get(0);
		Assert.assertTrue("Controller instanceof PanelTitleController", testSlot instanceof PanelTitleController);
		IAndroidController aggregator = controllers.get(1);
		final String expected = "TODAY MINING EXTRACTIONS [" + ((MiningExtractionAggregatorAsPanelTitle) aggregator.getModel()).getContentSize() + "]";
		Assert.assertEquals("Number of extractions", expected, ((PanelTitle) testSlot.getModel()).getTitle());
		testSlot = controllers.get(1);
		Assert.assertTrue("Controller instanceof MiningExtractionAggregatorController", testSlot instanceof MiningExtractionAggregatorController);
		Assert.assertEquals("Default System Name", ((PanelTitle) testSlot.getModel()).getTitle());
		testSlot = controllers.get(2);
		Assert.assertTrue(testSlot instanceof MiningExtractionController);
		Assert.assertEquals("Default System Name", ((MiningExtraction) testSlot.getModel()).getSystemName());
		Assert.assertEquals("Extraction Hour: Extraction 01", 15, ((MiningExtraction) testSlot.getModel()).getExtractionHour());
		Assert.assertEquals("Extected quantity: Extraction 01", 10000 + 15 * 100, ((MiningExtraction) testSlot.getModel()).getQuantity());
		testSlot = controllers.get(3);
		Assert.assertTrue(testSlot instanceof MiningExtractionController);
		Assert.assertEquals("Default System Name", ((MiningExtraction) testSlot.getModel()).getSystemName());
		Assert.assertEquals("Extraction Hour: Extraction 02", 16, ((MiningExtraction) testSlot.getModel()).getExtractionHour());
		Assert.assertEquals("Extraction Delta: Extraction 02", 100, ((MiningExtraction) testSlot.getModel()).getDelta());
	}

	private void generateExtractionsT1() {
		extractions4Test.clear();
		// Generate 5 different hours for today.
		extractions4Test.addAll(this.extractionGenerator(34, 10000));
		extractions4Test.addAll(this.extractionGenerator(45498, 10000));
	}

	private List<MiningExtraction> extractionGenerator( final int typeId, final int quantity ) {
		final List<MiningExtraction> todayExtractions = new ArrayList<>();
		// Generate 5 different hours for today.
		for (int i = 5; i > 0; i--) {
			final String date = DateTime.now().toString("YYYY/MM/dd");
			todayExtractions.add(new MiningExtraction.Builder()
					                     .withTypeId(typeId)
					                     .withSolarSystemId(30001647)
					                     .withQuantity(quantity + (20 - i) * 100)
					                     .withOwnerId(92223647)
					                     .withExtractionDate(new LocalDate())
					                     .withExtractionHour(20 - i)
					                     .build());
		}
		return todayExtractions;
	}
}
