package org.dimensinfin.eveonline.neocom.visual.tests.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;

public class VisualTestActionBarController extends AndroidController<VisualTestActionBar> {
	public VisualTestActionBarController( @NonNull final VisualTestActionBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new VisualTestActionBarRender(this, context);
	}

	// - S P L A S H A C T I O N B A R R E N D E R
	public static class VisualTestActionBarRender extends NeoComRenderv2 {
		// - U I   F I E L D S
		private ImageView activityIcon;
		private TextView pageName;

		// - C O N S T R U C T O R S
		public VisualTestActionBarRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public VisualTestActionBarController getController() {
			return (VisualTestActionBarController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.actionbar_visualtest;
		}

		@Override
		public void initializeViews() {
			this.activityIcon = this.getView().findViewById(R.id.activityIcon);
			this.pageName = this.getView().findViewById(R.id.pageName);
		}

		@Override
		public void updateContent() {
			this.activityIcon.setImageDrawable(this.getContext().getDrawable(this.getController().getModel().getActivityIconReference()));
			this.pageName.setText(this.getController().getModel().getPageName());
		}
	}
}
