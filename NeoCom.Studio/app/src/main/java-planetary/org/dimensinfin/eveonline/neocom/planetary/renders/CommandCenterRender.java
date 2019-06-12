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
	private TextView powerLevel;
	private TextView powerLevelData;
	private ProgressBar powerProgress;
	private TextView fillLevelLabel;
	private TextView fillLevelNumbers;
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
		this.cpuLevel = this.getView().findViewById(R.id.cpuLevel);
		this.cpuLevelData = this.getView().findViewById(R.id.cpuLevelData);
		this.cpuProgress = this.getView().findViewById(R.id.cpuProgress);
		this.powerLevel = this.getView().findViewById(R.id.powerLevel);
		this.powerLevelData = this.getView().findViewById(R.id.powerLevelData);
		this.powerProgress = this.getView().findViewById(R.id.powerProgress);
		this.fillLevelLabel = this.getView().findViewById(R.id.fillLevelLabel);
		this.fillLevelNumbers = this.getView().findViewById(R.id.fillLevelNumbers);
		this.storageUsedProgress = this.getView().findViewById(R.id.storageUsedProgress);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		cpuLevel.setText(pctFormatter.format(this.accessCpuUsagePercentage()));
		cpuLevelData.setText(this.accessCpuUsage());
		cpuProgress.setProgress(this.accessCpuUsagePercentage());
		powerLevel.setText(pctFormatter.format(this.accessPowerUsagePercentage()));
		powerLevelData.setText(this.accessPowerUsage());
		powerProgress.setProgress(this.accessPowerUsagePercentage());
		fillLevelLabel.setText("Capacity used: " + pctFormatter.format(this.accessStorageUsedPercentage()));
		fillLevelNumbers.setText(this.accessStorageUsed());
		storageUsedProgress.setProgress(this.accessStorageUsedPercentage());
	}

	protected int accessCpuUsagePercentage() {
		return ((CommandCenterFacility) this.getController().getModel()).cpuInUse()
				       /
				       ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity();
	}

	protected String accessCpuUsage() {
		return ((CommandCenterFacility) this.getController().getModel()).cpuInUse()
				       + "/"
				       + ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity()
				       + " Tf";
	}

	protected int accessPowerUsagePercentage() {
		return ((CommandCenterFacility) this.getController().getModel()).powerInUse()
				       /
				       ((CommandCenterFacility) this.getController().getModel()).getPowerOutput();
	}

	protected String accessPowerUsage() {
		return ((CommandCenterFacility) this.getController().getModel()).powerInUse()
				       + "/"
				       + ((CommandCenterFacility) this.getController().getModel()).getPowerOutput()
				       + " Mw";
	}
	protected int accessStorageUsedPercentage() {
		return 10;
	}

	protected String accessStorageUsed() {
		return 100
				       + "/"
				       + 500
				       + " M3";
	}
}
