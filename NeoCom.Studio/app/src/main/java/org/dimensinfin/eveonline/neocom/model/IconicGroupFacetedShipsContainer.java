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
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.interfaces.IShipContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class IconicGroupFacetedShipsContainer extends IconicGroup implements IShipContainer/*, IExpandable */{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("IconicGroupFacetedShipsContainer");

	// - F I E L D - S E C T I O N ............................................................................
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;

	private List<Ship> contents = new ArrayList<>();
	private double cargoValue = 0.0;
	private double cargoVolume = 0.0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public IconicGroupFacetedShipsContainer( final String title ) {
		super(title);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("IconicGroupFacetedShipsContainer [ ");
		buffer.append("contentSize: ").append(this.getContentSize()).append(" ");
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
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

	// --- I S H I P C O N T A I N E R
	@Override
	public int addShip( final Ship node ) {
		this.contents.add(node);
		return this.contents.size();
	}

	@Override
	public List<Ship> getShips() {
		return this.contents;
	}

	@Override
	public int getContentSize() {
		return this.contents.size();
	}

	//	@Override
	//	public double getCargoValue(){
	//		return this.cargoValue;
	//	}
	//	@Override
	//	public double getCargoVolume(){
	//		return this.cargoVolume;
	//	}

	// --- I E X P A N D A B L E   I N T E R F A C E
//	@Override
	public boolean collapse() {
		_expanded = false;
		return _expanded;
	}

//	@Override
	public boolean expand() {
		_expanded = true;
		return _expanded;
	}

//	@Override
	public boolean isEmpty() {
		if ( this.contents.size() > 0 ) return false;
		else return true;
	}

//	@Override
	public boolean isExpanded() {
		return true;
	}

//	@Override
	public boolean isRenderWhenEmpty() {
		return false;
	}

//	@Override
//	public IExpandable setRenderWhenEmpty( final boolean renderWhenEmpty ) {
//		_renderIfEmpty = renderWhenEmpty;
//		return this;
//	}
}
// - UNUSED CODE ............................................................................................
//[01]
