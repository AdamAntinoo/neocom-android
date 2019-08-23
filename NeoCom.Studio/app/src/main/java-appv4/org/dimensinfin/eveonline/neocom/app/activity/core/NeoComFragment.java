package org.dimensinfin.eveonline.neocom.app.activity.core;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;

public abstract class NeoComFragment extends MVCPagerFragment {
	private boolean onCreateCompleted = false;
	private boolean onStartCompleted = false;
	private Exception lastException;

	public Exception getLastException() {
		return this.lastException;
	}

	public boolean isPhaseOnCreateCompleted() {
		return this.onCreateCompleted;
	}

	public boolean isPhaseOnStartCompleted() {
		return this.onStartCompleted;
	}
}