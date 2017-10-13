//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.factory;

// - IMPORT SECTION .........................................................................................
import java.util.logging.Logger;

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.Corporation;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.model.NeoComApiKey;
import org.dimensinfin.eveonline.neocom.model.Pilot;
import org.dimensinfin.eveonline.neocom.part.ApiKeyPart;
import org.dimensinfin.eveonline.neocom.part.AssetsManagerPart;
import org.dimensinfin.eveonline.neocom.part.LoginPart;
import org.dimensinfin.eveonline.neocom.part.PilotInfoPart;
import org.dimensinfin.eveonline.neocom.part.PlanetaryManagerPart;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotPartFactory extends PartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PilotPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotPartFactory(final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart(final AbstractComplexNode node) {
		PilotPartFactory.logger.info("-- [PilotPartFactory.createPart]> Node class: " + node.getClass().getName());
		if (this.getVariant() == ENeoComVariants.CAPSULEER_LIST.name()) {
			if (node instanceof Login) {
				IPart part = new LoginPart(node).setFactory(this);
				return part;
			}
			if (node instanceof NeoComApiKey) {
				IPart part = new ApiKeyPart(node).setFactory(this);
				return part;
			}
			if (node instanceof Pilot) {
				IPart part = new PilotInfoPart(node).setFactory(this).setRenderMode(ENeoComVariants.CAPSULEER_LIST.name());
				return part;
			}
			if (node instanceof Corporation) {
				IPart part = new PilotInfoPart(node).setFactory(this);
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
		if (this.getVariant() == ENeoComVariants.PILOT_DETAILS.name()) {
			if (node instanceof Pilot) {
				IPart part = new PilotInfoPart(node).setFactory(this).setRenderMode(ENeoComVariants.PILOT_DETAILS.name());
				return part;
			}
			if (node instanceof AssetsManager) {
				IPart part = new AssetsManagerPart(node).setFactory(this);
				return part;
			}
			if (node instanceof PlanetaryManager) {
				IPart part = new PlanetaryManagerPart(node).setFactory(this);
				return part;
			}
		}
		// If no part is trapped then call the parent chain until one is found.
		PilotPartFactory.logger.info("-- [PilotPartFactory.createPart]> calling suoer imaplementation to get a part");
		return super.createPart(node);
	}
}

// - UNUSED CODE ............................................................................................
