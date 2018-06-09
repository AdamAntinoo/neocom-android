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

import org.dimensinfin.eveonline.neocom.fragment.RefiningDetailFragment;
import org.dimensinfin.eveonline.neocom.model.LocationFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;
import org.dimensinfin.eveonline.neocom.part.Location4MiningPart;
import org.dimensinfin.eveonline.neocom.part.RefiningDetailPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.DateFacetedMiningExtractionContainer;
import org.dimensinfin.eveonline.neocom.model.InformationPanel;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;
import org.dimensinfin.eveonline.neocom.part.AssetPart;
import org.dimensinfin.eveonline.neocom.part.DateFacetedMiningExtractionContainerPart;
import org.dimensinfin.eveonline.neocom.part.MiningExtractionPart;
import org.dimensinfin.eveonline.neocom.part.PanelTitlePart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class IndustryPartFactory extends PartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("IndustryPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public IndustryPartFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPart createPart( final ICollaboration node ) {
		logger.info("-- [IndustryPartFactory.createPart]> Node class: {}" + node.getClass().getSimpleName());
		if ( node instanceof MiningExtraction ) {
			IPart part = new MiningExtractionPart((MiningExtraction) node).setFactory(this)
			                                                              .setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof NeoComAsset ) {
			IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
			                                              .setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof PanelTitle ) {
			IPart part = new PanelTitlePart((PanelTitle) node).setFactory(this)
			                                                  .setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof DateFacetedMiningExtractionContainer ) {
			IPart part = new DateFacetedMiningExtractionContainerPart((DateFacetedMiningExtractionContainer) node).setFactory(this)
			                                                                                                      .setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof InformationPanel ) {
			IPart part = new PanelTitlePart((PanelTitle) node).setFactory(this)
					.setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof RefiningDetailReport) {
			IPart part = new RefiningDetailPart((RefiningDetailReport) node).setFactory(this)
					.setRenderMode(this.getVariant());
			return part;
		}
		if ( node instanceof LocationFacetedAssetsContainer) {
			IPart part = new Location4MiningPart((LocationFacetedAssetsContainer) node).setFactory(this)
					.setRenderMode(this.getVariant());
			return part;
		}
		return super.createPart(node);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
