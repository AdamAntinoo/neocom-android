package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import android.app.Application;
import android.content.res.Resources;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.appvisualtest.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.support.TestConfigurationProvider;
import org.dimensinfin.eveonline.neocom.support.TestFileSystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VisualTestsDataSourceTest {
	private static VisualTestsDataSource ds;

	@Before
	public void setUp() throws Exception {
		final TestConfigurationProvider configurationProvider = new TestConfigurationProvider.Builder("properties").build();
		final TestFileSystem fileSystem = new TestFileSystem("src/resources");
		final ESIDataAdapter esiDataAdapter = new ESIDataAdapter.Builder(configurationProvider, fileSystem).build();
		EveItem.injectEsiDataAdapter(esiDataAdapter);
		final VisualTestsControllerFactory factory = new VisualTestsControllerFactory("H");

		ds = new VisualTestsDataSource.Builder()
				     .addIdentifier("TEST DATA SOURCE")
				     .withVariant("V")
				     .withFactory(factory)
					 .withEsiDataAdapter(esiDataAdapter)
				     .build();
	}

	@Test
	public void prepareModel() {
		Assert.assertNotNull(ds);
		ds.prepareModel();
	}

	@Test
	public void collaborate2Model() {
		Assert.assertNotNull(ds);
		ds.collaborate2Model();
	}
}
