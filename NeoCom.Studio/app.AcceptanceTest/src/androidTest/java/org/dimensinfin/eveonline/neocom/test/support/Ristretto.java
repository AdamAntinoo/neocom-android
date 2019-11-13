package org.dimensinfin.eveonline.neocom.test.support;

public class Ristretto {
	private Ristretto() {}

	// - B U I L D E R
	public static class Builder {
		private Ristretto onConstruction;

		public Builder() {
			this.onConstruction = new Ristretto();
		}

		public Ristretto build() {
			return this.onConstruction;
		}
	}
}