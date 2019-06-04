package org.dimensinfin.eveonline.neocom.app.domain;

import org.dimensinfin.eveonline.neocom.support.PojoTestUtils;

import org.junit.Assert;
import org.junit.Test;

public class PanelTitleTest {
	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(PanelTitle.class);
	}

	@Test
	public void coreContract() {
		final PanelTitle panelTitle = new PanelTitle("TEST TITLE");
		final String expected = "PanelTitle [ title: TEST TITLE ]";
		final String obtained = panelTitle.toString();
		Assert.assertEquals("The toString matches", expected, obtained);
	}
}
