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
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.interfaces.IAssetContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class LocationFacetedAssetsContainer extends EveLocation implements IAssetContainer, IExpandable {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("LocationFacetedAssetsContainer");

	// - F I E L D - S E C T I O N ............................................................................
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;

	private List<NeoComAsset> contents = new ArrayList<>();
	private double totalValue = 0.0;
	private double totalVolume = 0.0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LocationFacetedAssetsContainer( final EveLocation loc ) {
		super();
		this.copyFrom(loc);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public LocationFacetedAssetsContainer copyFrom( final EveLocation loc ) {
		// Copy the EveLocation fields to this instance fields;
		this.setConstellation(loc.getConstellation());
		this.setConstellationId(loc.getConstellationId());
		this.setRegion(loc.getRegion());
		this.setRegionId(loc.getRegionId());
		this.setStation(loc.getStation());
		this.setStationId(loc.getStationId());
		this.setSystem(loc.getSystem());
		this.setSystemId(loc.getSystemId());
		this.setSecurity(loc.getSecurity());
		this.setTypeId(loc.getTypeId());
		return this;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("LocationFacetedAssetsContainer [ ");
		buffer.append("size: ").append(this.contents.size());
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// --- I C O L L A B O R A T I O N   I N T E R F A C E
	@Override
	public List<ICollaboration> collaborate2Model( String variant ) {
		final List<ICollaboration> collaboration = new ArrayList<>();
		for (ICollaboration node : this.contents) {
			collaboration.add(node);
		}
		return collaboration;
	}

	// --- I A S S E T C O N T A I N E R   I N T E R F A C E
	@Override
	public int addAsset( NeoComAsset asset ) {
		this.contents.add(asset);
		try {
			totalValue += asset.getQuantity() * asset.getItem().getHighestBuyerPrice().getPrice();
		} catch (ExecutionException ee) {
			totalValue += asset.getQuantity() * asset.getItem().getPrice();
		} catch (InterruptedException ie) {
			totalValue += asset.getQuantity() * asset.getItem().getPrice();
		}
		totalVolume += asset.getQuantity() * asset.getItem().getVolume();
		return contents.size();
	}

	@Override
	public List<NeoComAsset> getAssets() {
		return this.contents;
	}

	@Override
	public int getContentSize() {
		return this.contents.size();
	}

	@Override
	public double getTotalValue() {
		return this.totalValue;
	}

	@Override
	public double getTotalVolume() {
		return this.totalVolume;
	}

	// --- I E X P A N D A B L E   I N T E R F A C E
	@Override
	public boolean collapse() {
		_expanded = false;
		return _expanded;
	}

	@Override
	public boolean expand() {
		_expanded = true;
		return _expanded;
	}

	public boolean toggleExpand() {
		this._expanded = !this._expanded;
		return this._expanded;
	}

	@Override
	public boolean isEmpty() {
		if (this.contents.size() < 1) return true;
		else return false;
	}

	@Override
	public boolean isExpanded() {
		return _expanded;
	}

	@Override
	public boolean isRenderWhenEmpty() {
		return _renderIfEmpty;
	}

	@Override
	public IExpandable setRenderWhenEmpty( final boolean renderWhenEmpty ) {
		_renderIfEmpty = renderWhenEmpty;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
