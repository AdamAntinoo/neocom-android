package org.dimensinfin.eveonline.neocom.test.support;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;
import org.dimensinfin.eveonline.neocom.assets.domain.StationMVCWrapper;

public class PanelTypesTest {

	@Test
	public void decodeName() {
		Assert.assertEquals(PanelTypes.REGIONCONTROLLER , PanelTypes.decodeName( "Region" ));
		Assert.assertEquals(PanelTypes.NOT_SET , PanelTypes.decodeName( "Regions" ));
	}

	@Test
	public void matches() {
		final StationMVCWrapper model = Mockito.mock(StationMVCWrapper.class);
		final ControllerFactory factory = Mockito.mock(ControllerFactory.class);
		Assert.assertTrue( PanelTypes.STATIONCONTROLLER.matches( new StationController( model , factory) ) );
		Assert.assertFalse( PanelTypes.REGIONCONTROLLER.matches( new StationController( model , factory) ) );
	}
}
