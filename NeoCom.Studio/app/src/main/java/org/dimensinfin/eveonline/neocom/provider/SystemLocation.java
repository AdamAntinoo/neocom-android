package org.dimensinfin.eveonline.neocom.provider;

public class SystemLocation {
	private SystemLocation() {}

	// - B U I L D E R
	public static class Builder {
		private SystemLocation onConstruction;

		public Builder() {
			this.onConstruction = new SystemLocation();
		}

		public SystemLocation build() {
			return this.onConstruction;
		}
	}
}