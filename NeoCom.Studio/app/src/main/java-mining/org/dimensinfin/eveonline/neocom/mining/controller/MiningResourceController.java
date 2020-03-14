package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.render.MiningExtractionResourceRender;

public class MiningResourceController extends NeoComController<Resource> {
	// - C O N S T R U C T O R
	public MiningResourceController( @NonNull final Resource model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme( SpacerType.LINE_DARKBLUE );
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		if (this.getRenderMode().equalsIgnoreCase( PageNamesType.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningExtractionResourceRender(this, context);
		else
			return new MiningExtractionResourceRender(this, context);
	}
}
