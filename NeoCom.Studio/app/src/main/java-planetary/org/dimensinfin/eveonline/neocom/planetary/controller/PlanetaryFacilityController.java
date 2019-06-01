package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColoniesActivity;
import org.dimensinfin.eveonline.neocom.planetary.renders.BasicIndustryStructureRender;
import org.dimensinfin.eveonline.neocom.planetary.renders.PlanetaryFacility4ListRender;

public class PlanetaryFacilityController extends AAndroidController<PlanetaryFacility> {
	// - C O N S T R U C T O R S
	public PlanetaryFacilityController( @NonNull final PlanetaryFacility model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - A A N D R O I D C O N T R O L L E R   A B S T R A C T S
	@Override
	public boolean isVisible() {
		return true;
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		if (this.getRenderMode().equalsIgnoreCase(PlanetaryColoniesActivity.EPlanetaryVariants.PLANET_FACILITIES.name()))
			return new BasicIndustryStructureRender(this, context);
		if (this.getRenderMode().equalsIgnoreCase(PlanetaryColoniesActivity.EPlanetaryVariants.PLANETS_LIST.name())) {
			// Choose the render depending on the facility.
			return new PlanetaryFacility4ListRender(this, context);
		}
		throw new NeoComRuntimeException("[PlanetaryFacilityController]> There is no default render and the variant does not match.");
	}
}
