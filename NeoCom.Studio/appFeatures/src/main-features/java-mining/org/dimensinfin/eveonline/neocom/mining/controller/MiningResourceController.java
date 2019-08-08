package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.planetary.renders.ResourceFacetedRender;

public class MiningResourceController extends AndroidController<Resource> {
	// - C O N S T R U C T O R
	public MiningResourceController( @NonNull final Resource model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		if (this.getRenderMode().equalsIgnoreCase(PageNamesType.MINING_EXTRACTIONS_TODAY.name()))
			return new MiningExtractionResourceRender(this, context);
		else
			return new MiningExtractionResourceRender(this, context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - R E S O U R C E R E N D E R
	public static class MiningExtractionResourceRender extends ResourceFacetedRender {
		// - U I   F I E L D S
		private TextView resourceName;
		private TextView resourceIdentifier;
		private TextView quantity;
		private TextView value;

		// - C O N S T R U C T O R S
		public MiningExtractionResourceRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public MiningResourceController getController() {
			return (MiningResourceController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.resource4mininglist;
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			this.resourceName = this.getView().findViewById(R.id.resourceName);
			this.resourceIdentifier = this.getView().findViewById(R.id.resourceIdentifier);
			this.quantity = this.getView().findViewById(R.id.quantity);
			this.value = this.getView().findViewById(R.id.value);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			this.resourceName.setText(this.getController().getModel().getName());
			this.resourceIdentifier.setText("#[" + this.getController().getModel().getTypeId() + "]");
			this.quantity.setText(this.accessQuantity());
			this.value.setText(this.accessResourceValue());
		}

		private String accessQuantity() {
			return qtyFormatter.format(this.getController().getModel().getQuantity());
		}

		private String accessResourceValue() {
			final double value = this.getController().getModel().getItem().getPrice() * this.getController().getModel()
			                                                                                .getQuantity();
			return this.generatePriceString(value, true, true);
		}
	}
}
