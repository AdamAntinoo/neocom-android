package org.dimensinfin.eveonline.neocom.test.supersteps;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.core.SettingsActivity;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;


import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

public class BackgroundInitApp {
	private NeoComWorld world;
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;
//	private SupportCredentialRepository credentialRepository;
//	private SupportMiningRepository miningRepository;

	@Rule
	public GrantPermissionRule mPermissionReadExternalStorageRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionWriteExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);

	public BackgroundInitApp( final NeoComWorld world ) {
		this.world = world;
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}

	@Given("an initialized application")
	public void anInitializedApplication() {
		final Application application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull(application);
		this.world.setApplication((NeoComApplication) application);
	}

	@And("the application access privileges")
	public void theApplicationAccessPrivileges() throws InterruptedException {
		final ActivityTestRule<SettingsActivity> activityRuleSettings = new ActivityTestRule<>(SettingsActivity.class,
		                                                                                       false, false);
		Assert.assertNotNull(activityRuleSettings);
		activityRuleSettings.launchActivity(null);
		final Activity activity = activityRuleSettings.getActivity();
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
			                                  new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
			                                  10);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
			                                  new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
			                                  10);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.INTERNET)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
			                                  new String[]{ Manifest.permission.INTERNET },
			                                  20);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		if (ContextCompat.checkSelfPermission(this.world.getApplication(), Manifest.permission.ACCESS_NETWORK_STATE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			ActivityCompat.requestPermissions(activity,
			                                  new String[]{ Manifest.permission.ACCESS_NETWORK_STATE },
			                                  30);
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
	}

	@And("create the application public directory")
	public void createTheApplicationPublicDirectory() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File(Environment.getExternalStorageDirectory()
				, this.world.getApplication().getResources().getString(R.string.appname));
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		Assert.assertTrue(sdCardDir.exists());
	}

	@And("create the application cache directory")
	public void createTheApplicationCacheDirectory() {
		File cachedir = new File(Environment.getExternalStorageDirectory()
				, this.world.getApplication().getResources().getString(R.string.appname)
						  + "/" + this.configurationProvider.getResourceString("P.cache.directory.path"));
		cachedir.mkdir();
		Assert.assertTrue(cachedir.exists());
	}

	@And("copy the SDE database asset to the application private directory")
	public void copyTheSDEDatabaseAssetToTheApplicationPrivateDirectory() {
		this.fileSystem.copyFromAssets(this.world.getApplication().getResources().getString(R.string.sdedatabasefilename), null);
	}

	/**
	 * Replace the NeoCom components by the mocked and support components that should e used for
	 * instrumentation testing.
	 */
	@And("the reconfigured neocom component factory")
	public void theReconfiguredNeocomComponentFactory() throws SQLException {
//		this.credentialRepository = new SupportCredentialRepository.Builder()
//				                            .withCredentialDao(NeoComComponentFactory.getSingleton()
//						                                               .getNeoComDBHelper()
//						                                               .getCredentialDao())
//				                            .build();
//		Assert.assertNotNull(this.credentialRepository);
//		Assert.assertNotNull(NeoComComponentFactory.getSingleton()
//				                     .getCredentialRepository()); // Access the component to activate dependencies
//		NeoComComponentFactory.getSingleton().setCredentialRepository(this.credentialRepository);
//
//		this.miningRepository = new SupportMiningRepository.Builder()
//				                        .withMiningExtractionDao(NeoComComponentFactory.getSingleton()
//						                                                 .getNeoComDBHelper()
//						                                                 .getMiningExtractionDao())
//										.withEsiDataAdapter(NeoComComponentFactory.getSingleton().getEsiDataAdapter())
//				                        .build();
//		Assert.assertNotNull(this.miningRepository);
//		Assert.assertNotNull(NeoComComponentFactory.getSingleton()
//				                     .getMiningRepository()); // Access the component to activate dependencies
//		NeoComComponentFactory.getSingleton().setMiningRepository(this.miningRepository);
	}

	@And("check that the SDE database file on the application private directory {string}")
	public void checkThatTheSDEDatabaseFileOnTheApplicationPrivateDirectory( final String existenceFlag ) {
		final File sdeDatabasePath = new File(Environment.getExternalStorageDirectory()
				, this.world.getApplication().getResources().getString(R.string.appname)
						  + "/" + this.world.getApplication().getResources().getString(R.string.sdedatabasefilename));
		Assert.assertTrue(sdeDatabasePath.exists());
	}

	/**
	 * Cleanup the repository from all real records before setting up the new test records.
	 */
	@And("a clean application repository")
	public void aCleanApplicationRepository() {
//		((SupportCredentialRepository) NeoComComponentFactory.getSingleton().getCredentialRepository()).deleteAll();
//		((SupportMiningRepository) NeoComComponentFactory.getSingleton().getMiningRepository()).deleteAll();
	}

	@And("the number of records in {string} is {int}")
	public void theNumberOfRecordsInIs( final String repositoryName, final int records ) {
//		switch (repositoryName) {
//			case "CredentialRepository":
//				Assert.assertEquals(records, this.credentialRepository.countRecords().intValue());
//				break;
//			case "MiningRepository":
//				Assert.assertEquals(records, this.miningRepository.countRecords().intValue());
//				break;
//		}
	}
}
