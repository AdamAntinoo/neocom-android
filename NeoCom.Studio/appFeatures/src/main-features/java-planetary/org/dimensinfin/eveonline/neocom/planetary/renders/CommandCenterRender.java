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
	private TextView facilityName;
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
		this.facilityName = this.getView().findViewById(R.id.facilityName);
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
		this.facilityName.setText(this.getController().getModel().getName());
		this.cpuLevel.setText("CPU: " + pctFormatter.format(this.accessCpuUsagePercentage()));
		this.cpuLevelData.setText(this.accessCpuUsage());
		final Float cpuProgress = 100 * this.accessCpuUsagePercentage();
		this.cpuProgress.setProgress(cpuProgress.intValue());
		this.powerLevel.setText("POWER: " + pctFormatter.format(this.accessPowerUsagePercentage()));
		this.powerLevelData.setText(this.accessPowerUsage());
		final Float powerProgress = 100 * this.accessPowerUsagePercentage();
		this.powerProgress.setProgress(powerProgress.intValue());
		this.fillLevelLabel.setText("Storage used: " + pctFormatter.format(this.accessStorageUsedPercentage()));
		this.fillLevelNumbers.setText(this.accessStorageUsed());
		final Float storageProgress = 100 * this.accessStorageUsedPercentage();
		storageUsedProgress.setProgress(storageProgress.intValue());
	}

	protected Float accessCpuUsagePercentage() {
		final Float cpuUse = Float.valueOf(((CommandCenterFacility) this.getController().getModel()).cpuInUse());
		final Float cpuCapacity = Float.valueOf(((CommandCenterFacility) this.getController().getModel()).getCpuCapacity());
		return cpuUse / cpuCapacity;
	}

	protected String accessCpuUsage() {
		return ((CommandCenterFacility) this.getController().getModel()).cpuInUse()
				       + "/"
				       + ((CommandCenterFacility) this.getController().getModel()).getCpuCapacity()
				       + " Tf";
	}

	protected Float accessPowerUsagePercentage() {
		final Float powerUse = Float.valueOf(((CommandCenterFacility) this.getController().getModel()).powerInUse());
		final Float powerOutput = Float.valueOf(((CommandCenterFacility) this.getController().getModel()).getPowerOutput());
		return powerUse / powerOutput;
	}

	protected String accessPowerUsage() {
		return ((CommandCenterFacility) this.getController().getModel()).powerInUse()
				       + "/"
				       + ((CommandCenterFacility) this.getController().getModel()).getPowerOutput()
				       + " Mw";
	}

	protected Float accessStorageUsedPercentage() {
		final int capacity = ((CommandCenterFacility) this.getController().getModel()).getStorageCapacity();
		final double usage = ((CommandCenterFacility) this.getController().getModel()).getTotalVolume();
		return (float) (usage / capacity);
	}

	protected String accessStorageUsed() {
		final int capacity = ((CommandCenterFacility) this.getController().getModel()).getStorageCapacity();
		final double usage = ((CommandCenterFacility) this.getController().getModel()).getTotalVolume();
		return qtyFormatter.format(usage)
				       + "/"
				       + qtyFormatter.format(capacity)
				       + " M3";
	}
}
