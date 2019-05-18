package org.dimensinfin.eveonline.neocom.mining;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

public class MiningExtractionAggregatorAsPanelTitle<T extends IAggregableItem> extends PanelTitle implements IContainerAggregator<T> {
	protected boolean _expanded = false;
	protected boolean _renderIfEmpty = true;
	protected List<T> contents = new ArrayList<>();
	protected double totalValue = 0.0;
	protected double totalVolume = 0.0;

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
		totalValue += itemPack.getPrice();
		totalVolume += itemPack.getVolume();
		return contents.size();
	}

	// - I E X P A N D A B L E
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
		return (this.contents.size() <= 0);
	}

	/**
	 * Aggregators by default ar ever expanded.
	 *
	 * @return ever true since they cannot be collapsed.
	 */
	@Override
	public boolean isExpanded() {
		return true;
	}

	/**
	 * If an aggregator is empty is should not be visible.
	 *
	 * @return ever false to tell that empty should be removed.
	 */
	@Override
	public boolean isRenderWhenEmpty() {
		return false;
	}

	@Override
	public IExpandable setRenderWhenEmpty( final boolean renderWhenEmpty ) {
		_renderIfEmpty = renderWhenEmpty;
		return this;
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return (List<ICollaboration>) this.contents;
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
		return 0;
	}
}
