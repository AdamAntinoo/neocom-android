package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class CommandCenterRender extends PlanetaryFacility4ListRender {
	private TextView cpuLevel;

	public CommandCenterRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.commandcenter4colony;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		cpuLevel = this.getView().findViewById(R.id.cpuLevel);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		cpuLevel.setText(this.accessCpuUsage());
	}

	protected String accessCpuUsage() {
		return this.getController().getModel().getCpuUsage() + "/" + ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity();
	}
}
