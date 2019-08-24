package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.core.NeoComController;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;

public class DailyExtractionResourcesContainerController extends NeoComController<DailyExtractionResourcesContainer> implements IContainer {

	// - C O N S T R U C T O R
	public DailyExtractionResourcesContainerController( @NonNull final DailyExtractionResourcesContainer model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
		this.setTheme(SpacerType.LINE_GREEN);
	}

	// - I C O N T A I N E R
	@Override
	public boolean wants2Collaborate() {
		return true;
	}

	@Override
	public IRender buildRender( final Context context ) {
		return new DailyExtractionResourcesContainerRender(this, context);
	}

	// - I T E M P A C K R E N D E R
	public static class DailyExtractionResourcesContainerRender extends NeoComRenderv2 {
		// - C O N S T R U C T O R S
		public DailyExtractionResourcesContainerRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public DailyExtractionResourcesContainerController getController() {
			return (DailyExtractionResourcesContainerController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.resource4mininglistheader;
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
		}

		@Override
		public void updateContent() {
			super.updateContent();
		}
	}
}
