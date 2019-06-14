package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.app.EActivities;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComController;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.renders.ColonyPack4ListRender;

public class ColonyPackController extends NeoComController<ColonyPack> implements View.OnClickListener, View.OnLongClickListener {
	private Context context;

	public ColonyPackController( @NonNull final ColonyPack model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_GREEN);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
		return new ColonyPack4ListRender(this, context);
	}

	@Override
	public void onClick( final View v ) {
		logger.info(">> [ColonyPackController.onClick]");
		this.toggleExpanded();
		this.sendChangeEvent(SystemWideConstants.events.EVENTCONTENTS_ACTIONEXPANDCOLLAPSE.name());
		this.invalidate();
		logger.info("<< [ColonyPackController.onClick]");
	}

	protected void toggleExpanded() {
		final ICollaboration model = this.getModel();
		if (model instanceof IExpandable)
			this.getModel().toggleExpand();
	}

	@Override
	public boolean onLongClick( final View v ) {
		logger.info(">> [ColonyPackController.onClick]");
		Toast.makeText(this.context, "Activating Colony layout", Toast.LENGTH_LONG)
				.show();
		final Intent intent = this.getControllerFactory().prepareActivity(EActivities.PLANETARY_COLONY_LAYOUT_ACTIVITY.name(), this.context);
		//		new Intent(this.context, PlanetaryColonyLayoutActivity.class);
		intent.putExtra(EExtras.CAPSULEER_IDENTIFIER.name()
				, this.getModel().getCapsuleerIdentifier()); // Credential account identifier
		intent.putExtra(EExtras.PLANET_IDENTIFIER.name()
				, this.getModel().getPlanetId()); // The colony/planet identifier
		intent.putExtra(EExtras.PLANET_TYPE_NAME.name()
				, this.getModel().getPlanetType().name()); // The planet type as string
		this.context.startActivity(intent);
		logger.info("<< [ColonyPackController.onClick]");
		return true;
	}
}
