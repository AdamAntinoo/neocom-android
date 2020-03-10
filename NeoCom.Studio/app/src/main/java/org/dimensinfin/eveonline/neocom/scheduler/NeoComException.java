package org.dimensinfin.eveonline.neocom.scheduler;

public class NeoComException {
	private NeoComException() {}

	// - B U I L D E R
	public static class Builder {
		private NeoComException onConstruction;

		public Builder() {
			this.onConstruction = new NeoComException();
		}

		public NeoComException build() {
			return this.onConstruction;
		}
	}
}
