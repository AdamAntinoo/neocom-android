package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.NewLoginAction;

public class NewLoginController extends AndroidController<NewLoginAction> implements View.OnClickListener {
	private Context context;

	public NewLoginController( @NonNull final NewLoginAction model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - V I E W . O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * A click on this button will trigger the new login flow to get the ESI validation code to be interchanged with a
	 * valid token. The operations should be performed at the Activity that is part of the authorization flow. The process
	 * starts creating a Dialog where the progress and other user data will be collected. Once the Dialog is open and
	 * running the process starts to add tasks to be run in background. Each task will use a callback and that will
	 * continue with the authorization flow.
	 *
	 * @param view the view clicked. This is self.
	 */
	@Override
	public void onClick( final View view ) {
		logger.info(">> [NewLoginActionPart.onClick]");
		Toast.makeText(this.context, "New Credential action fired.", Toast.LENGTH_LONG)
				.show();
		// Launch the Authorization Flow so the display is blocked while we authorize a new Credential.
		final Intent intent = this.getControllerFactory().prepareActivity(PageNamesType.AUTHORIZATION.name(), this.context);
		// Tag this intent request with a flag to know we are firing it from the new login action part.
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EExtras.FIRE_AUTHORIZATION_FLOW.name(), "NEW-LOGIN-PART");
		this.context.startActivity(intent);
		logger.info("<< [NewLoginActionPart.onClick]");
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		this.context = context;
		return new NewLoginActionRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - N E W L O G I N A C T I O N R E N D E R
	public static class NewLoginActionRender extends MVCRender {
		private ImageView nodeIcon = null;

		public NewLoginActionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public NewLoginController getController() {
			return (NewLoginController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.newloginaction4login;
		}

		@Override
		public void initializeViews() {
			nodeIcon = this.getView().findViewById(R.id.nodeIcon);
		}

		@Override
		public void updateContent() {
			nodeIcon.setImageResource(R.drawable.account_plus);
			// Set the color changing the background color.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				nodeIcon.setImageTintList(getContext().getResources().getColorStateList(R.color.colorstates_neocom));
			}
		}
	}
}
