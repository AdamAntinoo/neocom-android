package org.dimensinfin.eveonline.neocom.app;

import android.content.Context;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;

public  abstract class NeoComDataProvider extends NeoComRenderv2 {
	public NeoComDataProvider(  final IAndroidController controller,  final Context context ) {
		super(controller, context);
	}

	@Override
	public int accessLayoutReference() {
		return 0;
	}
}
