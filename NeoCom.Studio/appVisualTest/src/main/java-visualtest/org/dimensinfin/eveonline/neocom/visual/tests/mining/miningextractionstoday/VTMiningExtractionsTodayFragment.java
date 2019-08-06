package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.visual.app.adapters.NeoComComponentFactory;

public class VTMiningExtractionsTodayFragment extends MVCPagerFragment {
	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		final Credential credential = new Credential.Builder(93813310)
				                              .withAccountId(93813310)
				                              .withAccountName("Perico Tuerto")
				                              .withAccessToken("1|CfDJ8HHFK/DOe6xKoNPHamc0mCXhPScKeLZLosJ4uMphVlAFxrs/gfhbfOFSrZAb+/8ppOOJdta9jvrkDrihXHfAKeef3jntpGDor+iUnTsAaBEOOT6x58gq59jF46W6iSuj2AxFNE8iHs1z65ePOQYEqyQBGWikEvDlb1zQ8PRWfqdN")
				                              .withRefreshToken("O5YPDzUXyJW4S8lCr-lu49YFwSZaE4_GCN59bu1kF2x6fqD_-lYy-n1x7o87D_OctkIMpvzQ2x2whHzXMKKBB1FAzYesVPKfc0lT4s9csGF6aRRBanBhsMZsOEdrPw5kdBRbiOzEwg5EH8IxyYoyCsTY6KnJvyyIt84ee_Svfuc1CM8U3igKGsw7f31xUd5I")
				                              .withDataSource("tranquility")
				                              .withScope("publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				                              .withAssetsCount(1476)
				                              .withWalletBalance(6.309543632E8)
				                              .withRaceName("Amarr")
				                              .build();
		return new VTMiningExtractionsTodayDataSource.Builder()
				       .addIdentifier(this.getVariant())
				       .addIdentifier("VISUAL TESTS")
				       .withVariant(this.getVariant())
				       .withExtras(this.getExtras())
				       .withFactory(this.createFactory())
				       .withCredential(credential)
//				       .withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				       .withMiningRepository(NeoComComponentFactory.getSingleton().getMiningRepository())
				       .build();
	}
}
