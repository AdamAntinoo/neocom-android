package org.dimensinfin.eveonline.neocom.mining.controller;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MiningExtractionControllerTest {
	private MiningExtraction miningExtraction;
	private ControllerFactory factory;

	@Before
	public void setUp() throws Exception {
		// Given
		miningExtraction = new MiningExtraction();
		factory = Mockito.mock(ControllerFactory.class);
	}

	@Test
	public void constructor() {
		final MiningExtractionController controller = new MiningExtractionController(miningExtraction, factory);
		// Asserts
		Assert.assertNotNull(controller);
	}


	@Test
	public void buildRender() {
	}

	@Test
	public void getModelId() {
		final MiningExtractionController controller = new MiningExtractionController(miningExtraction, factory);
		final long obtained =controller.getModelId();
		// Asserts
		Assert.assertNotEquals(0, obtained);
	}
}
