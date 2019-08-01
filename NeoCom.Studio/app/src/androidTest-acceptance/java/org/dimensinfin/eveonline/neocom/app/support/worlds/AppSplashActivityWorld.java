package org.dimensinfin.eveonline.neocom.app.support.worlds;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashFragment;
import org.mockito.Mockito;

public class AppSplashActivityWorld {
	private AppSplashActivity activity;
	private AppSplashActivity spyActivity;
	private AppSplashFragment fragment;
	private ListView dataSectionContainer;
	private ViewGroup headerContainer;
	private View appVersionPanel;

	public Activity getActivity() {
		return this.activity;
	}

	public AppSplashActivity getSpyActivity() {
		return spyActivity;
	}

	public void setActivity( final AppSplashActivity activity ) {
		this.activity = activity;
		this.spyActivity = Mockito.spy(this.activity);
	}

	public AppSplashFragment getFragment() {
		return fragment;
	}

	public AppSplashActivityWorld setFragment( final AppSplashFragment fragment ) {
		this.fragment = fragment;
		return this;
	}

	public ViewGroup getHeaderContainer() {
		return headerContainer;
	}

	public AppSplashActivityWorld setHeaderContainer( final ViewGroup headerContainer ) {
		this.headerContainer = headerContainer;
		return this;
	}

	public ListView getDataSectionContainer() {
		return dataSectionContainer;
	}

	public AppSplashActivityWorld setDataSectionContainer( final ListView dataSectionContainer ) {
		this.dataSectionContainer = dataSectionContainer;
		return this;
	}

	public View getAppVersionPanel() {
		return appVersionPanel;
	}

	public AppSplashActivityWorld setAppVersionPanel( final View appVersionPanel ) {
		this.appVersionPanel = appVersionPanel;
		return this;
	}
}
