package org.dimensinfin.eveonline.neocom.app.datasource;

import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.asset.provider.AssetProvider;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class AssetsByLocationDataSource extends MVCDataSource {
	private Credential credential;
	protected AssetProvider assetsProvider;
	protected String filter;

	// - C O N S T R U C T O R S
	private AssetsByLocationDataSource() {}

	/**
	 * This action is done in background so it is the right point to tell the assets provider to generate and process the data is not done already.
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
		logger.info( ">> [AssetsByLocationDataSource.collaborate2Model]" );
		// Check if we should use the cached version.
//		if (!isCached()) {
		// Get the complete list of assets.
//		try {
//			// Check that required parameters are not null.
//			if (null == this.credential)
//				throw new NeoComRuntimeException( "[AssetsByLocationDataSource.collaborate2Model]> Required Credential not available." );
//			if (null == this.assetsManager)
//				throw new NeoComRuntimeException( "[AssetsByLocationDataSource.collaborate2Model]> Required Assets Manager not available." );
//
//			// Initialize the data structures to be used for classification.
//			final HashMap<Long, Region> regions = new HashMap<>();
//			final HashMap<Long, LocationFacetedAssetsContainer> locations = new HashMap<>();
//			final HashMap<Long, IAssetContainer> containers = new HashMap<>();
//			final HashMap<Long, NeoComAsset> unprocessedAssets = new HashMap<>();
//
//			// Add the collector Location for orphaned assets.
//			final LocationFacetedAssetsContainer collectorLocation = new LocationFacetedAssetsContainer(
//					new EveLocation().setSystem( "UNKWNOWN DEEP SPACE" )
//			);
//			this.addModelContents( collectorLocation );
//
//			// Get the asset list for the database.
//			List<NeoComAsset> assetList = this.assetsManager.getFullAssetList();
//
//			// Process each asset received classifying them by location. The ones with locations not in the SDE get
//			// processed last because can be stored into containers.
//			// Check if the filter is active.
//			boolean filtering = false;
//			if (null != filter) filtering = true;
//
//			// --- L O O P   S T A R T
//			for (NeoComAsset asset : assetList) {
//				// Create a placeholder where to store the asset being processed.
//				NeoComAsset processingAsset = asset;
//				// Filter the assets by name.
//				if (filtering) {
//					final String name = processingAsset.getName();
//					final boolean check = name.toLowerCase().contains( filter.toLowerCase() );
//					if (!check) continue;
//				}
//				// Check the asset location. If of type UNKNOWN then it should be processed later.
//				final EveLocation loc = processingAsset.getLocation();
//				if (loc.getTypeId() == ELocationType.UNKNOWN) {
//					unprocessedAssets.put( processingAsset.getAssetId(), processingAsset );
//					// If this is a container we have to add it to the container list.
//					if (processingAsset.isContainer()) {
//						processingAsset = new ContainerFacetedAssetsContainer( asset );
//						containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//					}
//					if (processingAsset.isShip()) {
//						processingAsset = new ContainerFacetedAssetsContainer( asset );
//						containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//					}
//					continue;
//				}
//				// If the assets has a parent then also process it later when we have all the containers in place.
//				if (processingAsset.getParentContainerId() > 0) {
//					unprocessedAssets.put( processingAsset.getAssetId(), processingAsset );
//					// If this is a container we have to add it to the container list.
//					if (processingAsset.isContainer()) {
//						processingAsset = new ContainerFacetedAssetsContainer( asset );
//						containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//					}
//					if (processingAsset.isShip()) {
//						processingAsset = new ContainerFacetedAssetsContainer( asset );
//						containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//					}
//					continue;
//				}
//
//				//--- R E G I O N
//				// Search for the Location's region. Create a new one if not found.
//				final long regid = loc.getRegionId();
//				// Search for regions on the list of already detected Regions.
//				Region regionHit = regions.get( regid );
//				if (null == regionHit) {
//					// Create a new Region. This changes the UI so notify that to the Adapter.
//					regionHit = new Region( loc.getRegionId(), loc.getRegion() );
//					if (filtering) regionHit.expand();
//					regions.put( regid, regionHit );
//					addModelContents( regionHit );
//					logger.info( "-- [AssetsByLocationDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot."
//							, regionHit.toString() );
//				}
//
//				//--- L O C A T I O N
//				// Check if this Location is already present on this Region.
//				LocationFacetedAssetsContainer locationHit = locations.get( loc.getID() );
//				if (null == locationHit) {
//					// This is a new location. Add it to the Region and check if this affects the UI.
//					// Create and Expanded to allow the hold of contents and set its ContentManager to manual.
//					locationHit = new LocationFacetedAssetsContainer( loc );
//					//							locationHit.setContentManager(new StandardContentManager(locationHit));
//					if (filtering) locationHit.expand();
//					locations.put( loc.getID(), locationHit );
//					regionHit.addLocation( locationHit );
//					// If the Region is expanded then we should regenerate the UI.
//					if (regionHit.isExpanded())
//						firePropertyChange( SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null );
//				}
//
//				//--- C O N T A I N E R
//				// If this is a container we have to add it to the container list.
//				if (processingAsset.isContainer()) {
//					processingAsset = new ContainerFacetedAssetsContainer( asset );
//					containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//				}
//				if (processingAsset.isShip()) {
//					processingAsset = new ShipFacetedAssetsContainer( asset );
//					containers.put( processingAsset.getAssetId(), (IAssetContainer) processingAsset );
//				}
//
//				//--- A S S E T
//				// The Region and the Location were already registered. Add the asset to the Location and check if this
//				// affects the UI.
//				locationHit.addAsset( processingAsset );
//				if (locationHit.isExpanded())
//					firePropertyChange( SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null );
//			}
//
//			// Add orphaned assets to their parent containers if they exist.
//			for (NeoComAsset asset : unprocessedAssets.values()) {
//				// Search the parent in the current list of containers.
//				final long parentId = asset.getParentContainerId();
//				final IAssetContainer hit = containers.get( parentId );
//				if (null != hit) {
//					// Connect to the parent.
//					hit.addAsset( asset );
//				} else collectorLocation.addAsset( asset );
//			}
//		} catch (SQLException sqle) {
//			sqle.printStackTrace();
//		}
		logger.info( "<< [AssetsByLocationDataSource.collaborate2Model]" );
		//				return _dataModelRoot;
	}
//	}

	//		private void processAsset2Location (final long assetIdentifier, final HashMap<Long, NeoComAsset> unprocessedAssets) {
	//			final NeoComAsset asset = unprocessedAssets.get(assetIdentifier);
	//			final long parentId = asset.getParentContainerId();
	//			if ( parentId > 0 ) processAsset2Location(parentId, unprocessedAssets);
	//			else{
	//				// This is an end asset. Add it to the location and also to the processed list.
	//				ExtendedLocation locationHit = locations.get(loc.getID());
	//
	//			}
	//		}
	//		protected Activity getContext () {
	//			return context;
	//		}

	//		protected void setContext (final Activity activity) {
	//			this.context = activity;
	//		}

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
