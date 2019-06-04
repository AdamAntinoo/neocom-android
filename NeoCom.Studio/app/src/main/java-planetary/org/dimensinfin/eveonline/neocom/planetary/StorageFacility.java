package org.dimensinfin.eveonline.neocom.planetary;

import java.util.Objects;

public class StorageFacility extends PlanetaryFacilityWrapper  {
	private PlanetaryStorage planetaryStorage;

	// - B U I L D E R
	public static class Builder {
		private StorageFacility onConstruction;

		public Builder() {
			this.onConstruction = new StorageFacility();
		}

		//		public Builder withPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
		//			this.onConstruction.planetaryFacility = planetaryFacility;
		//			return this;
		//		}

		public Builder withPlanetaryStorage( final PlanetaryStorage planetaryStorage ) {
			this.onConstruction.planetaryStorage = planetaryStorage;
			return this;
		}

		public StorageFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryStorage);
			return this.onConstruction;
		}

	}
}
