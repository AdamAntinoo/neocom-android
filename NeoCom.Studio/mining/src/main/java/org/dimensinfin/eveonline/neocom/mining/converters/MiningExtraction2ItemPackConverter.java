package org.dimensinfin.eveonline.neocom.mining.converters;

import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.mining.ItemPack;

public class MiningExtraction2ItemPackConverter {
	public static ItemPack convert( final MiningExtraction input ) {
		return new ItemPack.Builder()
				.withResourceType(input.getTypeId())
				.withQuantity(input.getQuantity())
				.build();
	}
}
