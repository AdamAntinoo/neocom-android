package org.dimensinfin.eveonline.neocom.test.support;

import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;

public class NeoComWorld {
	private ActivityTestRule activityRule;
	private MVCMultiPageActivity activeActivity;
	private IPagerFragment fragment;

	public ActivityTestRule getActivityRule() {
		return activityRule;
	}

	public NeoComWorld setActivityRule( final ActivityTestRule activityRule ) {
		this.activityRule = activityRule;
		return this;
	}

	public MVCMultiPageActivity getActiveActivity() {
		return activeActivity;
	}

	public NeoComWorld setActiveActivity( final MVCMultiPageActivity activeActivity ) {
		this.activeActivity = activeActivity;
		return this;
	}

	public IPagerFragment getFragment() {
		return this.fragment;
	}

	public NeoComWorld setFragment( final IPagerFragment fragment ) {
		this.fragment = fragment;
		return this;
	}
}
