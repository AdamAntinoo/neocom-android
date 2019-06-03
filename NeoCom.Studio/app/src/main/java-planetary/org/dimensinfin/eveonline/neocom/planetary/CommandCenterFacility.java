package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import org.joda.time.DateTime;

public class CommandCenterFacility extends NeoComNode implements IPlanetaryFacility {
	private IPlanetaryFacility planetaryFacility;
	private int cpuCapacity = -1;
	private int powerOutput = -1;
	private int cpuInUse = -1;
	private int powerInUse = -1;

	// - C O M M A N D C E N T E R

	public int getCpuCapacity() {
		if (this.cpuCapacity < 0) {
			final GetUniverseTypesTypeIdOkDogmaAttributes cpu = this.planetaryFacility.getDogmaAttributeById(48);
			if (null == cpu) return 0;
			else this.cpuCapacity = Float.valueOf(cpu.getValue()).intValue();
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
			else this.powerOutput = Float.valueOf(power.getValue()).intValue();
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

	// - D E L E G A T E S
	@Override
	public String getName() {
		return this.planetaryFacility.getName();
	}

	@Override
	public PlanetaryFacilityType getFacilityType() {
		return this.planetaryFacility.getFacilityType();
	}

	@Override
	public int getCpuUsage() {
		return this.planetaryFacility.getCpuUsage();
	}

	@Override
	public int getPowerUsage() {
		return this.planetaryFacility.getPowerUsage();
	}

	@Override
	public GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId ) {
		return this.planetaryFacility.getDogmaAttributeById(attributeId);
	}

	@Override
	public int getIconReferenceId() {
		return planetaryFacility.getIconReferenceId();
	}

	@Override
	public int getStructureColorReference() {
		return planetaryFacility.getStructureColorReference();
	}

	@Override
	public int getBackgroundColor() {
		return planetaryFacility.getBackgroundColor();
	}

	@Override
	public int getIconColorReference() {
		return planetaryFacility.getIconColorReference();
	}

	@Override
	public DateTime getLastCycleStart() {
		return planetaryFacility.getLastCycleStart();
	}

	@Override
	public Integer getSchematicId() {
		return planetaryFacility.getSchematicId();
	}

	@Override
	public List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> getContents() {
		return planetaryFacility.getContents();
	}

	@Override
	public double getContentsVolume() {
		return planetaryFacility.getContentsVolume();
	}

	@Override
	public Float getStorageCapacity() {
		return planetaryFacility.getStorageCapacity();
	}

	@Override
	public PlanetaryFacility setCommandCenter( final IPlanetaryFacility commandCenter ) {
		return planetaryFacility.setCommandCenter(commandCenter);
	}

	@Override
	public FacilityGeoPosition getGeoPosition() {
		return planetaryFacility.getGeoPosition();
	}

	@Override
	public CommandCenterFacility getCommandCenter() {
		return planetaryFacility.getCommandCenter();
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

		public CommandCenterFacility build() {
			Objects.requireNonNull(this.onConstruction.planetaryFacility);
			return this.onConstruction;
		}
	}
}
