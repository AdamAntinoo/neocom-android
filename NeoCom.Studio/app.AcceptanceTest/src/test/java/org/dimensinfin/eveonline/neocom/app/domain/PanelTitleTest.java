package org.dimensinfin.eveonline.neocom.app.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PanelTitleTest {
	@Test
	public void gettersContract() {
		final String title = "-TITLE-";
		final PanelTitle panelTitle = new PanelTitle( title );
		Assertions.assertNotNull( panelTitle );
		Assertions.assertEquals( title, panelTitle.getTitle() );
	}

	@Test
	public void settersContract() {
		final String title = "-TITLE-";
		final PanelTitle panelTitle = new PanelTitle( title );
		Assertions.assertEquals( title, panelTitle.getTitle() );
		panelTitle.setTitle( "-NEW-TITLE-" );
		Assertions.assertEquals( "-NEW-TITLE-", panelTitle.getTitle() );
	}

	@Test
	public void toStringContract() {
		final String title = "-TITLE-";
		final PanelTitle panelTitle = new PanelTitle( title );
		final String expected = "PanelTitle [ title: -TITLE- ]";
		Assertions.assertEquals( expected, panelTitle.toString() );
	}
}