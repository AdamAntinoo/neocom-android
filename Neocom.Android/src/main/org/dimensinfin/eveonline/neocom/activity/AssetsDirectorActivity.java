//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.datasource.ModelGeneratorStore;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractDataSource;
import org.dimensinfin.android.mvc.core.AbstractGenerator;
import org.dimensinfin.android.mvc.core.AbstractPagerActivity;
import org.dimensinfin.android.mvc.core.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.DataSourceManager;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.activity.AssetsDirectorActivity.EAssetVariants;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.enums.EManagerType;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

import android.os.Bundle;

//- CLASS IMPLEMENTATION ...................................................................................
public class AssetsDirectorActivity extends AbstractPagerActivity {
	public enum EAssetVariants {
		ASSETS_BYLOCATION, ASSETS_BYCATEGORY, ASSETS_MATERIALS
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger	logger			= Logger.getLogger("AssetsDirectorActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private String				loginid			= null;
	private long					characterid	= -1;
	private EManagerType	manager			= EManagerType.ASSETS_MANAGER;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	//	/**
	//	 * Checks if there are the conditions to activate this particular manager. Each one will have it different
	//	 * rules to reach the activation point.<br>
	//	 * The BPOManager need that there are at least one BPO on the list of assets of the pilot.
	//	 */
	//	public boolean checkActivation(final NeoComCharacter checkPilot) {
	//		if (checkPilot.getAssetCount() > 0)
	//			return true;
	//		else
	//			return false;
	//	}
	//
	//	public int getIconReferenceActive() {
	//		return R.drawable.assets;
	//	}
	//
	//	public int getIconReferenceInactive() {
	//		return R.drawable.assetsdimmed;
	//	}

	public String getName() {
		return "Assets Manager";
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		AssetsDirectorActivity.logger.info(">> [AssetsDirectorActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Register this Activity as the current active Activity.
			AppModelStore.getSingleton().activateActivity(this);
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			Bundle extras = null;
			if (null != savedInstanceState) {
				extras = savedInstanceState;
				loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
				manager = EManagerType.valueOf(savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
			} else {
				extras = this.getIntent().getExtras();
				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
				String managerName = extras.getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name());
				manager = EManagerType.valueOf(managerName);
			}
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new AssetsFragment().setVariant(EAssetVariants.ASSETS_BYLOCATION.name()).setExtras(extras), page++);
			//			this.addPage(new AssetsFragment().setVariant(EAssetVariants.ASSETS_MATERIALS.name()).setExtras(extras), page++);
		} catch (Exception rtex) {
			AssetsDirectorActivity.logger.warning("RTEX> AssetsDirectorActivity.onCreate - " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX> AssetsDirectorActivity.onCreate - " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AssetsDirectorActivity.logger.info("<< [AssetsDirectorActivity.onCreate]"); //$NON-NLS-1$
	}

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		manager = EManagerType.valueOf(savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * recondtruct the AppModel state.
	 * 
	 */
	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), loginid);
		outState.putLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), manager.name());
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
/**
 * This DataSource will manage the elements to shown on the assets list for a character in the page that lists
 * those assets by Region - Location - Container order. If the number of locations is greater than a
 * predefined setting (that will be modifiable on the UI on the settings page) then the locations will be
 * grouped into Regions. If the number is lower the locations will be the first display level. <br>
 * The second and next levels will be composed with Containers, Ships and Assets. Those levels will only be
 * read from the database only if the container or location is expanded so this will use a lazy evaluation
 * pattern do read and generate the momory structures that contain the assets.<br>
 * To manage this asset information the DataSource will heavily interface with the model AssetsManager that
 * will have stored all the downloaded information (so a change on an Activity will not clear that data) and
 * all the functionalities to manage assets when they are stored on the database and also if they are stored
 * in local memory.
 * 
 * @author Adam Antinoo
 */
final class AssetsByLocationDataSource extends SpecialDataSource {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7522064699765330251L;
	//	private static Logger logger = Logger.getLogger("AssetsByLocationDataSource");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsByLocationDataSource(final DataSourceLocator locator, final IModelGenerator generator,
			final IPartFactory factory) {
		super(locator, factory);
		if (null == generator)
			throw new NeocomRuntimeException("UNEX. The Model Generator should not be empty when creating a DataSource.");
		// REFACTOR The next code should go to the SpecialDataSource once all the DataSources comply to the new implementation.
		_modelGenerator = generator;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	/**
	//	 * We will build the list of assets ordered by locations. This is quite easy since we have already selected
	//	 * the assets from the database and they are already stored on the AssetsManager. If the number of locations
	//	 * is below the limit then do not use the Regions.
	//	 */
	//	@Override
	@Override
	public RootNode collaborate2Model() {
		AbstractDataSource.logger.info(">> `[AssetsByLocationDataSource.collaborate2Model]");
		super.collaborate2Model();
		_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
				.setSorting(NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));

		//		_dataModelRoot = new RootNode();
		//		try {
		//			final AssetsManager manager = AppModelStore.getSingleton().getPilot().getAssetsManager();
		//			//		 HashMap<Long, EveLocation> locations = manager.getLocations();
		//			if (this.showRegions(manager.getLocations().size())) {
		//				for (Region region : manager.getRegions().values()) {
		//					_dataModelRoot.addChild(region);
		//				}
		//			} else {
		//				for (EveLocation location : manager.getLocations().values()) {
		//					_dataModelRoot.addChild(location);
		//				}
		//			}
		//		} catch (final RuntimeException rte) {
		//			rte.printStackTrace();
		//			AbstractDataSource.logger.severe("E> There is a problem at: AssetsByLocationDataSource.createHierarchy.");
		//		}
		AbstractDataSource.logger
				.info("<< [AssetsByLocationDataSource.collaborate2Model]> model size: " + _dataModelRoot.getChildren().size());
		return _dataModelRoot;
	}

	/**
	 * Set the RootPart and the sort element for it. Sort the regions or the locations by name.
	 */
	@Override
	public void createContentHierarchy() {
		//		_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
		//				.setSorting(NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		super.createContentHierarchy();
	}

	@Override
	public ArrayList<AbstractAndroidPart> getHeaderParts() {
		return new ArrayList<AbstractAndroidPart>();
	}

	/**
	 * This method is called whenever there is an event on any Part related to this DataSource. We just process
	 * structure changes that need the DataSource to reconstruct the Part model from the new Model state. Wrong.
	 * The model does not change but the result from the collaborate2View transformation does.
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		// The expand/collapse state has changed.
		if (event.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE)) {
			// REFACTOR Trying to fire a new model generation to solve the problem of renderization.
			//	_dataModelRoot = modelGenerator.collaborate2Model();
			this.createContentHierarchy();

			_bodyParts = new ArrayList<IPart>();
			_bodyParts.addAll(_partModelRoot.collaborate2View());
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES, event.getOldValue(),
					event.getNewValue());
			return;
		}
		// TODO Check if we should get this event and fire it again.
		if (event.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES)) {
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES, event.getOldValue(),
					event.getNewValue());
			return;
		}
		super.propertyChange(event);
	}

	//	private boolean showRegions(final int numberLocations) {
	//		final SharedPreferences prefs = PreferenceManager
	//				.getDefaultSharedPreferences(AppModelStore.getSingleton().getActivity());
	//		final String locLimitString = prefs.getString(AppWideConstants.preference.PREF_LOCATIONSLIMIT,
	//				NeoComApp.getSingletonApp().getResourceString(R.string.pref_numberOfLocations_default));
	//		// Check for the special value of unlimited.
	//		if (locLimitString.equalsIgnoreCase("Unlimited")) return false;
	//		// Convert the stored preference value to a number.
	//		int locLimit = 10;
	//		try {
	//			locLimit = Integer.parseInt(locLimitString);
	//		} catch (NumberFormatException nex) {
	//			locLimit = 10;
	//		}
	//		if (numberLocations > locLimit)
	//			return true;
	//		else
	//			return false;
	//	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsByLocationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String				_login				= null;
	private long					_characterid	= -1;
	private EManagerType	_manager			= EManagerType.ASSETS_MANAGER;

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
		NeoComCharacter pilot = AppConnector.getModelStore().activatePilot(_characterid);
		AssetsManager assetsMan = pilot.getAssetsManager();
		// Check it is initialized.
		if (!assetsMan.isInitialized()) {
			assetsMan.initialize();
		}
		assetsMan.accessAllAssets();
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());
		// Add the first level that are the Regions.
		HashMap<Long, Region> regs = assetsMan.getRegions();
		for (Long key : regs.keySet()) {
			_dataModelRoot.addChild(regs.get(key));
			AbstractGenerator.logger.info("-- [AssetsByLocationGenerator.collaborate2Model]> Adding '"
					+ regs.get(key).getContents() + "' to the _dataModelRoot");
		}
		AbstractGenerator.logger.info("<< [AssetsByLocationGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//private static Logger logger = Logger.getLogger("AssetsFragment");

	// - F I E L D - S E C T I O N ............................................................................
	//	private int _filter = AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory() {
		AssetsFragment.logger.info(">> [AssetsFragment.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		AssetsFragment.logger.info("<< [AssetsFragment.createFactory]");
	}

	@Override
	public String getSubtitle() {
		String st = "";
		if (this.getVariant() == EAssetVariants.ASSETS_BYLOCATION.name()) {
			st = "ASSETS - By Location";
		}
		if (this.getVariant() == EAssetVariants.ASSETS_MATERIALS.name()) {
			st = "ASSETS - Materials";
		}
		return st;
	}

	@Override
	public String getTitle() {
		return this.getPilotName();
	}

	@Override
	public void registerDataSource() {
		AssetsFragment.logger.info(">> [AssetsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		EManagerType manager = EManagerType
				.valueOf(this.getExtras().getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
		AppConnector.getModelStore().activateLoginIdentifier(loginid);
		AppConnector.getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(loginid)
				.addIdentifier(characterid).addIdentifier(manager.name());
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if (this.getVariant() == EAssetVariants.ASSETS_BYLOCATION.name()) {
			generator = ModelGeneratorStore
					.registerGenerator(new AssetsByLocationGenerator(locator, this.getVariant(), loginid, characterid, manager));
		}
		// Register the datasource. If this same datasource is already at the manager we get it instead creating a new one.
		SpecialDataSource ds = new AssetsByLocationDataSource(locator, generator, this.getFactory());
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
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents() {
		// TODO Auto-generated method stub

	}

	private String getPilotName() {
		return AppModelStore.getSingleton().getCurrentPilot().getName();
	}
}

//- UNUSED CODE ............................................................................................
