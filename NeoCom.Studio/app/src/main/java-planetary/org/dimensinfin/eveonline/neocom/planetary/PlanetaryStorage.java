package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.domain.IContainerAggregator;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;

public class PlanetaryStorage implements IContainerAggregator<PlanetaryResource> {
	private List<PlanetaryResource> contents = new ArrayList<>();

	/**
	 * Transform the default esi data into an aggregator data to be used on facilities.
	 */
	//	public PlanetaryStorage( final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents ) {
	//		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
	//			int quantity = 0;
	//			if (null != resource.getAmount()) quantity = resource.getAmount().intValue();
	//			this.contents.add(new PlanetaryResource.Builder()
	//					                  .withEveItem())
	//		}
	//	}
	public PlanetaryStorage( final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents, final ESIDataAdapter esiDataAdapter ) {
		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
			int quantity = 0;
			if (null != resource.getAmount()) quantity = resource.getAmount().intValue();
			this.contents.add(new PlanetaryResource.Builder()
					                  .withEveItem(esiDataAdapter.searchEsiItem4Id(resource.getTypeId()))
					                  .withQuantity(quantity)
					                  .build());
		}
	}

	public List<PlanetaryResource> getContents() {
		return this.contents;
	}

	public double getTotalValue() {
		double total = 0.0;
		for (PlanetaryResource node : this.contents)
			total += node.getPrice() * node.getQuantity();
		return total;
	}

	public double getTotalVolume() {
		double total = 0.0;
		for (PlanetaryResource node : this.contents)
			total += node.getVolume() * node.getQuantity();
		return total;
	}

	// -  I C O N T A I N E R A G G R E G A T O R
	@Override
	public int getContentSize() {
		return this.contents.size();
	}

	@Override
	public int addPack( final PlanetaryResource itemPack ) {
		this.contents.add(itemPack);
		return this.getContentSize();
	}
}
