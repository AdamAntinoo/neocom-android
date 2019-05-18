package org.dimensinfin.eveonline.neocom.mining;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;

public interface IContainerAggregator<T> extends IExpandable {
	int getContentSize();

	int addPack( final T itemPack );
}
