package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;

public class AppVersionController extends AAndroidController<AppVersion> {
	// - C O N S T R U C T O R S
	public AppVersionController( @NonNull final AppVersion model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new AppVersionRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - A P P V E R S I O N R E N D E R
	public static class AppVersionRender extends MVCRender {
		// - U I   F I E L D S
		protected TextView applicationName;
		protected TextView applicationVersion;

		public AppVersionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public AppVersionController getController() {
			return (AppVersionController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.appversion4dashboard;
		}

		@Override
		public void initializeViews() {
			this.applicationName = this.getView().findViewById(R.id.applicationName);
			this.applicationVersion = this.getView().findViewById(R.id.applicationVersion);
		}

		@Override
		public void updateContent() {
			this.applicationName.setText(this.getController().getModel().getName());
			this.applicationVersion.setText(this.getController().getModel().getVersion());
		}
	}
}
