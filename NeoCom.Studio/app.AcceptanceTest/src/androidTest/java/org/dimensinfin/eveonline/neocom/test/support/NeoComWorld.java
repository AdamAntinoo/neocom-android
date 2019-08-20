package org.dimensinfin.eveonline.neocom.test.support;

import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.activity.IPagerFragment;

public class NeoComWorld {
	private ActivityTestRule activityRule;
	private FragmentActivity activeActivity;
	private IPagerFragment fragment;
	private ViewGroup headerContainer;
	private ListView dataSectionContainer;

	public ActivityTestRule getActivityRule() {
		return this.activityRule;
	}

	public NeoComWorld setActivityRule( final ActivityTestRule activityRule ) {
		this.activityRule = activityRule;
		return this;
	}

	public FragmentActivity getActiveActivity() {
		return this.activeActivity;
	}

	public NeoComWorld setActiveActivity( final FragmentActivity activeActivity ) {
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

	public ViewGroup getHeaderContainer() {
		return this.headerContainer;
	}

	public NeoComWorld setHeaderContainer( final ViewGroup headerContainer ) {
		this.headerContainer = headerContainer;
		return this;
	}

	public ListView getDataSectionContainer() {
		return this.dataSectionContainer;
	}

	public NeoComWorld setDataSectionContainer( final ListView dataSectionContainer ) {
		this.dataSectionContainer = dataSectionContainer;
		return this;
	}
}
