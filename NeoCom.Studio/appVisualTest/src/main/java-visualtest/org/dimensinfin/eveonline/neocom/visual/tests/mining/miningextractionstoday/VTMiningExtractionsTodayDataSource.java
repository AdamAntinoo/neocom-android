package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Objects;

public class VTMiningExtractionsTodayDataSource extends MVCDataSource {
	private MiningRepository miningRepository;
	private Credential credential;
	private List<MiningExtraction> extractions;
	private MiningExtractionDatabaseRecordGenerator miningExtractionDatabaseRecordGenerator ;

	/**
	 * The preparation should generate two sets of data. One for the header (the list of resources mined) and the other for the
	 * data section with the hourly resource extractions.
	 */
	@Override
	public void prepareModel() {
		this.extractions = this.miningExtractionDatabaseRecordGenerator.accessTodayMiningExtractions4Pilot(this.credential);
	}

	@Override
	public void collaborate2Model() {
		this.addHeaderContents(new DailyExtractionResourcesContainer.Builder()
				                       .withCredential(this.credential)
				                       .withMiningRepository(this.miningRepository)
				                       .withTargetDate(new LocalDate("2019-08-07"))
				                       .build());
		for (MiningExtraction extraction : this.extractions) {
//			this.addHeaderContents(new Resource(extraction.getTypeId(), extraction.getQuantity()));
		}
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<VTMiningExtractionsTodayDataSource, VTMiningExtractionsTodayDataSource.Builder> {
		private VTMiningExtractionsTodayDataSource onConstruction;

		@Override
		protected VTMiningExtractionsTodayDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTMiningExtractionsTodayDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTMiningExtractionsTodayDataSource.Builder getActualBuilder() {
			return this;
		}

		public VTMiningExtractionsTodayDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.onConstruction.credential = credential;
			return this.getActualBuilder();
		}

		public VTMiningExtractionsTodayDataSource.Builder withMiningRepository( final MiningRepository miningRepository ) {
			Objects.requireNonNull(miningRepository);
			this.onConstruction.miningRepository = miningRepository;
			return this.getActualBuilder();
		}

		@Override
		public VTMiningExtractionsTodayDataSource build() {
			final VTMiningExtractionsTodayDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.miningRepository);
			this.onConstruction.miningExtractionDatabaseRecordGenerator= new MiningExtractionDatabaseRecordGenerator.Builder()
																				 .withMiningRepository(this.onConstruction.miningRepository)
					                                                             .build();
			return ds;
		}
	}

//	private List<MiningExtraction> generateMockExtractions() {
//		final List<MiningExtraction> extractions4Test = new MiningExtractionConverter()
//				                                                .generateExtraction("93813310-12-30", 12);
//		return extractions4Test;
////		extractions4Test.clear();
////		// Generate 5 different hours for today.
////		extractions4Test.addAll(this.extractionGenerator(34, 10000));
////		extractions4Test.addAll(this.extractionGenerator(45498, 10000));
//	}

	//	private List<MiningExtraction> extractionGenerator( final int typeId, final int quantity ) {
//		final List<MiningExtraction> todayExtractions = new ArrayList<>();
//		// Generate 5 different hours for today.
//		for (int i = 5; i > 0; i--) {
//			final String date = DateTime.now().toString("YYYY/MM/dd");
//			final String id = MiningExtraction.generateRecordId(date, 20 - i, typeId, 30001647, 92223647);
//			todayExtractions.add(new MiningExtraction.Builder()
//					                     .withTypeId(typeId)
//					                     .withSolarSystemId(30001647)
//					                     .withQuantity(quantity + (20 - i) * 100)
//					                     .withOwnerId(92223647)
//					                     .withExtractionDate(new LocalDate())
//					                     .withExtractionHour(20 - i)
//					                     .build());
//		}
//		return todayExtractions;
//	}
//	public static class MiningExtractionConverter {
//		public static final ObjectMapper jsonMapper = new ObjectMapper();
//
//		static {
//			jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
//			jsonMapper.registerModule(new JodaModule());
//		}
//
//		public List<MiningExtraction> generateExtraction( final String identifier, final int hour ) {
//			final String extractionSource = new MiningExtractionGenerator().searchExtraction(identifier, hour);
//			List<GetCharactersCharacterIdMining200Ok> minings = new ArrayList<>();
//			try {
//				minings = jsonMapper.readValue(extractionSource,
//				                               new TypeReference<List<GetCharactersCharacterIdMining200Ok>>() {
//				                               });
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
////			() {})
////		val gson = Gson()
////		val minings = gson?.fromJson(extractionSource, Array<GetCharactersCharacterIdMining200Ok>::class.java)
//			final List<MiningExtraction> results = new ArrayList<MiningExtraction>();
//			for (GetCharactersCharacterIdMining200Ok mine : minings) {
//				results.add(new MiningExtraction.Builder().withExtractionHour(hour).fromMining(mine).build());
//			}
//			return results;
//		}
//	}
//
//	protected void setupMiningRepositoryRecords() throws SQLException {
//		final List<Map<String, String>> dataTable = this.generateDataTable(recordSource);
//		for (Map<String, String> row : dataTable) {
//			final MiningExtraction extraction = this.dataTableToMiningExtractionConverter.convert(row);
//			this.miningRepository.persist(extraction);
//		}
//	}

}
