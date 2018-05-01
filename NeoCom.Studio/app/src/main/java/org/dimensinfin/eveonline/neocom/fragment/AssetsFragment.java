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

import android.app.Activity;
import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetContainer;
import org.dimensinfin.eveonline.neocom.manager.StandardContentManager;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.ExtendedLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AssetsFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory () {
		return new AssetPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle () {
		String subtitle = "";
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByLocation);
		}
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYCATEGORY.name() ) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByCategory);
		}
		return subtitle;
	}

	@Override
	public String getTitle () {
		return this.getPilotName();
	}

	@Override
	protected List<ICollaboration> registerHeaderSource () {
		return new ArrayList<>();
	}

	/**
	 * Complete rework of the Model adaptation process to populate the UI ListView.
	 * The update of the part list that is going to be feed into the ListView is going to be executed dynamically while
	 * the model and the rest of the objects are being generated.
	 */
	@Override
	public IDataSource registerDataSource () {
		AssetsFragment.logger.info(">> [AssetsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		if ( getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			int characterid = this.getExtras().getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
			                                                   .addIdentifier(characterid)
			                                                   .addIdentifier("ASSETS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			AssetsByLocationDataSource ds = new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras());
			ds.setVariant(this.getVariant());
//			ds.setContext(getActivity());
			ds.setCacheable(false);
			return ds;
		}
		if ( getVariant() == ENeoComVariants.ASSETS_SEARCH.name() ) {
			int characterid = this.getExtras().getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			String filter = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_ASSETFILTER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
			                                                   .addIdentifier(characterid)
			                                                   .addIdentifier("ASSETS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			AssetsByLocationDataSource ds = new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras());
			ds.setVariant(this.getVariant());
			ds.setFilter(filter);
//			ds.setContext(getActivity());
			return ds;
		}
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
		return null;
	}

	private String getPilotName () {
		return NeoComModelStore.getActiveCredential().getAccountName();
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class AssetsByLocationDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		//		private final DataSourceLocator locator;
		private int characterid = -1;
		//		private Activity context;
		private String filter = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetsByLocationDataSource (final DataSourceLocator locator, final String variant, final IPartFactory
				factory, final Bundle extras) {
			super(locator, variant, factory, extras);
			//			this.locator = locator;
			characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			filter = extras.getString(AppWideConstants.EExtras.EXTRA_ASSETFILTER.name());
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		//		public DataSourceLocator getDataSourceLocator () {
		//			return locator;
		//		}

		/**
		 * This adapter should generate the model for all the assets aggregated by Location. We are going to process all
		 * the list of assets and during the processing we will aggregate them into their containers and them into
		 * locations and them into regions. So all the assets levels will be used and processed. Because this is a
		 * lengthy operation we will add the root Region nodes as soon as they are discovered so the use can start to
		 * expand and play with the already classified items once they are on place.
		 */
		public void collaborate2Model () {
			logger.info(">> [AssetsByLocationDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if ( !isCached() ) {
				// We expect the contents empty so we have not to do anything before starting.
				//				_dataModelRoot.clean();
				// Get the complete list of assets.
				try {
					// Initialize the data structures to be used for classification.
					final HashMap<Long, Region> regions = new HashMap<>();
					final HashMap<Long, ExtendedLocation> locations = new HashMap<>();
					final HashMap<Long, IAssetContainer> containers = new HashMap<>();
					final List<NeoComAsset> unprocessedAssets = new ArrayList<>();

					// Get the asset list for the database.
					final List<NeoComAsset> assetList = GlobalAndroidDataManager.getNeocomDBHelper().getAssetDao().queryForEq(
							"ownerID"
							, Integer.valueOf(characterid).toString());

					// Process each asset received classifying them by location. The ones with locations not in the SDE get
					// processed last because can be stored into containers.
					// Check if the filter is active.
					boolean filtering = false;
					if ( null != filter ) filtering = true;

					//--- L O O P   S T A R T
					for (NeoComAsset asset : assetList) {
						// Filter the assets by name.
						if ( filtering ) {
							final String name = asset.getName();
							final boolean check = name.toLowerCase().contains(filter.toLowerCase());
							if ( !check ) continue;
						}
						// Check the asset location. If of type UNKNOWN then it should be processed later.
						final EveLocation loc = asset.getLocation();
						if ( loc.getTypeID() == ELocationType.UNKNOWN ) {
							unprocessedAssets.add(asset);
							continue;
						}

						//--- R E G I O N
						// Search for the Location's region. Create a new one if not found.
						final long regid = loc.getRegionID();
						// Search for regions on the list of already detected Regions.
						Region regionHit = regions.get(regid);
						if ( null == regionHit ) {
							// Create a new Region. This changes the UI so notify that to the Adapter.
							regionHit = new Region(loc.getRegion());
							if ( filtering ) regionHit.expand();
							regions.put(regid, regionHit);
							addModelContents(regionHit);
							logger.info("-- [AssetsByLocationDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot."
									, regionHit.toString());
						}

						//--- L O C A T I O N
						// Check if this Location is already present on this Region.
						ExtendedLocation locationHit = locations.get(loc.getID());
						if ( null == locationHit ) {
							// This is a new location. Add it to the Region and check if this affects the UI.
							// Create and Expanded to allow the hold of contents and set its ContentManager to manual.
							locationHit = new ExtendedLocation(characterid, loc);
							locationHit.setContentManager(new StandardContentManager(locationHit));
							if ( filtering ) locationHit.expand();
							locations.put(loc.getID(), locationHit);
							regionHit.addLocation(locationHit);
							// If the Region is expanded then we should regenerate the UI.
							if ( regionHit.isExpanded() )
								firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
						}

						//--- A S S E T
						// The Region and the Location were already registered. Add the asset to the Location and check if this
						// affects the UI.
						locationHit.addContent(asset);
						if ( locationHit.isExpanded() )
							firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				logger.info("<< [AssetsByLocationDataSource.collaborate2Model]");
//				return _dataModelRoot;
			}
		}

//		protected Activity getContext () {
//			return context;
//		}

//		protected void setContext (final Activity activity) {
//			this.context = activity;
//		}

		protected void setFilter (final String filter) {
			this.filter = filter;
		}
	}
}
//- UNUSED CODE ............................................................................................
