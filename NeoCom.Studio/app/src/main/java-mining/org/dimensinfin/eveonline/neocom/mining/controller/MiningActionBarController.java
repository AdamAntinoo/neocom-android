package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.MiningActionBar;

public class MiningActionBarController extends AndroidController<MiningActionBar> {
	public MiningActionBarController( @NonNull final MiningActionBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new MiningActionBarRender(this, context);
	}

	// - N E O C O M A C T I O N B A R P L A N E T A R Y R E N D E R
	public static class MiningActionBarRender extends MVCRender {
		// - U I   F I E L D S
		protected ImageView activityIcon;
		protected ImageView upperIcon;
		protected ImageView lowerIcon;
		private TextView titleTopLeft;
		private TextView titleTopRight;
		private TextView subtitle;

		// - C O N S T R U C T O R S
		public MiningActionBarRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public MiningActionBarController getController() {
			return (MiningActionBarController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.actionbar_neocombar;
		}

		@Override
		public void initializeViews() {
			this.activityIcon = this.getView().findViewById(R.id.activityIcon);
			this.upperIcon = this.getView().findViewById(R.id.upperIcon);
			this.lowerIcon = this.getView().findViewById(R.id.lowerIcon);
			this.titleTopLeft = this.getView().findViewById(R.id.toolBarTitleLeft);
			this.titleTopRight = this.getView().findViewById(R.id.toolBarTitleRight);
			this.subtitle = this.getView().findViewById(R.id.toolBarSubTitle);
			this.activityIcon.setVisibility(View.GONE);
			this.upperIcon.setVisibility(View.GONE);
			this.lowerIcon.setVisibility(View.GONE);
			this.titleTopRight.setVisibility(View.VISIBLE); // At least the main title should be visible.
			this.titleTopRight.setVisibility(View.GONE);
			this.subtitle.setVisibility(View.GONE);

			this.titleTopRight.setText(this.getContext().getResources().getString(R.string.appname));
		}

		@Override
		public void updateContent() {
			if (!this.getController().getModel().getTitleTopRight().isEmpty()) {
				this.titleTopRight.setText(this.getController().getModel().getTitleTopRight());
				this.titleTopRight.setVisibility(View.VISIBLE);
			}
			if (!this.getController().getModel().getTitleTopLeft().isEmpty()) {
				this.titleTopLeft.setText(this.getController().getModel().getTitleTopLeft());
				this.titleTopLeft.setVisibility(View.VISIBLE);
			}
			if (!this.getController().getModel().getSubtitle().isEmpty()) {
				this.subtitle.setText(this.getController().getModel().getSubtitle());
				this.subtitle.setVisibility(View.VISIBLE);
			}

			this.activityIcon.setImageResource(this.getController().getModel().getActivityIconReference());
			this.activityIcon.setVisibility(View.VISIBLE);
			final String link = this.accessUrlforAvatar();
			// TODO - This should be run on another thread during testing but still not ready.
//			Picasso.get().load(link).into(upperIcon);
			this.upperIcon.setVisibility(View.VISIBLE);
		}

		public String accessUrlforAvatar() {
			return "http://image.eveonline.com/character/" + this.getController().getModel().getPilotIdentifier() + "_256.jpg";
		}
	}
}
