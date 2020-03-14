package org.dimensinfin.eveonline.neocom.test.steps;

public class NC06MiningLedger {
	private NC06MiningLedger() {}

	// - B U I L D E R
	public static class Builder {
		private NC06MiningLedger onConstruction;

		public Builder() {
			this.onConstruction = new NC06MiningLedger();
		}

		public NC06MiningLedger build() {
			return this.onConstruction;
		}
	}
}
