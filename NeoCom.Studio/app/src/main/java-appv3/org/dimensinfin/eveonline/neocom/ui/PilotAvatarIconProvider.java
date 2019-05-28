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
package org.dimensinfin.eveonline.neocom.ui;

//import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.interfaces.IURLIconProvider;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotAvatarIconProvider implements IURLIconProvider {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("PilotAvatarIconProvider");

	// - F I E L D - S E C T I O N ............................................................................
	private int pilotIdentifier = -1;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotAvatarIconProvider( int id ) {
		this.pilotIdentifier = id;
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	// --- I U R L I C O N P R O V I D E R   I N T E R F A C E
	@Override
	public String getIconUrlLocation() throws NeoComException {
		if(this.pilotIdentifier<0) throw new NeoComException("[PilotAvatarIconProvider.getIconUrlLocation]> Pilot identifier is not " +
				"valid. Show default avatar in place.");
		return PilotV2.getUrlforAvatar(this.pilotIdentifier);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("PilotAvatarIconProvider [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
