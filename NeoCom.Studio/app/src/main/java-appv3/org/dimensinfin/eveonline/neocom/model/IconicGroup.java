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
package org.dimensinfin.eveonline.neocom.model;

import android.graphics.drawable.Drawable;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class IconicGroup extends PanelTitle {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("IconicGroup");

	// - F I E L D - S E C T I O N ............................................................................
	private String label = "-";
	private int iconReference = R.drawable.defaulticonplaceholder;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public IconicGroup( final String label ) {
		super(label);
		this.label = label;
	}

	public IconicGroup( final String label, final int iconRef ) {
		this(label);
		this.iconReference = iconRef;
	}

	public String getLabel() {
		return label;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("IconicGroup [ ");
		buffer.append("label: ").append(this.label).append(" ");
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// --- G E T T E R S   &   S E T T E R S
//	public String getLabel() {
//		return label;
//	}

	public int getIconReference() {
		return iconReference;
	}

	public Drawable getIcon() {
		return AndroidGlobalDataManager.getAppContext().getResources().getDrawable(this.getIconReference(), null);
	}

//	public IconicGroup setLabel( final String label ) {
//		this.label = label;
//		return this;
//	}

	public IconicGroup setIconReference( final int iconReference ) {
		this.iconReference = iconReference;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
