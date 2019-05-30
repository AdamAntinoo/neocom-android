package org.dimensinfin.eveonline.neocom.planetary;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseSchematicsSchematicIdOk;

public class PlanetarySchematic {
	private Integer schematicId;
	private int cycleTime = 3600;
	private String schematicName = "N/A";

	private ESIGlobalAdapter esiAdapter;

	// - C O N S T R U C T O R S
	private PlanetarySchematic( final Integer schematicId ) {
		this.schematicId = schematicId;
		if (null != this.schematicId) {
			// Get the schematics production data.
			final GetUniverseSchematicsSchematicIdOk schematic = this.esiAdapter.getUniversePlanetarySchematicsById("tranquility", this.schematicId);
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
		private final PlanetarySchematic onConstruction;

		public Builder( final Integer schematicId ) {
			this.onConstruction = new PlanetarySchematic(schematicId);
		}

		public Builder withEsiAdapter( final ESIGlobalAdapter esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}

		public PlanetarySchematic build() {
			Objects.requireNonNull(this.onConstruction.esiAdapter);
			return this.onConstruction;
		}
	}
}
