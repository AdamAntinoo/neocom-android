package org.dimensinfin.eveonline.neocom.app.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.domain.Spacer;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapters.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.adapters.IFileSystem;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationException;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.app.utilities.ChainTask;
import org.dimensinfin.eveonline.neocom.app.utilities.TaskSequencer;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;


import static org.dimensinfin.eveonline.neocom.BuildConfig.SDE_DB_VERSION;

public class AppInitializationActivity extends Activity {
	protected static Logger logger = LoggerFactory.getLogger(ChainTask.class);
	public static final String RETRY_INITIALIZATION_EVENT = "-RETRY_INITIALIZATION_EVENT-";
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;
	protected ActionBar actionBar;
	protected LinearContainer linearContainer;
//	private boolean requestPrivilege = true;
	private TaskSequencer taskSequencer;

	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.injectComponents(); // Connect to the application components.
		this.setContentView(R.layout.activity_initialization); // Set the layout to the core context
		this.deactivateActionBar();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.linearContainer = new LinearContainer.Builder()
				                       .withActivity(this)
				                       .withLayoutId(R.id.initializationLayout)
				                       .withFactory(new AppControllerFactory(PageNamesType.INITIALIZATION.name()))
				                       .build();
		this.linearContainer.clear();
		// Create the header component and insert it into the layout.
		final AppVersion appInfo = new AppVersion.Builder()
				                           .withAppName(this.getResources().getString(R.string.appname))
				                           .withAppVersion("v" + this.getResources().getString(R.string.appversion))
				                           .build();
		this.linearContainer.addModel(appInfo); // Set the application info header

		final Handler handler = new Handler();
		handler.post(() -> this.launchInitialization()); // Create the background initialization process.
		handler.post(() -> this.taskSequencer.run()); // Start the background initialization process.
	}

	@Override
	protected void onStop() {
		this.taskSequencer.stop(); // Report the task sequencer to stop processing more tasks.
		super.onStop();
	}

	protected void injectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.fileSystem = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	}

	protected void deactivateActionBar() {
		if (null != this.getActionBar()) {
			this.actionBar = this.getActionBar();
			this.actionBar.hide();
			this.actionBar.setDisplayHomeAsUpEnabled(true);

		}
	}

	/**
	 * Create a thread were to execute all the initialization steps. The should be executed in sequence and after each step
	 * there should be a new item to be rendered on the view panel.
	 */
	@WorkerThread
	public void launchInitialization() {
		this.taskSequencer = new TaskSequencer.Builder()
				                     .withActivity(this)
				                     .withContainer(this.linearContainer)
				                     .whenSuccessAction(() -> {
					                     // When the initialization completes we can move to the splash activity.
					                     logger.info("--[AppInitializationActivity.launchInitialization.whenSuccessAction]");
					                     this.runOnUiThread(() -> {
						                     final Intent intent = new Intent(this, AppSplashActivity.class);
						                     this.startActivity(intent);
					                     });
				                     })
				                     .whenExceptionAction(() -> {
					                     logger.info("--[AppInitializationActivity.launchInitialization.whenExceptionAction]");
					                     // If there is an exception then get the detail information and setup the exception fields.
					                     this.linearContainer.addModel(
							                     new Spacer.Builder().withType(SpacerType.LINE_WHITE).build());
					                     this.linearContainer.addModel(new InitializationException.Builder()
							                                                   .withException(this.taskSequencer.getException())
							                                                   .withRetryable(this.taskSequencer.isRetryable())
							                                                   .build());
				                     })
				                     .build();
		// Check that the application is available and the component factory is also present.
		final InitializationTask initialCheckTask = new InitializationTask.Builder()
				                                            .withMessage("Core instances check...")
				                                            .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                            .withRetryable(true)
				                                            .build();
		// Load the list of tasks.
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(initialCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           final NeoComComponentFactory componentFactory = NeoComComponentFactory.getSingleton();
					                           Objects.requireNonNull(componentFactory);
					                           final NeoComApplication application = NeoComComponentFactory.getSingleton().getApplication();
					                           Objects.requireNonNull(application);
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask privilegeExternalStorageCheckTask = new InitializationTask.Builder()
				                                                             .withMessage(
						                                                             "Application write external privilege check...")
				                                                             .withState(
						                                                             InitializationTask.InitializationTaskState.RUNNING)
				                                                             .withRetryable(true)
				                                                             .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(privilegeExternalStorageCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           if (ContextCompat.checkSelfPermission(this,
							                           Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						                           // Permission is not granted
						                           ActivityCompat.requestPermissions(this,
								                           new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 10);
					                           }
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask privilegeInternetCheckTask = new InitializationTask.Builder()
				                                                      .withMessage("Application internet privilege check...")
				                                                      .withState(
						                                                      InitializationTask.InitializationTaskState.RUNNING)
				                                                      .withRetryable(true)
				                                                      .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(privilegeInternetCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           if (ContextCompat.checkSelfPermission(this,
							                           Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
						                           ActivityCompat.requestPermissions(this,
								                           new String[]{ Manifest.permission.INTERNET }, 20);
					                           }
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask privilegeAccessNetworkCheckTask = new InitializationTask.Builder()
				                                                           .withMessage(
						                                                           "Application access network privilege check." +
								                                                           "..")
				                                                           .withState(
						                                                           InitializationTask.InitializationTaskState.RUNNING)
				                                                           .withRetryable(true)
				                                                           .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(privilegeAccessNetworkCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           if (ContextCompat.checkSelfPermission(this,
							                           Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
						                           ActivityCompat.requestPermissions(this,
								                           new String[]{ Manifest.permission.ACCESS_NETWORK_STATE }, 30);
					                           }
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask directoriesCheckTask = new InitializationTask.Builder()
				                                                .withMessage("Application directories check...")
				                                                .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                                .withRetryable(true)
				                                                .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(directoriesCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           this.createAppDir();
					                           this.createCacheDirectories();
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask sdeFileCheckTask = new InitializationTask.Builder()
				                                            .withMessage("SDE database existence check...")
				                                            .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                            .withRetryable(true)
				                                            .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(sdeFileCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           if (!this.sdeDatabaseExists()) {
						                           this.fileSystem.copyFromAssets(
								                           this.getResources().getString(R.string.sdedatabasefilename), null);
					                           }
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask validateSDEDatabaseVersionCheckTask = new InitializationTask.Builder()
				                                                               .withMessage("Validate SDE database version check...")
				                                                               .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                                               .withRetryable(true)
				                                                               .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(validateSDEDatabaseVersionCheckTask)
				                           .withRunTask(() -> {
//					                           try {
					                           final ISDEDatabaseAdapter sdeDatabase = NeoComComponentFactory.getSingleton().getSDEDatabaseAdapter();
					                           final int versionNumber = sdeDatabase.getDatabaseVersion();
					                           logger.info("--[AppInitializationActivity.validateSDEDatabaseVersionCheckTask]> Database " +
							                                       "version: {}", versionNumber);
					                           logger.info("--[AppInitializationActivity.validateSDEDatabaseVersionCheckTask]> Configuration " +
							                                       "version: {}", SDE_DB_VERSION);
					                           if (versionNumber != SDE_DB_VERSION) { // Currently installed version mismatch.
						                           new File(this.fileSystem.accessResource4Path(
								                           this.getResources().getString(R.string.sdedatabasefilename)))
								                           .delete();
						                           new File(this.fileSystem.accessResource4Path(
								                           this.getResources().getString(R.string.sdedatabasefilename) + "-journal"))
								                           .delete();
						                           if (!this.sdeDatabaseExists()) {
							                           this.fileSystem.copyFromAssets(
									                           this.getResources().getString(R.string.sdedatabasefilename), null);
						                           }
					                           }
//					                           } catch (final NullPointerException npe) {
//						                           return false;
//					                           }
					                           return true;
				                           })
				                           .build());
		final InitializationTask networkActiveCheckTask = new InitializationTask.Builder()
				                                                  .withMessage("Check the network is active...")
				                                                  .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                                  .withRetryable(true)
				                                                  .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(networkActiveCheckTask)
				                           .withRunTask(() -> {
					                           if (NeoComApplication.checkNetworkAccess())
						                           return true;
					                           else
						                           throw new NeoComRuntimeException(
								                           "The network is disabled so the application cannot work properly.");
				                           })
				                           .build());
		this.createInitializationTaskSlot("Connect to ESI server and download setup data...",
				() -> {
					NeoComComponentFactory.getSingleton().getEsiDataAdapter();
					return true;
				});
	}

	private void createInitializationTaskSlot( final String slotTitle, final Callable<Boolean> initialiationCode ) {
		final InitializationTask initializationTask = new InitializationTask.Builder()
				                                              .withMessage(slotTitle)
				                                              .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                              .withRetryable(true)
				                                              .build();
		this.taskSequencer.addTask(new ChainTask.Builder()
				                           .withTaskModel(initializationTask)
				                           .withRunTask(initialiationCode)
				                           .build());
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

	protected boolean sdeDatabaseExists() throws IOException {
		return new File(this.fileSystem.accessResource4Path(this.getResources().getString(R.string.sdedatabasefilename))).exists();
	}
}