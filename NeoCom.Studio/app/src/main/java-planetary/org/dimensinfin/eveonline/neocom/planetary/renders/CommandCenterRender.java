package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class CommandCenterRender extends ResourceFacetedRender {
	private TextView cpuLevel;
	private TextView cpuLevelData;
	private ProgressBar cpuProgress;

	private ProgressBar storageUsedProgress;

	public CommandCenterRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
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
		cpuLevelData = this.getView().findViewById(R.id.cpuLevelData);
		cpuProgress = this.getView().findViewById(R.id.cpuProgress);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		//		String link = this.getController().getModel().getURLForItem();
		//		Picasso.get().load(link).into(nodeIcon);
		cpuLevel.setText(pctFormatter.format(this.accessCpuUsagePercentage()));
		cpuLevelData.setText(this.accessCpuUsage());
		cpuProgress.setProgress(this.accessCpuUsagePercentage());
	}

	private int accessCpuUsagePercentage() {
		return this.getController().getModel().getCpuUsage()
				                           /
				                           ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity();
	}

	private String accessCpuUsage() {
		return this.getController().getModel().getCpuUsage()
				       + "/"
				       + ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity()
				+" Tf";
	}
}
