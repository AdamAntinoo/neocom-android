//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
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
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datasource.ACredentialEnabledDS;
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetsManagerOwner;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.managers.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.IconicGroupFacetedShipsContainer;
import org.dimensinfin.eveonline.neocom.model.Ship;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsShipsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("AssetsShipsFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new AssetPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle() {
		String subtitle = "";
		if ( this.getVariant() == NeoComApplication.EVariants.SHIPS_BYTYPE.name() ) {
			subtitle = "Ships By Type";
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
		logger.info(">> [AssetsShipsFragment.registerDataSource]");
		org.dimensinfin.android.mvc.interfaces.IPartsDataSource ds = null;
		// Get access to the parameters and reconstruct the data chain if required.
		if ( getVariant() == NeoComApplication.EVariants.SHIPS_BYTYPE.name() ) {
			int pilotIdentifier = this.getExtras().getInt(EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator()
					.addIdentifier(this.getVariant())
					.addIdentifier(pilotIdentifier)
					.addIdentifier("SHIPS-MANAGER");
			// On this test we are not going to use Generators but directly the DataSource to ease the connection to the
			// Adapter.
			ds = new ShipsByTypeDataSource(locator, getVariant(), getFactory(), getExtras())
					.setAssetsManager(((IAssetsManagerOwner) this.getAppContext()).accessAssetsManager())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setCacheable(false);
		}
		logger.info("<< [AssetsShipsFragment.registerDataSource]");
		return ds;
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class ShipsByTypeDataSource extends ACredentialEnabledDS {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected AssetsManager assetsManager = null;
		protected String filter = null;

		private List<ICollaboration> dataContents = new ArrayList<>();

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public ShipsByTypeDataSource( final DataSourceLocator locator, final String variant
				, final IPartFactory factory, final Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		public ShipsByTypeDataSource setFilter( final String filter ) {
			this.filter = filter;
			return this;
		}

		public ShipsByTypeDataSource setAssetsManager( final AssetsManager manager ) {
			this.assetsManager = manager;
			return this;
		}

		/**
		 *
		 */
		public void collaborate2Model() {
			logger.info(">> [ShipsByTypeDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if ( !isCached() ) {
				try {
					this.cleanup();
					// Check that required parameters are not null.
					if ( null == this.credential )
						throw new NeoComRuntimeException("[ShipsByTypeDataSource.collaborate2Model]> Required Credential not available.");
					if ( null == this.assetsManager )
						throw new NeoComRuntimeException("[ShipsByTypeDataSource.collaborate2Model]> Required Assets Manager not available.");

					// Initialize the data structures to be used for classification.
					final HashMap<String, IconicGroupFacetedShipsContainer> shipTypesList = new HashMap<>();

					// Get the asset list for the database.
					List<NeoComAsset> assetList = this.assetsManager.getFullAssetList();

					// Process each asset received, extracting the ships and classifying them into their groups.
					// Check if the filter is active.
					boolean filtering = false;
					if ( null != filter ) filtering = true;

					// TODO - Do not use the dynamic feature until it is fully operative.
					// --- L O O P   S T A R T
					for (NeoComAsset asset : assetList) {
						// Filter out all assets but ships.
						if ( asset.getCategoryName().equalsIgnoreCase("Ship") ) {
							if ( filtering ) {
								if ( asset.getGroupName().contains(filter) )
									this.addModelContents(new Ship().copyFrom(asset));
							} else {
								// Insert the ship on the ship category.
								Ship ship = new Ship().copyFrom(asset);
								final String shipGroup = ship.getGroupName();
								IconicGroupFacetedShipsContainer hit = shipTypesList.get(shipGroup);
								if ( null == hit ) {
									hit = new IconicGroupFacetedShipsContainer(shipGroup);
									// Select the icon reference form the list of ship types.
									hit.setIconReference(this.searchIconReference(shipGroup));
									shipTypesList.put(shipGroup, hit);
									this.addModelContents(hit);
								}
								hit.addShip(ship);
							}
						}
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				// Dump the collected contents to the model data source container.
				this.dumpData();
			}
			logger.info("<< [ShipsByTypeDataSource.collaborate2Model]");
		}

		@Override
		public org.dimensinfin.android.mvc.interfaces.IPartsDataSource addModelContents( final ICollaboration newnode ) {
			this.dataContents.add(newnode);
			return this;
		}

		public void dumpData() {
			for (ICollaboration node : this.dataContents) {
				super.addModelContents(node);
			}
		}
		protected int searchIconReference(final String groupName){
			switch (groupName){
				case "Corvette":
					return R.drawable.rookie_64;
				case "Frigate":
					return R.drawable.frigate_64;
				case "Cruiser":
					return R.drawable.cruiser_64;
				case "Industrial":
					return R.drawable.industrial_64;
				case "Combat Battlecruiser":
					return R.drawable.battlecruiser_64;
				case "Battleship":
					return R.drawable.battleship_64;
				default:
					return R.drawable.defaulticonplaceholder;
			}
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
