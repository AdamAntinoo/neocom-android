package org.dimensinfin.eveonline.neocom.visual.app.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;
import org.dimensinfin.eveonline.neocom.mining.controller.DailyExtractionResourcesContainerController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
import org.dimensinfin.eveonline.neocom.mining.controller.ResourceAggregatorContainer4EsilocationFacetController;
import org.dimensinfin.eveonline.neocom.mining.controller.ResourceAggregatorContainer4LocalTimeFacetController;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;
import org.dimensinfin.eveonline.neocom.planetary.controller.FactoryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryResourceController;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;
import org.dimensinfin.eveonline.neocom.visual.tests.ui.VisualTestActionBar;
import org.dimensinfin.eveonline.neocom.visual.tests.ui.VisualTestActionBarController;

public class VisualTestsControllerFactory extends ControllerFactory {
	public VisualTestsControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		logger.info("-- [VisualTestsControllerFactory.createController]> Node class: {}", node.getClass().getSimpleName());
		if (node instanceof Pilot) {
			return new PilotController((Pilot) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof Credential) {
			return new CredentialController((Credential) node, this);
		}
		if (node instanceof AppVersion) {
			return new AppVersionController((AppVersion) node, this);
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
			return new FactoryFacilityController((FactoryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof PlanetaryFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this)
					       .setRenderMode(this.getVariant());
		}
		if (node instanceof PlanetaryResource) {
			return new PlanetaryResourceController((PlanetaryResource) node, this);
		}
		if (node instanceof VisualTestActionBar) {
			return new VisualTestActionBarController((VisualTestActionBar) node, this);
		}
		if (node instanceof DailyExtractionResourcesContainer) {
			return new DailyExtractionResourcesContainerController((DailyExtractionResourcesContainer) node, this);
		}
		if (node instanceof Resource) {
			return new MiningResourceController((Resource) node, this);
		}
		if (node instanceof ResourceAggregatorContainer) {
			if ( ((ResourceAggregatorContainer) node).getFacetDelegate() instanceof EsiLocation)
				return new ResourceAggregatorContainer4EsilocationFacetController((ResourceAggregatorContainer) node, this);
			if ( ((ResourceAggregatorContainer) node).getFacetDelegate() instanceof Integer)
				return new ResourceAggregatorContainer4LocalTimeFacetController((ResourceAggregatorContainer) node, this);
		}
//		if (node instanceof MiningExtraction) {
//			return new MiningExtractionController((MiningExtraction) node, this);
//		}

		return super.createController(node);
	}
}
