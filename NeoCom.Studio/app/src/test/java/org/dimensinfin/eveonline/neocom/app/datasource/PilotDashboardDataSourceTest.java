package org.dimensinfin.eveonline.neocom.app.datasource;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.app.domain.PilotSection;
import org.dimensinfin.eveonline.neocom.test.support.AppCredentialSupportTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PilotDashboardDataSourceTest extends AppCredentialSupportTest {
	private ControllerFactory mockFactory;
	private PilotDashboardDataSource datasource;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		mockFactory = Mockito.mock(ControllerFactory.class);
		datasource = new PilotDashboardDataSource.Builder()
				.addIdentifier(100)
				.addIdentifier("PILOT-DASHBOARD")
				.withVariant("TEST-VARIANT")
				.withExtras(null)
				.withFactory(mockFactory)
				.withEsiDataAdapter(this.esiDataAdapter)
				.withCredential(this.credential)
				.build();
	}

	@Test
	public void build_success() {
		final ControllerFactory factory = Mockito.mock(ControllerFactory.class);
		final PilotDashboardDataSource datasource = new PilotDashboardDataSource.Builder()
				.addIdentifier(100)
				.addIdentifier("PILOT-DASHBOARD")
				.withVariant("TEST-VARIANT")
				.withExtras(null)
				.withFactory(factory)
				.withEsiDataAdapter(this.esiDataAdapter)
				.withCredential(this.credential)
				.build();
		Assert.assertNotNull(datasource);
	}

	@Test(expected = NullPointerException.class)
	public void build_failure() {
		final ControllerFactory factory = Mockito.mock(ControllerFactory.class);
		final PilotDashboardDataSource datasource = new PilotDashboardDataSource.Builder()
				.addIdentifier(100)
				.addIdentifier("PILOT-DASHBOARD")
				.withVariant("TEST-VARIANT")
				.withEsiDataAdapter(this.esiDataAdapter)
				.build();
	}

	@Test
	public void prepareModel() {
		Assert.assertNotNull(this.datasource);
		this.datasource.prepareModel();
		Assert.assertEquals("The number of sections should be the expected.", 4, this.datasource.sections.size());
		Assert.assertEquals("The number of model on the header section.", 0, this.datasource.getHeaderModelRoot().size());
		Assert.assertEquals("The number of model on the data section.", 1, this.datasource.getDataModelRoot().size());
	}

	@Test
	public void collaborate2Model() {
		Assert.assertNotNull(this.datasource);
		this.datasource.prepareModel();
		this.datasource.collaborate2Model();
		Assert.assertEquals("The number of model on the header section.", 0, this.datasource.getHeaderModelRoot().size());
		Assert.assertEquals("The number of model on the data section.", 1+4, this.datasource.getDataModelRoot().size());
	}

	@Test
	public void DataProviderGetDataFormatted() {
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanets");
		Assert.assertNotNull(this.datasource);
		this.datasource.prepareModel();
		for (PilotSection section : this.datasource.sections) {
			final String obtained = section.getDataFormatted();
			Assert.assertFalse(obtained.isEmpty());
		}
	}
}