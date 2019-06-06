package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.AMVCRender;

public class NeoComDashboardMenuRender extends AMVCRender {
	public NeoComDashboardMenuRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public int accessLayoutReference() {
		return 0;
	}

	@Override
	public void initializeViews() {

	}

	@Override
	public void updateContent() {

	}
}
