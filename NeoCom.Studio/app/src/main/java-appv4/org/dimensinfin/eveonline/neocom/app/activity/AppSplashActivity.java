package org.dimensinfin.eveonline.neocom.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.core.ANeoComActivity;
import org.dimensinfin.eveonline.neocom.core.updaters.NeoComUpdater;
import org.dimensinfin.eveonline.neocom.domain.EveItem;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import java.io.File;

public class AppSplashActivity extends ANeoComActivity {
	protected IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		this.injectComponents();
		super.onCreate(savedInstanceState);
		this.addPage(new AppSplashFragment());
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.validatePermissions(); // Get the permissions required to run the application.
		// Check if the files and folders to run on Android are in place. If not create/copy them.
		logger.info("-- [AppSplashActivity.onStart]> Creating application directories...");
		this.createApplicationDirectories(); // Create any application storage directories on the SD card.
		this.validateSDEDatabase();
		// Connect special classes to the ESI to allow internal access to game data
		EveItem.injectEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter());
		NeoComUpdater.injectsEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter());
	}

	protected void createApplicationDirectories() {
		this.createAppDir();
		this.createCacheDirectories();
	}

	/**
	 * Creates the application directory if it already does not exist on the sdcard.
	 */
	protected boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File(Environment.getExternalStorageDirectory()
				, this.getResources().getString(R.string.appname));
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		return sdCardDir.exists();
	}

	protected boolean createCacheDirectories() {
		File cachedir = new File(Environment.getExternalStorageDirectory()
				, this.getResources().getString(R.string.appname)
				+ "/" + this.configurationProvider.getResourceString("P.cache.directory.path"));
		cachedir.mkdir();
		return cachedir.exists();
	}

	protected void injectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}

	protected void validatePermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
					10);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.INTERNET },
					20);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.ACCESS_NETWORK_STATE },
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
}
