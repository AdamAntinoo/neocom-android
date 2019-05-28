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
import android.widget.TextView;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class ANodeBaseCountableRender extends ANodeBaseDecoratedRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - U I   F I E L D S
	protected TextView name = null;
	protected TextView childCount = null;

	// - L A Y O U T   L A B E L S
	protected TextView classLabel = null;
	protected TextView categoryLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ANodeBaseCountableRender( final NeoComAbstractPart newPart, final Activity context ) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void initializeViews() {
		super.initializeViews();
		name = (TextView) _convertView.findViewById(org.dimensinfin.android.mvc.R.id.name);
		childCount = (TextView) _convertView.findViewById(org.dimensinfin.android.mvc.R.id.childCount);

		classLabel = (TextView) _convertView.findViewById(org.dimensinfin.android.mvc.R.id.classLabel);
		categoryLabel = (TextView) _convertView.findViewById(org.dimensinfin.android.mvc.R.id.categoryLabel);
	}

	//	/**
	//	 * Check if the expandable nodes have a click active. During the click show the spinner until the action finishes
	//	 * with a new <code>updateContent()</code>.
	//	 */
	//	@Override
	//	public void updateContent () {
	//				super.updateContent();
	//		// Set the initial setup.
	//		classLabel.setVisibility(View.GONE);
	//		categoryLabel.setVisibility(View.GONE);
	//
	//		// Check if the model is expandable to chow the contents counter.
	//			if ( this.getPart().getCastedModel() instanceof IExpandable) {
	//				// Show the counter of the expandable contents.
	//				childCount.setText(this.getPart()
	//
	//						NeoComAbstractPart.itemCountFormatter.format(this.getCastedModel().getLocationCount());
	//
	//
	//				// Check if the click is running and then replace the right arrow by an spinner.
	//				if ( getPart().clickRunning() ) {
	//					if ( null != _rightArrow ) {
	//						logger.info("-- [NeoComRender.updateContent]> Activating spinner animation.");
	//						_rightArrow.setImageDrawable(getDrawable(org.dimensinfin.android.mvc.R.drawable.progress_spinner_orange));
	//						Animation rotation = AnimationUtils.loadAnimation(getContext(), org.dimensinfin.android.mvc.R.anim.clockwise_rotation);
	//						rotation.setRepeatCount(Animation.INFINITE);
	//						_rightArrow.startAnimation(rotation);
	//					}
	//				}
	//			} else _rightArrow.setVisibility(View.GONE);
	//	}
	@Override
	public int accessLayoutReference() {
		return R.layout.nodebase_countable4expand;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
