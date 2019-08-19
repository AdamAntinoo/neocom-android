package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComUpdatableAuthorizedController;
import org.dimensinfin.eveonline.neocom.app.render.PilotRender4Dashboard;
import org.dimensinfin.eveonline.neocom.core.EEvents;
import org.dimensinfin.eveonline.neocom.core.NeoComEvent;
import org.dimensinfin.eveonline.neocom.domain.Pilot;

import java.beans.PropertyChangeEvent;

public class PilotController extends NeoComUpdatableAuthorizedController<Pilot> {
	public PilotController( final Pilot model, final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_LIGHTBLUE);
	}

	// - I E V E N T R E C E I V E R
	@Override
	public void eventReceived( final NeoComEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			this.sendChangeEvent(new PropertyChangeEvent(this, event.getPropertyName(), this, this));
			this.notifyDataModelChange();
		}
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PilotRender4Dashboard(this, context);
	}
}
