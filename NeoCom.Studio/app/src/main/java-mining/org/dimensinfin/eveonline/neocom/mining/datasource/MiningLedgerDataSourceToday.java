package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.backend.NeoComBackendService;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtractionEntity;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.domain.space.SpaceSystem;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.domain.MiningExtractionAggregatorContainer;
import org.dimensinfin.eveonline.neocom.miningextraction.converter.MiningExtractionEntityToMiningExtractionConverter;
import org.dimensinfin.eveonline.neocom.miningextraction.domain.MiningExtraction;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

// - M I N I N G L E D G E R D A T A S O U R C E T O D A Y
public class MiningLedgerDataSourceToday extends MVCDataSource {
	protected final List<MiningExtraction> extractions = new ArrayList<>();
	private final List<MiningExtractionAggregatorContainer<SpaceSystem>> extractionSystems = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	// -  C O M P O N E N T S
	private Credential credential;
	private ESIDataProvider esiDataProvider;
	private MiningRepository miningRepository; // The mining persistence repository definition.
	private NeoComBackendService backendService;
	private LocationCatalogService locationCatalogService;

	// - C O N S T R U C T O R S
	protected MiningLedgerDataSourceToday() {}

	// - I D A T A S O U R C E   I N T E R F A C E

	/**
	 * Go to the data sources and prepare all the intermediate data structures required by the functionality to show on this page.
	 * There should be:
	 * <ul>
	 * <li>The list of hours that contains extractions. This is a AggregationContainer that will also show its contents.</li>
	 * <li>The list of resources and their market value and best station for selling.</li>
	 * <li>The profit estimates, the max values for an hour and the median.</li>
	 * </ul>
	 *
	 * Because the mining extractions data is aggregated at the ESI endpoint it is necessary to have the Android application up and running to get
	 * that information segmented by hours. If the data is not periodically read and persisted the hour differences and other aggregations lost
	 * detail. By that reason now the mining extractions provider is not located on the local repository but on an auxiliary backend server that is
	 * continuously monitoring this data. This way even if the android app is not running the hourly mined information for today is bein read and
	 * processed.
	 */
	@Override
	public void prepareModel() {
		NeoComLogger.enter();
		try {
			// Get the list of extractions from the backend server that should keep them updated.
			final List<MiningExtractionEntity> extractionEntities = this.backendService.accessTodayMiningExtractions4Pilot( this.credential );
			final Map<String, MiningExtraction> ids = new HashMap<>();
			// Create a map of all the record ids to search for previous hours
			this.extractions.clear();
			this.extractionSystems.clear();
			for (MiningExtractionEntity extractionEntity : extractionEntities) {
				final MiningExtraction extraction = new MiningExtractionEntityToMiningExtractionConverter( this.locationCatalogService )
						                                    .convert( extractionEntity );
				ids.put( extractionEntity.getId(), Objects.requireNonNull( extraction ) );
				this.extractions.add( extraction );
			}
			// - Process the records in order. Aggregate by system.
			long processingSystem = -1; // Set the flag that we have not currently created any system.
			MiningExtractionAggregatorContainer<SpaceSystem> systemAggregator = null;
			for (MiningExtraction extraction : this.extractions) {
				// Search on the ids if this record has a previous hour record. This is the current id less 1 hour.
				this.calculateDelta( extraction, ids );
//				final long systemId = extraction.getLocationId(); // Add the record but aggregate by system.
				if (processingSystem != extraction.getLocationId()) {
					// Create a new aggregator and use it to get the rest of the extractors inside.
					systemAggregator = new MiningExtractionAggregatorContainer.Builder<SpaceSystem>()
							                   .withFacet( extraction.getSolarSystemLocation() )
							                   .build();
					this.extractionSystems.add( systemAggregator );
					processingSystem = extraction.getLocationId();
				}
				if (null != systemAggregator) systemAggregator.addPack( extraction ); // Add the extraction to the aggregator
			}
		} catch (Exception rtex) {
			NeoComLogger.error( rtex );
		}
		NeoComLogger.exit();
	}

	@Override
	public void collaborate2Model() {
		// - H E A D E R   S E C T I O N
		// Aggregate today extraction resources.
		this.resources.clear();
		for (MiningExtraction extraction : this.extractions) {
			this.resources.add( new Resource( extraction.getTypeId(), (int) extraction.getQuantity() ) );
		}
		this.addHeaderContents( new DailyExtractionResourcesContainer.Builder()
				                        .withResourceList( this.resources )
				                        .build() ); // Add the resource table header

		// - D A T A   S E C T I O N
		final PanelTitle title = new PanelTitle( "TODAY MINING EXTRACTIONS DELTA [-]" );
		if (this.extractions.isEmpty()) title.setTitle( "There are no Mining Extractions Today." );
		else title.setTitle( "TODAY MINING EXTRACTIONS [" + this.extractions.size() + "]" );
		this.addModelContents( title );
		for (MiningExtractionAggregatorContainer panel : this.extractionSystems)
			this.addModelContents( panel );
	}

	private void calculateDelta( final MiningExtraction extraction, final Map<String, MiningExtraction> ids ) {
		final String searchId = extraction.getPreviousHourId();
		if (ids.containsKey( searchId ))  // Check if the id exists.
			extraction.setDelta( extraction.getQuantity() - Objects.requireNonNull( ids.get( searchId ) ).getQuantity() );
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<MiningLedgerDataSourceToday, MiningLedgerDataSourceToday.Builder> {
		private MiningLedgerDataSourceToday onConstruction;

		public Builder() {
			super();
			this.onConstruction = new MiningLedgerDataSourceToday();
		}

		@Override
		protected MiningLedgerDataSourceToday getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new MiningLedgerDataSourceToday();
			return this.onConstruction;
		}

		@Override
		protected MiningLedgerDataSourceToday.Builder getActualBuilder() {
			return this;
		}

		@Override
		public MiningLedgerDataSourceToday build() {
			super.build(); // Check additional mandatory fields.
			Objects.requireNonNull( this.onConstruction.credential );
			Objects.requireNonNull( this.onConstruction.miningRepository );
			Objects.requireNonNull( this.onConstruction.esiDataProvider );
			return this.onConstruction;
		}

		public MiningLedgerDataSourceToday.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull( credential );
			this.onConstruction.credential = credential;
			return this;
		}

		public MiningLedgerDataSourceToday.Builder withBackendService( final NeoComBackendService backendService ) {
			Objects.requireNonNull( backendService );
			this.onConstruction.backendService = backendService;
			return this;
		}

		public MiningLedgerDataSourceToday.Builder withMiningRepository( final MiningRepository miningRepository ) {
			Objects.requireNonNull( miningRepository );
			this.onConstruction.miningRepository = miningRepository;
			return this;
		}

		public MiningLedgerDataSourceToday.Builder withLocationCatalogService( final LocationCatalogService locationCatalogService ) {
			Objects.requireNonNull( locationCatalogService );
			this.onConstruction.locationCatalogService = locationCatalogService;
			return this;
		}

		public MiningLedgerDataSourceToday.Builder withEsiDataAdapter( final ESIDataProvider esiDataProvider ) {
			Objects.requireNonNull( esiDataProvider );
			this.onConstruction.esiDataProvider = esiDataProvider;
			return this;
		}
	}
}
