package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerAdapter;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;

public class MiningExtractionController extends AAndroidController<MiningExtraction> {
	public MiningExtractionController( @NonNull final MiningExtraction model, @NonNull final IControllerFactory factory ) {
		super(new ControllerAdapter<MiningExtraction>(model), factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return null;
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - M I N I N G E X T R A C T I O N R E N D E R

}
