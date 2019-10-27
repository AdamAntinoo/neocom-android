package org.dimensinfin.eveonline.neocom.mining;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.core.IAggregableItem;

public class MiningExtractionAggregator<T extends IAggregableItem> extends AggregatorContainer {
	protected List<T> contents = new ArrayList<>();

//		@Override
//	public int addPack( final T itemPack ) {
//		this.contents.add(itemPack);
//		return contents.size();
//	}
}
