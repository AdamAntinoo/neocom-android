package org.dimensinfin.eveonline.neocom.app.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dimensinfin.android.mvc.domain.Spacer;
import org.dimensinfin.android.mvc.domain.SpacerType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.app.utilities.InitializationException;
import org.dimensinfin.eveonline.neocom.app.utilities.TaskSequencer;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppInitializationActivity extends Activity {
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;
	protected ActionBar actionBar;
	protected LinearContainer linearContainer;
	private boolean requestPrivilege = true;
	private TaskSequencer taskSequencer;

	// - T E S T   A C C E S S O R S
	public LinearContainer getContainerView() {
		return this.linearContainer;
	}

	// - L I F E C Y C L E
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		this.injectComponents(); // Connect to the application components.
		this.setContentView(R.layout.activity_initialization); // Set the layout to the core context
		this.deactivateActionBar();
		this.linearContainer = new LinearContainer.Builder()
				                       .withActivity(this)
				                       .withLayoutId(R.id.initializationLayout)
				                       .withFactory(new AppControllerFactory(PageNamesType.INITIALIZATION.name()))
				                       .build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.linearContainer.clear();
		// Create the header component and insert it into the layout.
		final AppVersion appInfo = new AppVersion.Builder()
				                           .withAppName(this.getResources().getString(R.string.appname))
				                           .withAppVersion("v" + this.getResources().getString(R.string.appversion))
				                           .build();
		this.linearContainer.addModel(appInfo); // Set the application info header
		this.launchInitialization(); // Start the background initialization process.
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
	protected void launchInitialization() {
		this.taskSequencer= new TaskSequencer.Builder()
		.withActivity(this)
		.whenSuccessAction(()->{
			// When the initialization completes we can move to the splash activity.
				this.runOnUiThread(() -> {
					final Intent intent = new Intent(this, AppSplashActivity.class);
					this.startActivity(intent);
				});

		})
		.whenExceptionAction(()->{
			// If there is an exception then get the detail information and setup the exception fields.
			this.linearContainer.addModel(new Spacer.Builder().withType(SpacerType.LINE_WHITE).build());
			this.linearContainer.addModel(new InitializationException.Builder()
					                              .withException(this.taskSequencer.getException())
			.build());
		})
		.build();
		// Check that the application is available and the component factory is also present.
		final InitializationTask initialCheckTask = new InitializationTask.Builder()
				                                            .withMessage("Core instances check...")
				                                            .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                            .withRetryable(true)
				                                            .build();
		this.runOnUiThread(() -> {
			this.linearContainer.addModel(initialCheckTask);
		});
		backgroundExecutor.submit(() -> {
			try {
				final NeoComComponentFactory componentFactory = NeoComComponentFactory.getSingleton();
				Objects.requireNonNull(componentFactory);
				final NeoComApplication application = NeoComComponentFactory.getSingleton().getApplication();
				Objects.requireNonNull(application);
				Thread.sleep(TimeUnit.SECONDS.toMillis(3));
				this.runOnUiThread(() -> {
					initialCheckTask.setState(InitializationTask.InitializationTaskState.COMPLETED);
					this.linearContainer.needsUpdate();
				});
			} catch (final NullPointerException npe) {
				this.runOnUiThread(() -> {
					initialCheckTask.setState(InitializationTask.InitializationTaskState.EXCEPTION);
					this.linearContainer.needsUpdate();
				});
			} catch (final InterruptedException ie) {}
		});
		// Check the privileges required to run the application.
		final InitializationTask privilegesCheckTask = new InitializationTask.Builder()
				                                               .withMessage("Application privileges check...")
				                                               .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                               .withRetryable(true)
				                                               .build();
		this.runOnUiThread(() -> {
			this.linearContainer.addModel(privilegesCheckTask);
		});
		backgroundExecutor.submit(() -> {
			try {
				while (this.validatePermissions()) {
					this.requestPrivilege=false;
				}
				Thread.sleep(TimeUnit.SECONDS.toMillis(3));

				this.runOnUiThread(() -> {
					privilegesCheckTask.setState(InitializationTask.InitializationTaskState.COMPLETED);
					this.linearContainer.needsUpdate();
				});
			} catch (final InterruptedException ie) {}
		});
		// Check that the application directories are present.
		final InitializationTask directoriesChecktask = new InitializationTask.Builder()
				                                                .withMessage("Application directories check...")
				                                                .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                                .withRetryable(true)
				                                                .build();
		this.runOnUiThread(() -> {
			this.linearContainer.addModel(directoriesChecktask);
		});
		backgroundExecutor.submit(() -> {
			try {
				this.createAppDir();
				this.createCacheDirectories();
				Thread.sleep(TimeUnit.SECONDS.toMillis(3));

				this.runOnUiThread(() -> {
					directoriesChecktask.setState(InitializationTask.InitializationTaskState.COMPLETED);
					this.linearContainer.needsUpdate();
				});
			} catch (final InterruptedException ie) {}
		});
		// Check the SDE database file is present.
		final InitializationTask sdeFileCheckTask = new InitializationTask.Builder()
				                                            .withMessage("SDE database existence check...")
				                                            .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                            .withRetryable(true)
				                                            .build();
		this.runOnUiThread(() -> {
			this.linearContainer.addModel(sdeFileCheckTask);
		});
		backgroundExecutor.submit(() -> {
			try {
				if (this.sdeDatabaseExists()) {
					Thread.sleep(TimeUnit.SECONDS.toMillis(3));

					this.runOnUiThread(() -> {
						sdeFileCheckTask.setState(InitializationTask.InitializationTaskState.COMPLETED);
						this.linearContainer.needsUpdate();
					});
				} else {
					this.fileSystem.copyFromAssets(this.getResources().getString(R.string.sdedatabasefilename), null);
					this.runOnUiThread(() -> {
						sdeFileCheckTask.setState(InitializationTask.InitializationTaskState.COMPLETED);
						this.linearContainer.needsUpdate();
					});
				}
			} catch (final InterruptedException ie) {}
		});
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

	protected boolean sdeDatabaseExists() {
		return new File(this.fileSystem.accessResource4Path(this.getResources().getString(R.string.sdedatabasefilename)))
				       .exists();
	}

	protected boolean validatePermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			this.requestPrivilege = true;
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
					10);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			this.requestPrivilege = true;
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.INTERNET },
					20);
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
				    != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			this.requestPrivilege = true;
			ActivityCompat.requestPermissions(this,
					new String[]{ Manifest.permission.ACCESS_NETWORK_STATE },
					30);
		}
		return this.requestPrivilege;
	}

}