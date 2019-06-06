package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.domain.EsiItemV2;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.mining.IContainerAggregator;

public class PlanetaryStorage implements IContainerAggregator<PlanetaryResouceV2> {
	private List<PlanetaryResouceV2> contents=new ArrayList<>();

	/**
	 * Transform the default esi data into an aggregator data to be used on facilities.
	 */
	public PlanetaryStorage( final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents ) {
		//		this.contents = contents;
		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
			int quantity = 0;
//			final Long qty = ;
			if ( null != resource.getAmount())quantity=resource.getAmount().intValue();
			this.contents.add(new PlanetaryResouceV2(new EsiItemV2(resource.getTypeId()),quantity));
		}
	}

	public List<PlanetaryResouceV2> getContents() {
		return this.contents;
	}

	public double getTotalValue() {
		double total = 0.0;
		for (PlanetaryResouceV2 node : this.contents)
			total += node.getPrice() * node.getQuantity();
		return total;
	}

	public double getTotalVolume() {
		double total = 0.0;
		for (PlanetaryResouceV2 node : this.contents)
			total += node.getVolume() * node.getQuantity();
		return total;
	}

	// -  I C O N T A I N E R A G G R E G A T O R
	@Override
	public int getContentSize() {
		return this.contents.size();
	}

	@Override
	public int addPack( final PlanetaryResouceV2 itemPack ) {
		this.contents.add(itemPack);
		return this.getContentSize();
	}
}
