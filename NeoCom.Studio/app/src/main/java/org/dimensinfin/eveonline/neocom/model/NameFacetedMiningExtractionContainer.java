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
//import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.interfaces.IMiningExtractionContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NameFacetedMiningExtractionContainer extends PanelTitle implements IMiningExtractionContainer, IExpandable {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("NameFacetedMiningExtractionContainer");

	// - F I E L D - S E C T I O N ............................................................................
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;

	protected List<MiningExtraction> contents = new ArrayList<>();
	protected double totalValue = 0.0;
	protected double totalVolume = 0.0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NameFacetedMiningExtractionContainer( final String name ) {
		super(name);
		// Force this not to be expanded at all times.
		this.expand();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NameFacetedMiningExtractionContainer [ ");
		buffer.append("contentsSize: ").append(this.getContentSize()).append(" ");
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

	// --- I M I N I N G E X T R A C T I O N C O N T A I N E R   I N T E R F A C E
	public List<MiningExtraction> getExtractions() {
		return this.contents;
	}

	public int addExtraction( final MiningExtraction miningOperation ) {
		this.contents.add(miningOperation);
		try {
			totalValue += miningOperation.getQuantity() * miningOperation.getHighestBuyerPrice().getPrice();
		} catch (ExecutionException ee) {
			totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
		} catch (InterruptedException ie) {
			totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
		}
		totalVolume += miningOperation.getQuantity() * miningOperation.getVolume();
		return contents.size();
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

	@Override
	public boolean isEmpty() {
		if ( this.contents.size() > 0 ) return false;
		else return true;
	}

	@Override
	public boolean isExpanded() {
		return true;
	}

	@Override
	public boolean isRenderWhenEmpty() {
		return false;
	}

	@Override
	public IExpandable setRenderWhenEmpty( final boolean renderWhenEmpty ) {
		_renderIfEmpty = renderWhenEmpty;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
