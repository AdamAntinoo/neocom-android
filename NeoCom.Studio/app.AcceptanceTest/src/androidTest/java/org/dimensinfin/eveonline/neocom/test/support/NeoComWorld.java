package org.dimensinfin.eveonline.neocom.test.support;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

public class NeoComWorld {
	private ActivityTestRule activityRule;
	private Activity activeActivity;

	public ActivityTestRule getActivityRule() {
		return activityRule;
	}

	public NeoComWorld setActivityRule( final ActivityTestRule activityRule ) {
		this.activityRule = activityRule;
		return this;
	}

	public Activity getActiveActivity() {
		return activeActivity;
	}

	public NeoComWorld setActiveActivity( final Activity activeActivity ) {
		this.activeActivity = activeActivity;
		return this;
	}
}
