package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.renders.ColonyPack4ListRender;

public class ColonyPackController4Header extends NeoComController<ColonyPack>{
	public ColonyPackController4Header( @NonNull final ColonyPack model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_DARKBLUE);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new ColonyPack4ListRender(this, context);
	}
}
