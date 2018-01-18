//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//                 the game data to help capsuleers organize and prioritize activities. The strong points are
//                 help at the Industry level tracking and calculating costs and benefits. Also the market
//                 information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//                 from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//                 designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//                 rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.factory;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.part.AssetPart;
import org.dimensinfin.eveonline.neocom.part.ColonyPart;
import org.dimensinfin.eveonline.neocom.part.ColonyStructurePart;
import org.dimensinfin.eveonline.neocom.part.ExtractorPart;
import org.dimensinfin.eveonline.neocom.part.Location4PlanetaryPart;
import org.dimensinfin.eveonline.neocom.part.RegionPart;
import org.dimensinfin.eveonline.neocom.part.StructureWithContentsPart;
import org.dimensinfin.eveonline.neocom.planetary.Colony;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure.EPlanetaryStructureType;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PlanetaryPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryPartFactory (final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart (final ICollaboration node) {
		logger.info("-- [AssetPartFactory.createPart]> Node class: " + node.getClass().getName());
		if ( this.getVariant() == ENeoComVariants.PLANETARY_BYLOCATION.name() ) {
			if ( node instanceof Region ) {
				IPart part = new RegionPart((Region) node).setFactory(this)
				                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
			if ( node instanceof EveLocation ) {
				IPart part = new Location4PlanetaryPart((EveLocation) node).setFactory(this)
				                                                           .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
			if ( node instanceof NeoComAsset ) {
				IPart part = new AssetPart((NeoComAsset) node).setFactory(this)
				                                              .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
		}
		if ( this.getVariant() == ENeoComVariants.PLANETARY_OPTIMIZATION.name() ) {
		}
		if ( this.getVariant() == ENeoComVariants.PLANETARY_COLONIES.name() ) {
			if ( node instanceof Colony ) {
				IPart part = new ColonyPart((Colony) node).setFactory(this)
																									.setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
				return part;
			}
			if ( node instanceof ColonyCoreStructure ) {
				// Select the right part depending on the struture type.
				final EPlanetaryStructureType type = ((ColonyCoreStructure) node).getStructureTypeCode();
				IPart part = null;
				switch (type) {
					case COMMAND_CENTER:
					case LAUNCHPAD:
					case STORAGE:
						part = new StructureWithContentsPart((ColonyCoreStructure) node).setFactory(this).setRenderMode(getVariant());
						break;
					case EXTRACTOR:
						// Use the Pin on the passing because I need the Pin fields
						part = new ExtractorPart((ColonyCoreStructure) node).setFactory(this).setRenderMode(getVariant());
						break;
					default:
						part = new ColonyStructurePart((ColonyCoreStructure) node).setFactory(this).setRenderMode(getVariant());
						break;
				}
				return part;
			}
		}
		if ( this.getVariant() == ENeoComVariants.PLANETARY_COLONY_LAYOUT.name() ) {
		}

			// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
