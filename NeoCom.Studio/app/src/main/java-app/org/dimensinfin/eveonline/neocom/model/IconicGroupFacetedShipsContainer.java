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

import org.dimensinfin.core.interfaces.ICollaboration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class IconicGroupFacetedShipsContainer extends NeoComExpandableNode {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("DateFacetedMiningExtractionContainer");

	// - F I E L D - S E C T I O N ............................................................................
	private String containerTitle = "-TITLE-";
	private List<Ship> contents = new ArrayList<>();
	private int iconReference;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public IconicGroupFacetedShipsContainer( final String date ) {
		super();
		this.containerTitle = date;
		// Force this not to be expanded at all times.
		this.expand();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- I E X P A N D A B L E   I N T E R F A C E
	@Override
	public boolean isEmpty() {
		if ( this.contents.size() > 0 ) return true;
		else return false;
	}

	// --- I C O L L A B O R A T I O N   I N T E R F A C E
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		final List<ICollaboration> collaboration = new ArrayList<>();
		for (ICollaboration node : this.contents) {
			collaboration.add(node);
		}
		return collaboration;
	}

	// --- G E T T E R S   &   S E T T E R S
	public String getContainerTitle() {
		return containerTitle;
	}

	public List<Ship> getContents() {
		return contents;
	}

	//	public int addMiningExtraction( final MiningExtraction miningOperation ) {
//		this.contents.add(miningOperation);
//		return contents.size();
//	}
	public int addShip( final Ship newship ) {
		this.contents.add(newship);
		return contents.size();
	}

	public IconicGroupFacetedShipsContainer setContainerTitle( final String containerTitle ) {
		this.containerTitle = containerTitle;
		return this;
	}

	public void setIconReference( final int iconReference ) {
		this.iconReference = iconReference;
	}

//	@Override
//	public String toString() {
//		StringBuffer buffer = new StringBuffer("IconicGroupFacetedShipsContainer [ ");
//		buffer.append("name: ").append(0);
//		buffer.append("]");
//		buffer.append("->").append(super.toString());
//		return buffer.toString();
//	}
}
// - UNUSED CODE ............................................................................................
//[01]
