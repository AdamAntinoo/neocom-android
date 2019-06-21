package org.dimensinfin.eveonline.neocom.app.adapters;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class NeoComComponentFactoryTest {
	@Test
	public void getSDEDatabaseAdapter() {
		final Application application = Mockito.mock(Application.class);
		NeoComComponentFactory.initialiseSingleton(application);
		final ISDEDatabaseAdapter adapter = NeoComComponentFactory.getSingleton().getSDEDatabaseAdapter();
		Assert.assertNotNull(adapter);
	}

	@Test
	public void getPlanetaryRepository() {
		final Application application = Mockito.mock(Application.class);
		NeoComComponentFactory.initialiseSingleton(application);
		final PlanetaryRepository repository = NeoComComponentFactory.getSingleton().getPlanetaryRepository();
		Assert.assertNotNull(repository);
	}
}
