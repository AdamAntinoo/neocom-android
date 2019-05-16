package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.view.View;

import org.dimensinfin.android.mvc.activity.AMVCMultiPageActivity;

public class ANeoComActivity extends AMVCMultiPageActivity {
	private View neocomActionBar;

	public void setupActionBar( final View actionBarView ) {
		logger.info(">> [ANeoComActivity.setupActionBar]");
		try {
			this.neocomActionBar = actionBarView;
			this.getActionBar().setCustomView(actionBarView);
			logger.info("<< [ANeoComActivity.setupActionBar]");
		} catch (RuntimeException rtex) {
			logger.info("EX [ANeoComActivity.setupActionBar]> Exception changing Android ActionBar: {}. Returning to default setup.");
		}
	}
}
