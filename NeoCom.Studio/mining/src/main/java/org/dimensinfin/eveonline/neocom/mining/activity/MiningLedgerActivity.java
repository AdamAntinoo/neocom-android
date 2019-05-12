package org.dimensinfin.eveonline.neocom.mining.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.sharedcode.activity.NeoComActionBarActivity;

public class MiningLedgerActivity extends ACredentialActivity{
	public enum EMiningVariants {
		MINING_LEDGER, MINING_EXTRACTIONS_TODAY
	}

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		if ( null == savedInstanceState)
			throw new NeoComRuntimeException("Intent not defined. Unable to identify target pilot.");

		this.addPage(new MiningLedgerFragment());
	}
}
