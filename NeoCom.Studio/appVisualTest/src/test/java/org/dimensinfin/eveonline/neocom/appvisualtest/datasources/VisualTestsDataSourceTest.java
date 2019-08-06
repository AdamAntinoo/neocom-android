package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.visual.app.activity.VisualTestsControllerFactory;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
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
		final TestFileSystem fileSystem = new TestFileSystem();
		final ESIDataAdapter esiDataAdapter = new ESIDataAdapter.Builder(configurationProvider, fileSystem).build();
		final ISDEDatabaseAdapter sdeDatabaseAdapter = Mockito.mock(ISDEDatabaseAdapter.class);
		EveItem.injectEsiDataAdapter(esiDataAdapter);
		final VisualTestsControllerFactory factory = new VisualTestsControllerFactory(PageNamesType.PLANETS_LIST.name());

		ds = new VisualTestsDataSource.Builder()
				     .addIdentifier("TEST DATA SOURCE")
				     .withVariant(PageNamesType.PLANETS_LIST.name())
				     .withFactory(factory)
				     .withEsiDataAdapter(esiDataAdapter)
				     .withSDEDatabaseAdapter(sdeDatabaseAdapter)
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
		ds.prepareModel();
		ds.collaborate2Model();
	}
}
