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
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.manager.BlueprintManager;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.model.PilotV1;
import org.dimensinfin.eveonline.neocom.part.AssetsManagerPart;
import org.dimensinfin.eveonline.neocom.part.BlueprintManagerPart;
import org.dimensinfin.eveonline.neocom.part.CredentialPart;
import org.dimensinfin.eveonline.neocom.part.NewLoginActionPart;
import org.dimensinfin.eveonline.neocom.part.PilotInfoPart;
import org.dimensinfin.eveonline.neocom.part.PlanetaryManagerPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotPartFactory extends PartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("PilotPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotPartFactory (final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart (final ICollaboration node) {
		PilotPartFactory.logger.info("-- [PilotPartFactory.createPart]> Node class: " + node.getClass().getName());
		if ( this.getVariant() == ENeoComVariants.CAPSULEER_LIST.name() ) {
			if ( node instanceof Credential ) {
				IPart part = new CredentialPart((Credential) node).setFactory(this)
																													.setRenderMode(getVariant());
				return part;
			}
			if ( node instanceof NewLoginAction ) {
				IPart part = new NewLoginActionPart((NewLoginAction) node).setFactory(this)
																																	.setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
		}
//		if ( this.getVariant() == ENeoComVariants.PILOT_HEADER.name() ) {
//			if ( node instanceof Pilot ) {
//				IPart part = new PilotInfoPart((NeoComCharacter) node).setFactory(this)
//																															.setRenderMode(ENeoComVariants.PILOT_HEADER.name());
//				return part;
//			}
//		}
		//		if (this.getVariant() == EDashboardVariants.NEOCOM_DASHBOARD.name()) {
		//			if (node instanceof Director) {
		//				IPart part = new DirectorPart((Director) node);
		//				return part;
		//			}
		//			if (node instanceof Pilot) {
		//				IPart part = new PilotInfoPart(node).setFactory(this);
		//				return part;
		//			}
		//		}
		if ( this.getVariant() == ENeoComVariants.PILOT_DETAILS.name() ) {
			if ( node instanceof PilotV1 ) {
				IPart part = new PilotInfoPart((PilotV1) node).setFactory(this)
																											.setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
//			if ( node instanceof Pilot ) {
//				IPart part = new PilotInfoPart((NeoComCharacter) node).setFactory(this)
//																															.setRenderMode(ENeoComVariants.PILOT_DETAILS.name());
//				return part;
//			}
			if ( node instanceof AssetsManager ) {
				IPart part = new AssetsManagerPart((AssetsManager) node).setFactory(this)
																																.setRenderMode(ENeoComVariants.PILOT_DETAILS.name());
				return part;
			}
			if ( node instanceof PlanetaryManager ) {
				IPart part = new PlanetaryManagerPart((PlanetaryManager) node).setFactory(this)
																																			.setRenderMode(ENeoComVariants.PILOT_DETAILS.name());
				return part;
			}
			if ( node instanceof BlueprintManager ) {
				IPart part = new BlueprintManagerPart((BlueprintManager) node).setFactory(this)
																																			.setRenderMode(this.getVariant());
				return part;
			}
		}
		if ( node instanceof Separator ) {
			IPart part = new SeparatorPart((Separator) node).setFactory(this);
			return part;
		}
		// If no part is trapped then call the parent chain until one is found.
		PilotPartFactory.logger.info("-- [PilotPartFactory.createPart]> calling super implementation to get a part");
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
