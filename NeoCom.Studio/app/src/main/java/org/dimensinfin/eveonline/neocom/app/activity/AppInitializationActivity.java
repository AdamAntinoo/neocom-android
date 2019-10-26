package org.dimensinfin.eveonline.neocom.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.InitializationTask;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.app.ui.LinearContainer;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

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
				                           .withAppName(this.getResources().getString(R.string.app_name))
				                           .withAppVersion("v" + this.getResources().getString(R.string.appversion))
				                           .build();
//		final IRender appInfoRender = this.getControllerFactory().createController(appInfo)
//				                              .buildRender(this.getApplicationContext());
//		final View appInfoView = appInfoRender.getView();
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
		// Check that the application is available and the component factory is also present.
		final InitializationTask task = new InitializationTask.Builder()
				                                .withMessage("Core instances check.")
				                                .withState(InitializationTask.InitializationTaskState.RUNNING)
				                                .withRetryable(true)
				                                .build();
		this.runOnUiThread(() -> {
			this.linearContainer.addModel(task);
		});
		backgroundExecutor.submit(() -> {
			try {
				final NeoComComponentFactory componentFactory = NeoComComponentFactory.getSingleton();
				Objects.requireNonNull(componentFactory);
				final NeoComApplication application = NeoComComponentFactory.getSingleton().getApplication();
				Objects.requireNonNull(application);
				this.runOnUiThread(() -> {
					task.setState(InitializationTask.InitializationTaskState.COMPLETED);
					this.linearContainer.needsUpdate();
				});
				Thread.sleep(TimeUnit.SECONDS.toMillis(3));
			} catch (final NullPointerException npe) {
				this.runOnUiThread(() -> {
					task.setState(InitializationTask.InitializationTaskState.EXCEPTION);
					this.linearContainer.needsUpdate();
				});
			} catch (final InterruptedException ie) {}
		});
	}
}