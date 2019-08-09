package org.dimensinfin.eveonline.neocom.mining.datasource;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MiningLedgerDataSourceAll extends MiningLedgerDataSourceToday {
	private Credential credential;
	private ESIDataAdapter esiDataAdapter;
	private MiningRepository miningRepository; // The mining persistence repository definition.
	private List<MiningExtraction> extractions;
	protected List<MiningExtractionAggregatorAsPanelTitle> extractionsPanels = new ArrayList<>();

	@Override
	public void prepareModel() {
		// - A L L   E X T R A C T I O N S
//		try {
			extractions = this.miningRepository.accessMiningExtractions4Pilot(credential);
			String processingDate = "";
			MiningExtractionAggregatorAsPanelTitle<MiningExtraction> dateAggregator = null;
			for (MiningExtraction extraction : extractions) {
				// Get the extraction date.
				final String recordDate = extraction.getExtractionDateName().split(":")[0];
				if (!recordDate.equalsIgnoreCase(processingDate)) { // Create a new aggregator if the dates do not match.
					// Create a new aggregator and use it to get the rest of the extractors inside.
					dateAggregator = new MiningExtractionAggregatorAsPanelTitle(recordDate);
					this.extractionsPanels.add(dateAggregator);
					processingDate = recordDate;
				}
				// Only aggregate extractions marked as hour 24 because the others are daily or redundant data.
				if (extraction.getExtractionHour() == 24) dateAggregator.addPack(extraction);
			}
//		} catch (SQLException sqle) {
//			//			AndroidGlobalDataManager.registerException("MiningLedgerDataSourceAll.collaborate2Model"
//			//					, sqle, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
//			sqle.printStackTrace();
//		}
	}

	@Override
	public void collaborate2Model() {
		// - D A T A   S E C T I O N
		final PanelTitle title = new PanelTitle("ALL MINING EXTRACTIONS BY DATE");
		if (extractions.size() < 1)
			title.setTitle("There are no Mining Extractions.");
		//		else
		//			title.setTitle("TODAY MINING EXTRACTIONS [" + extractions.size() + "]");
		this.addModelContents(title);
		for (MiningExtractionAggregatorAsPanelTitle panel : this.extractionsPanels)
			this.addModelContents(panel);
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<MiningLedgerDataSourceAll, Builder> {
		private MiningLedgerDataSourceAll onConstruction;

		@Override
		protected MiningLedgerDataSourceAll getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new MiningLedgerDataSourceAll();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public Builder withCredential( final Credential credential ) {
			this.getActual().credential = credential;
			return this;
		}

		public Builder withMiningRepository( final MiningRepository miningRepository ) {
			this.onConstruction.miningRepository = miningRepository;
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
