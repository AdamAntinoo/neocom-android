//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.factory;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.manager.BlueprintManager;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.Corporation;
import org.dimensinfin.eveonline.neocom.model.Credential;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.model.Pilot;
import org.dimensinfin.eveonline.neocom.part.AssetsManagerPart;
import org.dimensinfin.eveonline.neocom.part.BlueprintManagerPart;
import org.dimensinfin.eveonline.neocom.part.CredentialPart;
import org.dimensinfin.eveonline.neocom.part.LoginPart;
import org.dimensinfin.eveonline.neocom.part.NewLoginActionPart;
import org.dimensinfin.eveonline.neocom.part.PilotInfoPart;
import org.dimensinfin.eveonline.neocom.part.PlanetaryManagerPart;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotPartFactory extends PartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PilotPartFactory");

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
			if ( node instanceof Login ) {
				IPart part = new LoginPart((Login) node).setFactory(this);
				return part;
			}
			if ( node instanceof Credential ) {
				IPart part = new CredentialPart((Credential) node).setFactory(this)
																													.setRenderMode(getVariant());
				return part;
			}
			if ( node instanceof Pilot ) {
				IPart part = new PilotInfoPart((Pilot) node).setFactory(this)
																										.setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
			if ( node instanceof Corporation ) {
				IPart part = new PilotInfoPart((NeoComCharacter) node).setFactory(this)
																															.setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
			if ( node instanceof NewLoginAction ) {
				IPart part = new NewLoginActionPart((NewLoginAction) node).setFactory(this)
																																	.setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
			if ( node instanceof Separator ) {
				IPart part = new SeparatorPart((Separator) node).setFactory(this);
				return part;
			}
		}
		if ( this.getVariant() == ENeoComVariants.PILOT_HEADER.name() ) {
			if ( node instanceof Pilot ) {
				IPart part = new PilotInfoPart((NeoComCharacter) node).setFactory(this)
																															.setRenderMode(ENeoComVariants.PILOT_HEADER.name());
				return part;
			}
		}
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
			if ( node instanceof Pilot ) {
				IPart part = new PilotInfoPart((NeoComCharacter) node).setFactory(this)
																															.setRenderMode(ENeoComVariants.PILOT_DETAILS.name());
				return part;
			}
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
		// If no part is trapped then call the parent chain until one is found.
		PilotPartFactory.logger.info("-- [PilotPartFactory.createPart]> calling super implementation to get a part");
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
