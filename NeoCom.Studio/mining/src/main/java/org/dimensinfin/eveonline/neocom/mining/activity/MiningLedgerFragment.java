package org.dimensinfin.eveonline.neocom.mining.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.NameFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.mining.TodayMiningTimeLimitedMinigExtractionContainer;
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
			return new MiningLedgerDataSource.Builder()
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

	// - M I N I N G L E D G E R D A T A S O U R C E
	public static class MiningLedgerDataSource extends AMVCDataSource {
		private Credential credential;
		private MiningRepository miningRepository;
		private List<MiningExtraction> extractions = new ArrayList<>();

		public MiningLedgerDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}

		public MiningLedgerDataSource setMiningRepository( final MiningRepository miningRepository ) {
			this.miningRepository = miningRepository;
			return this;
		}

		@Override
		public void prepareModel() {
			try {
				// - E X T R A C T I O N S   T O D A Y
				if (getVariant() == MiningLedgerActivity.EMiningVariants.MINING_EXTRACTIONS_TODAY.name()) {
					// Add the panel title up front of the Credential list.
					final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
					int extractionCount = 0;
					this.addModelContents(title);

					extractions = miningRepository.accessTodayMiningExtractions4Pilot(this.credential);
					// TODO - Add the InformationPanel component.
					//					if (extractions.size() < 1) {
					//						// The list ofr today is empty. Put a label to report that case.
					//						this.addModelContents(new InformationPanel("There are no Mining Extractions Today."));
					//					}

					// Create a map of all the record ids
					Map<String, MiningExtraction> ids = new HashMap();
					for (MiningExtraction extraction : extractions) {
						ids.put(extraction.getRecordId(), extraction);
					}
					// Process the records in order. Aggregate by system.
					int processingSystem = -1;
					NameFacetedMiningExtractionContainer systemAggregator = null;
					for (MiningExtraction extraction : extractions) {
						// Get the extraction date and the hour.
						final String recordDate = extraction.getExtractionDate().split(":")[0];
						final int hour = extraction.getExtractionHour();
						final int system = extraction.getSolarSystemId();

						// Search on the ids if this record should have a previous hour record. This is the current id less 1.
						final String searchId = MiningExtraction.generateRecordId(recordDate, hour - 1
								, extraction.getTypeId(), system, extraction.getOwnerId());
						// Check if the id exists.
						if (ids.keySet().contains(searchId)) {
							// Get the previous record and perform the update before adding the record.
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
							systemAggregator = new TodayMiningTimeLimitedMinigExtractionContainer(extraction.getSystemName());
							this.addModelContents(systemAggregator);
							processingSystem = extraction.getSolarSystemId();
						}
						systemAggregator.addExtraction(extraction);
						extractionCount++;
					}
					// Update the number of extractions.
					title.setTitle("TODAY MINING EXTRACTIONS [" + extractionCount + "]");
					//					scheduleRefresh(this.refreshTime);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		@Override
		public void collaborate2Model() {

		}

		// - B U I L D E R
		public static class Builder extends AMVCDataSource.BaseBuilder<MiningLedgerDataSource, Builder> {
			private MiningLedgerDataSource onConstruction;

			@Override
			protected MiningLedgerDataSource getActual() {
				if (null == this.onConstruction) this.onConstruction = new MiningLedgerDataSource();
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
		}
	}
}
