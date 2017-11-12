//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.factory;


import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.constants.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.models.NeoComAsset;
import org.dimensinfin.eveonline.neocom.parts.AssetPart;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetPartFactory /*extends PartFactory*/ implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger ("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................
	private final String variant;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetPartFactory(final String selectedVariant) {
		variant = selectedVariant;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getVariant() {
		return variant;
	}

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	public IPart createPart (final AbstractComplexNode node) {
		logger.info ("-- [AssetPartFactory.createPart]> Node class: " + node.getClass ().getName ());
		//		if (node instanceof Region) {
		//			IPart part = new RegionPart((Region) node).setFactory(this)
		//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
		//			return part;
		//		}
//		if ( node instanceof NeoComLocation ) {
//			IPart part = new LocationAssetsPart ((EveLocation) node)
//					.setRenderMode (ENeoComVariants.valueOf (this.getVariant ()).hashCode ()).setFactory (this);
//			return part;
//		}
		//		if (node instanceof Separator) {
		//			// These special separators can configure an specific icon.
		//			IPart part = null;
		//			switch (((Separator) node).getType()) {
		//				case SHIPSECTION_HIGH:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.filtericonhighslot)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				case SHIPSECTION_MED:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.filtericonmediumslot)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				case SHIPSECTION_LOW:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.filtericonlowslot)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				case SHIPSECTION_RIGS:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.filtericonrigslot)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				case SHIPSECTION_DRONES:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.filtericondrones)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				case SHIPSECTION_CARGO:
		//					part = new GroupPart((Separator) node).setIconReference(org.dimensinfin.eveonline.neocom.R.drawable.itemhangar)
		//							.setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING).setFactory(this);
		//					break;
		//				default:
		//					part = new GroupPart((Separator) node).setRenderMode(AppWideConstants.rendermodes.RENDER_GROUPSHIPFITTING)
		//							.setFactory(this);
		//
		//			}
		//			return part;
		//		}
		//		if (node instanceof Ship) {
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
		//		if (node instanceof SpaceContainer) {
		//			IPart part = new ContainerPart((NeoComAsset) node)
		//					.setRenderMode(AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION);
		//			part.setFactory(this);
		//			return part;
		//		}
		if ( node instanceof NeoComAsset ) {
			IPart part = new AssetPart ((NeoComAsset) node)
					.setRenderMode (ENeoComVariants.valueOf (this.getVariant ()).hashCode ()).setFactory (this);
			return part;
		}
		//		if (node instanceof ProcessingAction) {
		//			IPart part = new PlanetaryProcessingActionPart(node)
		//					.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).hashCode()).setFactory(this);
		//			return part;
		//		}
		//
		//		// If no part is trapped then call the parent chain until one is found.
		//		return super.createPart(node);
		return null;
	}
}

// - UNUSED CODE ............................................................................................
