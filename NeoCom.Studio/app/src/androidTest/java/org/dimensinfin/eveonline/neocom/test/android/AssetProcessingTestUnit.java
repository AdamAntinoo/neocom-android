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
package org.dimensinfin.eveonline.neocom.test.android;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetProcessingTestUnit extends NeoComCoreTestEngine {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(AssetProcessingTestUnit.class);

	// - F I E L D - S E C T I O N ............................................................................
//assetData="[   {     \\\"type_id\\\": 34,     \\\"quantity\\\": 1,     \\\"location_id\\\": 60006526,     \\\"location_type\\\": \\\"station\\\",     \\\"item_id\\\": 1019261169419,     \\\"location_flag\\\": \\\"Hangar\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 34,     \\\"quantity\\\": 1,     \\\"location_id\\\": 60012928,     \\\"location_type\\\": \\\"station\\\",     \\\"item_id\\\": 1019141088111,     \\\"location_flag\\\": \\\"Hangar\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 34,     \\\"quantity\\\": 1,     \\\"location_id\\\": 1024922896856,     \\\"location_type\\\": \\\"other\\\",     \\\"item_id\\\": 1024922896864,     \\\"location_flag\\\": \\\"Cargo\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 585,     \\\"quantity\\\": 2,     \\\"location_id\\\": 1024674208617,     \\\"location_type\\\": \\\"other\\\",     \\\"item_id\\\": 1008136195038,     \\\"location_flag\\\": \\\"Unlocked\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 587,     \\\"quantity\\\": 1,     \\\"location_id\\\": 60006220,     \\\"location_type\\\": \\\"station\\\",     \\\"item_id\\\": 1009598394170,     \\\"location_flag\\\": \\\"Hangar\\\",     \\\"is_singleton\\\": true   },   {     \\\"type_id\\\": 587,     \\\"quantity\\\": 2,     \\\"location_id\\\": 1024674208617,     \\\"location_type\\\": \\\"other\\\",     \\\"item_id\\\": 1007888251094,     \\\"location_flag\\\": \\\"Unlocked\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 605,     \\\"quantity\\\": 1,     \\\"location_id\\\": 1024674208617,     \\\"location_type\\\": \\\"other\\\",     \\\"item_id\\\": 1008801626014,     \\\"location_flag\\\": \\\"Unlocked\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 3766,     \\\"quantity\\\": 2,     \\\"location_id\\\": 1024674208617,     \\\"location_type\\\": \\\"other\\\",     \\\"item_id\\\": 1007872201468,     \\\"location_flag\\\": \\\"Unlocked\\\",     \\\"is_singleton\\\": false   },   {     \\\"type_id\\\": 29248,     \\\"quantity\\\": 1,     \\\"location_id\\\": 60006220,     \\\"location_type\\\": \\\"station\\\",     \\\"item_id\\\": 1008333727518,     \\\"location_flag\\\": \\\"Hangar\\\",     \\\"is_singleton\\\": true   },   {     \\\"type_id\\\": 29248,     \\\"quantity\\\": 1,     \\\"location_id\\\": 60014089,     \\\"location_type\\\": \\\"station\\\",     \\\"item_id\\\": 1009006682931,     \\\"location_flag\\\": \\\"Hangar\\\",     \\\"is_singleton\\\": true   }, "
	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetProcessingTestUnit () {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
//	@Test
//	public void processMockAssetsESI () {
//		StringBuffer buffer = new StringBuffer("AssetProcessingTestUnit [");
//		buffer.append("name: ").append(0);
//		buffer.append("]");
//		buffer.append("->").append(super.toString());
//		return buffer.toString();
//	}
}
// - UNUSED CODE ............................................................................................
//[01]
