package org.dimensinfin.eveonline.neocom.mining.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dimensinfin.android.mvc.activity.AMVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.converters.MiningExtraction2ItemPackConverter;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.NeoComAndroid;

public class MiningLedgerFragment extends AMVCPagerFragment {
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
					       .withCacheStatus(false)
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
					       .withCacheStatus(false)
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
