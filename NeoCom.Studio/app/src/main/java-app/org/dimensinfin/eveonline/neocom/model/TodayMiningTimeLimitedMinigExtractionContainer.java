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

//import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.joda.time.DateTime;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class TodayMiningTimeLimitedMinigExtractionContainer extends NameFacetedMiningExtractionContainer {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public TodayMiningTimeLimitedMinigExtractionContainer( final String name ) {
		super(name);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public int addExtraction( final MiningExtraction miningOperation ) {
		this.contents.add(miningOperation);
		// Sum only the values if we are on the same hour that the current hour.
		final int hour = DateTime.now().getHourOfDay();
		final int extractionHour = miningOperation.getExtractionHour();
		if (hour == extractionHour) {
			try {
				totalValue += miningOperation.getQuantity() * miningOperation.getHighestBuyerPrice().getPrice();
			} catch (ExecutionException ee) {
				totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
			} catch (InterruptedException ie) {
				totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
			}
			totalVolume += miningOperation.getQuantity() * miningOperation.getVolume();
		}
		return contents.size();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
