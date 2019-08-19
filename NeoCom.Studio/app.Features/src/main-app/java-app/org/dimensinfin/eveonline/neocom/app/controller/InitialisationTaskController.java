package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.core.interfaces.IEventReceiver;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;
import org.dimensinfin.eveonline.neocom.app.render.InitialisationTaskRender;

public class InitialisationTaskController extends AndroidController<InitialisationTask> implements IEventReceiver {
	public InitialisationTaskController( @NonNull final InitialisationTask model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		model.addEventListener(this);
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

	// - P R O P E R T Y C H A N G E L I S T E N E R
	@Override
	public void receiveEvent( final IntercommunicationEvent event ) {
		if (event.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			this.notifyDataModelChange();
		}
	}
}
