package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

public class VTMiningExtractionsTodayDataSource extends MVCDataSource {
	private MiningRepository miningRepository;
	private Credential credential;

	/**
	 * The preparation should generate two sets of data. One for the header (the list of resources mined) and the other for the
	 * data section with the hourly resource extractions.
	 */
	@Override
	public void prepareModel() {
		this.miningRepository = Mockito.mock(MiningRepository.class);
		Mockito.when(this.miningRepository.accessTodayMiningExtractions4Pilot(Mockito.any(Credential.class)))
		       .thenReturn(this.generateMockExtractions());

		final List<MiningExtraction> extractions = this.miningRepository.accessTodayMiningExtractions4Pilot(this.credential);

	}

	@Override
	public void collaborate2Model() {

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
//			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.miningRepository);
			return ds;
		}
	}

	private List<MiningExtraction> generateMockExtractions() {
		final List<MiningExtraction> extractions4Test = new MiningExtractionGenerator()
				                                                .generateExtraction("93813310-12-30", 12);
		return extractions4Test;
//		extractions4Test.clear();
//		// Generate 5 different hours for today.
//		extractions4Test.addAll(this.extractionGenerator(34, 10000));
//		extractions4Test.addAll(this.extractionGenerator(45498, 10000));
	}

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
}
