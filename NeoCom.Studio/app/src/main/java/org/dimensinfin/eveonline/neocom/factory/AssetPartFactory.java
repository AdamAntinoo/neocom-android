//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.factory;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.model.AssetCategory;
import org.dimensinfin.eveonline.neocom.model.NeoComSimpleAsset;
import org.dimensinfin.eveonline.neocom.part.AssetCategoryPart;
import org.dimensinfin.eveonline.neocom.part.NeoComSimpleAssetPart;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetPartFactory(final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart(final ICollaboration node) {
		AssetPartFactory.logger.info("-- [AssetPartFactory.createPart]> Node class: " + node.getClass().getName());
		if (this.getVariant() == ENeoComVariants.ASSETS_BYCATEGORY.name()) {
			if (node instanceof AssetCategory ) {
				IPart part = new AssetCategoryPart((AssetCategory) node).setFactory(this)
				                                                        .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
//			if (node instanceof AssetCategory ) {
//				IPart part = new AssetCategoryPart((AssetCategory) node).setFactory(this)
//				                                                  .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
//				return part;
//			}
			if (node instanceof NeoComSimpleAsset ) {
				IPart part = new NeoComSimpleAssetPart((NeoComSimpleAsset) node).setFactory(this)
				                                                                .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
		}
		if (this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name()) {
			//		if (node instanceof Region) {
			//			IPart part = new RegionPart((Region) node).setFactory(this)
			//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
			//			return part;
			//		}
			//		if (node instanceof EveLocation) {
			//			IPart part = new LocationAssetsPart((EveLocation) node)
			//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
			//			return part;
			//		}
			//		if (node instanceof Separator ) {
			//			// These special separators can configure an specific icon.
			//			IPart part = null;
			//			switch (((Separator) node).getType()) {
			//				case SHIPSECTION_HIGH:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.filtericonhighslot)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				case SHIPSECTION_MED:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.filtericonmediumslot)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				case SHIPSECTION_LOW:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.filtericonlowslot)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				case SHIPSECTION_RIGS:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.filtericonrigslot)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				case SHIPSECTION_DRONES:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.filtericondrones)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				case SHIPSECTION_CARGO:
			//					part = new GroupPart((Separator) node).setIconReference(R.drawable.itemhangar)
			//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
			//					break;
			//				default:
			//					part = new GroupPart((Separator) node).setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING)
			//							.setFactory(this);
			//
			//			}
			//			return part;
			//		}
			//		if (node instanceof Ship ) {
			//			// There are two renders for Ship. Fitted that are ShipParts and packaged that are AssetParts.
			//			if (((Ship) node).isPackaged()) {
			//				IPart part = new ShipPart((NeoComAsset) node)
			//						.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
			//				return part;
			//			} else {
			//				IPart part = new AssetPart((NeoComAsset) node)
			//						.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
			//				return part;
			//			}
			//		}
			//		if (node instanceof SpaceContainer ) {
			//			IPart part = new ContainerPart((NeoComAsset) node)
			//					.setRenderMode(AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION);
			//			part.setFactory(this);
			//			return part;
			//		}
			//		if (node instanceof NeoComAsset) {
			//			IPart part = new AssetPart((NeoComAsset) node)
			//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
			//			return part;
			//		}
			//		if (node instanceof ProcessingAction) {
			//			IPart part = new PlanetaryProcessingActionPart(node)
			//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
			//			return part;
			//		}
		}
		// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
