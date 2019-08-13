package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;
import org.dimensinfin.eveonline.neocom.mining.render.ResourceAggregatorContainer4EsiLocationFacetRender;

public class ResourceAggregatorContainer4EsilocationFacetController extends NeoComController<ResourceAggregatorContainer<EsiLocation>> implements IContainer {

	// - C O N S T R U C T O R
	public ResourceAggregatorContainer4EsilocationFacetController( @NonNull final ResourceAggregatorContainer<EsiLocation> model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_GREEN);
	}

	// - I C O N T A I N E R
	@Override
	public boolean wants2Collaborate() {
		return true;
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new ResourceAggregatorContainer4EsiLocationFacetRender(this, context);
	}
}
