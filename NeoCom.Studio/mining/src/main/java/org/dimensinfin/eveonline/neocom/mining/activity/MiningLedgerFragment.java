package org.dimensinfin.eveonline.neocom.mining.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.NameFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.mining.converters.MiningExtraction2ItemPackConverter;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.NeoComAndroid;

public class MiningLedgerFragment extends MVCPagerFragment {
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new MiningControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		if (this.getVariant().equalsIgnoreCase(MiningLedgerActivity.EMiningVariants.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningLedgerDataSourceToday.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCacheStatus(true)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComAndroid.getSingleton().getMiningRepository())
					       .build();
		if (this.getVariant().equalsIgnoreCase(MiningLedgerActivity.EMiningVariants.MINING_EXTRACTIONS_ALL.name()))
			return new MiningLedgerDataSourceAll.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier(this.getCredential().getAccountId())
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCacheStatus(true)
					       .withCredential(this.getCredential())
					       .withMiningRepository(NeoComAndroid.getSingleton().getMiningRepository())
					       .build();
		throw new NeoComRuntimeException("Mining variant has not defined Data Source to generate content.");
	}

	public Credential getCredential() {
		return credential;
	}

	public MiningLedgerFragment setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	// - M I N I N G L E D G E R D A T A S O U R C E T O D A Y
	public static class MiningLedgerDataSourceToday extends AMVCDataSource {
		private Credential credential;
		private MiningRepository miningRepository;
		//		private List<MiningExtraction> extractions = new ArrayList<>();

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
				final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
				int extractionCount = 0;
				this.addModelContents(title);
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
						this.addModelContents(systemAggregator);
						processingSystem = extraction.getSolarSystemId();
					}
					systemAggregator.addPack(MiningExtraction2ItemPackConverter.convert(extraction));
					extractionCount++;
				}
				// Update the number of extractions.
				title.setTitle("TODAY MINING EXTRACTIONS [" + extractionCount + "]");
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

		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<MiningLedgerDataSourceToday, Builder> {
			private MiningLedgerDataSourceToday onConstruction;

			@Override
			protected MiningLedgerDataSourceToday getActual() {
				if (null == this.onConstruction) this.onConstruction = new MiningLedgerDataSourceToday();
				return this.onConstruction;
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}

			public Builder withCredential( final Credential credential ) {
				this.getActual().setCredential(credential);
				return this;
			}

			public Builder withMiningRepository( final MiningRepository miningRepository ) {
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

	// - M I N I N G L E G D E R D A T A S O U R C E A L L
	public static class MiningLedgerDataSourceAll extends AMVCDataSource {
		private Credential credential;
		private MiningRepository miningRepository;
		private List<MiningExtraction> extractions = new ArrayList<>();

		public MiningLedgerDataSourceAll setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}

		public MiningLedgerDataSourceAll setMiningRepository( final MiningRepository miningRepository ) {
			this.miningRepository = miningRepository;
			return this;
		}

		@Override
		public void prepareModel() {
//			// --- A L L   E X T R A C T I O N S
//			if (getVariant() == NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()) {
//				// Add the panel title up front of the Credential list.
//				final PanelTitle title = new PanelTitle("ALL MINING EXTRACTIONS BY DATE");
//				this.addModelContents(title);
//
//				extractions = AndroidGlobalDataManager.accessMiningExtractions4Pilot(credential);
//
//				// Aggregate the extractions by date and system so that information can be removed from the extractions panel.
//				String processingDate = "";
//				NameFacetedMiningExtractionContainer dateAggregator = null;
//				for (MiningExtraction extraction : extractions) {
//					// Get the extraction date.
//					final String recordDate = extraction.getExtractionDate().split(":")[0];
//					// Create a new aggregator if the dates do not match.
//					if (!recordDate.equalsIgnoreCase(processingDate)) {
//						// Create a new aggregator and use it to get the rest of the extractors inside.
//						dateAggregator = new NameFacetedMiningExtractionContainer(recordDate);
//						this.addModelContents(dateAggregator);
//						processingDate = recordDate;
//					}
//					// Only aggregate extractions marked as hour 24 because the others are daily or redundant data.
//					if (extraction.getExtractionHour() == 24) dateAggregator.addExtraction(extraction);
//				}
//				scheduleRefresh(this.refreshTime);
//			}

		}

		@Override
		public void collaborate2Model() {

		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<MiningLedgerDataSourceAll, Builder> {
			private MiningLedgerDataSourceAll onConstruction;

			@Override
			protected MiningLedgerDataSourceAll getActual() {
				if (null == this.onConstruction) this.onConstruction = new MiningLedgerDataSourceAll();
				return this.onConstruction;
			}

			@Override
			protected Builder getActualBuilder() {
				return this;
			}

			public Builder withCredential( final Credential credential ) {
				this.getActual().setCredential(credential);
				return this;
			}

			public Builder withMiningRepository( final MiningRepository miningRepository ) {
				this.onConstruction.setMiningRepository(miningRepository);
				return this;
			}

			@Override
			public MiningLedgerDataSourceAll build() {
				// Check additional mandatory fields.
				final MiningLedgerDataSourceAll target = super.build();
				Objects.requireNonNull(target.credential);
				Objects.requireNonNull(target.miningRepository);
				return target;
			}
		}
	}
}
