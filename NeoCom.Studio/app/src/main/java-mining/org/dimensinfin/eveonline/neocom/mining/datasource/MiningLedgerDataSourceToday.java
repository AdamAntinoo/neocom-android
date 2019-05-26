package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.TableHeader;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

// - M I N I N G L E D G E R D A T A S O U R C E T O D A Y
public class MiningLedgerDataSourceToday extends AMVCDataSource {
	private Credential credential;
	private MiningRepository miningRepository; // The mining persistence repository definition.
	//	private PanelTitle sectionTitle; // The panel title for the data section
	private List<MiningExtraction> extractions;
	//	private int extractionsCount = 0; // Number of extractions processed
	private List<MiningExtractionAggregatorAsPanelTitle> extractionsPanels = new ArrayList<>();
	private Map<Integer, Resource> resources = new HashMap<>();

	// - C O N S T R U C T O R S
	private MiningLedgerDataSourceToday() {}

	// - B U I L D E R   S E T T E R S
	private MiningLedgerDataSourceToday setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	private MiningLedgerDataSourceToday setMiningRepository( final MiningRepository miningRepository ) {
		this.miningRepository = miningRepository;
		return this;
	}

	// - I D A T A S O U R C E   I N T E R F A C E

	/**
	 * Go to the data sources and prepare all the intermediate data structures required by the functionality to show on this page.
	 * There should be:
	 * <ul>
	 * <li>The list of hours that contains extractions. This is a AggregationContainer that will also show its contents.</li>
	 * <li>The list of resources and their market value and best station for selling.</li>
	 * <li>The profit estimates, the max values for an hour and the median.</li>
	 * </ul>
	 */
	@Override
	public void prepareModel() {
		logger.info(">> [MiningLedgerDataSource.prepareModel]");
		try {
			extractions = this.miningRepository.accessTodayMiningExtractions4PilotMock(credential);
			final Map<String, MiningExtraction> ids = new HashMap();
			for (MiningExtraction extraction : extractions)  // Create a map of all the record ids to search for previous hours
				ids.put(extraction.getId(), extraction);

			// - Process the records in order. Aggregate by system.
			int processingSystem = -1; // Set the flag that we have not currently created any system.
			MiningExtractionAggregatorAsPanelTitle systemAggregator = null;
			for (MiningExtraction extraction : extractions) {
				// Search on the ids if this record has a previous hour record. This is the current id less 1 hour.
//				if (this.search4PreviousHour(extraction, ids)) continue;
				this.search4PreviousHour(extraction, ids);
				final int system = extraction.getSolarSystemId(); // Add the record but aggregate by system.
				if (processingSystem != system) {
					// Create a new aggregator and use it to get the rest of the extractors inside.
					systemAggregator = new MiningExtractionAggregatorAsPanelTitle(extraction.getSystemName());
					this.extractionsPanels.add(systemAggregator);
					processingSystem = extraction.getSolarSystemId();
				}
				systemAggregator.addPack(extraction); // Add the extraction to the aggregator
				this.accountResource(extraction.getTypeId(), extraction.getQuantity());
				//				extractionCount++;
			}
			// Update the number of extractions.
			//				title.setTitle("TODAY MINING EXTRACTIONS [" + extractionCount + "]");
			//				scheduleRefresh(this.refreshTime);
			//					}

		} catch (SQLException sqle) {
			AndroidGlobalDataManager.registerException("MiningLedgerDataSource.collaborate2Model"
					, sqle, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			sqle.printStackTrace();
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("MiningLedgerDataSource.collaborate2Model"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
		//			}
		logger.info("<< [MiningLedgerDataSource.collaborate2Model]");
	}

	@Override
	public void collaborate2Model() {
		// - H E A D E R   S E C T I O N
		this.addHeaderContents(new TableHeader()); // Add the resource table header
		for ( Resource resource : this.resources.values()) {
			this.addHeaderContents(resource);
		}

		// - D A T A   S E C T I O N
		final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
		if (extractions.size() < 1)
			title.setTitle("There are no Mining Extractions Today.");
		else
			title.setTitle("TODAY MINING EXTRACTIONS [" + extractions.size() + "]");
		this.addModelContents(title);
		for (MiningExtractionAggregatorAsPanelTitle panel : this.extractionsPanels)
			this.addModelContents(panel);
	}

	// - S U P P O R T
	public int getExtractionsProcessed() {
		return this.extractions.size();
	}

	public Map<Integer, Resource> getResources() {
		return this.resources;
	}

	/**
	 * Record the resources extracted on some extractions to generate a list of resources mined today.
	 *
	 * @param typeId   the type identifier of the resource
	 * @param quantity the quantity of resource units
	 */
	private void accountResource( final int typeId, final long quantity ) {
		final int qty = Long.valueOf(quantity).intValue();
		if (this.resources.containsKey(typeId)) {
			final Resource hit = this.resources.get(typeId);
			hit.add(qty);
		} else this.resources.put(typeId, new Resource(typeId, qty));
	}

	private boolean search4PreviousHour( final MiningExtraction extraction, final Map<String, MiningExtraction> ids ) {
		final String recordDate = extraction.getExtractionDateName()/*.split(":")[0]*/;
		final int hour = extraction.getExtractionHour();
		final int system = extraction.getSolarSystemId();
		final String searchId = MiningExtraction.generateRecordId(recordDate, hour - 1
				, extraction.getTypeId(), system, extraction.getOwnerId());
		if (ids.keySet().contains(searchId)) { // Check if the id exists.
			// Get the previous record and perform the delta calculation, then update before adding the record.
			final MiningExtraction previous = ids.get(searchId);
			long delta = extraction.getQuantity() - previous.getQuantity();
			// Skip if there is no change during the period.
			if (delta <= 0) return true;
			else {
				extraction.setDelta(delta);
				// TODO - activate back this line after the mock is completed.
				//						extraction.store();
			}
		}
		return false;
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<MiningLedgerDataSourceToday, MiningLedgerDataSourceToday.Builder> {
		private MiningLedgerDataSourceToday onConstruction;

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

		public MiningLedgerDataSourceToday.Builder withCredential( final Credential credential ) {
			this.getActual().setCredential(credential);
			return this;
		}

		public MiningLedgerDataSourceToday.Builder withMiningRepository( final MiningRepository miningRepository ) {
			this.onConstruction.setMiningRepository(miningRepository);
			return this;
		}

		@Override
		public MiningLedgerDataSourceToday build() {
			// Check additional mandatory fields.
			final MiningLedgerDataSourceToday target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.miningRepository);
			return target;
		}
	}
}
