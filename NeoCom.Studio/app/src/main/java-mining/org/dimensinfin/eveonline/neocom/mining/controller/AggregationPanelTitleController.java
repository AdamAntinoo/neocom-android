package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.mining.domain.AggregationPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.render.AggregationPanelTitleRender;

public class AggregationPanelTitleController extends AndroidController<AggregationPanelTitle> {
	public AggregationPanelTitleController( @NonNull final AggregationPanelTitle model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new AggregationPanelTitleRender( this, context );
	}
}
