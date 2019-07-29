package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.renders.PlanetaryResourceRender;

public class PlanetaryResourceController extends AndroidController<PlanetaryResource> {
	// - C O N S T R U C T O R S
	public PlanetaryResourceController( @NonNull final PlanetaryResource model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R
	@Override
	public IRender buildRender( final Context context ) {
		return new PlanetaryResourceRender(this, context);
	}
}
