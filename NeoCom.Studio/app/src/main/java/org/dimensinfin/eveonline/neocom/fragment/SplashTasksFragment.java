//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.LoginListActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.InitializationTask;
import org.dimensinfin.eveonline.neocom.model.RunnableTask;
import org.dimensinfin.eveonline.neocom.model.RunnableTask.ERunState;
import org.dimensinfin.eveonline.neocom.parts.AppVersionPart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * This Fragment is created with the new MVC structure. As previous fragments will initialize the key data structures
 * required by the core code as the Title, Subtitle, Header contents and List contents providers. But wil many of the
 * new simplifications like the removal of the need to define any DataSource or manage any Part directly.
 */
public class SplashTasksFragment extends AbstractPagerFragment {
	/**
	 * Class to perform the update of the user information as a background task. If the data is not present or
	 * if the information is obsolete we launch a task to update and download the pilot information.
	 *
	 * @author Adam Antinoo
	 */
	//- CLASS IMPLEMENTATION ...................................................................................
	private class NeoComInitialization extends AsyncTask<Void, Void, Boolean> {
		// - F I E L D - S E C T I O N ............................................................................
		protected Activity _activity = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public NeoComInitialization (final Activity act) {
			_activity = act;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This is the main method where we do the application data initialization. The process has some steps
		 * that are listed below:
		 * <ol>
		 * <li>STEP 01. Check required files availability on app directory.</li>
		 * </ol>
		 */
		@Override
		protected Boolean doInBackground (final Void... entry) {
			logger.info(">> [NeoComInitialization.doInBackground]");

			// Just check that the files exist and if not load the Api list and their characters.
			logger.info(">> [NeoComInitialization.doStep01]");
			this.updateStateLabel("Checking application databases...");
			// STEP 01. Check required files availability on app directory.
			this.createAppDir();
			this.createCacheDirectories();
			logger.info("-- [NeoComInitialization.doStep01]> STEP 01. App Directories created");
			// STEP 02. Check existence of required files.
			if ( !this.checkAppFile(NeoComAppConnector.getSingleton().getResourceString(R.string.ccpdatabasefilename)) ) {
				// TODO The CCP database is not distributed with the application. If not available then close the App
				this.copyFromAssets(R.string.ccpdatabasefilename);
			}
			logger.info("-- [NeoComInitialization.doStep01]> STEP 02. Required files on place");

			// Create the login information that later will be requested to the user at the login page.
			this.updateStateLabel("Processing Character Information...");
			NeoComAppConnector.getSingleton().getModelStore().accessLoginList();
			logger.info("<< [NeoComInitialization.doStep01]");

			// Start the timers for background processing.
			// Do the start controlled by a settings toggle.
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_activity);
			boolean blockBackgroundTimer = sharedPrefs.getBoolean(AppWideConstants.preferenceKeys.prefkey_BlockBackgorundTimer.name(), false);
			if ( !blockBackgroundTimer ) NeoComApp.getSingletonApp().startTimer();
			logger.info("<< [NeoComInitialization.doInBackground]");
			return true;
		}

		@Override
		protected void onPostExecute (final Boolean result) {
			// Check if we have pilots to show or we have to go to the AddAPI page.
			if ( result ) {
				startActivity(new Intent(_activity, LoginListActivity.class));
			}
		}

		private boolean checkAppFile (final String resourceString) {
			return NeoComAppConnector.getSingleton().getStorageConnector().checkStorageResource(
					NeoComAppConnector.getSingleton().getStorageConnector().accessAppStorage(null), resourceString);
		}

		/**
		 * Copies the parameter files from the assets installation directory to the user application directory.
		 * Current contents are the static database and the initial API list if found.
		 */
		private void copyFromAssets (final int resourceId) {
			InputStream istream = null;
			OutputStream ostream = null;
			String resourceName = NeoComAppConnector.getSingleton().getResourceString(resourceId);
			//		final Context context = singleton.getApplicationContext();
			try {
				istream = _activity.getAssets().open(resourceName);
				final File destination = NeoComAppConnector.getSingleton().getStorageConnector().accessAppStorage(resourceName);
				destination.createNewFile();
				ostream = new FileOutputStream(destination);
				final byte[] buffer = new byte[8192];
				int length;
				while ((length = istream.read(buffer)) > 0) {
					ostream.write(buffer, 0, length);
				}
				ostream.flush();
				logger.info("-- Copied resource from assets [" + resourceName + "]");
			} catch (final Exception e) {
				e.printStackTrace();
				logger.severe("E> Failed to copy resource: " + resourceName);
			} finally {
				try {
					if ( ostream != null ) {
						ostream.close();
					}
					if ( istream != null ) {
						istream.close();
					}
				} catch (final IOException e) {
				}
			}
		}

		/**
		 * Creates the application directory if it already does not exist on the sdcard.
		 */
		private boolean createAppDir () {
			// Create file application directory if it doesn't exist
			final File sdcarddir = new File(Environment.getExternalStorageDirectory(),
					NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
							/*+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix)*/);
			final boolean existence = sdcarddir.exists();
			if ( !sdcarddir.exists() ) {
				sdcarddir.mkdir();
			}
			return !existence;
		}

		private void createCacheDirectories () {
			// Create additional cache folders.
			File cachedir = NeoComAppConnector.getSingleton().getStorageConnector().getCacheStorage();
			cachedir.mkdir();
			File alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.drawablecachefoldername));
			alternatedir.mkdir();
			alternatedir = new File(cachedir,
					NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
			alternatedir.mkdir();
		}

		private void updateStateLabel (final String newLabel) {
			//			SplashActivity.this.runOnUiThread(new Runnable() {
			//				public void run() {
			//					stateLabel.setText(newLabel);
			//					stateLabel.invalidate();
			//				}
			//			});
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Because the initialization of the model has to start after the elements are connected and in place I should move
	 * the intialization code
	 * from the Activity to the Fragment. After initializing all the adapters and models we can launch the initiallization
	 * check tasks.
	 */
	public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		AbstractPagerFragment.logger.info(">> [SplashTasksFragment.onCreateView]");
		final View theView = super.onCreateView(inflater, container, savedInstanceState);
		new NeoComInitialization(getActivity()).execute();
		AbstractPagerFragment.logger.info("<< [SplashTasksFragment.onCreateView]");
		return theView;
	}

	@Override
	public void createFactory () {
		logger.info(">> [SplashTasksFragment.createFactory]");
		this.setFactory(new SplashPartFactory(this.getVariant()));
		logger.info("<< [SplashTasksFragment.createFactory]");
	}

	@Override
	protected void registerDataSource () {
		logger.info(">> [SplashTasksFragment.registerDataSource]");
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant());

		// Create a new Model Generator and register it onto the Fragment to use on request to generate the root model.
		setGenerator(new InitializationGenerator(locator, this.getVariant()));
		logger.info("<< [SplashTasksFragment.registerDataSource]");
	}

	/**
	 * Set the model or model generator that defines the content of the Header section (non scroll list of View) of the
	 * current Page. The current version inserts directly the Parts so the Fragment will have the responsibility to
	 * generate all the content parts from the supposed model. This sample code will have to be added to the Superclass to
	 * remove all that knowledge from the development fragment.
	 */
	@Override
	protected void setHeaderContents () {
		//		return;
		// Get the model from a generator or from a List.
		try {
			ArrayList<ICollaboration> headerModelContents = new ArrayList<ICollaboration>();
			headerModelContents.add(new AppVersion());
			RootNode headerModel = new RootNode();
			headerModel.addChildren(headerModelContents);

			// Do the same operations as in the body contents. Create a root, add to it the model elements and then
			// recursively generate the Part list.
			final RootPart partModelRoot = new RootPart(headerModel, getFactory());
			partModelRoot.refreshChildren();
			final ArrayList<IPart> bodyParts = new ArrayList<IPart>();
			// Select for the body contents only the viewable Parts from the Part model. Make it a list.
			bodyParts.addAll(partModelRoot.collaborate2View());

			// Now do the old functionality by copying each of the resulting parts to the Header container.
			for (IPart part : bodyParts) {
				addtoHeader((IAndroidPart) part);
			}
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class SplashPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SplashPartFactory (final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart (final ICollaboration node) {
		logger.info(">< [SplashPartFactory.createPart]> Node class: " + node.getClass().getSimpleName());
		if ( node instanceof AppVersion ) {
			IPart part = new AppVersionPart((AppVersion) node).setFactory(this)
			                                                  .setRenderMode(this.getVariant());
			return part;
		}
		//		if (node instanceof InitializationTask ) {
		//			IPart part = new RegionPart((Region) node).setFactory(this)
		//			                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
		//			return part;
		//		}

		// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class InitializationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................
	private Handler _handler = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public InitializationGenerator (final DataSourceLocator locator, final String variant) {
		super(locator, variant);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This is the real initialization code manager. It will be generating the initialization steps and then the tasks
	 * inside them once one by one
	 * are being executed. This code is most a test of how to use Observers while managing the model objects.
	 */
	@Override
	public RootNode collaborate2Model () {
		logger.info(">> [InitializationGenerator.collaborate2Model]");
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());

		// Start the loop that processes the tasks.
		// Create a new background thread for processing messages or runnables sequentially
		HandlerThread handlerThread = new HandlerThread("NeoComInitializationThread");
		// Starts the background thread
		handlerThread.start();
		// Create a handler attached to the HandlerThread's Looper
		_handler = new Handler(handlerThread.getLooper());

		// Create the initial task, connect it to the adapter and schedule for running.
		final RunnableTask t1 = new RunnableTask("Checking App Directories.");
		t1.setRunnableBlock(
				new Runnable() {
					@Override
					public void run () {
						// Stage A. Initialize the UI with the task data.
						t1.setState(ERunState.RUNNING);
						//						NeoComModelStore.getSingleton().getActivity().runOnUiThread(
						//								new Runnable() {
						//									@Override
						//									public void run () {
						//										stateLabel.invalidate();
						//									}
						//								}
						//						);
						// Stage B. Do the task.
						this.createAppDir();
						this.createCacheDirectories();
						// Stage C. Check termination and select continuation task depending on result.
						//						nextStepCheckCPPDB();
						// Stage D. Complete the UI state.
						t1.complete(ERunState.COMPLETED_OK);
					}

					/**
					 * Creates the application directory if it already does not exist on the sdcard.
					 */
					private boolean createAppDir () {
						// Create file application directory if it doesn't exist
						final File sdcarddir = new File(Environment.getExternalStorageDirectory(),
								NeoComAppConnector.getSingleton().getResourceString(R.string.appfoldername)
							/*+ NeoComAppConnector.getSingleton().getResourceString(R.string.app_versionsuffix)*/);
						final boolean existence = sdcarddir.exists();
						if ( !sdcarddir.exists() ) {
							sdcarddir.mkdir();
						}
						return !existence;
					}

					private void createCacheDirectories () {
						// Create additional cache folders.
						File cachedir = NeoComAppConnector.getSingleton().getStorageConnector().getCacheStorage();
						cachedir.mkdir();
						File alternatedir = new File(cachedir,
								NeoComAppConnector.getSingleton().getResourceString(R.string.drawablecachefoldername));
						alternatedir.mkdir();
						alternatedir = new File(cachedir,
								NeoComAppConnector.getSingleton().getResourceString(R.string.marketdatacachefoldername));
						alternatedir.mkdir();
					}
				}
		);
		_dataModelRoot.addChild(t1);
		final RunnableTask t2 = new RunnableTask("Checking CCP Database.");
		t2.setRunnableBlock(
				new Runnable() {
					public void run () {
						t2.complete(ERunState.COMPLETED_OK);
					}
				});
		_dataModelRoot.addChild(t2);

		// Execute the specified code on the worker thread
		//		_handler.post();


		// Add the steps to the list of works to do.
		_dataModelRoot.addChild(new InitializationTask());

		logger.info("<< [InitializationGenerator.collaborate2Model]> nodes: " + _dataModelRoot.getChildren().size());
		return _dataModelRoot;
	}
}
// - UNUSED CODE ............................................................................................
