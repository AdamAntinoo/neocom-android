package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.render.DailyExtractionResourcesContainerRender;

public class DailyExtractionResourcesContainerController extends NeoComController<DailyExtractionResourcesContainer> implements IContainer {

	// - C O N S T R U C T O R
	public DailyExtractionResourcesContainerController( @NonNull final DailyExtractionResourcesContainer model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
		this.setTheme( SpacerType.LINE_GREEN );
	}

	// - I C O N T A I N E R
	@Override
	public boolean wants2Collaborate() {
		return true;
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new DailyExtractionResourcesContainerRender( this, context );
	}
}
