package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.app.render.PilotSectionRender;

public class PilotSectionController extends NeoComController<PilotSection> {
	public PilotSectionController( final PilotSection model, final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_WHITE);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PilotSectionRender(this, context);
	}
}
