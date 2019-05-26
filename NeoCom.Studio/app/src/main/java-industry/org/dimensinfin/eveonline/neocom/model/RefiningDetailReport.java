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

import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.market.MarketDataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailReport extends EveItem {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("RefiningDetailReport");

	// - F I E L D - S E C T I O N ............................................................................
	private int resourceTypeId = 1230;
	private Credential credential = null;
	private int resourceLocation;
	private EveLocation closestRefiningLocation;

	private transient EveItem item = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
//	public RefiningDetailReport() {
//		super();
//	}

	public RefiningDetailReport( int resourceTypeId ) /*throws NeoComRegisteredException*/ {
		super();
		this.resourceTypeId = resourceTypeId;
		item = AndroidGlobalDataManager.getSingleton().searchItem4Id(resourceTypeId);

		// Locate the nearest refining location to the current Pilot location.
//		final PilotV2 pilotv2 = AndroidGlobalDataManager.requestPilotV2(getCredential());
//		final List<Property> roles = pilotv2.getLocationRoles();
//		// Search for the role of the refining locations
//		for (Property locrole : roles) {
//			// Filter out all the roles not related to the type of role. Searching for refining.
//			if (locrole.getPropertyType()== EPropertyTypes.LOCATIONROLE) {
//			}
//		}
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	public Credential getCredential() {
		return credential;
	}

	public RefiningDetailReport setCredential( Credential credential ) {
		this.credential = credential;
		return this;
	}

	// --- D E L E G A T E D   M E T H O D S

	@Override
	public int getTypeId() {
		return item.getTypeId();
	}

	@Override
	public String getName() {
		return item.getName();
	}

	@Override
	public double getBaseprice() {
		return item.getBaseprice();
	}

	@Override
	public String getCategoryName() {
		return item.getCategoryName();
	}

	@Override
	public String getGroupName() {
		return item.getGroupName();
	}

	@Override
	public double getVolume() {
		return item.getVolume();
	}

	@Override
	public MarketDataEntry getLowestSellerPrice() throws ExecutionException, InterruptedException {
		return item.getLowestSellerPrice();
	}

	@Override
	public MarketDataEntry getHighestBuyerPrice() throws ExecutionException, InterruptedException {
		return item.getHighestBuyerPrice();
	}

	@Override
	public double getPrice() {
		return item.getPrice();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("RefiningDetailReport [ ");
		buffer.append("resourceTypeId: ").append(resourceTypeId);
		buffer.append(" ]");
//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	public RefiningDetailReport setResourceLocation( final int resourceLocation ) {
		this.resourceLocation = resourceLocation;
		return this;
	}

	public RefiningDetailReport setClosestRefiningLocation( final EveLocation closestRefiningLocation ) {
		this.closestRefiningLocation = closestRefiningLocation;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
