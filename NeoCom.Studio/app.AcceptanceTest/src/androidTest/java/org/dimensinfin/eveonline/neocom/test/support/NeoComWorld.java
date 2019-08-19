package org.dimensinfin.eveonline.neocom.test.support;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

public class NeoComWorld {
	private ActivityScenario scenario;
	private ActivityTestRule activityRule;
	private Activity activeActivity;

	public ActivityScenario getScenario() {
		return this.scenario;
	}

	public NeoComWorld setScenario( final ActivityScenario scenario ) {
		this.scenario = scenario;
		return this;
	}

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
