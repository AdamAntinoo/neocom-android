package org.dimensinfin.eveonline.neocom;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.dimensinfin.android.datasource.AbstractGenerator;
import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.datasource.ModelGeneratorStore;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.datasource.DataSourceAdapter;
import org.dimensinfin.android.mvc.datasource.DataSourceManager;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.constants.EManagerType;
import org.dimensinfin.eveonline.neocom.constants.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.datasource.NeoComDataSource;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.models.NeoComAsset;

import java.io.IOException;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AssetsActivity extends NeoComBaseActivity {
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			// Get the result of the operation from the AccountManagerFuture.
			Bundle bundle = null;
			try {
				bundle = result.getResult();
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				e.printStackTrace();
			}

			// The token is a named value in the bundle. The name of the value
			// is stored in the constant AccountManager.KEY_AUTHTOKEN.
			final String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
		}
	}
	//- CLASS IMPLEMENTATION ...................................................................................
	final class CreatePartsTask extends android.os.AsyncTask<android.app.Activity, Void, Void> {
		// - S T A T I C - S E C T I O N ..........................................................................
		//		protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger ("CreatePartsTask");

		// - F I E L D - S E C T I O N ............................................................................
		private android.app.Activity _activity = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CreatePartsTask (final android.app.Activity targetactivity) {

			_activity = targetactivity;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * The datasource is ready and the new hierarchy should be created from the current model. All the stages are
		 * executed at this time both the model contents update and the list of parts to be used on the ListView. First, the
		 * model is checked to be initialized and if not then it is created. Then the model is run from start to end to
		 * create all the visible elements and from this list then we create the full list of the parts with their right
		 * renders.<br> This is the task executed every time a datasource gets its model modified and hides all the update
		 * time from the main thread as it is recommended by Google.
		 */
		@Override
		protected Void doInBackground (final android.app.Activity... arg0) {

			logger.info(">> [CreatePartsTask.doInBackground]");
			try {
				// Create the hierarchy structure to be used on the Adapter.
				_datasource.collaborate2Model();
				_datasource.createContentHierarchy();
			} catch (final RuntimeException rtex) {
				rtex.printStackTrace();
			}
			logger.info("<< [CreatePartsTask.doInBackground]");
			return null;
		}

		@Override
		protected void onPostExecute (final Void result) {

			logger.info(">> CreatePartsTask.onPostExecute");
			// Activate the display of the list to force a redraw. Stop user UI waiting.
			_adapter = new DataSourceAdapter(_activity, _datasource);
			_modelContainer.setAdapter(_adapter);

			_progressLayout.setVisibility(android.view.View.GONE);
			_modelContainer.setVisibility(android.view.View.VISIBLE);
			//		_container.invalidate();

			//		// Add the header parts once the display is initialized.
			//		ArrayList<AbstractAndroidPart> headerContents = _datasource.getHeaderParts();
			//		if (headerContents.size() > 0) {
			//			_headerContainer.removeAllViews();
			//			_headerContainer.invalidate();
			//			for (final AbstractAndroidPart part : headerContents) {
			//				_activity.addViewtoHeader(part);
			//			}
			//		}
			super.onPostExecute(result);
			logger.info("<< CreatePartsTask.onPostExecute");
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	protected static Logger logger = Logger.getLogger("AssetsActivity");

	// - F I E L D - S E C T I O N ............................................................................
//	private OAuth oauth;
	private String _title = "<TITLE>";
	private String _subtitle = "";
	private IPartFactory _factory = null;
	protected IDataSource _datasource = null;
	protected DataSourceAdapter _adapter = null;

	// - U I    F I E L D S
	protected ViewGroup _container = null;
	/** The view that handles the non scrolling header. */
	protected ViewGroup _headerContainer = null;
	/** The view that represent the list view and the space managed though the adapter. */
	protected ListView _modelContainer = null;
	protected ViewGroup _progressLayout = null;
	//protected IMenuActionTarget _listCallback			= null;
	//private Bundle															_extras						= new Bundle();

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the layout the standard way and add the ToolBar.
		setContentView(R.layout.content_basic_asset_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.neocomToolbar);
		setSupportActionBar(toolbar);

		ButterKnife.bind(this);
	//	this.oauth = new OAuth(this, "EveSSO Sample");

		// Initialize the DataSourceAdapter and connect it to the Data source.
		this.onCreateViewSuper(R.layout.activity_assets, getLayoutInflater(), null,
				savedInstanceState);
		try {
			this.createFactory();
			this.registerDataSource();
			//		this.setHeaderContents();
		} catch (final RuntimeException rtex) {
			logger.severe("RTEX [AssetsActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			// Instead blocking the application drop a toast and move to the First Activity.
			this.goFirstActivity(rtex);
		}

	}

	protected void onNewIntent (final Intent intent) {
		super.onNewIntent(intent);
		if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) {
//			startESIAuthentication();
			Uri uri = intent.getData();
			String code = uri.getQueryParameter("code");
			String checkingState = uri.getQueryParameter("state");
			auth.finishFlow(code, state);
			System.out.println("Refresh Token: " + auth.getRefreshToken());

			//	String valueTwo = uri.getQueryParameter("keyTwo");
//			Observable<OAuthVerified> observable =
//					Observable.defer(() -> {
//						if ( oauth.authorize(intent.getData()) ) {
//							return Observable.just(oauth.verify());
//						}
//						return null;
//					}).
//							          subscribeOn(Schedulers.io()).
//							          observeOn(AndroidSchedulers.mainThread());
//			observable.subscribe(v -> setVerified(v));
		}
	}

	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_launchUpdate:
				onStartClicked();
				return false;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onStartClicked () {
		// Initialize the OAuth flow with the new eve-esi package data.
		final String uri =	startESIAuthentication();
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);

//		this.oauth.clear();
//		final String uri = this.oauth.start();
//		if ( null == uri ) {
//			return;
//		}
//		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//		startActivity(intent);
//		androidOauth();
	}
	private void androidOauth(){

		// Get the other authentication item with Android code.
		AccountManager am = AccountManager.get(this);
		Bundle options = new Bundle();

//			am.getAuthToken(
//				myAccount_,                     // Account retrieved using getAccountsByType()
//				"Manage your tasks",            // Auth scope
//				options,                        // Authenticator-specific options
//				this,                           // Your activity
//				new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//				new Handler(new OnError()));    // Callback called if an error occurs

	}

	/**
	 * This is the code called when the assets request completed. Porcess the first 20 assets into the DataSource.
	 *
	 * @param v
	 */
	private void setVerified (final OAuthVerified v) {
		if ( null == v ) {
			return;
		}

		//		Glide.with(this).
		//				load("https://image.eveonline.com/Character/" + v.getCharacterID() + "_128.jpg").
		//				     into(characterImageView);
		//		this.characterTextView.setText(v.getCharacterName() + " (" + v.getCharacterID() + ")");
	}

	public void createFactory () {
		logger.info(">> [AssetsActivity.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		logger.info("<< [AssetsActivity.createFactory]");
	}

	/**
	 * Creates the structures when the fragment is about to be shown. We have to check that the parent Activity
	 * is compatible with this kind of fragment. So the fragment has to check of it has access to a valid pilot
	 * before returning any UI element.
	 */
	public View onCreateViewSuper (final int layout, final LayoutInflater inflater, final ViewGroup container,
	                               final Bundle savedInstanceState) {
		logger.info(">> [AbstractPagerFragment.onCreateViewSuper]");
		//	super.onCreateView(inflater, container, savedInstanceState);
		try {
			//		_container = (ViewGroup) inflater.inflate(layout, container, false);
			_headerContainer = (ViewGroup) findViewById(R.id.headerContainer);
			_modelContainer = (ListView) findViewById(R.id.listContainer);
			_progressLayout = (ViewGroup) findViewById(R.id.progressLayout);
			// Prepare the structures for the context menu.
			//		this.registerForContextMenu(_headerContainer);
			this.registerForContextMenu(_modelContainer);
		} catch (final RuntimeException rtex) {
			logger.severe("RTEX [AbstractPagerFragment.onCreateViewSuper]> " + rtex.getMessage());
			rtex.printStackTrace();
			// Instead blocking the application drop a toast and move to the First Activity.
			this.goFirstActivity(rtex);
		}
		logger.info("<< [AbstractPagerFragment.onCreateViewSuper]");
		return _container;
	}

	/**
	 * When the execution reaches this point to activate the fragment we have to check that all the elements
	 * required are defined, mainly the Data Source. If the DS is ready and valid then we launch the DS data
	 * loading code. If that was performed on a previous start and the DS is already loaded we can skip this
	 * step. This last approach will reduce CPU usage and give a better user feeling when activating and
	 * deactivating activities and fragments.
	 */
	@Override
	public void onStart () {
		logger.info(">> [AbstractPagerFragment.onStart]");
		super.onStart();
		try {
			// Create the adapter to the view and connect it to the DS
			if ( null == this.getDataSource() )
				throw new RuntimeException("Datasource not initialized. Fragment: " + this.getTitle());
			// REFACTOR This methods should change the name to a more suitable because the models are initialized
			logger.info("-- [AbstractPagerFragment.onStart]> - Launching CreatePartsTask");
			new CreatePartsTask(this).execute();
			// Update the spinner counter on the actionbar.
			this.getActivity().invalidateOptionsMenu();
			// Add the header parts once the display is initialized.
			//			if (_headerContents.size() > 0) {
			//				_headerContainer.removeAllViews();
			//				for (final AbstractAndroidPart part : _headerContents) {
			//					this.addViewtoHeader(part);
			//				}
			//			}
		} catch (final RuntimeException rtex) {
			logger.severe("RTEX [AbstractPagerFragment.onStart]> " + rtex.getMessage());
			rtex.printStackTrace();
			// Instead blocking the application drop a toast and move to the First Activity.
			this.goFirstActivity(rtex);
		}
		logger.info("<< [AbstractPagerFragment.onStart]");
	}

	public IDataSource getDataSource () {
		return _datasource;
	}

	public void registerDataSource () {

		logger.info(">> [AssetsActivity.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = "Adam Antinoo";
		long characterid = 92223647;
		EManagerType manager = EManagerType.ASSETS_MANAGER;
		//		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		//		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(characterid);
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			generator = ModelGeneratorStore
					.registerGenerator(new AssetsByLocationGenerator(locator, this.getVariant(), loginid, characterid, manager));
		}
		// Register the datasource. If this same datasource is already at the manager we get it instead creating a new one.
		SpecialDataSource ds = new NeoComDataSource(locator, this.getFactory(), generator);
		ds.setVariant(this.getVariant());
		//	ds.addParameter(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
		this.setDataSource(DataSourceManager.registerDataSource(ds));
		//		if (this.getVariant() == EAssetVariants.ASSETS_MATERIALS.name()) {
		//			// Register the datasource. If this same datasource is already at the manager we get it instead creating a new one.
		//			SpecialDataSource ds = new AssetsMaterialsDataSource(locator, getFactory());
		//			ds.setVariant(this.getVariant());
		//			ds.addParameter(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
		//			this.setDataSource(AppModelStore.getSingleton().getDataSourceConector().registerDataSource(ds));
		//		}
		logger.info("<< [AssetsActivity.registerDataSource]");
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_assets, menu);
		return true;
	}

	public void setDataSource (final IDataSource dataSource) {
		if ( null != dataSource ) {
			_datasource = dataSource;
		}
	}

	public IPartFactory getFactory () {
		// Check if we have already a factory.
		if ( null == _factory ) {
			this.createFactory();
		}
		return _factory;
	}

	public void setFactory (final IPartFactory factory) {
		_factory = factory;
	}

	protected String getVariant () {

		return ENeoComVariants.ASSETS_BYLOCATION.name();
	}

	public void goFirstActivity (final RuntimeException rtex) {

		Toast.makeText(this.getActivity(), rtex.getMessage(), Toast.LENGTH_LONG).show();
		//	this.startActivity(new Intent(this.getActivity(), MVCAppConnector.getSingleton().getFirstActivity()));
	}

	private Activity getActivity () {
		return this;
	}

	@Override
	protected void onRestoreInstanceState (final Bundle savedInstanceState) {
		//		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		//		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState (final Bundle outState) {

		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		//		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), loginid);
		//		outState.putLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	final class AssetsByLocationGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private String _login = null;
		private long _characterid = -1;
		private EManagerType _manager = EManagerType.ASSETS_MANAGER;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetsByLocationGenerator (final DataSourceLocator locator, final String variant, final String login,
		                                  final long characterid, final EManagerType manager) {

			super(locator, variant);
			_login = login;
			_characterid = characterid;
			_manager = manager;
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This adapter should generate the model for all the assets aggregated by Location. The Locations are lazy
		 * evaluated so no need to get all the assets by the list of locations and then construct the first two
		 * levels, Regions then Locations. Once the Location is expanded then the model will extend itself to the
		 * new list of assets.<br>
		 * REFACTOR: For testing purposes we are going to download all the assets.
		 */
		public RootNode collaborate2Model () {

			AbstractGenerator.logger.info(">> [AssetsByLocationGenerator.collaborate2Model]");
			// Get the Assets Manager active at the character.
			//NeoComCharacter pilot = NeoComAppConnector.getSingleton ().getModelStore ().activatePilot (_characterid);
			//AssetsManager assetsMan = pilot.getAssetsManager ();
			// Check it is initialized.
			//	assetsMan.initialize ();
			// Initialize the Adapter data structures.
			this.setDataModel(new RootNode());
			// Add the first level that are the Regions.
			//	Hashtable<Long, Region> regs = assetsMan.getRegions ();
			//			for (Long key : regs.keySet ()) {
			//				_dataModelRoot.addChild (regs.get (key));
			//				AbstractGenerator.logger.info ("-- [AssetsByLocationGenerator.collaborate2Model]> Adding '"
			//						+ regs.get (key).getLocations () + "' to the _dataModelRoot");
			//			}

			// Add seed data to show in interface.
			NeoComAsset asset = new NeoComAsset();
			asset.setName("asset 1");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			asset = new NeoComAsset();
			asset.setName("asset 2");
			_dataModelRoot.addChild(asset);
			AbstractGenerator.logger.info("<< [AssetsByLocationGenerator.collaborate2Model]");
			return _dataModelRoot;
		}

	}
}
