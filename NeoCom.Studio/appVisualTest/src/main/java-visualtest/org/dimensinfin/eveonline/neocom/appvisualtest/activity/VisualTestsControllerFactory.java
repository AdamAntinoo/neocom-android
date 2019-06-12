package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class VisualTestsControllerFactory extends ControllerFactory {
	public VisualTestsControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		logger.info("-- [VisualTestsControllerFactory.createController]> Node class: {}", node.getClass().getSimpleName());
		if (node instanceof AppVersion) {
			return new AppVersionController((AppVersion) node, this);
		}
		if (node instanceof ColonyPack) {
			return new ColonyPackController((ColonyPack) node, this);
		}
		if (node instanceof CommandCenterFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(EVariants.PLANETS_LIST.name());
		}
		return super.createController(node);
	}
}
