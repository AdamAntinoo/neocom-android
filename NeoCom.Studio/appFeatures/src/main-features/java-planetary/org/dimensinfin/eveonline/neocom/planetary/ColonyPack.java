package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.model.NeoComExpandableNode;

public class ColonyPack extends NeoComExpandableNode {
	private int capsuleerIdentifier = 0; // Use this identifier to identify the owner of the colony when clicking on the item.
	private GetCharactersCharacterIdPlanets200Ok colony;
	private GetUniversePlanetsPlanetIdOk planetData;
	private List<IPlanetaryFacility> facilities;
	private CommandCenterFacility commandCenter;

	// - C O N S T R U C T O R S
	private ColonyPack() {}

	// - G E T T E R S   &   S E T T E R S
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
		return this.colony.getUpgradeLevel();
	}

	public int getCapsuleerIdentifier() {
		return capsuleerIdentifier;
	}

	public List<IPlanetaryFacility> getFacilities() {
		return this.facilities;
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
		return new ArrayList<ICollaboration>((Collection<? extends ICollaboration>) this.facilities);
	}

	// - C O R E
	@Override
	public int compareTo( final Object o ) {
		if (o instanceof ColonyPack)
			return this.capsuleerIdentifier - ((ColonyPack) o).capsuleerIdentifier;
		return 0;
	}

	protected void initialise() {
		this.commandCenter = this.accessCommandCenter();
		this.commandCenter.setCpuInUse(this.calculateUsedCpu());
		this.commandCenter.setPowerInUse(this.calculateUsedPower());
		for (IPlanetaryFacility facility : this.facilities) {
			facility.setCommandCenterPosition(this.commandCenter.getGeoPosition());
		}
	}

	protected CommandCenterFacility accessCommandCenter() {
		for (IPlanetaryFacility facility : this.facilities) {
			if (facility.getFacilityType() == PlanetaryFacilityType.COMMAND_CENTER)
				return (CommandCenterFacility) facility;
		}
		throw new NeoComRuntimeException("There is no command center facility for this colony. Unable to process initialisation.");
	}

	protected int calculateUsedCpu() {
		int usedCpu = 0;
		for (IPlanetaryFacility facility : this.facilities) {
			usedCpu += facility.getCpuUsage();
		}
		return usedCpu;
	}

	protected int calculateUsedPower() {
		int usedPower = 0;
		for (IPlanetaryFacility facility : this.facilities) {
			usedPower += facility.getPowerUsage();
		}
		return usedPower;
	}

	public Float getStorageInUse() {
		Float storage = 0.0F;
		for (IPlanetaryFacility facility : this.facilities) {
			if (facility instanceof IPlanetaryStorage)
				storage += ((IPlanetaryStorage) facility).getTotalVolume();
		}
		return storage;
	}
	public Float getStorageCapacity() {
		Float storage = 0.0F;
		for (IPlanetaryFacility facility : this.facilities) {
			if (facility instanceof IPlanetaryStorage)
				storage += ((IPlanetaryStorage) facility).getStorageCapacity();
		}
		return storage;
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

		public Builder withFacilities( final List<IPlanetaryFacility> facilities ) {
			this.onConstruction.facilities = facilities;
			return this;
		}

		public ColonyPack build() {
			Objects.requireNonNull(this.onConstruction.colony);
			Objects.requireNonNull(this.onConstruction.planetData);
			Objects.requireNonNull(this.onConstruction.facilities);
			// Do other initialisations like calculating the power and cpu used.
			this.onConstruction.initialise();
			return this.onConstruction;
		}
	}
}
