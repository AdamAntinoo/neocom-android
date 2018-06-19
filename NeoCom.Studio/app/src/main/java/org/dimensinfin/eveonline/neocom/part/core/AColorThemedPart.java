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
package org.dimensinfin.eveonline.neocom.part.core;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.model.NameFacetedNodeContainer;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class AColorThemedPart<T extends NeoComNode> extends TypedPart<T> {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private Separator.ESeparatorType _themeColor = Separator.ESeparatorType.LINE_WHITE;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AColorThemedPart( final T model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public T getCastedModel() {
		return (T) this.getModel();
	}

	// --- P A N E L   C O L O R   T H E M E
	public Separator.ESeparatorType getThemeColor() {
		return _themeColor;
	}

	public IAndroidPart setThemeColor( final Separator.ESeparatorType themeColor ) {
		this._themeColor = themeColor;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
