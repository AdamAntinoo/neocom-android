//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.render;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.eveonline.neocom.R;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class BaseExpandableRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	protected ImageView nodeIcon = null;
	protected TextView name = null;
	protected TextView childCount = null;

	// - L A Y O U T   L A B E L S
	protected TextView classLabel = null;
	protected TextView categoryLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public BaseExpandableRender (final AbstractAndroidPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		childCount = (TextView) _convertView.findViewById(R.id.childCount);

		classLabel = (TextView) _convertView.findViewById(R.id.classLabel);
		categoryLabel = (TextView) _convertView.findViewById(R.id.categoryLabel);
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.baseexpandablenode);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
