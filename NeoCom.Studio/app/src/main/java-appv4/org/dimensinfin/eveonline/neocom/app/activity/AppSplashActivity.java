package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Bundle;
import android.os.Environment;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.datasource.IDataSource;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.core.ANeoComActivity;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import java.io.File;

public class AppSplashActivity extends ANeoComActivity {
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;

	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		if (this.checkStartup()) {
			this.injectComponents();
			this.addPage(new AppSplashFragment());
		} else
			this.addPage(new BlockFragment());
	}

	@Override
	protected void onStart() {
		super.onStart();
//		this.validatePermissions(); // Get the permissions required to run the application.
		// Check if the files and folders to run on Android are in place. If not create/copy them.
		logger.info("-- [AppSplashActivity.onStart]> Creating application directories...");
		this.createApplicationDirectories(); // Create any application storage directories on the SD card.
		this.validateSDEDatabase();
	}

	private boolean checkStartup() {
		if (null != NeoComComponentFactory.getSingleton())
			return NeoComComponentFactory.getSingleton().getApplication().isSchedulerActive();
		return false;
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

	protected void validateSDEDatabase() {
		if (!this.sdeDatabaseExists()) {
			// Just copy the assets to the application directory.
			this.fileSystem.copyFromAssets(this.getResources().getString(R.string.sdedatabasefilename), null);
		}
	}

	protected boolean sdeDatabaseExists() {
		return new File(this.fileSystem.accessResource4Path(this.getResources().getString(R.string.sdedatabasefilename)))
				       .exists();
	}

	public static class BlockFragment extends MVCPagerFragment {

		@Override
		public IControllerFactory createFactory() {
			return new AppControllerFactory(this.getVariant());
		}

		@Override
		public IDataSource createDS() {
			return null;
		}
	}
}
