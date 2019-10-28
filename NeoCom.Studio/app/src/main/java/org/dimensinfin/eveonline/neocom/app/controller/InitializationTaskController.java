package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.dimensinfin.eveonline.neocom.app.render.InitializationTaskRender;

public class InitializationTaskController extends AndroidController<InitializationTask> {
	public InitializationTaskController( @NonNull final InitializationTask model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new InitializationTaskRender(this, context);
	}
}