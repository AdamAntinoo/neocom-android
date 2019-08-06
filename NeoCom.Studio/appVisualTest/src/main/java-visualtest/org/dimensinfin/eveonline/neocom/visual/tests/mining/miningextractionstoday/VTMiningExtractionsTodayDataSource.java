package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;

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
}
