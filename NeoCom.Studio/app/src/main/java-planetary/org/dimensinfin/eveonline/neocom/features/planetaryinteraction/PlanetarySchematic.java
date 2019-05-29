package org.dimensinfin.eveonline.neocom.features.planetaryinteraction;

import org.dimensinfin.eveonline.neocom.app.adapters.NeoComAndroid;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;

public class PlanetarySchematic {
	private Integer schematicId;
	private int cycleTime = 3600;
	private String schematicName = "N/A";

	// - C O N S T R U C T O R S
	private PlanetarySchematic( final Integer schematicId ) {
		this.schematicId = schematicId;
		if (null != this.schematicId) {
			// Get the schematics production data.
			final GetUniverseSchematicsSchematicIdOk schematic = NeoComAndroid.getSingleton().getEsiAdapter().getUniversePlanetarySchematicsById("tranquility",this.schematicId);
			if (null != schematic) {
				this.schematicName = schematic.getSchematicName();
				this.cycleTime = schematic.getCycleTime();
			}
		}
	}

	public Integer getSchematicId() {
		return schematicId;
	}

	// - B U I L D E R
	public static class Builder {
		private final PlanetarySchematic object;

		public Builder( final Integer schematicId ) {
			this.object = new PlanetarySchematic(schematicId);
		}

		public PlanetarySchematic build() {
			return this.object;
		}
	}
}
