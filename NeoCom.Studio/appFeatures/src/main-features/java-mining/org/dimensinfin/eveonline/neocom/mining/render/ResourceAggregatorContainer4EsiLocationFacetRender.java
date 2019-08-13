package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.mining.controller.ResourceAggregatorContainer4EsilocationFacetController;

public  class ResourceAggregatorContainer4EsiLocationFacetRender extends NeoComRenderv2 {
	private TextView systemName;
	private TextView extractionValue;

	// - C O N S T R U C T O R S
	public ResourceAggregatorContainer4EsiLocationFacetRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public ResourceAggregatorContainer4EsilocationFacetController getController() {
		return (ResourceAggregatorContainer4EsilocationFacetController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.systemresourceaggregator;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.systemName = this.getView().findViewById(R.id.systemName);
		this.extractionValue = this.getView().findViewById(R.id.extractionValue);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.systemName.setText(this.getController().getModel().getFacetDelegate().getSystemName());
		this.extractionValue.setText(this.accessExtractionValue());
	}

	private String accessExtractionValue() {
		return this.generatePriceString(this.getController().getModel().getResourcesValue());
	}
}
