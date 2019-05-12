//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.AuthorizationFlowActivity;
//import org.dimensinfin.eveonline.neocom.core.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;

/**
 * This part will trigger an Activity change when the <b>OnClick</b> event is received. If will show a simple message to the user along
 * with an icon following the base structure of most interface items. When clicked it will start the authorization flow against the ESI
 * authentication platform and will end registering a new Credential on the application database.
 */
// - CLASS IMPLEMENTATION ...................................................................................
public class NewLoginActionPart extends AbstractAndroidPart implements View.OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 6148259479329269362L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NewLoginActionPart( final NewLoginAction node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This is the method to convert the generic model node to this Part specific model class. Conversion failure can
	 * exist so the methods that use this call should be aware if it and have an alternative to intercept the exception.
	 */
	public NewLoginAction getCastedModel() {
		try {
			return (NewLoginAction) this.getModel();
		} catch (final RuntimeException rtex) {
			throw new NeoComRuntimeException("RTEX [NewLoginActionPart.getCastedModel]> Stored model class does not match the expected 'NewLoginAction' model class.");
		}
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return 1;
	}

	@Override
	public AbstractRender selectRenderer() {
		return new NewLoginActionRender(this, getActivity());
	}

	// --- O N C L I C K L I S T E N E R   I N T E R F A C E

	/**
	 * A click on this button will trigger the new login flow to get the ESI validation code to be interchanged with a
	 * valid token. The operations should be performed at the Activity that is part of the authorization flow. The process
	 * starts creating a Dialog where the progress and other user data will be collected. Once the Dialog is open and
	 * running the process starts to add tasks to be run in background. Each task will use a callback and that will
	 * continue with the authorization flow.
	 * @param view the view clicked. This is self.
	 */
	@Override
	public void onClick( final View view ) {
		logger.info(">> [NewLoginActionPart.onClick]");
		Toast.makeText(this.getActivity(), "New Credential action fired.", Toast.LENGTH_LONG)
		     .show();
		// Launch the Authorization Flow so the display is blocked while we authorize a new Credential.
		final Intent intent = new Intent(this.getActivity(), AuthorizationFlowActivity.class);
		// Tag this intent request with a flag to know we are firing it from the new login action part.
		intent.putExtra(EExtras.FIRE_AUTHORIZATION_FLOW.name(), "NEW-LOGIN-PART");
		getActivity().startActivity(intent);
		logger.info("<< [NewLoginActionPart.onClick]");
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("NewLoginActionPart [");
		buffer.append(this.getCastedModel().toString());
		buffer.append("]");
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class NewLoginActionRender extends AbstractRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private ImageView nodeIcon = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public NewLoginActionRender( final NewLoginActionPart target, final Activity context ) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public NewLoginActionPart getPart() {
			return (NewLoginActionPart) super.getPart();
		}

		// --- I R E N D E R   I N T E R F A C E
		@Override
		public void initializeViews() {
			//			super.initializeViews();
			nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		}

		@Override
		public void updateContent() {
			//			super.updateContent();
			nodeIcon.setImageResource(R.drawable.account_plus);
			// Set the color changing the background color.
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
				nodeIcon.setImageTintList(getContext().getResources().getColorStateList(R.color.colorstates_neocom));
			}
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.newloginaction4login;
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer("NewLoginActionRender [ ");
			buffer.append(getPart().toString());
			buffer.append(" ]");
			return buffer.toString();
		}
	}
}
// - UNUSED CODE ............................................................................................
