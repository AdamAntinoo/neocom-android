package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.EVariants;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

public class StorageFacility extends PlanetaryFacilityWrapper implements IPlanetaryStorage {
	private PlanetaryStorage planetaryStorage;

	public PlanetType getPlanetType() {
		return this.planetaryFacility.getPlanetType();
	}

	public Float getStorageCapacity() {
		if (this.planetaryStorage.storageCapacity < 0) {
			final GetUniverseTypesTypeIdOkDogmaAttributes storage = this.planetaryFacility.getDogmaAttributeById(38);
			if (null == storage) return 0.0F;
			else this.planetaryStorage.storageCapacity = storage.getValue();
		}
		return this.planetaryStorage.storageCapacity;
	}

	public Float getTotalVolume() {
		return this.planetaryStorage.getTotalVolume();
	}

	public double getTotalValue() {
		return this.planetaryStorage.getTotalValue();
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		if (variant.equalsIgnoreCase(EVariants.PLANET_FACILITIES.name())) return new ArrayList<>();
		return new ArrayList<>(this.planetaryStorage.getContents());
	}

	// - B U I L D E R
	public static class Builder {
		private StorageFacility onConstruction;

		public Builder() {
			this.onConstruction = new StorageFacility();
		}

		public Builder withPlanetaryFacility( final PlanetaryFacility planetaryFacility ) {
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
