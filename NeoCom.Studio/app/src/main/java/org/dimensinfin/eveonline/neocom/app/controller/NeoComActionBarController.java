package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.render.DashboardActionBarRender;
import org.dimensinfin.eveonline.neocom.app.ui.DashboardActionBar;

public class NeoComActionBarController extends AndroidController<DashboardActionBar> {
	public NeoComActionBarController( @NonNull final DashboardActionBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new DashboardActionBarRender(this, context);
	}

//	@Ov     ic long getModelId() {
//		return this.getModel().hashCode();
//	}
}
