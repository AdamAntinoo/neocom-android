package org.dimensinfin.eveonline.neocom.assets.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.assets.controller.AssetActionBarController;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;
import org.dimensinfin.eveonline.neocom.assets.domain.RegionMVCWrapper;
import org.dimensinfin.eveonline.neocom.assets.domain.StationMVCWrapper;
import org.dimensinfin.eveonline.neocom.domain.container.FacetedExpandableContainer;
import org.dimensinfin.eveonline.neocom.domain.space.SpaceRegion;
import org.dimensinfin.eveonline.neocom.domain.space.StationImplementation;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class AssetControllerFactory extends ControllerFactory {
	public AssetControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}
	@Override
	public IAndroidController createController( final ICollaboration node ) {
		NeoComLogger.info("Node class: {}", node.getClass().getSimpleName());
//		if ( node instanceof Region) {
//			return new RegionController((Region) node, this);
//		}
		if ( node instanceof PlanetaryActionBar) {
			return new AssetActionBarController((PlanetaryActionBar) node, this);
		}
		if ( node instanceof FacetedExpandableContainer) {
			// This is an special case. The model to render depends on the associated facet.
			final ICollaboration facet = ((FacetedExpandableContainer) node).getFacet();
			if ( facet instanceof StationImplementation){
				return new StationController( new StationMVCWrapper( (FacetedExpandableContainer) node ), this);
			}
			if ( facet instanceof SpaceRegion){
				return new RegionController(new RegionMVCWrapper( (FacetedExpandableContainer) node ), this);
			}
		}
//		if ( this.getVariant() == PageNamesType.ASSETS_BYLOCATION.name() ) {
//			if (node instanceof AppVersion) {
//				return new AppVersionController((AppVersion) node, this);
//			}
//			if ( node instanceof LocationFacetedAssetsContainer ) {
//				IPart part = new Location4AssetsPart((LocationFacetedAssetsContainer) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
//			}
//			if ( node instanceof ContainerFacetedAssetsContainer ) {
//				IPart part = new Container4AssetsPart((ContainerFacetedAssetsContainer) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
//			}
//			if ( node instanceof NeoComAsset ) {
//				IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
//			}
//		}
//		if ( this.getVariant() == PageNamesType.SHIPS_BYTYPE.name() ) {
////			if ( node instanceof IconicGroup ) {
////				IPart part = new IconicGroupPart((IconicGroup) node).setFactory(this)
////						.setRenderMode(this.getVariant());
////				return part;
////			}
////			if ( node instanceof NeoComAsset ) {
////				IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
////						.setRenderMode(this.getVariant());
////				return part;
////			}
//		}
		return super.createController(node);
	}
}
