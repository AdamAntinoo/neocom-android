package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;
import org.dimensinfin.eveonline.neocom.planetary.ui.CommandCenterImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.ExtractorImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.FactoryImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.IPlanetaryImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.StorageImageView;

import java.util.concurrent.TimeUnit;

public class FacilityLayoutCustomRender extends MVCRender {
	private FacilityGeoPosition mapCenter;
	private IPlanetaryImageView structureImage;
	private FrameLayout layoutStructureContainer;

	// - C O N S T R U C T O R S
	public FacilityLayoutCustomRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
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
		handler.postDelayed(animateCycle, TimeUnit.SECONDS.toMillis(10));
		switch (this.getController().getModel().getFacilityType()) {
			case COMMAND_CENTER:
				this.structureImage = new CommandCenterImageView.Builder(this.getContext())
						                      .withFacility(this.getController().getModel())
						                      .build();
				break;
			case EXTRACTOR_CONTROL_UNIT:
				this.structureImage = new ExtractorImageView.Builder(this.getContext())
						                      .withFacility(this.getController().getModel())
						                      .build();
				break;
			case STORAGE:
				this.structureImage = new StorageImageView.Builder(this.getContext())
						                      .withFacility(this.getController().getModel())
						                      .build();
				break;
			case LAUNCHPAD:
				this.structureImage = new StorageImageView.Builder(this.getContext())
						                      .withFacility(this.getController().getModel())
						                      .build();
				break;
			case PLANETARY_FACTORY:
				this.structureImage = new FactoryImageView.Builder(this.getContext())
						                      .withFacility(this.getController().getModel())
						                      .build();
		}
		this.layoutStructureContainer.addView((ImageView) this.structureImage);
	}

	@Override
	public void updateContent() { }

	@Override
	public void createView() {
		this.layoutStructureContainer = new FrameLayout(this.getContext());
		this.layoutStructureContainer.setTag(this);
		this.setNewView(this.layoutStructureContainer);
	}
}
