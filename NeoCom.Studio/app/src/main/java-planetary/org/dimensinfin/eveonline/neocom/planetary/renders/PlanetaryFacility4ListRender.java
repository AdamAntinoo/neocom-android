package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class PlanetaryFacility4ListRender extends NeoComRenderv2 {
	private TextView structureName;

	public PlanetaryFacility4ListRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.commandcenterfacility4list;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		structureName = this.getView().findViewById(R.id.structureName);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		structureName.setText(this.getController().getModel().getName());
	}
}
