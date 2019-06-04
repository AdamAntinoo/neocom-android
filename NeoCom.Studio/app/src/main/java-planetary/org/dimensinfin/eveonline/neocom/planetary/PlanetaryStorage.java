package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;

public class PlanetaryStorage {
	private List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents;

	public PlanetaryStorage( final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents ) {
		this.contents = contents;
	}
}
