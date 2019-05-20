package org.dimensinfin.eveonline.neocom.mining.datasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.converters.MiningExtraction2ItemPackConverter;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;

// - M I N I N G L E D G E R D A T A S O U R C E T O D A Y
public class MiningLedgerDataSourceToday extends AMVCDataSource {
	private Credential credential;
	private MiningRepository miningRepository;
	//		private List<MiningExtraction> extractions = new ArrayList<>();
	private int extractionsCount = 0;
	private List<MiningExtractionAggregatorAsPanelTitle> extractionsPanels = new ArrayList<>();

	public MiningLedgerDataSourceToday setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	public MiningLedgerDataSourceToday setMiningRepository( final MiningRepository miningRepository ) {
		this.miningRepository = miningRepository;
		return this;
	}

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
			List<MiningExtraction> extractions = new ArrayList<>();
			//				final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
			int extractionCount = 0;
			//				this.addModelContents(title);
			extractions = this.miningRepository.accessTodayMiningExtractions4Pilot(credential);
			if (extractions.size() < 1)
				this.addModelContents( // The list ofr today is empty. Put a label to report that case.
						new PanelTitle("There are no Mining Extractions Today.")
				);
			final Map<String, MiningExtraction> ids = new HashMap();
			for (MiningExtraction extraction : extractions)  // Create a map of all the record ids
				ids.put(extraction.getRecordId(), extraction);

			// - Process the records in order. Aggregate by system.
			int processingSystem = -1; // Set the flag that we have not currently created any system.
			MiningExtractionAggregatorAsPanelTitle systemAggregator = null;
			for (MiningExtraction extraction : extractions) {
				// Get the extraction date and the hour.
				final String recordDate = extraction.getExtractionDate()/*.split(":")[0]*/;
				final int hour = extraction.getExtractionHour();
				final int system = extraction.getSolarSystemId();

				// Search on the ids if this record has a previous hour record. This is the current id less 1 hour.
				final String searchId = MiningExtraction.generateRecordId(recordDate, hour - 1
						, extraction.getTypeId(), system, extraction.getOwnerId());
				if (ids.keySet().contains(searchId)) { // Check if the id exists.
					// Get the previous record and perform the delta calculation, then update before adding the record.
					final MiningExtraction previous = ids.get(searchId);
					long delta = extraction.getQuantity() - previous.getQuantity();
					// Skip if there is no change during the period.
					if (delta <= 0) continue;
					else {
						extraction.setDelta(delta);
						extraction.store();
					}
				}

				// Add the record but aggregate by system.
				if (processingSystem != system) {
					// Create a new aggregator and use it to get the rest of the extractors inside.
					systemAggregator = new MiningExtractionAggregatorAsPanelTitle(extraction.getSystemName());
					this.extractionsPanels.add(systemAggregator);
					processingSystem = extraction.getSolarSystemId();
				}
				systemAggregator.addPack(MiningExtraction2ItemPackConverter.convert(extraction));
				extractionCount++;
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
		final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
		title.setTitle("TODAY MINING EXTRACTIONS [" + extractionsCount + "]");
		this.addModelContents(title);
		for (MiningExtractionAggregatorAsPanelTitle panel : this.extractionsPanels)
			this.addModelContents(panel);
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
