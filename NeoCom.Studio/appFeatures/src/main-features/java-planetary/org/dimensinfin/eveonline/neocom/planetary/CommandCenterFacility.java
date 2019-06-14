package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

public class CommandCenterFacility extends PlanetaryFacilityWrapper implements IPlanetaryStorage {
	private int level = 0;
	private int cpuCapacity = -1;
	private int powerOutput = -1;
	private int cpuInUse = -1;
	private int powerInUse = -1;
	private transient PlanetaryStorage planetaryStorage;

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		return new ArrayList<>(this.planetaryStorage.getContents());
	}

	// - C O M M A N D C E N T E R
	@Override
	public String getName() {
		return CommandCenterType.getTypeByLevel(level).getName(this.getPlanetType());
	}

	public Integer getUpgradeLevel() {
		return this.level;
	}

	public PlanetType getPlanetType() {
		return this.planetaryFacility.getPlanetType();
	}

	public int getCpuCapacity() {
		if (this.cpuCapacity < 0) {
			final GetUniverseTypesTypeIdOkDogmaAttributes cpu = this.planetaryFacility.getDogmaAttributeById(48);
			if (null == cpu) return 0;
			else this.cpuCapacity = cpu.getValue().intValue();
		}
		return this.cpuCapacity;
	}

	/**
	 * Get the dogma attribute that represents the power output.
	 */
	public int getPowerOutput() {
		if (this.powerOutput < 0) {
			final GetUniverseTypesTypeIdOkDogmaAttributes power = this.planetaryFacility.getDogmaAttributeById(11);
			if (null == power) return 0;
			else this.powerOutput = power.getValue().intValue();
		}
		return this.powerOutput;
	}

	public CommandCenterFacility setCpuInUse( final int cpuInUse ) {
		this.cpuInUse = cpuInUse;
		return this;
	}

	public CommandCenterFacility setPowerInUse( final int powerInUse ) {
		this.powerInUse = powerInUse;
		return this;
	}

	/**
	 * Get the CPU already used by the planet facilities. This calculation requires to scan through all the facilities and add the used capacities
	 * from each of them.
	 */
	public int cpuInUse() {
		return this.cpuInUse;
	}

	public int powerInUse() {
		return this.powerInUse;
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

	// - B U I L D E R
	public static class Builder {
		private CommandCenterFacility onConstruction;

		public Builder() {
			this.onConstruction = new CommandCenterFacility();
		}

		public Builder withPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			return this;
		}

		public Builder withLevel( final int level ) {
			this.onConstruction.level = level;
			this.onConstruction.cpuCapacity = CommandCenterType.getTypeByLevel(level).getCpuCapacity();
			this.onConstruction.powerOutput = CommandCenterType.getTypeByLevel(level).getPowerOutput();
			return this;
		}

		public Builder withPlanetaryStorage( final PlanetaryStorage planetaryStorage ) {
			this.onConstruction.planetaryStorage = planetaryStorage;
			return this;
		}

		public CommandCenterFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			Objects.requireNonNull(this.onConstruction.planetaryStorage);
			return this.onConstruction;
		}

	}
}
