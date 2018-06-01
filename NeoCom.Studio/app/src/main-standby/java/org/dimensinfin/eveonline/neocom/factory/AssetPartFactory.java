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
package org.dimensinfin.eveonline.neocom.factory;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.part.AssetPart;
import org.dimensinfin.eveonline.neocom.part.Location4AssetsPart;
import org.dimensinfin.eveonline.neocom.part.RegionPart;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetPartFactory (final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart (final ICollaboration node) {
		AssetPartFactory.logger.info("-- [AssetPartFactory.createPart]> Node class: " + node.getClass().getName());
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			if ( node instanceof Region ) {
				IPart part = new RegionPart((Region) node).setFactory(this)
				                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
			if ( node instanceof EveLocation ) {
				IPart part = new Location4AssetsPart((EveLocation) node).setFactory(this)
				                                                        .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
//			if ( node instanceof SpaceContainer ) {
//				IPart part = new ContainerPart((SpaceContainer) node).setFactory(this)
//				                                                  .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//				return part;
//			}
//			if ( node instanceof Ship ) {
//				// There are two renders for Ship. Fitted that are ShipParts and packaged that are AssetParts.
//				if ( ((Ship) node).isPackaged() ) {
//					IPart part = new ShipPart((Ship) node).setFactory(this)
//					                                      .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//					return part;
//				} else {
//					IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
//					                                              .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//					return part;
//				}
//			}
			if ( node instanceof NeoComAsset ) {
				IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
				                                              .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
//			if ( node instanceof Ship.ShipAssetGroup ) {
//				IPart part = new AssetGroupPart((Ship.ShipAssetGroup) node).setFactory(this)
//				                                                           .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//				return part;
//			}
		}
//		if ( this.getVariant() == ENeoComVariants.ASSETS_BYCATEGORY.name() ) {
//			if ( node instanceof AssetCategory ) {
//				IPart part = new AssetCategoryPart((AssetCategory) node).setFactory(this)
//				                                                        .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//				return part;
//			}
//			if ( node instanceof NeoComSimpleAsset ) {
//				IPart part = new NeoComSimpleAssetPart((NeoComSimpleAsset) node).setFactory(this)
//				                                                                .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//				return part;
//			}
//		}

		if ( node instanceof Separator ) {
			IPart part = new SeparatorPart((Separator) node).setFactory(this);
			return part;
		}

		// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
