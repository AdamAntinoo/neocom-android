package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.mining.ItemPack;

public class ItemPackController extends AAndroidController<ItemPack> {

	// - C O N S T R U C T O R
	public ItemPackController( @NonNull final ItemPack model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return null;
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - I T E M P A C K R E N D E R
	public static class ItemPackRender extends NeoComRenderv2 {
		// - C O N S T R U C T O R S
		public ItemPackRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public ItemPackController getController() {
			return (ItemPackController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return 0;
		}

		@Override
		public void initializeViews() {

		}

		@Override
		public void updateContent() {

		}
		// - U I   F I E L D S

	}
}
