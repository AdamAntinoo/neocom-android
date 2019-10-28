package org.dimensinfin.eveonline.neocom.planetary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.dimensinfin.eveonline.neocom.domain.EveItem;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.test.support.ESIDataAdapterSupportTest;

public class PlanetaryStorageTest extends ESIDataAdapterSupportTest {
	@Before
	public void setUp() throws IOException {
		super.setUp();
		EveItem.injectEsiDataAdapter(this.esiDataAdapter);
	}

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

	@Test
	public void getTotalValue_withContents() {
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2463); // Nanites
		content.setAmount(3000L);
		contents.add(content);
		final PlanetaryStorage storage = new PlanetaryStorage(contents);
		final double obtained = storage.getTotalValue();
		final EveItem item = new EveItem(2463);
		final double expected = item.getPrice() * 3000;

		Assert.assertNotNull(storage);
		Assert.assertEquals("Verify the total amount.", expected, obtained, 0.1);
	}

	@Test
	public void getTotalValue_withoutContents() {
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = new ArrayList<>();
		final PlanetaryStorage storage = new PlanetaryStorage(contents);
		final double obtained = storage.getTotalValue();

		Assert.assertNotNull(storage);
		Assert.assertEquals("Verify the total amount.", 0.0, obtained, 0.1);
	}
}