package org.dimensinfin.eveonline.neocom.mining.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.sharedcode.activity.ACredentialActivity;

public class MiningLedgerActivity extends ACredentialActivity {
	public enum EMiningVariants {
		MINING_LEDGER, MINING_EXTRACTIONS_TODAY
	}

	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new MiningLedgerFragment()
				             .setCredential(this.getCredential())
				             .setVariant(EMiningVariants.MINING_EXTRACTIONS_TODAY.name()));
	}
}
