//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.AssetsManagerActivity;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

// - CLASS IMPLEMENTATION ...................................................................................
public class NewLoginActionPart extends NeoComAbstractPart implements View.OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 6148259479329269362L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NewLoginActionPart (final NewLoginAction node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This is the method to convert the generic model node to this Part specific model class. Conversion failure can
	 * exist so the methods that use this call should be aware if it and have an alternativo to intercept the exception.
	 */
	public NewLoginAction getCastedModel () {
		try {
			return (NewLoginAction) this.getModel();
		} catch (final RuntimeException rtex) {
			throw new NeocomRuntimeException("RTEX [NewLoginActionPart.getCastedModel]> Stored model class does not match the expected 'NewLoginAction' model class.");
		}
	}

	@Override
	public long getModelID () {
		return 1;
	}
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
	public void onClick (final View view) {
		logger.info(">> [NewLoginActionPart.onClick]");
		// launch the LoginProgress so the display is blocked while we authorize a new Credential.
		final Intent intent = new Intent(this.getActivity(), AssetsManagerActivity.class);
		// Add all the identification chain as in the backend Angular calls. This is Login/Pilot
		getActivity().startActivity(intent);
		logger.info("<< [NewLoginActionPart.onClick]");
	}

	@Override
	public String toString () {
		final StringBuffer buffer = new StringBuffer("NewLoginActionPart [");
		buffer.append(this.getCastedModel().toString());
		buffer.append("]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new NewLoginActionRender(this, getActivity());
	}

}

// - CLASS IMPLEMENTATION ...................................................................................
final class NewLoginActionRender extends AbstractRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private ImageView nodeIcon = null;
	//	private TextView applicationVersion = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NewLoginActionRender (final NewLoginActionPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NewLoginActionPart getPart () {
		return (NewLoginActionPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		//		applicationVersion = (TextView) _convertView.findViewById(R.id.applicationVersion);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void updateContent () {
		super.updateContent();
		nodeIcon.setImageResource(R.drawable.account_plus);
		// Set the color changing the background color.
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
			nodeIcon.setImageTintList(getContext().getResources().getColorStateList(R.color.colorstates_neocom));
		}
	}

	public String toString () {
		StringBuffer buffer = new StringBuffer("NewLoginActionRender [");
		buffer.append(getPart().toString());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.newloginaction4login);
		_convertView.setTag(this);
	}
}
// - UNUSED CODE ............................................................................................
