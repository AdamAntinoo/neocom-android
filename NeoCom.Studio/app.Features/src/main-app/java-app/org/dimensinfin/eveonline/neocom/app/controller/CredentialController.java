package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComUpdatableController;
import org.dimensinfin.eveonline.neocom.app.render.CredentialRender;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class CredentialController extends NeoComUpdatableController<Credential> implements View.OnClickListener {
	private Context context;

	// - C O N S T R U C T O R S
	public CredentialController( @NonNull final Credential model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I E V E N T R E C E I V E R
	@Override
	public void receiveEvent( final IntercommunicationEvent event ) {
		if (event.getEventName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			this.sendChangeEvent(event);
			this.notifyDataModelChange();
		}
	}

	// - O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * If the Pilot is active the click has to change the current Activity to the Pilot Dashboard Activity. If the Pilot is not active
	 * the
	 * event should be discarded.
	 * If this part is used on more places we should control the variant so the click can be active for ones and not active for others.
	 */
	public void onClick( final View view ) {
		logger.info(">> [CredentialController.onClick]");
		final Intent intent = this.getControllerFactory().prepareActivity(PageNamesType.PILOT_DASHBOARD.name(), this.context);
		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
				, this.getModel().getAccountId()); // Credential account identifier
		view.getContext().startActivity(intent);
		logger.info("<< [CredentialController.onClick]");
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
//		this.updater.refresh(); // Signal the updater to start the refresh activity.
		return new CredentialRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().getAccountId();
	}

	// - M O D E L   A D A P T E R   M E T H O D S
	public String getPilotName() {
		return this.getModel().getName();
	}

	public String getPilotIdentifier() {
		return Integer.valueOf(this.getModel().getAccountId()).toString();
	}
}
