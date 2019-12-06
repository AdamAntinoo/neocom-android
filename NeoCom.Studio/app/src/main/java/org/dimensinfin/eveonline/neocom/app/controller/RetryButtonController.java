package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.RetryButton;
import org.dimensinfin.eveonline.neocom.app.render.RetryButtonRender;

public class RetryButtonController extends AndroidController<RetryButton> /*implements View.OnClickListener*/ {
	public RetryButtonController( @NonNull final RetryButton model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new RetryButtonRender( this, context );
	}

	// - V I E W . O N C L I C K L I S T E N E R
//	@LogEnterExit
//	@Override
//	public void onClick( final View view ) {
//		view.getContext().startActivity( new Intent( view.getContext(), AppInitializationActivity.class ) );
//	}
}
