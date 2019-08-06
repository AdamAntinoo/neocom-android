package org.dimensinfin.eveonline.neocom.app.support.worlds;

import android.view.View;

import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerFragment;

public class MiningLedgerActivityWorld extends NeoComWorld {
	private MiningLedgerActivity activity;
	private MiningLedgerFragment fragment;
	private View tableHeaderPanel;

	public MiningLedgerActivity getActivity() {
		return this.activity;
	}

	public MiningLedgerActivityWorld setActivity( final MiningLedgerActivity activity ) {
		this.activity = activity;
		return this;
	}

	public MiningLedgerFragment getFragment() {
		return this.fragment;
	}

	public MiningLedgerActivityWorld setFragment( final MiningLedgerFragment fragment ) {
		this.fragment = fragment;
		return this;
	}

	public View getTableHeaderPanel() {
		return this.tableHeaderPanel;
	}

	public MiningLedgerActivityWorld setTableHeaderPanel( final View tableHeaderPanel ) {
		this.tableHeaderPanel = tableHeaderPanel;
		return this;
	}
}
