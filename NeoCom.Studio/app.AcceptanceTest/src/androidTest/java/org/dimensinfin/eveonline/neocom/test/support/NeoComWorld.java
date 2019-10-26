package org.dimensinfin.eveonline.neocom.test.support;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.activity.core.NeoComFragment;
import org.joda.time.LocalDate;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NeoComWorld {
	public static final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(1);
	private NeoComApplication application;
	private ActivityScenario activityScenario;
	private Activity activeActivity;

	private ActivityTestRule activityRule;
	private NeoComFragment fragment;
//	private ViewGroup headerContainer;
//	private ListView dataSectionContainer;
	private LocalDate todayDate;
	private View actionBar;

	public ExecutorService getExecutor() {
		return backgroundExecutor;
	}
	public NeoComApplication getApplication() {
		return this.application;
	}

	public NeoComWorld setApplication( final NeoComApplication application ) {
		this.application = application;
		return this;
	}

	public ActivityScenario getActivityScenario() {
		return this.activityScenario;
	}

	public NeoComWorld setActivityScenario( final ActivityScenario activityScenario ) {
		this.activityScenario = activityScenario;
		return this;
	}
	public Activity getActiveActivity() {
		return this.activeActivity;
	}

	public NeoComWorld setActiveActivity( final Activity activeActivity ) {
		this.activeActivity = activeActivity;
		return this;
	}

	// --------------------------------------------------------------
	public View getActionBar() {
		return this.actionBar;
	}

	public NeoComWorld setActionBar( final View actionBar ) {
		this.actionBar = actionBar;
		return this;
	}


	public ActivityTestRule getActivityRule() {
		return this.activityRule;
	}

	public NeoComWorld setActivityRule( final ActivityTestRule activityRule ) {
		this.activityRule = activityRule;
		return this;
	}


	public NeoComFragment getFragment() {
		return this.fragment;
	}

	public NeoComWorld setFragment( final NeoComFragment fragment ) {
		this.fragment = fragment;
		return this;
	}

	public ViewGroup getHeaderContainer() {
		Objects.requireNonNull(this.fragment);
		return this.fragment.accessHeaderContainer();
	}

//	public NeoComWorld setHeaderContainer( final ViewGroup headerContainer ) {
//		this.headerContainer = headerContainer;
//		return this;
//	}

	public ListView getDataSectionContainer() {
		Objects.requireNonNull(this.fragment);
		return this.fragment.accessDataSectionContainer();
	}

//	public NeoComWorld setDataSectionContainer( final ListView dataSectionContainer ) {
//		this.dataSectionContainer = dataSectionContainer;
//		return this;
//	}

	public LocalDate getTodayDate() {
		return this.todayDate;
	}

	public NeoComWorld setTodayDate( final LocalDate todayDate ) {
		this.todayDate = todayDate;
		return this;
	}
}
