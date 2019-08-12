package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.mining.ResourceAggregatorContainer;

public class ResourceAggregatorContainerController extends NeoComController<ResourceAggregatorContainer<EsiLocation>> implements IContainer {

	// - C O N S T R U C T O R
	public ResourceAggregatorContainerController( @NonNull final ResourceAggregatorContainer<EsiLocation> model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(Separator.ESeparatorType.LINE_GREEN);
	}

	// - I C O N T A I N E R
	@Override
	public boolean wants2Collaborate() {
		return true;
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new ResourceAggregatorContainerRender(this, context);
	}

	public static class ResourceAggregatorContainerRender extends NeoComRenderv2 {
		private TextView systemName;
		private TextView extractionValue;

		// - C O N S T R U C T O R S
		public ResourceAggregatorContainerRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public ResourceAggregatorContainerController getController() {
			return (ResourceAggregatorContainerController) super.getController();
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
}
