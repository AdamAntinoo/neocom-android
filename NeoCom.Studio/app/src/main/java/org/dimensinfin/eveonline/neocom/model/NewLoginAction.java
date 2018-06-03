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
package org.dimensinfin.eveonline.neocom.model;

import org.dimensinfin.core.interfaces.ICollaboration;

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * This model class will serve as a placeholder to trigger the action to add new Logins to the list of authentications
 * stored on the local database. The existence of this node will generate an specific Part that will inform the user how
 * to add a new Login by clicking it. This model will serve as the temporal storage of the new login creation until it
 * is stored on the database
 */
public class NewLoginAction extends NeoComNode implements ICollaboration {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String name = "-CHARACTER NAME-";

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NewLoginAction () {
		super();
		jsonClass = "NewLoginAction";
	}

	// - M E T H O D - S E C T I O N ..........................................................................
//	@Override
//	public ArrayList<AbstractComplexNode> collaborate2Model (final String variant) {
//		return new ArrayList<AbstractComplexNode>();
//	}

	public String getName () {
		return name;
	}

	public void setName (final String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("NewLoginAction [");
		buffer.append("name:").append(getName());
		buffer.append(" ]");
		return buffer.toString();
	}
}
