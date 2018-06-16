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
package org.dimensinfin.eveonline.neocom.model;

import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ShipFacetedAssetsContainer extends Ship {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("ShipFacetedAssetsContainer");

	// - F I E L D - S E C T I O N ............................................................................
	//	private List<NeoComAsset> contents = new ArrayList<>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ShipFacetedAssetsContainer() {
		super();
	}

	public ShipFacetedAssetsContainer( final NeoComAsset asset ) {
		this();
		this.copyFrom(asset);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Even this object inherits from the asset structure, it is a new instance of the object and we should copy
	 * the data from the original reference to this instance instead using delegates that will not work when
	 * accessing directly to fields.
	 * @return this same instance updated with the reference data.
	 */
	public ShipFacetedAssetsContainer copyFrom( final NeoComAsset asset ) {
		this.id = asset.getDAOID();

		this.setAssetId(asset.getAssetId());
		this.setTypeId(asset.getTypeId());
		this.setQuantity(asset.getQuantity());
		this.setLocationId(asset.getLocationId());
		this.setLocationType(asset.getLocationType());
		this.setLocationFlag(asset.getFlag());
		this.setSingleton(asset.isPackaged());
		this.setParentId(asset.getParentContainerId());

		//- D E R I V E D   F I E L D S
		this.setOwnerId(asset.getOwnerId());
		this.setName(asset.getName());
		this.setCategory(asset.getCategoryName());
		this.setGroupName(asset.getGroupName());
		this.setTech(asset.getTech());
		// blueprintFlag there is no method to copy this field but for containers it is false.
		this.setUserLabel(asset.getUserLabel());
		this.setShip(asset.isShip());
		this.setContainer(asset.isContainer());
		this.setIskValue(asset.getIskValue());
		return this;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ContainerFacetedAssetsContainer [ ");
		//		buffer.append("contentSize: ").append(this.contents.size());
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
