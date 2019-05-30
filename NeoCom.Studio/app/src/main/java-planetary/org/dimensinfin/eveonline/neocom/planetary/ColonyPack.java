package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;

public class ColonyPack implements ICollaboration {
	private int capsuleerIdentifier = 0;
	private GetCharactersCharacterIdPlanets200Ok colony;
	private GetUniversePlanetsPlanetIdOk planetData;
	private GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer;

	public ColonyPack( final GetCharactersCharacterIdPlanets200Ok colony, final GetUniversePlanetsPlanetIdOk planetData, final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer ) {
		this.colony = colony;
		this.planetData = planetData;
		this.structureContainer = structureContainer;
	}

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
		return this.structureContainer.getPins().size();
	}

	public Integer getUpgradeLevel() {
		return colony.getUpgradeLevel();
	}

	public int getCapsuleerIdentifier() {
		return capsuleerIdentifier;
	}

	public ColonyPack setCapsuleerIdentifier( final int capsuleerIdentifier ) {
		this.capsuleerIdentifier = capsuleerIdentifier;
		return this;
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		if (o instanceof ColonyPack)
			return this.capsuleerIdentifier - ((ColonyPack) o).capsuleerIdentifier;
		return 0;
	}
}
