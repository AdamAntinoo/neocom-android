package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class AppVersionRender extends MVCRender {
	// - U I   F I E L D S
	protected TextView applicationName;
	protected TextView applicationVersion;

	public AppVersionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
		NeoComLogger.enter();
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
		this.applicationName = Objects.requireNonNull(this.getView().findViewById(R.id.applicationName));
		this.applicationVersion = Objects.requireNonNull(this.getView().findViewById(R.id.applicationVersion));
	}

	@Override
	public void updateContent() {
		this.applicationName.setText(this.getController().getModel().getAppName());
		this.applicationVersion.setText(this.getController().getModel().getAppVersion());
	}
}
