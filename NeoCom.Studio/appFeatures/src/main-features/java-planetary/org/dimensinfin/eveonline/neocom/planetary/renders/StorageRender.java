package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class StorageRender extends ResourceFacetedRender {
	private TextView facilityName;
	private TextView fillLevelLabel;
	private TextView fillLevelNumbers;
	private ProgressBar storageUsedProgress;

	public StorageRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.storage4colony;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.facilityName = this.getView().findViewById(R.id.facilityName);
		this.fillLevelLabel = this.getView().findViewById(R.id.fillLevelLabel);
		this.fillLevelNumbers = this.getView().findViewById(R.id.fillLevelNumbers);
		this.storageUsedProgress = this.getView().findViewById(R.id.storageUsedProgress);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.facilityName.setText(this.getController().getModel().getName());
		this.fillLevelLabel.setText("Storage used: " + pctFormatter.format(this.accessStorageUsedPercentage()));
		this.fillLevelNumbers.setText(this.accessStorageUsed());
		final Float storageProgress = 100 * this.accessStorageUsedPercentage();
		this.storageUsedProgress.setProgress(storageProgress.intValue());
	}

	protected Float accessStorageUsedPercentage() {
		final Float capacity = ((StorageFacility) this.getController().getModel()).getStorageCapacity();
		final double usage = ((StorageFacility) this.getController().getModel()).getTotalVolume();
		return (float) (usage / capacity);
	}

	protected String accessStorageUsed() {
		final Float capacity = ((StorageFacility) this.getController().getModel()).getStorageCapacity();
		final double usage = ((StorageFacility) this.getController().getModel()).getTotalVolume();
		return qtyFormatter.format(usage)
				       + "/"
				       + qtyFormatter.format(capacity)
				       + " M3";
	}
}
