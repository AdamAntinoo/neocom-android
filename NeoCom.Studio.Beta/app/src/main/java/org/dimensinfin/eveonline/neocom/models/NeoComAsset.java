package org.dimensinfin.eveonline.neocom.models;

import org.dimensinfin.core.interfaces.IViewableNode;
import org.dimensinfin.core.model.AbstractComplexNode;

import java.util.ArrayList;

public class NeoComAsset extends AbstractComplexNode implements IViewableNode{
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

	@Override
	public ArrayList<AbstractComplexNode> collaborate2Model (final String variant) {
		return new ArrayList<AbstractComplexNode>();
	}

	@Override
	public boolean isEmpty () {
		return true;
	}
}
