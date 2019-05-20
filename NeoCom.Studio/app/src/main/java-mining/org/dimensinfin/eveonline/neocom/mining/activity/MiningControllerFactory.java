package org.dimensinfin.eveonline.neocom.mining.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionController;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;
import org.dimensinfin.eveonline.neocom.sharedcode.controller.PanelTitleController;

/**
 * @author Adam Antinoo
 */
public class MiningControllerFactory extends AppControllerFactory {
	public MiningControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		if (node instanceof MiningExtraction) {
			return new MiningExtractionController((MiningExtraction) node, this);
		}
		return super.createController(node);
	}
	//
	//	@Override
	//	public IPart createPart( final ICollaboration node ) {
	//		logger.info("-- [IndustryPartFactory.createPart]> Node class: {}", node.getClass().getSimpleName());
	//		if ( node instanceof NeoComAsset ) {
	//			IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
	//					.setRenderMode(this.getVariant());
	//			return part;
	//		}
	//		if ( node instanceof NameFacetedMiningExtractionContainer ) {
	//			IPart part = new NameFacetedMiningExtractionContainerPart((NameFacetedMiningExtractionContainer) node).setFactory(this)
	//					.setRenderMode(this.getVariant());
	//			return part;
	//		}
	//		if ( node instanceof InformationPanel ) {
	//			IPart part = new PanelTitlePart((PanelTitle) node).setFactory(this)
	//					.setRenderMode(this.getVariant());
	//			return part;
	//		}
	//		if ( node instanceof RefiningDetailReport ) {
	//			IPart part = new RefiningDetailPart((RefiningDetailReport) node).setFactory(this)
	//					.setRenderMode(this.getVariant());
	//			return part;
	//		}
	//		if ( node instanceof LocationFacetedAssetsContainer ) {
	//			IPart part = new Location4AssetsPart((LocationFacetedAssetsContainer) node).setFactory(this)
	//					.setRenderMode(this.getVariant());
	//			return part;
	//		}
	//		return super.createPart(node);
	//	}
}
