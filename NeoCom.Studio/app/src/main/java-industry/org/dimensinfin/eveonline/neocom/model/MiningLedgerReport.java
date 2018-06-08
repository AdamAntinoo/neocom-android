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

import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;

import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.model.NeoComNode;

/**
 * This model will recover the information needed to display some key information on the Mining Ledger Page Header.
 * The information to include should be some of the items on the next list.
 * - Pilot Name and Wallet
 * - Economic value of all the Ore/Mineral assets at market prices.
 * - Graphic of the mining profit by the 24 clock hour.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningLedgerReport extends NeoComNode {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningLedgerReport");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MiningLedgerReport() {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("MiningLedgerReport [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
