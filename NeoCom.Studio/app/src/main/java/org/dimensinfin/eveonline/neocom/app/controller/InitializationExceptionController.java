package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.render.InitializationExceptionRender;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationException;

public class InitializationExceptionController extends AndroidController<InitializationException> {
	public InitializationExceptionController( @NonNull final InitializationException model,
	                                          @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new InitializationExceptionRender(this, context);
	}
}