package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.BuildConfig;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.app.render.PilotSectionRender;

public class PilotSectionController extends NeoComController<PilotSection> implements View.OnClickListener {
	public PilotSectionController( final PilotSection model, final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(SpacerType.LINE_WHITE);
	}


	@Override
	public void onClick( final View view ) {
		if (null != this.getModel().getClickTarget()) {
			logger.info(">> [PilotSectionController.onClick]");
			if (BuildConfig.DEBUG)
				Toast.makeText(view.getContext(),
						"Planetary Dashboard selected!", Toast.LENGTH_SHORT).show();
			final int identifier = this.getModel().getPilotIdentifier();
			final Intent intent = this.getControllerFactory().prepareActivity(this.getModel().getClickTarget().name(),
					view.getContext());
			intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name(), identifier);
			view.getContext().startActivity(intent);
			logger.info("<< [PilotSectionController.onClick]");
		}
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PilotSectionRender(this, context);
	}
}
