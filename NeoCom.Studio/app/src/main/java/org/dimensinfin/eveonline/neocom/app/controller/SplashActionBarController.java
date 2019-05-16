package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerAdapter;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;

public class SplashActionBarController extends AAndroidController<SplashActionBar> {
	public SplashActionBarController( @NonNull final SplashActionBar model, @NonNull final IControllerFactory factory ) {
		super(new ControllerAdapter<>(model), factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new SplashActionBarRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - S P L A S H A C T I O N B A R R E N D E R
	public static class SplashActionBarRender extends AMVCRender {
//		private ImageView activityIcon = null;
		private TextView appNameLabel;
		private TextView appVersion;
		private TextView serverStatus;

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
			return R.layout.actionbar_neocombar;
		}

		@Override
		public void initializeViews() {
			appNameLabel = this.getView().findViewById(R.id.toolBarTitleLeft);
			appVersion = this.getView().findViewById(R.id.toolBarTitleRight);
			serverStatus = this.getView().findViewById(R.id.toolBarSubTitle);
		}

		@Override
		public void updateContent() {
			appNameLabel.setVisibility(View.VISIBLE); // At least the application name should be visible.
			appNameLabel.setText(this.getContext().getResources().getString(R.string.appname));
			appVersion.setVisibility(View.VISIBLE);
			appVersion.setText(this.getContext().getResources().getString(R.string.appversionname));
			serverStatus.setVisibility(View.GONE);
			serverStatus.setText(this.composeServerStatus());
		}

		private Spanned composeServerStatus() {
			final StringBuffer htmlFragment = new StringBuffer();
			htmlFragment.append("<font color='lightblue'>");
			htmlFragment.append(this.getController().getModel().getServerName().toUpperCase());
			htmlFragment.append("</font>");
			htmlFragment.append("  ");
			htmlFragment.append("<font color='green'>");
			htmlFragment.append(this.getController().getModel().getServerStatus().toUpperCase());
			htmlFragment.append("</font>");
			return Html.fromHtml(htmlFragment.toString());
		}
	}
}
