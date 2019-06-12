package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.mining.TableHeader;

public class TableHeaderController extends AAndroidController<TableHeader> {

	// - C O N S T R U C T O R
	public TableHeaderController( @NonNull final TableHeader model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new TableHeaderRender(this,context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - I T E M P A C K R E N D E R
	public static class TableHeaderRender extends NeoComRenderv2 {
		// - C O N S T R U C T O R S
		public TableHeaderRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public TableHeaderController getController() {
			return (TableHeaderController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.resource4mininglistheader;
		}

		@Override
		public void initializeViews() {
		}

		@Override
		public void updateContent() {
		}
	}
}
