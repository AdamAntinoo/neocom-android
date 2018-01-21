//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
//                  Latest version adds support for ESI authentication since CREST and other sources will be
//                  removed on 2018.
package org.dimensinfin.eveonline.neocom.parts;

// - IMPORT SECTION .........................................................................................

import android.app.Activity;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

// - CLASS IMPLEMENTATION ...................................................................................
public class AppVersionPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 6148259479329269362L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AppVersionPart (final AppVersion node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This is the method to convert the generic model node to this Part specific model class. Conversion failure can
	 * exist so the methods that use this call should be aware if it and have an alternativo to intercept the exception.
	 */
	public AppVersion getCastedModel () {
		try {
			return (AppVersion) this.getModel();
		} catch (final RuntimeException rtex) {
			throw new NeocomRuntimeException("RTEX [AppVersionPart.getCastedModel]> Stored model class does not match the expected 'AppVersion' part class.");
		}
		//		return (AppVersion) this.getModel();
	}

	@Override
	public long getModelID () {
		return this.getCastedModel().getAppVersionNumber();
	}

	public String getName () {
		return this.getCastedModel().getAppName();
	}

	public String getTVersionNumber(){
return new StringBuffer("v").append(getCastedModel().getAppVersionNumber()).toString();
	}
	@Override
	public String toString () {
		final StringBuffer buffer = new StringBuffer("AppVersionPart [");
		buffer.append(this.getCastedModel().toString());
		buffer.append("]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new AppVersionRender(this, getActivity());
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class AppVersionRender extends AbstractRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private TextView applicationName = null;
	private TextView applicationVersion = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AppVersionRender (final AppVersionPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AppVersionPart getPart () {
		return (AppVersionPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		applicationName = (TextView) _convertView.findViewById(R.id.applicationName);
		applicationVersion = (TextView) _convertView.findViewById(R.id.applicationVersion);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		applicationName.setText(getPart().getCastedModel().getAppName());
		applicationVersion.setText(getPart().getTVersionNumber());
		// During initialization set the background borders to red until completion of the core tasks.
		_convertView.setBackground(getDrawable(R.drawable.panelborderred));
	}

	public String toString () {
		StringBuffer buffer = new StringBuffer("AppVersionRender [");
		buffer.append(getPart().toString());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.appversion4splash);
		_convertView.setTag(this);
	}
}
// - UNUSED CODE ............................................................................................
