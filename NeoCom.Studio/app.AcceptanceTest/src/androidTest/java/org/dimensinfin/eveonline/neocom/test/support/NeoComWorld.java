package org.dimensinfin.eveonline.neocom.test.support;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.joda.time.LocalDate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NeoComWorld {
	public static final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(1);
	private NeoComApplication application;
	private ActivityTestRule activityRule;
	private FragmentActivity activeActivity;
	private IPagerFragment fragment;
	private ViewGroup headerContainer;
	private ListView dataSectionContainer;
	private LocalDate todayDate;
	private View actionBar;

	public ExecutorService getExecutor() {
		return backgroundExecutor;
	}

	public View getActionBar() {
		return this.actionBar;
	}

	public NeoComWorld setActionBar( final View actionBar ) {
		this.actionBar = actionBar;
		return this;
	}

	public NeoComApplication getApplication() {
		return this.application;
	}

	public NeoComWorld setApplication( final NeoComApplication application ) {
		this.application = application;
		return this;
	}

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

	public LocalDate getTodayDate() {
		return this.todayDate;
	}

	public NeoComWorld setTodayDate( final LocalDate todayDate ) {
		this.todayDate = todayDate;
		return this;
	}
}
