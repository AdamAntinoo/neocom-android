package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryResourceController;

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
		if (node instanceof Credential) {
			return new CredentialController.Builder((Credential) node, this, NeoComComponentFactory.getSingleton().getDownloaderService()).build();
		}
		if (node instanceof ColonyPack) {
			return new ColonyPackController((ColonyPack) node, this);
		}
		if (node instanceof CommandCenterFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof StorageFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof ExtractorFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof FactoryFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof PlanetaryFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof PlanetaryResource) {
			return new PlanetaryResourceController((PlanetaryResource) node, this);
		}
		return super.createController(node);
	}
}
