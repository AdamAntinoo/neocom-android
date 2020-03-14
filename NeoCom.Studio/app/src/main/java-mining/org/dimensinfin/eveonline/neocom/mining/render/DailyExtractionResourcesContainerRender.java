package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.mining.controller.DailyExtractionResourcesContainerController;

/**
 * This render does nothing because the render result for this instance is a fixed label with the column titles.
 * @author Adam Antinoo (adamantinoo.git@gmail.com)
 * @since 0.19.0
 */
public  class DailyExtractionResourcesContainerRender extends MVCRender {
	// - C O N S T R U C T O R S
	public DailyExtractionResourcesContainerRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
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
	public void initializeViews() { }

	@Override
	public void updateContent() { }
}
