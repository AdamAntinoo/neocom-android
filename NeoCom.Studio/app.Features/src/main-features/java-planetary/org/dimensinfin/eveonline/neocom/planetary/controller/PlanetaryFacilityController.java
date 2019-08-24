package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.renders.AdvancedFactoryFacilityRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.BasicFactoryFacilityRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.CommandCenterRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.ExtractorRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.FacilityLayoutCustomRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.PlanetaryFacility4ListRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.StorageRender;

public class PlanetaryFacilityController extends NeoComController<IPlanetaryFacility> implements IContainer {
	// - C O N S T R U C T O R S
	public PlanetaryFacilityController( @NonNull final IPlanetaryFacility model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I C O N T A I N E R
	@Override
	public boolean wants2Collaborate() {
		return true;
	}

	// - I A N D R O I D C O N T R O L L E R
	@Override
	public IRender buildRender( final Context context ) {
		if (this.getRenderMode().equalsIgnoreCase(PageNamesType.PLANET_FACILITIES_LAYOUT.name()))
			return new FacilityLayoutCustomRender(this, context);
		if ((this.getRenderMode().equalsIgnoreCase(PageNamesType.PLANETS_LIST.name()))
				|| (this.getRenderMode().equalsIgnoreCase(PageNamesType.PLANET_FACILITIES_DETAILS.name()))) {
			// Choose the render depending on the facility.
			switch (this.getModel().getFacilityType()) {
				case COMMAND_CENTER:
					return new CommandCenterRender(this, context);
				case STORAGE:
				case LAUNCHPAD:
					this.setTheme(SpacerType.LINE_YELLOW);
					return new StorageRender(this, context);
				case EXTRACTOR_CONTROL_UNIT:
					return new ExtractorRender(this, context);
				case PLANETARY_FACTORY:
					switch (((FactoryFacility) this.getModel()).getFactoryType()) {
						case BASIC_INDUSTRY:
							return new BasicFactoryFacilityRender(this, context);
						case ADVANCED_INDUSTRY:
							return new AdvancedFactoryFacilityRender(this, context);
						default:
							return new BasicFactoryFacilityRender(this, context);
					}
				default:
					return new PlanetaryFacility4ListRender(this, context);
			}
		}
		throw new NeoComRuntimeException("[PlanetaryFacilityController]> There is no default render and the variant does not match.");
	}
}
