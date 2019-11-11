package org.dimensinfin.eveonline.neocom.assets.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.assets.render.RegionRender;
import org.dimensinfin.eveonline.neocom.domain.space.Region;

public class RegionController extends AndroidController<Region> {
	public RegionController( @NonNull final Region model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new RegionRender(this, context);
	}
}
