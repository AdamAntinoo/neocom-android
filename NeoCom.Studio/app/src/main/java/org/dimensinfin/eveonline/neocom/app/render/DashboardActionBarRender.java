package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComActionBarController;

public class DashboardActionBarRender extends MVCRender {
	// - U I   F I E L D S
	protected ImageView activityIcon;
	protected ImageView upperIcon;
	protected ImageView lowerIcon;
	private TextView titleTopLeft;
	private TextView titleTopRight;
	private TextView subtitle;

	// - C O N S T R U C T O R S
	public DashboardActionBarRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public NeoComActionBarController getController() {
		return (NeoComActionBarController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.actionbar_neocombar;
	}

	@Override
	public void initializeViews() {
		activityIcon = this.getView().findViewById(R.id.activityIcon);
		upperIcon = this.getView().findViewById(R.id.upperIcon);
		lowerIcon = this.getView().findViewById(R.id.lowerIcon);
		titleTopLeft = this.getView().findViewById( R.id.toolBarTitleLeft);
		titleTopRight = this.getView().findViewById(R.id.toolBarTitleRight);
		subtitle = this.getView().findViewById(R.id.toolBarSubTitle);
		activityIcon.setVisibility( View.GONE);
		upperIcon.setVisibility(View.GONE);
		lowerIcon.setVisibility(View.GONE);
		titleTopRight.setVisibility(View.VISIBLE); // At least the main title should be visible.
		titleTopRight.setVisibility(View.GONE);
		subtitle.setVisibility(View.GONE);

		titleTopRight.setText(this.getContext().getResources().getString(R.string.appname));
	}

	@Override
	public void updateContent() {
		if (!this.getController().getModel().getAppVersion().isEmpty()) {
			this.titleTopRight.setText(this.getController().getModel().getAppVersion());
			this.titleTopRight.setVisibility(View.VISIBLE);
		}
		if (!this.getController().getModel().getAppName().isEmpty()) {
			this.titleTopLeft.setText(this.getController().getModel().getAppName());
			this.titleTopLeft.setVisibility(View.VISIBLE);
		}
		if (!this.getController().getModel().getPilotName().isEmpty()) {
			this.subtitle.setText(this.getController().getModel().getPilotName());
			this.subtitle.setVisibility(View.VISIBLE);
		}
	}
}
