package org.dimensinfin.eveonline.neocom.planetary.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilitySummary;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.StorageFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.ColonyPackController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilitySummaryController;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryResourceController;

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
		if (node instanceof PlanetaryFacilitySummary) {
			return new PlanetaryFacilitySummaryController((PlanetaryFacilitySummary) node, this);
		}
		if (node instanceof IPlanetaryFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this).setRenderMode(this.getVariant());
		}
		if (node instanceof CommandCenterFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this).setRenderMode(this.getVariant());
		}
		if (node instanceof StorageFacility) {
			return new PlanetaryFacilityController((IPlanetaryFacility) node, this).setRenderMode(this.getVariant());
		}
		if (node instanceof PlanetaryResource) {
			return new PlanetaryResourceController((PlanetaryResource) node, this).setRenderMode(this.getVariant());
		}
		return super.createController(node);
	}
}
