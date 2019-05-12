package org.dimensinfin.eveonline.neocom.main;

import org.dimensinfin.eveonline.neocom.model.PilotV2;

public class PilotV3 extends PilotV2 {
	// - D A T A   A C C E S S O R S
	public double getAccountBalance() {
		return super.getAccountBalance();
	}

	public long getTotalAssetsNumber() {
		return this.totalAssetsNumber;
	}
}
