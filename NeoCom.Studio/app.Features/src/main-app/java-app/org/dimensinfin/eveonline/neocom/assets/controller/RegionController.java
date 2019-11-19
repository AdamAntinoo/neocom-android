package org.dimensinfin.eveonline.neocom.assets.controller;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.assets.render.RegionRender;
import org.dimensinfin.eveonline.neocom.domain.space.Region;

public class RegionController extends AndroidController<Region> implements View.OnClickListener {
	public RegionController( @NonNull final Region model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new RegionRender( this, context );
	}

	// - V I E W . O N C L I C K L I S T E N E R
	@LogEnterExit
	@Override
	public void onClick( final View view ) {
		logger.info( ">> [RegionController.onClick]" );
		this.getModel().toggleExpand();
		this.invalidate();
		this.sendChangeEvent( EEvents.EVENT_ACTIONEXPANDCOLLAPSE.name() );
	}
}
