package org.dimensinfin.eveonline.neocom.app.factory;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
public class AssetControllerFactory extends ControllerFactory {
	public AssetControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}
	@Override
	public IAndroidController createController( final ICollaboration node ) {
		logger.info("-- [AppControllerFactory.createController]> Node class: {}", node.getClass().getSimpleName());
		if ( this.getVariant() == PageNamesType.ASSETS_BYLOCATION.name() ) {
			if (node instanceof AppVersion) {
				return new AppVersionController((AppVersion) node, this);
			}
//			if ( node instanceof Region ) {
//				IPart part = new RegionPart((Region) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
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
		}
		if ( this.getVariant() == PageNamesType.SHIPS_BYTYPE.name() ) {
//			if ( node instanceof IconicGroup ) {
//				IPart part = new IconicGroupPart((IconicGroup) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
//			}
//			if ( node instanceof NeoComAsset ) {
//				IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
//						.setRenderMode(this.getVariant());
//				return part;
//			}
		}
		return super.createController(node);
	}
}
