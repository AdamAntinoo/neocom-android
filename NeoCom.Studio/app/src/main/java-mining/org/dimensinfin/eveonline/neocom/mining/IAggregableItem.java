package org.dimensinfin.eveonline.neocom.mining;

import org.dimensinfin.core.interfaces.ICollaboration;

public interface IAggregableItem extends ICollaboration {
	long getQuantity();
	double getVolume();
	double getPrice();
}
