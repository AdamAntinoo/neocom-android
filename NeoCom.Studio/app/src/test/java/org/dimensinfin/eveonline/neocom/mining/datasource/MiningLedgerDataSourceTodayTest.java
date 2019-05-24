package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.database.INeoComDBHelper;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.domain.EsiItemV2;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningControllerFactory;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionController;
import org.dimensinfin.eveonline.neocom.model.ANeoComEntity;
import org.dimensinfin.eveonline.neocom.services.DataDownloaderService;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.controller.PanelTitleController;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
		EsiItemV2.injectDownloaderService(downloader);
		//		Mockito.when(ANeoComEntity.accessGlobal()).thenReturn(global);
		Mockito.when(global.getNeocomDBHelper()).thenReturn(helper);
		Mockito.when(helper.getMiningExtractionDao()).thenReturn(dao);

		miningRepository = Mockito.mock(MiningRepository.class);
		credential = Mockito.mock(Credential.class);
	}

	//	@Test
	//	public void prepareModel_noitems() {
	//
	//	}
	//
	//	@Test
	//	public void prepareModel_singleLocation() throws SQLException {
	//		// Given
	//		final Credential credential = Mockito.mock(Credential.class);
	//		final MiningRepository miningRepository = Mockito.mock(MiningRepository.class);
	//		final MiningControllerFactory factory = new MiningControllerFactory("TEST-VARIANT");
	//		final MiningLedgerDataSourceToday dataSource = new MiningLedgerDataSourceToday.Builder()
	//				                                               .addIdentifier("TEST/MINING-TODAY")
	//				                                               .withFactory(factory)
	//				                                               .withCredential(credential)
	//				                                               .withMiningRepository(miningRepository)
	//				                                               .build();
	//		// When
	//		Mockito.when(miningRepository.accessTodayMiningExtractions4Pilot(credential)).thenReturn(this.extractionsToday_singleLocation());
	//		// Testing
	//		Assert.assertEquals(0, dataSource.getDataSectionContents().size());
	//		dataSource.prepareModel();
	//		Assert.assertEquals(0, dataSource.getDataSectionContents().size());
	//		dataSource.collaborate2Model();
	//		// Asserts
	//		Assert.assertEquals(2, dataSource.getDataSectionContents().size());
	//	}
	//
	//	@Test
	//	public void initializeEntities() {
	//		//		final Credential credential = new Credential(123)
	//		//				                              .setDataSource("Tranquility");
	//		//		Mockito.when(ANeoComEntity.accessGlobal()).thenReturn(global);
	//		//		Mockito.when(global.getNeocomDBHelper()).thenReturn(helper);
	//		//		Mockito.when(helper.getCredentialDao()).thenReturn(dao);
	//		//		final String obtained = credential.getDataSource();
	//		//		Assert.assertEquals("tranquility", obtained);
	//
	//	}

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
		Assert.assertEquals("Number of Tritanium units.", 10000 * 5 + 1500, obtained);
		obtained = dataSource.getResources().get(34).getQuantity();
		Assert.assertEquals("Number of Other units.", 10000 * 5 + 1500, obtained);
		// Test contents details.
		IAndroidController testSlot = controllers.get(0);
		Assert.assertTrue(testSlot instanceof PanelTitleController);
		testSlot = controllers.get(1);
		Assert.assertTrue(testSlot instanceof PanelTitleController);
		Assert.assertEquals("Default System Name", ((PanelTitle) testSlot.getModel()).getTitle());
		testSlot = controllers.get(2);
		Assert.assertTrue(testSlot instanceof MiningExtractionController);
		Assert.assertEquals("Default System Name", ((MiningExtraction) testSlot.getModel()).getSystemName());
		final int expectedHour = new LocalDateTime().getHourOfDay();
		Assert.assertEquals("Extraction Hour: Extraction 01", 15, ((MiningExtraction) testSlot.getModel()).getExtractionHour());
		testSlot = controllers.get(3);
		Assert.assertTrue(testSlot instanceof MiningExtractionController);
		Assert.assertEquals("Default System Name", ((MiningExtraction) testSlot.getModel()).getSystemName());
		Assert.assertEquals("Extraction Hour: Extraction 02", 16, ((MiningExtraction) testSlot.getModel()).getExtractionHour());
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
			final String id = MiningExtraction.generateRecordId(date, 20 - i, typeId, 30001647, 92223647);
			final LocalDateTime extractionTime = new LocalDateTime().withHourOfDay(20 - i);
			todayExtractions.add(new MiningExtraction.Builder()
					                     .withTypeId(typeId)
					                     .withSolarSystemId(30001647)
					                     .withQuantity(quantity + i * 100)
					                     .withOwnerId(92223647)
					                     .withExtractionDate(new LocalDate())
					                     .withExtractionHour(20 - i)
					                     .build());
		}
		return todayExtractions;
	}

	//	private List<MiningExtraction> extractionsToday_singleLocation() {
	//		final List<MiningExtraction> extractions = new ArrayList<>();
	//		MiningExtraction row = new MiningExtraction()
	//				                       .setOwnerId(123)
	//				                       .setExtractionDate(new LocalDate())
	//				                       .setQuantity(1000)
	//				                       .setTypeId(34)
	//				                       .setSolarSystemId(30001647);
	//		extractions.add(row);
	//		row = new MiningExtraction()
	//				      .setOwnerId(123)
	//				      .setExtractionDate(new LocalDate())
	//				      .setQuantity(2000)
	//				      .setTypeId(35)
	//				      .setSolarSystemId(30001647);
	//		extractions.add(row);
	//		return extractions;
}
//}
