package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerAdapter;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.render.NeoComRenderv2;

public class MiningExtractionController extends AAndroidController<MiningExtraction> {
	public MiningExtractionController( @NonNull final MiningExtraction model, @NonNull final IControllerFactory factory ) {
		super(new ControllerAdapter<MiningExtraction>(model), factory);
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
	public static class MiningExtractionRenderV5 extends NeoComRenderv2 {
		// - U I    F I E L D S
		private TextView resourceName;
		private TextView resourceTypeId;
		private TextView delta;
		private TextView quantity;
		private TextView separatorLabel;

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
			resourceName = this.getView().findViewById(R.id.resourceName);
			resourceTypeId = this.getView().findViewById(R.id.resourceTypeId);
			delta = this.getView().findViewById(R.id.delta);
			quantity = this.getView().findViewById(R.id.quantity);
			separatorLabel = this.getView().findViewById(R.id.separatorLabel);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			resourceName.setText(this.getController().getModel().getResourceName());
			resourceTypeId.setText("[#" + this.getController().getModel().getTypeId() + "]");
			delta.setText(this.accessDelta());
			quantity.setText(this.accessQuantity());
			// Hide some elements if the delta os ZERO
			if (this.getController().getModel().getDelta() < 1) {
				delta.setVisibility(View.INVISIBLE);
				separatorLabel.setVisibility(View.INVISIBLE);
			}
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
	}

	public static class MiningExtractionRenderV4 extends MiningExtractionPackedRender {
		// - F I E L D - S E C T I O N
		private TextView extractionSystemName = null;
		private TextView extractionHour = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView unitPrice = null;
		private TextView valueIndex = null;
		private TextView delta = null;
		private TextView separatorLabel = null;

		// - C O N S T R U C T O R S
		public MiningExtractionRenderV4( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public MiningExtractionController getController() {
			return (MiningExtractionController) super.getController();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			extractionSystemName = this.getView().findViewById(R.id.extractionSystemName);
			extractionHour = this.getView().findViewById(R.id.extractionHour);
			unitPrice = this.getView().findViewById(R.id.unitPrice);
			valueIndex = this.getView().findViewById(R.id.valueIndex);
			delta = this.getView().findViewById(R.id.delta);
			separatorLabel = this.getView().findViewById(R.id.separatorLabel);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			//		extractionSystemName.setText(this.getPart().getCastedModel().getSystemName());
			//		extractionHour.setText("HOUR: " + this.getPart().getCastedModel().getExtractionHour());
			//		unitPrice.setText(this.getPart().getTResourcePrice());
			//		valueIndex.setText(this.getPart().getTValueIndex());
			//		delta.setText(this.getPart().getTDelta());
			//
			//		// Hide some elements if the delta os ZERO
			//		if (this.getPart().getCastedModel().getDelta() < 1) {
			//			delta.setVisibility(View.INVISIBLE);
			//			separatorLabel.setVisibility(View.INVISIBLE);
			//		}
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4today;
		}
	}

	public static class MiningExtractionPackedRender extends AMVCRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView resourceName = null;
		private TextView resourceTypeId = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView volume = null;

		// - C O N S T R U C T O R S
		public MiningExtractionPackedRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public void initializeViews() {
			//			super.initializeViews();
			resourceName = this.getView().findViewById(R.id.resourceName);
			resourceTypeId = this.getView().findViewById(R.id.resourceTypeId);
			quantity = this.getView().findViewById(R.id.quantity);
			value = this.getView().findViewById(R.id.value);
			volume = this.getView().findViewById(R.id.volume);
		}

		@Override
		public void updateContent() {
			//			super.updateContent();
			//			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			//			if ( (null != link) && (null != nodeIcon) )
			//				Picasso.get().load(link).into(nodeIcon);
			//			resourceName.setText(this.getPart().getCastedModel().getResourceName());
			//			resourceTypeId.setText("[#" + this.getPart().getCastedModel().getTypeId() + "]");
			//			quantity.setText(this.getPart().getTQuantity());
			//			value.setText(this.getPart().getTExtractionValue());
			//			volume.setText(this.getPart().getTStackVolume());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4datefacet;
		}
	}
}
