package org.dimensinfin.eveonline.neocom.mining;

import org.dimensinfin.eveonline.neocom.interfaces.IAggregableItem;

import java.util.ArrayList;
import java.util.List;

public class MiningExtractionAggregator <T extends IAggregableItem>  {
	protected List<T> contents = new ArrayList<>();
//	@Override
	public int addPack( final T itemPack ) {
		this.contents.add(itemPack);
		return contents.size();
	}
}
