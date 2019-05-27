package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;

public class ColonyPack implements ICollaboration {
	private GetCharactersCharacterIdPlanets200Ok colony;
	private GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer;

	public ColonyPack( final GetCharactersCharacterIdPlanets200Ok colony, final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer ) {
		this.colony = colony;
		this.structureContainer = structureContainer;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}
}
