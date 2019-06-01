package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.model.NeoComExpandableNode;

public class ColonyPack extends NeoComExpandableNode {
	private int capsuleerIdentifier = 0;
	private GetCharactersCharacterIdPlanets200Ok colony;
	private GetUniversePlanetsPlanetIdOk planetData;
	private List<PlanetaryFacility> facilities;

	private ColonyPack() {}

	public String getName() {
		if (null != this.planetData)
			return this.planetData.getName();
		else return "-";
	}

	public Integer getPlanetId() {
		return colony.getPlanetId();
	}

	public GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum getPlanetType() {
		return this.colony.getPlanetType();
	}

	public int getInstallationsCount() {
		return this.facilities.size();
	}

	public Integer getUpgradeLevel() {
		return colony.getUpgradeLevel();
	}

	public int getCapsuleerIdentifier() {
		return capsuleerIdentifier;
	}

	// - I E X P A N D A B L E
	@Override
	public boolean isEmpty() {
		return this.facilities.isEmpty();
	}

	// - I C O L L A B O R A T I O N

	/**
	 * Generate the list of facilities as the collaboration to the model list. Colonies will collaborate with the colony
	 * components that are the list of facilities.
	 * To be able to integrate this list into the application I should adapt the ESI data to the MVC model so a converter is required.
	 *
	 * @param variation an string to differentiate the environment. This will allow to generate different contents on different environments.
	 * @return the list of colony facilities.
	 */
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>(this.facilities);
	}

	@Override
	public int compareTo( final Object o ) {
		if (o instanceof ColonyPack)
			return this.capsuleerIdentifier - ((ColonyPack) o).capsuleerIdentifier;
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private ColonyPack onConstruction;

		public Builder() {
			this.onConstruction = new ColonyPack();
		}

		public Builder withCapsuleerIdentifier( final int capsuleerIdentifier ) {
			this.onConstruction.capsuleerIdentifier = capsuleerIdentifier;
			return this;
		}

		public Builder withColony( final GetCharactersCharacterIdPlanets200Ok colony ) {
			this.onConstruction.colony = colony;
			return this;
		}

		public Builder withPlanetData( final GetUniversePlanetsPlanetIdOk planetData ) {
			this.onConstruction.planetData = planetData;
			return this;
		}

		public Builder withFacilities( final List<PlanetaryFacility> facilities ) {
			this.onConstruction.facilities = facilities;
			return this;
		}

		public ColonyPack build() {
			Objects.requireNonNull(this.onConstruction.colony);
			Objects.requireNonNull(this.onConstruction.planetData);
			Objects.requireNonNull(this.onConstruction.facilities);
			return this.onConstruction;
		}
	}
}
