package org.dimensinfin.eveonline.neocom.mining.render;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.annotations.RequiresNetwork;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
import org.dimensinfin.eveonline.neocom.planetary.renders.ResourceFacetedRender;

public class MiningExtractionResourceRender extends ResourceFacetedRender {
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

	@RequiresNetwork
	private String accessResourceValue() {
		final Handler _handler = new Handler(Looper.getMainLooper());
		_handler.post( ()-> {
			final double value = this.getController().getModel().getItem().getPrice() *
					                     this.getController().getModel().getQuantity();
			((Activity) this.getContext()).runOnUiThread(() -> {
				this.value.setText(this.generatePriceString(value, true, true));
			});
		});
		return "-";
	}
}