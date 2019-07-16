package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlanetaryStorageTest {
	@Test
	public void constructor() {
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final PlanetaryStorage storage = new PlanetaryStorage(contents);
		Assert.assertNotNull(storage);
	}

	@Test
	public void getContents() {
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final PlanetaryStorage storage = new PlanetaryStorage(contents);
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2463); // Nanites
		content.setAmount(3000L);
		contents.add(content);
		final List<PlanetaryResource> obtained = storage.getContents();

		Assert.assertNotNull(storage);
		Assert.assertEquals("Check the number of contents.", 1, obtained.size());
		Assert.assertEquals("Check the quantity of the contained item.", 3000L, obtained.get(0).getQuantity());
		Assert.assertEquals("Check the name of the contained item.", "Nanites", obtained.get(0).getName());
	}
}