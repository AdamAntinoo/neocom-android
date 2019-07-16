package org.dimensinfin.eveonline.neocom.planetary.facilities;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterType2LevelConverter;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryStorage;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityWrapper;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandCenterFacility extends PlanetaryFacilityWrapper implements IPlanetaryStorage {
	private int upgradeLevel = 0;
	private int cpuInUse = -1;
	private int powerInUse = -1;
	private PlanetaryStorage planetaryStorage;
	private CommandCenterType2LevelConverter commandCenterLevel = CommandCenterType2LevelConverter.getTypeByLevel(this.upgradeLevel);

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		if (variant.equalsIgnoreCase(PageNamesType.PLANET_FACILITIES_LAYOUT.name())) return new ArrayList<>();
		return new ArrayList<>(this.planetaryStorage.getContents());
	}

	// - C O M M A N D C E N T E R
	@Override
	public String getName() {
		return CommandCenterType2LevelConverter.getTypeByLevel(upgradeLevel).getName(this.getPlanetType());
	}

	public Integer getUpgradeLevel() {
		return this.upgradeLevel;
	}

	public CommandCenterFacility setUpgradeLevel( final int upgradeLevel ) {
		this.upgradeLevel = upgradeLevel;
		this.commandCenterLevel = CommandCenterType2LevelConverter.getTypeByLevel(this.upgradeLevel);
		return this;
	}

	public PlanetType getPlanetType() {
		return this.planetaryFacility.getPlanetType();
	}

	public int getCpuCapacity() {
		return this.commandCenterLevel.getCpuCapacity();
	}

	public int getPowerOutput() {
		return this.commandCenterLevel.getPowerOutput();
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

//	public Float getStorageCapacity() {
//		if (this.planetaryStorage.getStorageCapacity() < 0) {
//			final GetUniverseTypesTypeIdOkDogmaAttributes storage = this.planetaryFacility.getDogmaAttributeById(38);
//			if (null == storage) return 0.0F;
//			else this.planetaryStorage.setStorageCapacity(storage.getValue());
//		}
//		return this.planetaryStorage.getStorageCapacity();
//	}

	public Float getTotalVolume() {
		return this.planetaryStorage.getTotalVolume();
	}

	@Override
	public int getIconReferenceId() {
		return R.drawable.commandcenter80_white;
	}

	@Override
	public int getIconColorReference() {
		return R.color.pi_commandcentericoncolor;
	}

	// - B U I L D E R
	public static class Builder {
		private CommandCenterFacility onConstruction;

		public Builder() {
			this.onConstruction = new CommandCenterFacility();
		}

		public Builder withPlanetaryFacility( final PlanetaryFacility planetaryFacility ) {
			this.onConstruction.planetaryFacility = planetaryFacility;
			return this;
		}

//		public Builder withLevel( final int level ) {
//			this.onConstruction.level = level;
//			this.onConstruction.commandCenterLevel = CommandCenterType2LevelConverter.getTypeByLevel(this.onConstruction.level);
////			this.onConstruction.cpuCapacity = CommandCenterType2LevelConverter.getTypeByLevel(level).getCpuCapacity();
////			this.onConstruction.powerOutput = CommandCenterType2LevelConverter.getTypeByLevel(level).getPowerOutput();
//			return this;
//		}

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
