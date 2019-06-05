package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import android.app.Activity;
import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AMVCMultiPageActivity;

public class VisualTestsActivity extends AMVCMultiPageActivity {
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.addPage(new VisualTestsFragment());
	}
}
