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
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.contentmngmt.StandardContentManager;
import org.dimensinfin.eveonline.neocom.core.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetContainer;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetsManagerOwner;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.managers.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.ExtendedLocation;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AssetsFragment");

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
		return "";
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
	public IDataSource registerDataSource() {
		AssetsFragment.logger.info(">> [AssetsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		if (getVariant() == NeoComApplication.EVariants.ASSETS_BYLOCATION.name()) {
			int characterid = this.getExtras().getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
					.addIdentifier(characterid)
					.addIdentifier("ASSETS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			return new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setAssetsManager(((IAssetsManagerOwner) this.getAppContext()).accessAssetsManager())
					.setCacheable(true);
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
			AssetsByLocationDataSource ds = new AssetsByLocationDataSource(locator, getVariant(), getFactory(), getExtras());
			ds.setVariant(this.getVariant());
			ds.setFilter(filter);
			return ds;
		}
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
		return null;
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class AssetsByLocationDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		/** Credential to be used on all database requests and data retrievals. */
		protected Credential credential = null;
		protected AssetsManager assetsManager = null;
		protected String filter = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetsByLocationDataSource( final DataSourceLocator locator, final String variant
				, final IPartFactory factory, final Bundle extras ) {
			super(locator, variant, factory, extras);
			//			characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			//			filter = extras.getString(AppWideConstants.EExtras.EXTRA_ASSETFILTER.name());
			//
			//			// Get the Credential for the pilot identifier.
			//			if ( GlobalAndroidDataManager.getResourceBoolean("R.runtime.mockdata", false) ) {
			//				List<Credential> credentialList = null;
			//				try {
			//					final String inFileName = GlobalDataManager.getResourceString("R.runtime.mockdata.location")
			//							+ "accessAllCredentials.data";
			//					final InputStream inFile = GlobalAndroidDataManager.openAsset4Input(inFileName);
			//					try {
			//						final BufferedInputStream buffer = new BufferedInputStream(inFile);
			//						final ObjectInputStream input = new ObjectInputStream(buffer);
			//						try {
			//							credentialList = (List<Credential>) input.readObject();
			//							logger.info("-- [AssetsByLocationDataSource.<constructor>]> Restored credential list: {} entries."
			//									, credentialList.size());
			//						} finally {
			//							input.close();
			//							buffer.close();
			//						}
			//					} catch (final ClassNotFoundException ex) {
			//						logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> ClassNotFoundException."); //$NON-NLS-1$
			//					} catch (final FileNotFoundException fnfe) {
			//						logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> FileNotFoundException."); //$NON-NLS-1$
			//					} catch (final IOException ex) {
			//						logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> IOException."); //$NON-NLS-1$
			//					} catch (final RuntimeException rex) {
			//						rex.printStackTrace();
			//					}
			//				} catch (IOException e) {
			//					e.printStackTrace();
			//				}
			//				for (Credential cred : credentialList) {
			//					if ( cred.getAccountId() == characterid ) credential = cred;
			//				}
			//			}
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public AssetsByLocationDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
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
		 * the list of assets and during the processing we will aggregate them into their containers and them into
		 * locations and them into regions. So all the assets levels will be used and processed. Because this is a
		 * lengthy operation we will add the root Region nodes as soon as they are discovered so the use can start to
		 * expand and play with the already classified items once they are on place.
		 */
		public void collaborate2Model() {
			logger.info(">> [AssetsByLocationDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if (!isCached()) {
				// We expect the contents empty so we have not to do anything before starting.
				//				_dataModelRoot.clean();
				// Get the complete list of assets.
				try {
					// Check that required parameters are not null.
					if (null == this.credential)
						throw new NeoComRuntimeException("[AssetsByLocationDataSource.collaborate2Model]> Required Credential not available.");
					if (null == this.assetsManager)
						throw new NeoComRuntimeException("[AssetsByLocationDataSource.collaborate2Model]> Required Assets Manager not available.");

					// Initialize the data structures to be used for classification.
					final HashMap<Long, Region> regions = new HashMap<>();
					final HashMap<Long, ExtendedLocation> locations = new HashMap<>();
					final HashMap<Long, IAssetContainer> containers = new HashMap<>();
					final HashMap<Long, NeoComAsset> unprocessedAssets = new HashMap<>();

					// Get the asset list for the database.
					List<NeoComAsset> assetList = new ArrayList<>();
//					if (AndroidGlobalDataManager.getResourceBoolean("R.runtime.mockdata", false)) {
//						try {
//							final String inFileName = GlobalDataManager.getResourceString("R.runtime.mockdata.location")
//									+ "accessAllAssets4Credential-" + this.credential.getAccountId() + ".data";
//							final InputStream inFile = AndroidGlobalDataManager.openAsset4Input(inFileName);
//							try {
//								final BufferedInputStream buffer = new BufferedInputStream(inFile);
//								final ObjectInputStream input = new ObjectInputStream(buffer);
//								try {
//									assetList = (List<NeoComAsset>) input.readObject();
//									logger.info("-- [AssetsByLocationDataSource.<constructor>]> Restored asset list: {} entries."
//											, assetList.size());
//								} finally {
//									input.close();
//									buffer.close();
//								}
//							} catch (final ClassNotFoundException ex) {
//								logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> ClassNotFoundException."); //$NON-NLS-1$
//							} catch (final FileNotFoundException fnfe) {
//								logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> FileNotFoundException."); //$NON-NLS-1$
//							} catch (final IOException ex) {
//								logger.warn("W> [MarketDataServer.readMarketDataCacheFromStorage]> IOException."); //$NON-NLS-1$
//							} catch (final RuntimeException rex) {
//								rex.printStackTrace();
//							}
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					} else {
						assetList = new AndroidGlobalDataManager()
								.getNeocomDBHelper()
								.getAssetDao()
								.queryForEq("ownerId", Integer.valueOf(this.credential.getAccountId()).toString());
//					}

					// Process each asset received classifying them by location. The ones with locations not in the SDE get
					// processed last because can be stored into containers.
					// Check if the filter is active.
					boolean filtering = false;
					if (null != filter) filtering = true;

					//--- L O O P   S T A R T
					for (NeoComAsset asset : assetList) {
						// Filter the assets by name.
						if (filtering) {
							final String name = asset.getName();
							final boolean check = name.toLowerCase().contains(filter.toLowerCase());
							if (!check) continue;
						}
						// Check the asset location. If of type UNKNOWN then it should be processed later.
						final EveLocation loc = asset.getLocation();
						if (loc.getTypeId() == ELocationType.UNKNOWN) {
							unprocessedAssets.put(asset.getAssetId(), asset);
							continue;
						}
						// If the assets has a parent then also process it later when we have all the containers in place.
						if (asset.getParentContainerId() > 0) {
							unprocessedAssets.put(asset.getAssetId(), asset);
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
						ExtendedLocation locationHit = locations.get(loc.getID());
						if (null == locationHit) {
							// This is a new location. Add it to the Region and check if this affects the UI.
							// Create and Expanded to allow the hold of contents and set its ContentManager to manual.
							locationHit = new ExtendedLocation(this.credential.getAccountId(), loc);
							locationHit.setContentManager(new StandardContentManager(locationHit));
							if (filtering) locationHit.expand();
							locations.put(loc.getID(), locationHit);
							regionHit.addLocation(locationHit);
							// If the Region is expanded then we should regenerate the UI.
							if (regionHit.isExpanded())
								firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
						}

						//--- A S S E T
						// The Region and the Location were already registered. Add the asset to the Location and check if this
						// affects the UI.
						locationHit.addContent(asset);
						if (locationHit.isExpanded())
							firePropertyChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), null, null);
					}

					// Pending the processing for the orphaned nodes that are contained.
					//					final List<Long> processedIdList = new Vector<>();
					//					for (Long assetId : unprocessedAssets.keySet()) {
					//						// Check if the asset has already been processed.
					//						if ( processedIdList.contains(assetId) ) continue;
					//						else {
					//							//							final NeoComAsset asset = unprocessedAssets.get(assetId);
					//							//							final long parentId = asset.getParentContainerId();
					//							//							if ( parentId > 0 )
					//							processAsset2Location(assetId, unprocessedAssets);
					//						}
					//					}
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
