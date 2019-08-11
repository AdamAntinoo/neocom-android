package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;
import org.dimensinfin.eveonline.neocom.visual.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.visual.app.support.SupportMiningRepository;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VTMiningExtractionsTodayDataSource extends MVCDataSource {
	private ESIDataAdapter esiDataAdapter;
	private MiningRepository miningRepository;
	private Credential credential;
	private MiningExtractionDatabaseRecordGenerator miningExtractionDatabaseRecordGenerator;

	private List<MiningExtraction> extractions;
	private List<ResourceAggregatorContainer<EsiLocation>> extractionSystems = new ArrayList<>();

	/**
	 * The preparation should generate two sets of data. One for the header (the list of resources mined) and the other for the
	 * data section with the hourly resource extractions.
	 */
	@Override
	public void prepareModel() {
		this.initializeRepository();
		logger.info(">> [VTMiningExtractionsTodayDataSource.prepareModel]");
		try {
			this.extractions = this.miningRepository.accessTodayMiningExtractions4Pilot(this.credential);
			final Map<String, MiningExtraction> ids = new HashMap<>();
			// Create a map of all the record ids to search for previous hours
			for (MiningExtraction extraction : this.extractions)
				ids.put(extraction.getId(), extraction);

			// - Process the records in order. Aggregate by system.
			int processingSystem = -1; // Set the flag that we have not currently created any system.
			ResourceAggregatorContainer systemAggregator = null;
			for (MiningExtraction extraction : this.extractions) {
				// Search on the ids if this record has a previous hour record. This is the current id less 1 hour.
//				this.search4PreviousHour(extraction, ids);
				final int systemId = extraction.getSolarSystemId(); // Add the record but aggregate by system.
				if (processingSystem != systemId) {
					// Create a new aggregator and use it to get the rest of the extractors inside.
					systemAggregator = new ResourceAggregatorContainer.Builder<EsiLocation>()
							                   .withFacet(this.esiDataAdapter.searchLocation4Id(
									                   extraction.getSolarSystemId()))
							                   .build();
					this.extractionSystems.add(systemAggregator);
					processingSystem = extraction.getSolarSystemId();
				}
				if (null != systemAggregator) systemAggregator.addPack(extraction); // Add the extraction to the aggregator
			}
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
		logger.info("<< [VTMiningExtractionsTodayDataSource.collaborate2Model]");
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

	private void initializeRepository() {
		try {
			final SupportMiningRepository miningRepository = new SupportMiningRepository.Builder()
					                                                 .withMiningExtractionDao(
							                                                 NeoComComponentFactory.getSingleton()
									                                                 .getNeoComDBHelper()
									                                                 .getMiningExtractionDao())
					                                                 .build();
			miningRepository.deleteAll(); // Clean up the respository.
			this.miningExtractionDatabaseRecordGenerator = new MiningExtractionDatabaseRecordGenerator.Builder()
					                                               .withMiningRepository(miningRepository)
					                                               .build();
			this.miningExtractionDatabaseRecordGenerator.loadMockData2Repository(
					this.credential); // Load up the mock data used on the acceptance tests.
		} catch (SQLException e) {
			e.printStackTrace();
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

		@Override
		public VTMiningExtractionsTodayDataSource build() {
			final VTMiningExtractionsTodayDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.miningRepository);
			this.onConstruction.miningExtractionDatabaseRecordGenerator = new MiningExtractionDatabaseRecordGenerator.Builder()
					                                                              .withMiningRepository(
							                                                              this.onConstruction.miningRepository)
					                                                              .build();
			return ds;
		}

		public VTMiningExtractionsTodayDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
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
	}
}
