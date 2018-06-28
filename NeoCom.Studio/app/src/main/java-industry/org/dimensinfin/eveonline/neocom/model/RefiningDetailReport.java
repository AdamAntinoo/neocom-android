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

import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.RefiningData;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.market.MarketDataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
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
	private int resourceLocation = -1; // This is the location where this resource was mined out.
	private EveLocation closestRefiningLocation = null; // This is the user marked REFINING location closer
	private int refiningLocationIdentifier = -1;
	private float refiningRatio = 30.0f;

	private transient EveItem item = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RefiningDetailReport( int resourceTypeId ) /*throws NeoComRegisteredException*/ {
		super();
		this.resourceTypeId = resourceTypeId;
		item = new AndroidGlobalDataManager().searchItem4Id(resourceTypeId);

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
	public void refineResource() {
		if ( null == item )
			this.item = new AndroidGlobalDataManager().searchItem4Id(this.resourceTypeId);
		// Get the target refining station and from it get the refining percentage.

		// Start the refining process and store the refining results.

	}

	// --- G E T T E R S   &   S E T T E R S
	public Credential getCredential() {
		return credential;
	}

	public int getResourceLocation() {
		return resourceLocation;
	}

	public EveLocation getClosestRefiningLocation() {
		return closestRefiningLocation;
	}

	public float getRefiningRatio() {
		return refiningRatio;
	}

	public RefiningDetailReport setRefiningRatio( final float refiningRatio ) {
		this.refiningRatio = refiningRatio;
		return this;
	}

	public RefiningDetailReport setResourceLocation( final int resourceLocation ) {
		this.resourceLocation = resourceLocation;
		this.refiningLocationIdentifier = resourceLocation;
//		this.refiningRatio = AndroidGlobalDataManager.getResourceFloat("prefkey_DefaultRefiningRatio");
		return this;
	}

	/**
	 * This sets the closest role marked location set by the user to REFINING. If there is no role location marked the result will be
	 * the resource system and the refining ration will be searched using that identifier. If no refining ratio data is found I will
	 * default to a property data value.
	 * @param closestRefiningLocation
	 * @return
	 */
	public RefiningDetailReport setClosestRefiningLocation( final EveLocation closestRefiningLocation ) {
		this.closestRefiningLocation = closestRefiningLocation;
		// Search the location identifier on the refining ratio table to see if we have that information.
		try {
			final List<RefiningData> refiningData = accessGlobal().getNeocomDBHelper().getRefiningDataDao().queryForEq("locationIdentifier"
					, closestRefiningLocation.getID());
			if ( refiningData.size() > 0 ) {
				this.refiningLocationIdentifier = refiningData.get(0).getLocationIdentifier();
				this.refiningRatio = refiningData.get(0).getRefiningRatio();
			}
		} catch (SQLException sqle) {
			this.refiningLocationIdentifier = this.resourceLocation;
		}
		// If no data found then set the default refining ratio property value.
		return this;
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
}
// - UNUSED CODE ............................................................................................
//[01]
