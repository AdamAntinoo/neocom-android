package org.dimensinfin.eveonline.neocom.planetary.renders;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryResourceController;

public class PlanetaryResourceRender extends ResourceFacetedRender {
	private ViewGroup nodeIconBlock;
	private TextView tier;
	private TextView resourceName;
	private TextView resourceTypeId;
	private TextView volume;
	private TextView quantity;
	private TextView value;

	public PlanetaryResourceRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public PlanetaryResourceController getController() {
		return (PlanetaryResourceController) super.getController();
	}

	// - I R E N D E R
	@Override
	public int accessLayoutReference() {
		return R.layout.planetaryresource4storage;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.nodeIconBlock = this.getView().findViewById(R.id.nodeIconBlock);
		this.tier = this.getView().findViewById(R.id.tier);
		this.resourceName = this.getView().findViewById(R.id.resourceName);
		this.resourceTypeId = this.getView().findViewById(R.id.resourceTypeId);
		this.volume = this.getView().findViewById(R.id.volume);
		this.quantity = this.getView().findViewById(R.id.quantity);
		this.value = this.getView().findViewById(R.id.value);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		AppCompatibilityUtils.backgroundExecutor.submit(() -> {
			try {
				final String name = this.getController().getModel().getName(); // Access and download the item.
				final String valueData = this.accessValue();
				((Activity) this.getContext()).runOnUiThread(() -> {
					this.resourceName.setText(this.getController().getModel().getName());
					this.resourceTypeId.setText("[#" + this.getController().getModel().getTypeId() + "]");
					this.tier.setText(this.getController().getModel().getTier().getTypeCode());
					this.volume.setText(this.accessVolume() + " m3");
					this.quantity.setText(this.accessQuantity() + " items");
					this.value.setText(this.accessValue());
				});
			} catch (RuntimeException rte) {
				rte.printStackTrace();
			}
		});
	}

	protected String accessVolume() {
		return volumeFormatter.format(this.getController().getModel().getVolume() * this.getController().getModel().getQuantity());
	}

	protected String accessQuantity() {
		return qtyFormatter.format(this.getController().getModel().getQuantity());
	}

	protected String accessValue() {
		return this.generatePriceString(this.getController().getModel().getQuantity() * this.getController().getModel().getPrice());
	}
}
