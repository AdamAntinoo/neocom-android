package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.renders.BasicIndustryRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.FacilityLayoutCustomRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.CommandCenterRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.ExtractorRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.PlanetaryFacility4ListRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.StorageRender;

public class PlanetaryFacilityController extends AAndroidController<IPlanetaryFacility> implements IContainer {
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
		if (this.getRenderMode().equalsIgnoreCase(EVariants.PLANET_FACILITIES.name()))
			return new FacilityLayoutCustomRender(this, context);
		if (this.getRenderMode().equalsIgnoreCase(EVariants.PLANETS_LIST.name())) {
			// Choose the render depending on the facility.
			switch (this.getModel().getFacilityType()) {
				case COMMAND_CENTER:
					return new CommandCenterRender(this, context);
				case STORAGE:
					return new StorageRender(this, context);
				case LAUNCHPAD:
					return new StorageRender(this, context);
				case EXTRACTOR_CONTROL_UNIT:
					return new ExtractorRender(this, context);
				case BASIC_INDUSTRY:
					return new BasicIndustryRender(this, context);
				default:
					return new PlanetaryFacility4ListRender(this, context);
			}
		}
		throw new NeoComRuntimeException("[PlanetaryFacilityController]> There is no default render and the variant does not match.");
	}
}
