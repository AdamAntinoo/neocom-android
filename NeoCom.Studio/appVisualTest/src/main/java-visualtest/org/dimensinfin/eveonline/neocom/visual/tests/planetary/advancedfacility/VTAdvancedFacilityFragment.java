package org.dimensinfin.eveonline.neocom.visual.tests.planetary.advancedfacility;

import android.view.View;
import android.view.ViewGroup;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.visual.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.visual.tests.ui.VisualTestActionBar;
import org.dimensinfin.eveonline.neocom.visual.tests.ui.VisualTestActionBarController;

public class VTAdvancedFacilityFragment extends MVCPagerFragment {
	private ViewGroup backgroundContainer;

	@Override
	public IControllerFactory createFactory() {
		return new VisualTestsControllerFactory(this.getVariant());
	}

	@Override
	public IDataSource createDS() {
		final Credential credential = new Credential.Builder(92002067)
				.withAccountId(92002067)
				.withAccountName("Adam Antinoo")
				.withAccessToken("lfS7LIBbjLKnglJsujkNERgbwgOE0dCDiudhCdyrBxbxRp1xtFYzTRMxY2G2EssiS44UvvdMfRrXiLtn0SW9Zw")
				.withRefreshToken("oCHpz8dm7MJNZ6PYqRvpWU6IkaD_Z5PsNx9SkI54UkvBY92yIUqEpIiFv03nxnLLnx-w_uTBBmsITYxM7WqzjUio4pXTJJN-GUGb-YNBfe0YNia_fl-NUNlmIGCwIMQCQhLpDZEUmECKUt7Do4T9ZW7FimJrhJUyw5xumUPN-d64oeY7Nd-UO4mc-By8i3aQ")
				.withDataSource("tranquility")
				.withScope("publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1")
				.build();
		return new VTAdvancedFactoryDataSource.Builder()
				.addIdentifier(this.getVariant())
				.addIdentifier("VISUAL TESTS")
				.withVariant(this.getVariant())
				.withExtras(this.getExtras())
				.withFactory(this.createFactory())
				.withCredential(credential)
				.withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
				.withPlanetaryRepository(NeoComComponentFactory.getSingleton().getPlanetaryRepository())
				.build();
	}

	@Override
	public View generateActionBarView() {
		// Create the bar content model.
		final VisualTestActionBar actionBar = new VisualTestActionBar.Builder()
				.withActivityIconReference(R.drawable.planets)
				.withPageName("Advanced Facility Details")
				.build();
		// Get a controller from the model, then the view from the render.
		final VisualTestActionBarController controller =
				(VisualTestActionBarController) new VisualTestsControllerFactory(this.getVariant())
						.createController(actionBar);
		return this.convertActionBarView(controller);
	}
}
