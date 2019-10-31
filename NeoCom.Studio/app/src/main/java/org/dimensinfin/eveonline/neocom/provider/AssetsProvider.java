package org.dimensinfin.eveonline.neocom.provider;

import android.annotation.SuppressLint;
import android.util.LongSparseArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;

@SuppressLint("UseSparseArrays")
public class AssetsProvider implements Serializable {
	private static final long serialVersionUID = -4896485833695914012L;
	private static final Logger logger = LoggerFactory.getLogger( AssetsProvider.class );
	private Credential credential;
	// - C O M P O N E N T S
	private AssetsRepository assetsRepository;

	private LocalTime assetsReadTime;
	private int assetCounter = 0;
	/** Use a map to allow the removal of more nodes during the processing. */
	private transient HashMap<Long, NeoAsset> assetMap = new HashMap<>();

	// - P R I V A T E   I N T E R C H A N G E   V A R I A B L E S
	/**
	 * Used during the processing of the assets into the different structures.
	 */
	private transient LongSparseArray<NeoAsset> assetMap = new LongSparseArray<NeoAsset>();
	private List<NeoAsset> unlocatedAssets = new ArrayList<>();

	public void classifyAssetsByLocation() {
		if (this.verifyTimeStamp()) return;
		this.clear();
		for (NeoAsset asset : this.assetsRepository.findAllByOwnerId( this.credential.getAccountId() ))
			this.assetMap.put( asset.getAssetId(), asset );
		// Process the map until all elements are removed.
		Long key = this.assetMap.keySet().iterator().next();
		this.assetCounter++;
		NeoAsset point = this.assetMap.get(key);
		while (null != point) {
			this.processElement(point);
			key = this.assetMap.keySet().iterator().next();
			point = this.assetMap.get(key);
		}
	}

	private boolean verifyTimeStamp() {
		if (null == this.assetsReadTime) return false;
		// TODO - verify that the time stamp has elapsed to get a new list of assets updated.
		return false;
	}

	private void clear() {
		this.assetCounter = 0;
		this.assetMap.clear();
//		regions.clear();
//		locations.clear();
//		containers.clear();
//		assetsAtContainer.clear();
//		ships.clear();
		int assetCounter = 0;

	}
	/**
	 * Get one asset and performs some checks to transform it into another type or to process its parentship
	 * because with the flat listing there is only relationship through the location id. <br>
	 * If the Category of the asset is a container or a ship then it is encapsulated into another type that
	 * specializes the view presentation. This is the case of Containers and Ships. <br>
	 * If it found one of those items gets the list of contents to be removed to the to be processed list
	 * because the auto model generation will already include those items. Only Locations or Regions behave
	 * differently.
	 */
	private void processElement( final NeoAsset asset ) {
		try {
			this.assetMap.remove(asset.getAssetId()); // Remove the element from the map as is to be processed.
			// Add the asset to the verification count.
			//			verificationAssetCount++;
			// Add the asset value to the owner balance.
//			totalAssetsValue += asset.getIskValue();
			// Transform the asset if on specific categories like Ship or Container
			if (asset.isShip()) {
				// TODO - Ship class is still under development.
				// Check if the ship is packaged. If packaged leave it as a simple asset.
//				if (!asset.isPackaged()) {
//					// Transform the asset to a ship.
//					Ship ship = new Ship(/*getCredentialIdentifier()*/).copyFrom(asset);
//					ships.put(ship.getAssetId(), ship);
//					// The ship is a container so add it and forget about this asset.
//					if (ship.hasParent()) {
//						this.processElement(ship.getParentContainer());
//					} //else {
//					this.add2Location(ship);
//					// Remove all the assets contained because they will be added in the call to collaborate2Model
//					// REFACTOR set the default variant as a constant even that information if defined at other project
//					List<ICollaboration> removableList = ship.collaborate2Model("DEFAULT");
//					// The list returned is not the real list of assets contained but the list of Separators
//					for (ICollaboration node : removableList) {
//						this.removeNode(node);
//					}
//				} else {
					this.add2Location(asset);
//				}
				return;
			}
			if (asset.isContainer()) {
				// Check if the asset is packaged. If so leave as asset
				if (!asset.isPackaged()) {
					// Transform the asset to a ship.
					SpaceContainer container = new SpaceContainer().copyFrom(asset);
					containers.put(container.getAssetId(), container);
					// The container is a container so add it and forget about this asset.
					if (container.hasParent()) {
						this.processElement(container.getParentContainer());
					} // else {
					this.add2Location(container);
					// Remove all the assets contained because they will be added in the call to collaborate2Model
					// REFACTOR set the default variant as a constant even that information if defined at other project
					List<ICollaboration> removableList = container.collaborate2Model("DEFAULT");
					// The list returned is not the real list of assets contained but the list of Separators
					for (ICollaboration node : removableList) {
						this.removeNode(node);
					}
				} else {
					this.add2Location(asset);
				}
				//				// Remove all the assets contained because they will be added in the call to collaborate2Model
				//				ArrayList<AbstractComplexNode> removable = asset.collaborate2Model("REPLACE");
				//				for (AbstractComplexNode node : removable) {
				//					assetMap.remove(((Container) node).getAssetID());
				//				}
				//	}
				return;
			}
			// Process the asset parent if this is the case because we should add first parent to the hierarchy
			if (asset.hasParent()) {
				NeoComAsset parent = asset.getParentContainer();
				if (null == parent) {
					this.add2Location(asset);
				} else {
					this.processElement(parent);
				}
			} else {
				this.add2Location(asset);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void add2Location( final NeoAsset asset ) {
		long locid = asset.getLocationId();
		ExtendedLocation target = locations.get(locid);
		if (null == target) {
			EsiLocation intermediary = GlobalDataManager.searchLocation4Id(locid);
			// Create another new Extended Location as a copy if this one to disconnect it from the unique cache copy.
			ExtendedLocation newloc = new ExtendedLocation(credential, intermediary);
			newloc.setContentManager(new PlanetaryAssetsContentManager(newloc));
			locations.put(new Long(locid), target);
			this.add2Region(target);
			newloc.addContent(asset);
		} else {
			target.addContent(asset);
		}
	}

	// - B U I L D E R
	public static class Builder {
		private AssetsProvider onConstruction;

		public Builder() {
			this.onConstruction = new AssetsProvider();
		}

		public AssetsProvider.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull( credential );
			this.onConstruction.credential = credential;
			return this;
		}

		public AssetsProvider.Builder withAssetsRepository( final AssetsRepository assetsRepository ) {
			Objects.requireNonNull( assetsRepository );
			this.onConstruction.assetsRepository = assetsRepository;
			return this;
		}

		public AssetsProvider build() {
			Objects.requireNonNull( this.onConstruction.credential );
			Objects.requireNonNull( this.onConstruction.assetsRepository );
			return this.onConstruction;
		}
	}
}