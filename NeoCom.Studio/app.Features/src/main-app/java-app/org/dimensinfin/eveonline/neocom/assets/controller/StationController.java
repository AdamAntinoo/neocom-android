package org.dimensinfin.eveonline.neocom.assets.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.assets.render.StationRender;
import org.dimensinfin.eveonline.neocom.domain.space.StationImplementation;

public class StationController extends AndroidController<StationImplementation> {

	public StationController( @NonNull final StationImplementation model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	// - I A N D R O I D C O N T R O L L E R
	@Override
	public IRender buildRender( final Context context ) {
		return new StationRender(this, context);
	}
}
