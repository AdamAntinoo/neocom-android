package org.dimensinfin.eveonline.neocom;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.dimensinfin.eveonline.mvclibrary.DataSourceLocator;
import org.dimensinfin.eveonline.mvclibrary.IPartFactory;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;


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

	// - F I E L D - S E C T I O N ............................................................................
	private String															_title						= "<TITLE>";
	private String															_subtitle					= "";
	private IPartFactory _factory					= null;
	protected IDataSource												_datasource				= null;
	protected DataSourceAdapter									_adapter					= null;

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
	//	String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = 92223647;
//		EManagerType manager = EManagerType.valueOf(this.getExtras().getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
//		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
//		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(loginid)
				.addIdentifier(characterid).addIdentifier(manager.name());
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
}
