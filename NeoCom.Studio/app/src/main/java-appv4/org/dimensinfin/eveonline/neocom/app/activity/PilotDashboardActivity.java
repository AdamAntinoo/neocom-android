package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.Objects;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;

/**
 * @author Adam Antinoo
 */
public class PilotDashboardActivity extends ACredentialActivity {
	// - A C T I V I T Y   L I F E C Y C L E
	@Override
	public void onCreate( @Nullable Bundle savedInstanceState ) {
		logger.info(">> [PilotDashboardActivity.onCreate]");
		super.onCreate(savedInstanceState);
		Objects.requireNonNull(this.getCredential());
		//		this.setupActionBar(this.generateActionBar()); // Set the page action bar component
		this.addPage(new PilotDashboardFragment().setCredential(this.getCredential()));
		logger.info("<< [PilotDashboardActivity.onCreate]");
	}
}
