package org.dimensinfin.eveonline.neocom.planetary.controller;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.ui.ExtractorImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.FacilityImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.IPlanetaryImageView;
import org.dimensinfin.eveonline.neocom.planetary.ui.StorageImageView;

import androidx.annotation.NonNull;
import org.joda.time.DateTime;

public class PlanetaryFacilityController extends AAndroidController<PlanetaryFacility> {
	// - C O N S T R U C T O R S
	public PlanetaryFacilityController( @NonNull final PlanetaryFacility model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

//	protected PlanetaryFacilityController( @NonNull final ControllerAdapter<PlanetaryFacility> delegate, @NonNull final IControllerFactory factory ) {
//		super(delegate, factory);
//	}

	// - A A N D R O I D C O N T R O L L E R   A B S T R A C T S
	@Override
	public boolean isVisible() {
		return true;
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new BasicIndustryStructureRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - B A S I C  I N D U S T R Y  S T R U C T U R E  R E N D E R
	public static class BasicIndustryStructureRender extends MVCRender {
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
			this.mapCenter = this.getController().getModel().getCommandCenter().getGeoPosition();
			// Select the view content renderer depending on the planetaryFacility type.
			logger.info("-- [BasicIndustryStructureRender.initializeViews]> PlanetaryFacility type: {}"
					, this.getController().getModel().getStructureType().name());
			final View targetView = this.getView();
			Runnable animateCycle = new Runnable() {
				@Override
				public void run() {
					targetView.invalidate();
					handler.postDelayed(this, TimeUnit.SECONDS.toMillis(10));
				}
			};
			this.getController().getModel().setLastCycleStart(DateTime.now());
			handler.postDelayed(animateCycle, TimeUnit.SECONDS.toMillis(10));
			switch (this.getController().getModel().getStructureType()) {
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
			final double longitudeDelta = this.getController().getModel().getLongitude() - this.mapCenter.getLongitude();
			final double latitudeDelta = this.getController().getModel().getLatitude() - this.mapCenter.getLatitude();
			// The rest of the parameters should be hidden on the View itself.
			this.structureImage.setDeltaPosition(new FacilityGeoPosition()
					                                     .setLongitude(longitudeDelta)
					                                     .setLatitude(latitudeDelta));
			this.structureImage.setStructure(this.getController().getModel());
		}

		@Override
		public void createView() {
			this.layoutStructureContainer = new FrameLayout(getContext());
			this.convertView = this.layoutStructureContainer;
			this.convertView.setTag(this);
		}
	}

	// - B A S I C  I N D U S T R Y  S T R U C T U R E  R E N D E R
//	public abstract static class PlanetaryStructureRender extends MVCRender {
//		private FacilityGeoPosition mapCenter;
//		private IPlanetaryImageView structureImage;
//
//		// - C O N S T R U C T O R S
//		public PlanetaryStructureRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
//			super(controller, context);
//		}
//
//		@Override
//		public PlanetaryFacilityController getController() {
//			return (PlanetaryFacilityController) super.getController();
//		}
//
//		// - I R E N D E R
//		@Override
//		public void initializeViews() {
//			// Initialize the central location from the data source data.
//			this.mapCenter = this.getController().getModel().getCommandCenter().getGeoPosition();
////			this.structureImage = new FacilityImageView(getContext());
//		}
//
//		@Override
//		protected void createView() {
//			final FrameLayout layoutStructureContainer = new FrameLayout(getContext());
//			//			structureImage = new FacilityImageView(getContext());
//			layoutStructureContainer.addView((ImageView) this.structureImage);
//			convertView = layoutStructureContainer;
//			convertView.setTag(this);
//		}
//	}
}
