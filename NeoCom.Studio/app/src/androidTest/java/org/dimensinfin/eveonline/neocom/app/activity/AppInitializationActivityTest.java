package org.dimensinfin.eveonline.neocom.app.activity;

import android.os.Environment;
import android.widget.LinearLayout;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;

@RunWith(AndroidJUnit4.class)
public class AppInitializationActivityTest {
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;

	@Before
	public void connectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}

	@Before
	public void revokePermissions() {
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
		                       .executeShellCommand("pm revoke ${getTargetContext().packageName} android.permission.WRITE_EXTERNAL_STORAGE");
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
		                       .executeShellCommand("pm revoke ${getTargetContext().packageName} android.permission.INTERNET");
		InstrumentationRegistry.getInstrumentation().getUiAutomation()
		                       .executeShellCommand("pm revoke ${getTargetContext().packageName} android.permission.ACCESS_NETWORK_STATE");
	}

	public void removeFilesAndDirectories( final AppInitializationActivity activity ) throws IOException {
		final File sdCardDir = new File(Environment.getExternalStorageDirectory()
				, activity.getResources().getString( R.string.appname));
		sdCardDir.delete();
		final File cachedir = new File(Environment.getExternalStorageDirectory()
				, activity.getResources().getString(R.string.appname)
						  + "/" + this.configurationProvider.getResourceString("P.cache.directory.path"));
		cachedir.delete();
		final File sdeDatabase = new File(this.fileSystem.accessResource4Path(
				activity.getResources().getString(R.string.sdedatabasefilename)));
		sdeDatabase.delete();
		final File sdeDatabaseJournal = new File(this.fileSystem.accessResource4Path(
				activity.getResources().getString(R.string.sdedatabasefilename) + "-journal"));
		sdeDatabaseJournal.delete();
	}

	@Test
	public void applicationLifecycle() {
		try (ActivityScenario<AppInitializationActivity> scenario = ActivityScenario.launch(AppInitializationActivity.class)) {
			scenario.onActivity(( activity ) -> {
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [applicationLifecycle]> Clearing filesystem...");
				try {
					this.removeFilesAndDirectories(activity);
				} catch (IOException e) { }
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Entering onActivity");
				final LinearLayout layout = activity.findViewById(R.id.initializationLayout);
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Checking asserts");
				Assert.assertNotNull(layout);
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Waiting to complete the page...");
				try {
					Thread.sleep(TimeUnit.SECONDS.toMillis(5));
				} catch (InterruptedException e) { }
				Assert.assertTrue(layout.getChildCount() > 0);
			});
		}
	}
}
