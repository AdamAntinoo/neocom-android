package org.dimensinfin.eveonline.neocom.planetary.activity;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.StructureGroup;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.controllers.BasicIndustryStructureController;
import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.controllers.StructureGroupController;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.Structure;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;

/**
 * @author Adam Antinoo
 */
public class PlanetaryControllerFactory extends AppControllerFactory {
	public PlanetaryControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		if (node instanceof ColonyPack) {
			return new ColonyPackController((ColonyPack) node, this);
		}
		if (node instanceof StructureGroup) {
			return new StructureGroupController((StructureGroup) node, this);
		}
		if (node instanceof Structure) {
			return new BasicIndustryStructureController((Structure) node, this);
		}
		return super.createController(node);
	}
}
