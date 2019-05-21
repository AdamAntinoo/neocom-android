package org.dimensinfin.eveonline.neocom.mining;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

/**
 * MiningExtractionAggregatorAsPanelTitle
 * @author Adam Antinoo
 */
public class NameFacetedMiningExtractionContainer extends PanelTitle implements IMiningExtractionContainer, IExpandable {
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;

	protected List<MiningExtraction> contents = new ArrayList<>();
	protected double totalValue = 0.0;
	protected double totalVolume = 0.0;

	// - C O N S T R U C T O R S
	public NameFacetedMiningExtractionContainer( final String name ) {
		super(name);
		// Force this not to be expanded at all times.
		this.expand();
	}

	// - I C O L L A B O R A T I O N   I N T E R F A C E
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		final List<ICollaboration> collaboration = new ArrayList<>();
		for (ICollaboration node : this.contents) {
			collaboration.add(node);
		}
		return collaboration;
	}

	// - I M I N I N G E X T R A C T I O N C O N T A I N E R   I N T E R F A C E
	public List<MiningExtraction> getExtractions() {
		return this.contents;
	}

	public int addExtraction( final MiningExtraction miningOperation ) {
		this.contents.add(miningOperation);
//		try {
//			totalValue += miningOperation.getQuantity() * miningOperation.getHighestBuyerPrice().getPrice();
//		} catch (ExecutionException ee) {
			totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
//		} catch (InterruptedException ie) {
//			totalValue += miningOperation.getQuantity() * miningOperation.getPrice();
//		}
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

	public boolean toggleExpand() {
		this._expanded = !this._expanded;
		return this._expanded;
	}

	@Override
	public boolean isEmpty() {
		if (this.contents.size() > 0) return false;
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

	// - C O R E
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NameFacetedMiningExtractionContainer [ ");
		buffer.append("contentsSize: ").append(this.getContentSize()).append(" ");
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}
}
