package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dimensinfin.android.mvc.activity.AMVCMultiPageActivity;
import org.dimensinfin.eveonline.neocom.app.EVariants;

public class VisualTestsActivity extends AMVCMultiPageActivity {
	public enum VisualTestPages {
		SPLASH, PLANETARY_COMMANDCENTER,
		PLANETARY_LAYOUT
	}

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.validatePermissions();
		this.addPage(new VisualTestsPlanetaryLayoutFragment().setVariant(EVariants.PLANET_FACILITIES.name()));
		this.addPage(new VisualTestsFragment().setVariant(EVariants.PLANETS_LIST.name()));
		this.addPage(new VisualTestsFragment().setVariant(VisualTestPages.SPLASH.name()));
	}

	protected void validatePermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					10);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.INTERNET},
					20);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
					30);
		}
	}
}
