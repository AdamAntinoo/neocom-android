package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.domain.IContainerAggregator;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanetaryStorage implements IContainerAggregator<PlanetaryResource>, Serializable {
	private static final long serialVersionUID = 110028978014639183L;
//	private Float storageCapacity=-1.0F;
	private List<PlanetaryResource> contents = new ArrayList<>();

	public PlanetaryStorage( final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents) {
		for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
			int quantity = 0;
			if (null != resource.getAmount()) quantity = resource.getAmount().intValue();
			this.addPack(new PlanetaryResource(resource.getTypeId(), quantity));
		}
	}

	public List<PlanetaryResource> getContents() {
		return this.contents;
	}

//	public Float getStorageCapacity() {
//		return this.storageCapacity;
//	}
//
//	public PlanetaryStorage setStorageCapacity( final Float storageCapacity ) {
//		this.storageCapacity = storageCapacity;
//		return this;
//	}

	public double getTotalValue() {
		double total = 0.0;
		for (PlanetaryResource node : this.contents)
			total += node.getPrice() * node.getQuantity();
		return total;
	}

	public Float getTotalVolume() {
		Double total = 0.0;
		for (PlanetaryResource node : this.contents)
			total += node.getVolume() * node.getQuantity();
		return total.floatValue();
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
