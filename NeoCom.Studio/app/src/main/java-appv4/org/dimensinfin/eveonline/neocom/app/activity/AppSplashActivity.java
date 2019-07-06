package org.dimensinfin.eveonline.neocom.app.activity;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.core.ANeoComActivity;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;

public class AppSplashActivity extends ANeoComActivity {
	protected IConfigurationProvider configurationProvider;

	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		this.injectComponents();
		super.onCreate(savedInstanceState);
		this.addPage(new AppSplashFragment());
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Check if the files and folders to run on Android are in place. If not create/copy them.
		this.createAppDir();
		this.createCacheDirectories();
	}

	/**
	 * Creates the application directory if it already does not exist on the sdcard.
	 */
	protected boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdcarddir = new File(Environment.getExternalStorageDirectory()
				, this.getResources().getString(R.string.appname));
		if (!sdcarddir.exists())
			sdcarddir.mkdir();
		return sdcarddir.exists();
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
	}
}
