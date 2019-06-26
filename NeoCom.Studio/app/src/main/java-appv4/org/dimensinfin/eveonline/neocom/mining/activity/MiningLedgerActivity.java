package org.dimensinfin.eveonline.neocom.mining.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;

public class MiningLedgerActivity extends ACredentialActivity {
	@Override
	public void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new MiningLedgerFragment()
				             .setCredential(this.getCredential())
				             .setVariant(PageNamesType.MINING_EXTRACTIONS_TODAY.name()));
		this.addPage(new MiningLedgerFragment()
				             .setCredential(this.getCredential())
				             .setVariant(PageNamesType.MINING_EXTRACTIONS_ALL.name()));
	}
}
