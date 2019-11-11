package org.dimensinfin.eveonline.neocom.assets.activity;

import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.asset.provider.AssetProvider;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.domain.space.Region;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class AssetsByLocationDataSource extends MVCDataSource {
	private Credential credential;
	private AssetProvider assetsProvider;
	private String filter;

	// - C O N S T R U C T O R S
	private AssetsByLocationDataSource() {}

	/**
	 * This action is done in background so it is the right point to tell the assets provider to read the repository records and
	 * process if not done already.
	 */
	@Override
	public void prepareModel() {
		this.assetsProvider.classifyAssetsByLocation();
	}

	/**
	 * This adapter should generate the model for all the assets aggregated by Location. We are going to process all
	 * the list of assets and during the processing we will aggregate them into their locations as registered on their final
	 * location. We skip all the assets that are located into containers  to be processed on a second phase.
	 *
	 * During the second phase we can be sure that the containers are already on the container list and also linked to their
	 * location if this is a real location. If not a location the asset will be also on the unprocessed list.
	 *
	 * Because this is a lengthy operation we will add the root Region nodes as soon as they are discovered so the use can start to
	 * expand and play with the already classified items once they are on place.
	 */
	public void collaborate2Model() {
		NeoComLogger.enter();
		for ( Region region :this.assetsProvider.getRegionList()){
			this.addModelContents( region );
		}
		NeoComLogger.exit();
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<AssetsByLocationDataSource, AssetsByLocationDataSource.Builder> {
		private AssetsByLocationDataSource onConstruction;

		@Override
		protected AssetsByLocationDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new AssetsByLocationDataSource();
			return this.onConstruction;
		}

		@Override
		protected AssetsByLocationDataSource.Builder getActualBuilder() {
			return this;
		}

		public AssetsByLocationDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull( credential );
			this.onConstruction.credential = credential;
			return this;
		}

		public AssetsByLocationDataSource.Builder withAssetProvider( final AssetProvider assetsProvider ) {
			Objects.requireNonNull( assetsProvider );
			this.onConstruction.assetsProvider = assetsProvider;
			return this;
		}
		public AssetsByLocationDataSource.Builder optionalFilter( final String filter ) {
			this.onConstruction.filter = filter;
			return this;
		}
	}
}
