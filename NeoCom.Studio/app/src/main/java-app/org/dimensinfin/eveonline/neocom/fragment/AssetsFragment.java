//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datasource.ACredentialEnabledDS;
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetContainer;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetsManagerOwner;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.managers.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.ContainerFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.LocationFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.model.ShipFacetedAssetsContainer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new AssetPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle() {
		String subtitle = "";
		if (this.getVariant() == NeoComApplication.EVariants.ASSETS_BYLOCATION.name()) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByLocation);
		}
		if (this.getVariant() == NeoComApplication.EVariants.ASSETS_BYCATEGORY.name()) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByCategory);
		}
		return subtitle;
	}

	@Override
	public String getTitle() {
		return "ASSETS MANAGER";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	/**
	 * Complete rework of the Model adaptation process to populate the UI ListView.
	 * The update of the part list that is going to be feed into the ListView is going to be executed dynamically while
	 * the model and the rest of the objects are being generated.
	 */
	@Override
	public org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		AssetsFragment.logger.info(">> [AssetsFragment.registerDataSource]");
		org.dimensinfin.android.mvc.interfaces.IPartsDataSource ds = null;
		// Get access to the parameters and reconstruct the data chain if required.
		if (getVariant() == NeoComApplication.EVariants.ASSETS_BYLOCATION.name()) {
			int pilotIdentifier = this.getExtras().getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator()
					.addIdentifier(this.getVariant())
					.addIdentifier(pilotIdentifier)
					.addIdentifier("ASSETS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			ds = new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras())
					.setAssetsManager(((IAssetsManagerOwner) this.getAppContext()).accessAssetsManager())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setCacheable(false);
		}
		if (getVariant() == NeoComApplication.EVariants.ASSETS_SEARCH.name()) {
			int characterid = this.getExtras().getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
			String filter = this.getExtras().getString(NeoComApplication.EExtras.ASSETFILTER_DEFINITION.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
					.addIdentifier(characterid)
					.addIdentifier("ASSETS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			ds = new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras())
					.setAssetsManager(((IAssetsManagerOwner) this.getAppContext()).accessAssetsManager())
					.setFilter(filter)
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setCacheable(false);
		}
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
		return ds;
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class AssetsByLocationDataSource extends ACredentialEnabledDS {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected AssetsManager assetsManager = null;
		protected String filter = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetsByLocationDataSource( final DataSourceLocator locator, final String variant
				, final IPartFactory factory, final Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		public AssetsByLocationDataSource setFilter( final String filter ) {
			this.filter = filter;
			return this;
		}

		public AssetsByLocationDataSource setAssetsManager( final AssetsManager manager ) {
			this.assetsManager = manager;
			return this;
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
			logger.info(">> [AssetsByLocationDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if (!isCached()) {
				// Get the complete list of assets.
				try {
					// Check that required parameters are not null.
					if (null == this.credential)
						throw new NeoComRuntimeException("[AssetsByLocationDataSource.collaborate2Model]> Required Credential not available.");
					if (null == this.assetsManager)
						throw new NeoComRuntimeException("[AssetsByLocationDataSource.collaborate2Model]> Required Assets Manager not available.");

					// Initialize the data structures to be used for classification.
					final HashMap<Long, Region> regions = new HashMap<>();
					final HashMap<Long, LocationFacetedAssetsContainer> locations = new HashMap<>();
					final HashMap<Long, IAssetContainer> containers = new HashMap<>();
					final HashMap<Long, NeoComAsset> unprocessedAssets = new HashMap<>();

					// Add the collector Location for orphaned assets.
					final LocationFacetedAssetsContainer collectorLocation = new LocationFacetedAssetsContainer(
							new EveLocation().setSystem("UNKWNOWN DEEP SPACE")
					);
					this.addModelContents(collectorLocation);

					// Get the asset list for the database.
					List<NeoComAsset> assetList = this.assetsManager.getFullAssetList();

					// Process each asset received classifying them by location. The ones with locations not in the SDE get
					// processed last because can be stored into containers.
					// Check if the filter is active.
					boolean filtering = false;
					if (null != filter) filtering = true;

					// --- L O O P   S T A R T
					for (NeoComAsset asset : assetList) {
						// Create a placeholder where to store the asset being processed.
						NeoComAsset processingAsset = asset;
						// Filter the assets by name.
						if (filtering) {
							final String name = processingAsset.getName();
							final boolean check = name.toLowerCase().contains(filter.toLowerCase());
							if (!check) continue;
						}
						// Check the asset location. If of type UNKNOWN then it should be processed later.
						final EveLocation loc = processingAsset.getLocation();
						if (loc.getTypeId() == ELocationType.UNKNOWN) {
							unprocessedAssets.put(processingAsset.getAssetId(), processingAsset);
							// If this is a container we have to add it to the container list.
							if (processingAsset.isContainer()) {
								processingAsset = new ContainerFacetedAssetsContainer(asset);
								containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
							}
							if (processingAsset.isShip()) {
								processingAsset = new ContainerFacetedAssetsContainer(asset);
								containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
							}
							continue;
						}
						// If the assets has a parent then also process it later when we have all the containers in place.
						if (processingAsset.getParentContainerId() > 0) {
							unprocessedAssets.put(processingAsset.getAssetId(), processingAsset);
							// If this is a container we have to add it to the container list.
							if (processingAsset.isContainer()) {
								processingAsset = new ContainerFacetedAssetsContainer(asset);
								containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
							}
							if (processingAsset.isShip()) {
								processingAsset = new ContainerFacetedAssetsContainer(asset);
								containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
							}
							continue;
						}

						//--- R E G I O N
						// Search for the Location's region. Create a new one if not found.
						final long regid = loc.getRegionId();
						// Search for regions on the list of already detected Regions.
						Region regionHit = regions.get(regid);
						if (null == regionHit) {
							// Create a new Region. This changes the UI so notify that to the Adapter.
							regionHit = new Region(loc.getRegionId(), loc.getRegion());
							if (filtering) regionHit.expand();
							regions.put(regid, regionHit);
							addModelContents(regionHit);
							logger.info("-- [AssetsByLocationDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot."
									, regionHit.toString());
						}

						//--- L O C A T I O N
						// Check if this Location is already present on this Region.
						LocationFacetedAssetsContainer locationHit = locations.get(loc.getId());
						if (null == locationHit) {
							// This is a new location. Add it to the Region and check if this affects the UI.
							// Create and Expanded to allow the hold of contents and set its ContentManager to manual.
							locationHit = new LocationFacetedAssetsContainer(loc);
							//							locationHit.setContentManager(new StandardContentManager(locationHit));
							if (filtering) locationHit.expand();
							locations.put(loc.getId(), locationHit);
							regionHit.addLocation(locationHit);
							// If the Region is expanded then we should regenerate the UI.
							if (regionHit.isExpanded())
								firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
						}

						//--- C O N T A I N E R
						// If this is a container we have to add it to the container list.
						if (processingAsset.isContainer()) {
							processingAsset = new ContainerFacetedAssetsContainer(asset);
							containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
						}
						if (processingAsset.isShip()) {
							processingAsset = new ShipFacetedAssetsContainer(asset);
							containers.put(processingAsset.getAssetId(), (IAssetContainer) processingAsset);
						}

						//--- A S S E T
						// The Region and the Location were already registered. Add the asset to the Location and check if this
						// affects the UI.
						locationHit.addAsset(processingAsset);
						if (locationHit.isExpanded())
							firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
					}

					// Add orphaned assets to their parent containers if they exist.
					for (NeoComAsset asset : unprocessedAssets.values()) {
						// Search the parent in the current list of containers.
						final long parentId = asset.getParentContainerId();
						final IAssetContainer hit = containers.get(parentId);
						if (null != hit) {
							// Connect to the parent.
							hit.addAsset(asset);
						} else collectorLocation.addAsset(asset);
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				logger.info("<< [AssetsByLocationDataSource.collaborate2Model]");
				//				return _dataModelRoot;
			}
		}

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

	}
}
//- UNUSED CODE ............................................................................................
