package org.dimensinfin.eveonline.neocom.models;

import org.dimensinfin.core.model.AbstractComplexNode;

public class NeoComAsset extends AbstractComplexNode {
	private String name = "-NAME-";
	private int quantity = 0;

	public String getName () {
		return name;
	}

	public int getQuantity () {
		return quantity;
	}

	public void setName (final String name) {
		this.name = name;
	}

	public void setQuantity (final int quantity) {
		this.quantity = quantity;
	}


	public long getAssetId () {
		return 0;
	}
}
