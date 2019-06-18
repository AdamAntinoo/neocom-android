package org.dimensinfin.eveonline.neocom.planetary.renders;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.ui.ExtractorImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.FacilityImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.IPlanetaryImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.StorageImageView;

import org.joda.time.DateTime;

public class BasicIndustryStructureRender extends AMVCRender {
	private FacilityGeoPosition mapCenter;
	private IPlanetaryImageView structureImage;
	private FrameLayout layoutStructureContainer;

	// - C O N S T R U C T O R S
	public BasicIndustryStructureRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryFacilityController getController() {
		return (PlanetaryFacilityController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.colony4map;
	}

	@Override
	public void initializeViews() {
		// Initialize the central location from the data source data.
		this.mapCenter = this.getController().getModel().getCommandCenterPosition();
		// Select the view content renderer depending on the planetaryFacility type.
		logger.info("-- [BasicIndustryStructureRender.initializeViews]> PlanetaryFacility type: {}"
				, this.getController().getModel().getFacilityType().name());
		final View targetView = this.getView();
		Runnable animateCycle = new Runnable() {
			@Override
			public void run() {
				targetView.invalidate();
				handler.postDelayed(this, TimeUnit.SECONDS.toMillis(10));
			}
		};
		// TODO - This should be recalculated
//		((PlanetaryFacility) this.getController().getModel()).setLastCycleStart(DateTime.now());
		handler.postDelayed(animateCycle, TimeUnit.SECONDS.toMillis(10));
		switch (this.getController().getModel().getFacilityType()) {
			case EXTRACTOR_CONTROL_UNIT:
				this.structureImage = new ExtractorImageView.Builder(this.getContext())
						                      .withStructure(this.getController().getModel())
						                      .withDeltaPosition(this.mapCenter)
						                      .build();
				// Start the extraction cycle animation.
				//					handler.postDelayed(animateCycle, TimeUnit.SECONDS.toMillis(10));
				break;
			case STORAGE:
				this.structureImage = new StorageImageView.Builder(this.getContext())
						                      .withStructure(this.getController().getModel())
						                      .withDeltaPosition(this.mapCenter)
						                      .build();
				break;
			case LAUNCHPAD:
				this.structureImage = new StorageImageView.Builder(this.getContext())
						                      .withStructure(this.getController().getModel())
						                      .withDeltaPosition(this.mapCenter)
						                      .build();
				break;
			case COMMAND_CENTER:
				this.structureImage = new FacilityImageView.Builder(this.getContext())
						                      .withStructure(this.getController().getModel())
						                      .withDeltaPosition(this.mapCenter)
						                      .build();
				break;
			default:
				this.structureImage = new FacilityImageView.Builder(this.getContext())
						                      .withStructure(this.getController().getModel())
						                      .withDeltaPosition(this.mapCenter)
						                      .build();
		}
		this.layoutStructureContainer.addView((ImageView) this.structureImage);
	}

	@Override
	public void updateContent() {
		final double longitudeDelta = this.getController().getModel().getGeoPosition().getLongitude() - this.mapCenter.getLongitude();
		final double latitudeDelta = this.getController().getModel().getGeoPosition().getLatitude() - this.mapCenter.getLatitude();
		// The rest of the parameters should be hidden on the View itself.
		this.structureImage.setDeltaPosition(new FacilityGeoPosition()
				                                     .setLongitude(longitudeDelta)
				                                     .setLatitude(latitudeDelta));
		this.structureImage.setPlanetaryFacility(this.getController().getModel());
	}

	@Override
	public void createView() {
		this.layoutStructureContainer = new FrameLayout(this.getContext());
		this.convertView = this.layoutStructureContainer;
		this.convertView.setTag(this);
	}
}
