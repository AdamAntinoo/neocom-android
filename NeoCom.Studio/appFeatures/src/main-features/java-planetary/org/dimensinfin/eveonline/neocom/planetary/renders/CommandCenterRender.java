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
		cpuLevel.setText(pctFormatter.format(this.accessCpuUsagePercentage()));
		cpuLevelData.setText(this.accessCpuUsage());
		cpuProgress.setProgress(this.accessCpuUsagePercentage());
		powerLevel.setText(pctFormatter.format(this.accessPowerUsagePercentage()));
		powerLevelData.setText(this.accessPowerUsage());
		powerProgress.setProgress(this.accessPowerUsagePercentage());
		fillLevelLabel.setText("Storage used: " + pctFormatter.format(this.accessStorageUsedPercentage()));
		fillLevelNumbers.setText(this.accessStorageUsed());
		final Float storageProgress = 100 * this.accessStorageUsedPercentage();
		storageUsedProgress.setProgress(storageProgress.intValue());
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
