package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;

public class MiningExtractionController extends AndroidController<MiningExtraction> {
	public MiningExtractionController( @NonNull final MiningExtraction model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
//		model.addPropertyChangeListener(this);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new MiningExtractionRenderV5(this, context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - M I N I N G E X T R A C T I O N R E N D E R
	public static class MiningExtractionRenderV5 extends NeoComRender {
		// - U I    F I E L D S
		private ImageView nodeIcon;
		private TextView resourceName;
		private TextView resourceTypeId;
		private TextView delta;
		private TextView quantity;
		private TextView separatorLabel;
		private TextView extractionHour;
		private TextView extractionValue;

		// - C O N S T R U C T O R S
		public MiningExtractionRenderV5( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public MiningExtractionController getController() {
			return (MiningExtractionController) super.getController();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			nodeIcon = this.getView().findViewById(R.id.nodeIcon);
			resourceName = this.getView().findViewById(R.id.resourceName);
			resourceTypeId = this.getView().findViewById(R.id.resourceTypeId);
			delta = this.getView().findViewById(R.id.delta);
			quantity = this.getView().findViewById(R.id.quantity);
			separatorLabel = this.getView().findViewById(R.id.separatorLabel);
			extractionHour = this.getView().findViewById(R.id.extractionHour);
			extractionValue = this.getView().findViewById(R.id.extractionValue);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			String link = this.getController().getModel().getURLForItem();
			Picasso.get().load(link).into(nodeIcon);
			resourceName.setText(this.getController().getModel().getResourceName());
			resourceTypeId.setText("[#" + this.getController().getModel().getTypeId() + "]");
			delta.setText(this.accessDelta());
			quantity.setText(this.accessQuantity());
			// Hide some elements if the delta os ZERO
			if (this.getController().getModel().getDelta() < 1) {
				delta.setVisibility(View.INVISIBLE);
				separatorLabel.setVisibility(View.INVISIBLE);
			}
			extractionHour.setText("Hour: " + this.accessExtractionHour());
			extractionValue.setText(this.accessExtractionValue());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4todayv5;
		}

		private String accessDelta() {
			return qtyFormatter.format(this.getController().getModel().getDelta());
		}

		private String accessQuantity() {
			return qtyFormatter.format(this.getController().getModel().getQuantity());
		}

		private String accessExtractionHour() {
			return qtyFormatter.format(this.getController().getModel().getExtractionHour());
		}

		private String accessExtractionValue() {
			final double value = this.getController().getModel().getPrice() * this.getController().getModel().getQuantity();
			if (value < 0.0) return "- ISK";
			return this.generatePriceString(value, true, true);
		}
	}
}
