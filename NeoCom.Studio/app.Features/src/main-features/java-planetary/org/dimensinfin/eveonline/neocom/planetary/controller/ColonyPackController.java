package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComUpdatableAuthorizedController;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.renders.ColonyPackRender;

public class ColonyPackController extends NeoComUpdatableAuthorizedController<ColonyPack> implements View.OnClickListener {
	private Context context;

	public ColonyPackController( @NonNull final ColonyPack model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(SpacerType.LINE_GREEN);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
//		if (this.getRenderMode().equalsIgnoreCase(PageNamesType.PLANETS_LIST.name()))
//			return new ColonyPackRender(this, context);
//		else
			return new ColonyPackRender(this, context);
	}

	@Override
	public void onClick( final View v ) {
		logger.info(">> [ColonyPackController.onClick]");
		final Intent intent = this.getControllerFactory().prepareActivity(PageNamesType.PLANET_FACILITIES_DETAILS.name(), this.context);
		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
				, this.getModel().getPilotIdentifier()); // Credential account identifier
		intent.putExtra(EExtras.PLANET_IDENTIFIER.name()
				, this.getModel().getPlanetId()); // The colony/planet identifier
		intent.putExtra(EExtras.PLANET_TYPE_NAME.name()
				, this.getModel().getPlanetType().name()); // The planet type as string
		this.context.startActivity(intent);
		logger.info("<< [ColonyPackController.onClick]");
	}
}
