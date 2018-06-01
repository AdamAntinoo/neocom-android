//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
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
	protected ImageView nodeIcon = null;
	protected ImageView topleftDecorator = null;
	protected ImageView toprightDecorator = null;
	protected ImageView botrightDecorator = null;
	protected ImageView botleftDecorator = null;
	protected ImageView separator = null;
	protected ImageView menuIcon = null;

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
		separator.setVisibility(View.GONE);
		menuIcon.setVisibility(View.GONE);
	}
}

// - UNUSED CODE ............................................................................................
