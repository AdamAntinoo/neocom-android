package org.dimensinfin.eveonline.neocom.appvisualtest.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dimensinfin.android.mvc.activity.AMVCMultiPageActivity;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.appvisualtest.app.pilot.VTPilotFragment;
import org.dimensinfin.eveonline.neocom.appvisualtest.planetary.advancedfacility.VTAdvancedFacilityFragment;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import java.io.File;

public class VisualTestsActivity extends AMVCMultiPageActivity {
	private IFileSystem fileSystem;

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.injectComponents();
		this.validatePermissions();
		this.validateSDEDatabase();
		this.addPage(new VTPilotFragment().setVariant(PageNamesType.PILOT_DASHBOARD.name()));

		this.addPage(new VTAdvancedFacilityFragment().setVariant(PageNamesType.PLANET_FACILITIES.name()));
		this.addPage(new VTCredentialsFragment().setVariant(PageNamesType.SPLASH.name()));
		this.addPage(new VisualTestsPlanetaryLayoutFragment().setVariant(PageNamesType.PLANET_FACILITIES.name()));
		this.addPage(new VisualTestsFragment().setVariant(PageNamesType.PLANETS_LIST.name()));
		this.addPage(new VisualTestsFragment().setVariant(PageNamesType.SPLASH.name()));
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

	protected void validateSDEDatabase() {
		if (!this.sdeDatabaseExists()) {
			// Just copy the assets to the application directory.
			this.fileSystem.copyFromAssets(this.getResources().getString(R.string.sdedatabasefilename), null);
		}
	}

	protected boolean sdeDatabaseExists() {
		return new File(this.fileSystem.accessResource4Path(this.getResources().getString(R.string.sdedatabasefilename))).exists();
	}

	protected void injectComponents() {
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}
}
