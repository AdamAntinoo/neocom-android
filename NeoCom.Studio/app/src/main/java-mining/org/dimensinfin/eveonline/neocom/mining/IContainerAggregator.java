package org.dimensinfin.eveonline.neocom.mining;

public interface IContainerAggregator<T> /*extends IExpandable*/ {
	int getContentSize();

	int addPack( final T itemPack );
}
