package org.dimensinfin.eveonline.neocom.app.support.worlds;

import android.view.View;

import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;

public class PilotDashboardActivityWorld extends NeoComWorld {
	private PilotDashboardActivity activity;
	private PilotDashboardFragment fragment;
	private View miningDataPanel;

	public PilotDashboardActivity getActivity() {
		return this.activity;
	}

	public PilotDashboardActivityWorld setActivity( final PilotDashboardActivity activity ) {
		this.activity = activity;
		return this;
	}

	public PilotDashboardFragment getFragment() {
		return this.fragment;
	}

	public PilotDashboardActivityWorld setFragment( final PilotDashboardFragment fragment ) {
		this.fragment = fragment;
		return this;
	}

	public View getMiningDataPanel() {
		return this.miningDataPanel;
	}

	public PilotDashboardActivityWorld setMiningDataPanel( final View miningDataPanel ) {
		this.miningDataPanel = miningDataPanel;
		return this;
	}
}
