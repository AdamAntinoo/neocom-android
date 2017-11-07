package org.dimensinfin.eveonline.neocom;

import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import org.dimensinfin.eveonline.neocom.constants.EManagerType;
import org.dimensinfin.eveonline.neocom.constants.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;

import java.util.Hashtable;
import java.util.logging.Logger;


public class AssetsActivity extends AppCompatActivity {
	//- CLASS IMPLEMENTATION ...................................................................................
	protected class CreatePartsTask extends android.os.AsyncTask<android.app.Activity, Void, Void> {
		// - S T A T I C - S E C T I O N ..........................................................................
		protected static java.util.logging.Logger logger					= java.util.logging.Logger.getLogger("CreatePartsTask");

		// - F I E L D - S E C T I O N ............................................................................
		private android.app.Activity _activity = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CreatePartsTask(final android.app.Activity targetactivity) {
			_activity = targetactivity;
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		/** The datasource is ready and the new hierarchy should be created from the current model. All the stages are executed at this time both the model contents update and the list of parts to be used on the ListView. First, the model is checked to be initialized and if not then it is created. Then the model is run from start to end to create all the visible elements and from this list then we create the full list of the parts with their right renders.<br> This is the task executed every time a datasource gets its model modified and hides all the update time from the main thread as it is recommended by Google. */
		@Override
		protected Void doInBackground(final android.app.Activity... arg0) {
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
		protected void onPostExecute(final Void result) {
			logger.info(">> CreatePartsTask.onPostExecute");
			// Activate the display of the list to force a redraw. Stop user UI waiting.
			_adapter = new DataSourceAdapter(_activity, _datasource);
			_modelContainer.setAdapter(_adapter);

//		_progressLayout.setVisibility(android.view.View.GONE);
			_modelContainer.setVisibility(android.view.View.VISIBLE);
			_container.invalidate();

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
	protected static Logger logger						= Logger.getLogger("AssetsActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private String															_title						= "<TITLE>";
	private String															_subtitle					= "";
	private IPartFactory _factory					= null;
	protected IDataSource _datasource				= null;
	protected DataSourceAdapter _adapter					= null;

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the layout the standard way and add the ToolBar.
		setContentView(R.layout.activity_assets);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Connect the button to the download of the sset list for the Character selected.
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		// Initialize the DataSourceAdapter and connect it to the Data source.
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
	public void createFactory() {
		logger.info(">> [AssetsActivity.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		logger.info("<< [AssetsActivity.createFactory]");
	}
	public void registerDataSource() {
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
		if (this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name()) {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_assets, menu);
		return true;
	}
	protected String getVariant() {
		return ENeoComVariants.ASSETS_BYLOCATION.name();
	}
	public void goFirstActivity(final RuntimeException rtex) {
		Toast.makeText(this.getActivity(), rtex.getMessage(), Toast.LENGTH_LONG).show();
	//	this.startActivity(new Intent(this.getActivity(), MVCAppConnector.getSingleton().getFirstActivity()));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
//		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
//		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 *
	 */
	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
//		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), loginid);
//		outState.putLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
	}
	//- CLASS IMPLEMENTATION ...................................................................................
	final class AssetsByLocationGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private String				_login				= null;
		private long					_characterid	= -1;
		private EManagerType _manager			= EManagerType.ASSETS_MANAGER;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetsByLocationGenerator(final DataSourceLocator locator, final String variant, final String login,
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
		public RootNode collaborate2Model() {
			AbstractGenerator.logger.info(">> [AssetsByLocationGenerator.collaborate2Model]");
			// Get the Assets Manager active at the character.
			NeoComCharacter pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
			AssetsManager assetsMan = pilot.getAssetsManager();
			// Check it is initialized.
			assetsMan.initialize();
			// Initialize the Adapter data structures.
			this.setDataModel(new RootNode());
			// Add the first level that are the Regions.
			Hashtable<Long, Region> regs = assetsMan.getRegions();
			for (Long key : regs.keySet()) {
				_dataModelRoot.addChild(regs.get(key));
				AbstractGenerator.logger.info("-- [AssetsByLocationGenerator.collaborate2Model]> Adding '"
						+ regs.get(key).getLocations() + "' to the _dataModelRoot");
			}
			AbstractGenerator.logger.info("<< [AssetsByLocationGenerator.collaborate2Model]");
			return _dataModelRoot;
		}

	}
}
