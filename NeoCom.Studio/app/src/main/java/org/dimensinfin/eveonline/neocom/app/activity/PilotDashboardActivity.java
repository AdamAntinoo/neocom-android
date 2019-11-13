package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class PilotDashboardActivity extends ACredentialActivity {
	// - A C T I V I T Y   L I F E C Y C L E
	@LogEnterExit
	@Override
	public void onCreate( @Nullable Bundle savedInstanceState ) {
		NeoComLogger.enter();
		super.onCreate( savedInstanceState );
		Objects.requireNonNull( this.getCredential() );
		this.addPage( new PilotDashboardFragment() );
		logger.info( "<< [PilotDashboardActivity.onCreate]" );
	}
}
