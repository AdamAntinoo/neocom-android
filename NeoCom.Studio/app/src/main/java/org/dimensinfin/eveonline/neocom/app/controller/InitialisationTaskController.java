package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;
import org.dimensinfin.eveonline.neocom.app.render.InitialisationTaskRender;

public class InitialisationTaskController extends AAndroidController<InitialisationTask> {
	public InitialisationTaskController( @NonNull final InitialisationTask model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new InitialisationTaskRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}
}
