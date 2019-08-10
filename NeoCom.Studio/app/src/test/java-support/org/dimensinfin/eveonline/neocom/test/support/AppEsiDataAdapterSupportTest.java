package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.domain.EveItem;
import org.dimensinfin.eveonline.neocom.support.TestConfigurationProvider;
import org.junit.Before;

public class AppEsiDataAdapterSupportTest {
	protected ESIDataAdapter esiDataAdapter;

	@Before
	public void setUp() {
		final IConfigurationProvider configurationProvider = new TestConfigurationProvider.Builder("properties").build();
		final IFileSystem fileSystemAdapter = new AppTestFileSystem();
		this.esiDataAdapter = new ESIDataAdapter.Builder(configurationProvider, fileSystemAdapter).build();
		EveItem.injectEsiDataAdapter(this.esiDataAdapter);
	}
}
