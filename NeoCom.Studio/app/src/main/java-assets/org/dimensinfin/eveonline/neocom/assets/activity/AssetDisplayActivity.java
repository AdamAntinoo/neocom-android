package org.dimensinfin.eveonline.neocom.assets.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.asset.provider.AssetProvider;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

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
public class AssetDisplayActivity extends ACredentialActivity {
	private AssetProvider assetsProvider; // Use the same assets management instance for all the pages so only a copy is processed

	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		NeoComLogger.enter();
		super.onCreate( savedInstanceState );
		this.assetsProvider = new AssetProvider.Builder()
				                      .withCredential( this.getCredential() )
				                      .withAssetRepository( NeoComComponentFactory.getSingleton().getAssetRepository() )
				                      .withLocationCatalogService( NeoComComponentFactory.getSingleton().getLocationCatalogService() )
				                      .build();
		this.addPage( new AssetsByLocationFragment()
				              .setAssetProvider( this.assetsProvider )
//				              .setCredential( this.getCredential() )
				              .setVariant( PageNamesType.ASSETS_BYLOCATION.name() ) );
		NeoComLogger.exit();
	}
}
