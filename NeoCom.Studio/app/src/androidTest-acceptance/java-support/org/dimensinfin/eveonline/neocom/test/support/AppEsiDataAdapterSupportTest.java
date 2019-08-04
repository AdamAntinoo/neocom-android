package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.junit.Before;

public class AppEsiDataAdapterSupportTest {
	protected ESIDataAdapter esiDataAdapter;

	@Before
	public void setUp() throws Exception{
		final IConfigurationProvider configurationProvider = new AcceptanceTestConfigurationProvider.Builder("properties").build();
		final IFileSystem fileSystemAdapter = new AppTestFileSystem();
		this.esiDataAdapter = new ESIDataAdapter.Builder(configurationProvider, fileSystemAdapter).build();
		EveItem.injectEsiDataAdapter(this.esiDataAdapter);
	}
}
