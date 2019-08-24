package org.dimensinfin.eveonline.neocom.planetary.controller;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;

public class PlanetaryActionBarController extends AndroidController<PlanetaryActionBar> {
	public PlanetaryActionBarController( @NonNull final PlanetaryActionBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PlanetaryActionBarRender(this, context);
	}

	// - N E O C O M A C T I O N B A R P L A N E T A R Y R E N D E R
	public static class PlanetaryActionBarRender extends MVCRender {
		// - U I   F I E L D S
		protected ImageView activityIcon;
		protected ImageView upperIcon;
		protected ImageView lowerIcon;
		private TextView titleTopLeft;
		private TextView titleTopRight;
		private TextView subtitle;

		// - C O N S T R U C T O R S
		public PlanetaryActionBarRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public PlanetaryActionBarController getController() {
			return (PlanetaryActionBarController) super.getController();
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
			titleTopLeft = this.getView().findViewById(R.id.toolBarTitleLeft);
			titleTopRight = this.getView().findViewById(R.id.toolBarTitleRight);
			subtitle = this.getView().findViewById(R.id.toolBarSubTitle);
			activityIcon.setVisibility(View.GONE);
			upperIcon.setVisibility(View.GONE);
			lowerIcon.setVisibility(View.GONE);
			titleTopRight.setVisibility(View.VISIBLE); // At least the main title should be visible.
			titleTopRight.setVisibility(View.GONE);
			subtitle.setVisibility(View.GONE);

			titleTopRight.setText(this.getContext().getResources().getString(R.string.appname));
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

			activityIcon.setImageResource(this.getController().getModel().getActivityIconReference());
			activityIcon.setVisibility(View.VISIBLE);
			final String link = this.accessUrlforAvatar();
			Picasso.get().load(link).into(upperIcon);
			upperIcon.setVisibility(View.VISIBLE);
			//			try {
			//				final String link = provider.getIconUrlLocation();
			//				Picasso.get().load(link).into(lowerIcon);
			//				lowerIcon.setVisibility(View.VISIBLE);
			//			} catch (NeoComException e) {
			//				lowerIcon.setImageResource(R.drawable.nopilot);
			//			}
		}

		public String accessUrlforAvatar() {
			return "http://image.eveonline.com/character/" + this.getController().getModel().getPilotIdentifier() + "_256.jpg";
		}
	}
}
