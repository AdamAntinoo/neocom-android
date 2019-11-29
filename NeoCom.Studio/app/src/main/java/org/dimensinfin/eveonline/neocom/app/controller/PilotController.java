package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComUpdatableAuthorizedController;
import org.dimensinfin.eveonline.neocom.app.datasource.PilotDashboardDataSource;
import org.dimensinfin.eveonline.neocom.app.render.PilotRender4Dashboard;
import org.dimensinfin.eveonline.neocom.domain.Pilot;
import org.dimensinfin.eveonline.neocom.provider.ESIUniverseDataProvider;

public class PilotController extends NeoComUpdatableAuthorizedController<Pilot> {
	public PilotController( final Pilot model, final IControllerFactory factory ) {
		super( model, factory );
		this.setTheme( SpacerType.LINE_DARKBLUE );
	}

	public ESIUniverseDataProvider getEsiUniverseDataProvider() {
		return ((PilotDashboardDataSource) this.getDataSource()).getEsiUniverseDataProvider();
	}

//	// - I E V E N T R E C E I V E R
//	@Override
//	public void receiveEvent( final IntercommunicationEvent event ) {
//		if (event.getEventName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
//			this.sendChangeEvent(event);
//			this.notifyDataModelChange();
//		}
//	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PilotRender4Dashboard( this, context );
	}
}
