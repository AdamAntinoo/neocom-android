package org.dimensinfin.eveonline.neocom.app.activity;

import java.util.Objects;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.ACredentialActivity;
import org.dimensinfin.eveonline.neocom.app.controller.NeoComActionBarController;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComActionBarV2;

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
