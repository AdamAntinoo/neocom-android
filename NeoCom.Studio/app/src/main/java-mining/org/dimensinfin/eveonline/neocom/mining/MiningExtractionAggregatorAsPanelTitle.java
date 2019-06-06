package org.dimensinfin.eveonline.neocom.mining;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.events.EEvents;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.interfaces.IAggregableItem;

public class MiningExtractionAggregatorAsPanelTitle<T extends IAggregableItem> extends PanelTitle implements IContainerAggregator<T>, PropertyChangeListener {
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;
	protected List<T> contents = new ArrayList<>();

	public MiningExtractionAggregatorAsPanelTitle( final String title ) {
		super(title);
	}

	// - I C O N T A I N E R A G G R E G A T O R
	@Override
	public int getContentSize() {
		return this.contents.size();
	}

	@Override
	public int addPack( final T itemPack ) {
		this.contents.add(itemPack);
		//		totalValue += itemPack.getPrice() * itemPack.getQuantity();
		//		totalVolume += itemPack.getVolume() * itemPack.getQuantity();
//		itemPack.addPropertyChangeListener(this);
		return contents.size();
	}

	public double getTotalValue() {
		double total = 0.0;
		for (T node : this.contents)
			total += node.getPrice() * node.getQuantity();
		return total;
	}

	public double getTotalVolume() {
		double total = 0.0;
		for (T node : this.contents)
			total += node.getVolume() * node.getQuantity();
		return total;
	}

	// - P R O P E R T Y C H A N G E L I S T E N E R
	@Override
	public void propertyChange( final PropertyChangeEvent evt ) {
		if (evt.getPropertyName().equalsIgnoreCase(EEvents.EVENTCONTENTS_ACTIONMODIFYDATA.name())) {
			this.sendChangeEvent(evt);
		}
	}

	// - I E X P A N D A B L E
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

	public boolean toggleExpand() {
		this._expanded = !this._expanded;
		return this._expanded;
	}

//	@Override
	public boolean isEmpty() {
		return (this.contents.size() <= 0);
	}

	/**
	 * Aggregators by default ar ever expanded.
	 *
	 * @return ever true since they cannot be collapsed.
	 */
//	@Override
	public boolean isExpanded() {
		return true;
	}

	/**
	 * If an aggregator is empty is should not be visible.
	 *
	 * @return ever false to tell that empty should be removed.
	 */
//	@Override
	public boolean isRenderWhenEmpty() {
		return false;
	}

//	@Override
//	public IExpandable setRenderWhenEmpty( final boolean renderWhenEmpty ) {
//		_renderIfEmpty = renderWhenEmpty;
//		return this;
//	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return new ArrayList<>(this.contents);
	}

	// - C O R E
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("MiningExtractionAggregatorAsPanelTitle [ ");
		buffer.append("contentsSize: ").append(this.getContentSize()).append(" ");
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	@Override
	public int compareTo( final Object o ) {
		if (o instanceof MiningExtractionAggregatorAsPanelTitle) {
			final MiningExtractionAggregatorAsPanelTitle target = (MiningExtractionAggregatorAsPanelTitle) o;
			return this.getTitle().compareTo(target.getTitle());
		}
		return 0;
	}
}
