package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.android.mvc.controller.ControllerFactory;

public class TestFeaturesControllerFactory extends ControllerFactory {
	public TestFeaturesControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}
//	private TestFeaturesControllerFactory(  ) { }
//	public TestFeaturesControllerFactory( final String s ) { }

	// - B U I L D E R
	public static class Builder {
		private TestFeaturesControllerFactory onConstruction;

		public Builder() {
			this.onConstruction = new TestFeaturesControllerFactory("-TEST-");
		}

		public TestFeaturesControllerFactory build() {
			return this.onConstruction;
		}
	}
}