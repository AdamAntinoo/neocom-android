package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;

public class SplashActionBarController extends AAndroidController<SplashActionBar> {
	public SplashActionBarController( @NonNull final SplashActionBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new SplashActionBarRender(this, context);
	}

	// - S P L A S H A C T I O N B A R R E N D E R
	public static class SplashActionBarRender extends NeoComRenderv2 {
		// - U I   F I E L D S
		private TextView appName;
		private TextView appVersion;
		private TextView serverName;
		private TextView serverStatus;
		private TextView capsuleersNumber;

		// - C O N S T R U C T O R S
		public SplashActionBarRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public SplashActionBarController getController() {
			return (SplashActionBarController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.actionbar_splash;
		}

		@Override
		public void initializeViews() {
			this.appName = this.getView().findViewById(R.id.appName);
			this.appVersion = this.getView().findViewById(R.id.appVersion);
			this.serverName = this.getView().findViewById(R.id.serverName);
			this.serverStatus = this.getView().findViewById(R.id.serverStatus);
			this.capsuleersNumber = this.getView().findViewById(R.id.capsuleersNumber);
		}

		@Override
		public void updateContent() {
			this.appName.setVisibility(View.VISIBLE); // At least the application name should be visible.
			this.appName.setText(this.getContext().getResources().getString(R.string.appname));
			this.appVersion.setVisibility(View.VISIBLE);
			this.appVersion.setText(this.getContext().getResources().getString(R.string.appversionname));
			this.serverName.setVisibility(View.VISIBLE);
			this.serverName.setText(this.getController().getModel().getServerName());
			this.serverStatus.setVisibility(View.VISIBLE);
			this.serverStatus.setText(this.getController().getModel().getServerStatus());
			this.capsuleersNumber.setVisibility(View.VISIBLE);
			this.capsuleersNumber.setText(this.accessCapsuleersNumber());
		}

		private String accessCapsuleersNumber() {
			return qtyFormatter.format(1234);
		}
	}
}
