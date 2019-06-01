package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilitySummary;

public class PlanetaryFacilitySummaryController extends AAndroidController<PlanetaryFacilitySummary> {
	public PlanetaryFacilitySummaryController( @NonNull final PlanetaryFacilitySummary model, @NonNull final IControllerFactory factory) {
		super(model, factory);
	}
//	public PlanetaryFacilitySummaryController(@NonNull final ControllerAdapter<PlanetaryFacilitySummary> delegate, @NonNull final IControllerFactory factory) {
//		super(delegate, factory);
//	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public IRender buildRender(final Context context) {
		return new StructureRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	public static class StructureRender extends MVCRender {
		private ImageView nodeIcon;
		private TextView structureName;
		private TextView structureCount;

		// - C O N S T R U C T O R S
		public StructureRender(@NonNull final IAndroidController controller, @NonNull final Context context) {
			super(controller, context);
		}

		@Override
		public PlanetaryFacilitySummaryController getController() {
			return (PlanetaryFacilitySummaryController) super.getController();
		}

		// - I R E N D E R
		@Override
		public int accessLayoutReference() {
			return R.layout.facility4grouplist;
		}

		@Override
		public void initializeViews() {
			this.nodeIcon = this.getView().findViewById(R.id.nodeIcon);
			this.structureName = this.getView().findViewById(R.id.facilityName);
			this.structureCount = this.getView().findViewById(R.id.facilityCount);
		}

		@Override
		public void updateContent() {
			this.nodeIcon.setImageResource(this.getController().getModel().getIconReference());
			this.structureName.setText(this.getController().getModel().getName());
			this.structureCount.setText(Integer.valueOf(this.getController().getModel().getGroupCount()).toString());
		}
	}
}
