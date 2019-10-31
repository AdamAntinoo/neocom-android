package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.provider.AssetsProvider;

/**
 * This activity collects tha pages related to the presentation and aggregation og pilot's assets. The different pages show the same
 * asset record but filtered and presented in different aggregations.
 * The Activity relays the data access to a Manager so the data can be reused by different pages and does not need to be reloaded
 * every time se swipe to another page.
 *
 * P A R A M E T E R S
 * This Activity requires the <b>CAPSULEER_IDENTIFIER</b> to be able to locate the current active pilot's assets.
 *
 * @author Adam Antinoo
 */
public class AssetsManagerActivity extends ACredentialActivity {
	private AssetsProvider assetsProvider; // use the same assets manager for all the pages so only a copy is processed

	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [AssetsManagerActivity.onCreate]");
		super.onCreate(savedInstanceState);
		this.assetsProvider = new AssetsProvider.Builder()
				                     .withCredential(this.getCredential())
				                     .withAssetsRepository( NeoComComponentFactory.getSingleton().getAssetsRepository() )
				                     .build();
		this.addPage(new AssetsFragment()
				             .setAssetsProvider(this.assetsProvider)
				             .setVariant(PageNamesType.ASSETS_BYLOCATION.name()));
//			this.addPage(new AssetsShipsFragment().setVariant(NeoComApplication.EVariants.SHIPS_BYTYPE.name()));
		logger.info("<< [AssetsManagerActivity.onCreate]");
	}
}
