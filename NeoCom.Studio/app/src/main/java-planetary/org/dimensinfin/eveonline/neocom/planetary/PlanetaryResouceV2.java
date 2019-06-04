package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.domain.EsiItemV2;

public class PlanetaryResouceV2 extends PlanetaryResource  {
	private EsiItemV2 item;

	// - C O N S T R U C T O R S
	public PlanetaryResouceV2( final int typeId ) {
		super(typeId);
	}

	public PlanetaryResouceV2( final int typeId, final int newQty ) {
		super(typeId, newQty);
	}

	public PlanetaryResouceV2( final int typeId, final int newQty, final int stackSize ) {
		super(typeId, newQty, stackSize);
	}

	public PlanetaryResouceV2( final EsiItemV2 item, final int quantity ) {
//		Objects.requireNonNull(item);
		super(item.getTypeId(), quantity);
		this.item = item;
		this.baseQty = Long.valueOf(quantity).intValue();
	}
}
