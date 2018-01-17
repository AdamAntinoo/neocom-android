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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.eveonline.neocom.R;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class NewBaseExpandableRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	private ImageView topleftDecorator=null;
	private ImageView toprightDecorator=null;
	private ImageView botrightDecorator=null;
	private ImageView botleftDecorator=null;
	private ImageView separator=null;
	private ImageView menuIcon=null;
//	private ImageView rightArrow=null;

	// - L A Y O U T   L A B E L S
	private ViewGroup nodeIconBlock = null;
	private ViewGroup rightBlock = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NewBaseExpandableRender (final AbstractAndroidPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		topleftDecorator = (ImageView) _convertView.findViewById(R.id.topleftDecorator);
		toprightDecorator = (ImageView) _convertView.findViewById(R.id.toprightDecorator);
		botrightDecorator = (ImageView) _convertView.findViewById(R.id.botrightDecorator);
		botleftDecorator = (ImageView) _convertView.findViewById(R.id.botleftDecorator);
		separator = (ImageView) _convertView.findViewById(R.id.separator);
		menuIcon = (ImageView) _convertView.findViewById(R.id.menuIcon);
//		rightArrow = (ImageView) _convertView.findViewById(R.id.rightArrow);
	}
	@Override
	public void updateContent () {
		super.updateContent();
		// Setup the initial state for all the layout elements.
		// Hide the decorators.
		topleftDecorator.setVisibility(View.INVISIBLE);
		toprightDecorator.setVisibility(View.INVISIBLE);
		botrightDecorator.setVisibility(View.INVISIBLE);
		botleftDecorator.setVisibility(View.INVISIBLE);
		menuIcon.setVisibility(View.GONE);
		_rightArrow.setVisibility(View.GONE);
//		nodeIcon.setBackground(getContext().getResources().getColor(Color.TRANSPARENT));
	}
	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.nodebase4expandnew);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
