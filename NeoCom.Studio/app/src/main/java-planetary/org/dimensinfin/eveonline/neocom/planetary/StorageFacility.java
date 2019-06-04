package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

public class StorageFacility extends PlanetaryFacilityWrapper {
	private PlanetaryStorage planetaryStorage;

	public double getStorageCapacity() {
		final GetUniverseTypesTypeIdOkDogmaAttributes capacity = this.planetaryFacility.getDogmaAttributeById(38);
		if (null != capacity) return capacity.getValue();
		else return 0.0;
	}

	public double getTotalValue() {
		return planetaryStorage.getTotalValue();
	}

	public double getTotalVolume() {
		return planetaryStorage.getTotalVolume();
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return new ArrayList<>(this.planetaryStorage.getContents());
	}

	// - B U I L D E R
	public static class Builder {
		private StorageFacility onConstruction;

		public Builder() {
			this.onConstruction = new StorageFacility();
		}

		public Builder withPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			return this;
		}

		public Builder withPlanetaryStorage( final PlanetaryStorage planetaryStorage ) {
			this.onConstruction.planetaryStorage = planetaryStorage;
			return this;
		}

		public StorageFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			Objects.requireNonNull(this.onConstruction.planetaryStorage);
			return this.onConstruction;
		}

	}
}
