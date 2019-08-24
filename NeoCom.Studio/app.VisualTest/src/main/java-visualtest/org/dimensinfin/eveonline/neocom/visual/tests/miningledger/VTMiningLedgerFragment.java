package org.dimensinfin.eveonline.neocom.visual.tests.miningledger;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;
import org.dimensinfin.eveonline.neocom.mining.datasource.MiningLedgerDataSourceToday;
import org.dimensinfin.eveonline.neocom.visual.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.visual.app.support.SupportMiningRepository;
import org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday.MiningExtractionDatabaseRecordGenerator;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VTMiningLedgerFragment extends MVCPagerFragment {
	private MiningExtractionDatabaseRecordGenerator miningExtractionDatabaseRecordGenerator;
	private Credential credential;

	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		this.credential = new Credential.Builder(92223647)
				                  .withAccountId(92223647)
				                  .withAccountName("Beth Ripley")
				                  .withAccessToken(
						                  "1|CfDJ8HHFK/DOe6xKoNPHamc0mCW+m3KssaFdj/Gz5yz6LbeUZqk4wH1O/p+at7oiNS9OHwO+YY3wjMe+mXBCSsLKWMnbIf7qXeRyIb4hZZ1EAcrifvXRyD5V+V9NR8f2ti5LTx/QwAwo8g89dRmJyuHoDBFi0D0lpfxJOh9csWRWbozG")
				                  .withRefreshToken(
						                  "bQuMWmfQ8pDzPKaUtkYzFrlrdbajWLbroV6c49QSxPhli3OT2GLqoErgddXgwa2yTWsNEj7zOemFsmey-C4zZE-VV6tdlPHDEUS5w_aU8ckotYF1Ppc3DSdvRGeuVgxLM5CsZq1eNVlQIqyaZDj4aGHk7mRhjuYI8hzhct9Y9vATrF_DdYqvuxhw8RHtfQUc")
				                  .withDataSource("tranquility")
				                  .withScope(
						                  "publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				                  .withAssetsCount(1290)
				                  .withWalletBalance(2.76586637596E9)
				                  .withRaceName("Minmatar")
				                  .build();
		try {
			SupportMiningRepository miningRepository = new SupportMiningRepository.Builder()
					                                           .withMiningExtractionDao(
							                                           NeoComComponentFactory.getSingleton().getNeoComDBHelper()
									                                           .getMiningExtractionDao())
															   .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
					                                           .build();
			this.miningExtractionDatabaseRecordGenerator = new MiningExtractionDatabaseRecordGenerator.Builder()
					                                               .withEsiDataAdapter(NeoComComponentFactory.getSingleton()
							                                                                   .getEsiDataAdapter())
					                                               .withMiningRepository(miningRepository)
					                                               .build();
			this.initializeRepository(miningRepository);

			return new VTMiningLedgerDataSource.Builder()
					       .addIdentifier(this.getVariant())
					       .addIdentifier("VISUAL TESTS")
					       .withVariant(this.getVariant())
					       .withExtras(this.getExtras())
					       .withFactory(this.createFactory())
					       .withCredential(this.credential)
					       .withMiningRepository(miningRepository)
						   .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
					       .build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void initializeRepository( final SupportMiningRepository miningRepository ) {
		miningRepository.deleteAll(); // Clean up the repository.
		this.miningExtractionDatabaseRecordGenerator.loadMockData2Repository(
				this.credential); // Load up the mock data used on the acceptance tests.
	}

	public static class VTMiningLedgerDataSource extends MiningLedgerDataSourceToday {
//		private Credential credential;
//		private MiningRepository miningRepository;
		private LocalDate targetDate;
		private List<Resource> resources = new ArrayList<>();

		@Override
		public void prepareModel() {
			super.prepareModel();
			int i =0;
		}

		@Override
		public void collaborate2Model() {
			if (null == this.targetDate) this.targetDate = LocalDate.now();
			final List<MiningExtraction> extractions = this.miningRepository.accessResources4Date(
					this.credential,
					this.targetDate);
			this.resources.clear();
			for (MiningExtraction extraction : extractions)
				this.resources.add(new Resource(extraction.getTypeId(), extraction.getQuantity()));
			this.addHeaderContents(new DailyExtractionResourcesContainer.Builder()
					                       .withResourceList(this.resources)
					                       .build()); // Add the resource table header

			final PanelTitle title = new PanelTitle("TODAY MINING EXTRACTIONS DELTA [-]");
			if (this.extractions.isEmpty()) title.setTitle("There are no Mining Extractions Today.");
			else title.setTitle("TODAY MINING EXTRACTIONS [" + this.extractions.size() + "]");
			this.addModelContents(title);
			for (ResourceAggregatorContainer panel : this.extractionSystems)
				this.addModelContents(panel);
		}

//		public static class Builder extends MiningLedgerDataSourceToday.Builder {
//			private VTMiningLedgerDataSource onConstruction;
//
//			@Override
//			protected VTMiningLedgerDataSource getActual() {
//				if (null == this.onConstruction) this.onConstruction = new VTMiningLedgerDataSource();
//				return this.onConstruction;
//			}
//
//			@Override
//			protected Builder getActualBuilder() {
//				return this;
//			}
//
////			public VTMiningLedgerDataSource.Builder withCredential( final Credential credential ) {
////				Objects.requireNonNull(credential);
////				this.onConstruction.credential = credential;
////				return this.getActualBuilder();
////			}
////
////			public VTMiningLedgerDataSource.Builder withMiningRepository( final MiningRepository miningRepository ) {
////				Objects.requireNonNull(miningRepository);
////				this.onConstruction.miningRepository = miningRepository;
////				return this.getActualBuilder();
////			}
//		}
	}
}