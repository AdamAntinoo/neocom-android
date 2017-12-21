//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.model.ExtendedLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class IndustryBlueprintsFragment extends AbstractPagerFragment {

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	//	private String _tech = ModelWideConstants.eveglobal.TechI;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		logger.info(">> [IndustryBlueprintsFragment.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		logger.info("<< [IndustryBlueprintsFragment.createFactory]");
	}

	@Override
	public String getSubtitle () {
		String st = "";
		if ( this.getVariant() == ENeoComVariants.BLUEPRINTS_TII.name() ) {
			st = NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_BlueprintsT2);
		}
		return st;
	}

	@Override
	public String getTitle () {
		return this.getPilotName();
	}

	private String getPilotName () {
		return NeoComModelStore.getSingleton().getActiveCharacter().getName();
	}

	@Override
	public void registerDataSource () {
		logger.info(">> [IndustryBlueprintsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(loginid)
		                                                   .addIdentifier(characterid)
		                                                   .addIdentifier("INDUSTRY-MANAGER");
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.BLUEPRINTS_TII.name() ) {
			generator = ModelGeneratorStore
					.registerGenerator(new BlueprintsTIIGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( null != generator )
			setGenerator(generator);
		logger.info("<< [IndustryBlueprintsFragment.registerDataSource]");
	}

	//	/**
	//	 * Creates the structures when the fragment is about to be shown. It will inflate the layout where the
	//	 * generic fragment will be layered to show the content. It will get the Activity functionality for single
	//	 * page activities.
	//	 */
	//	@Override
	//	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
	//		Log.i("NEOCOM", ">> IndustryBlueprintsFragment.onCreateView");
	//		final View theView = super.onCreateView(inflater, container, savedInstanceState);
	//		try {
	//			if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechIII)) {
	//				this.setIdentifier(AppWideConstants.fragment.FRAGMENT_INDUSTRYT3BLUEPRINTS);
	//			}
	//			if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechII)) {
	//				this.setIdentifier(AppWideConstants.fragment.FRAGMENT_INDUSTRYT2BLUEPRINTS);
	//			}
	//			if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechI)) {
	//				this.setIdentifier(AppWideConstants.fragment.FRAGMENT_INDUSTRYT1BLUEPRINTS);
	//			}
	//		} catch (final RuntimeException rtex) {
	//			Log.e("NEOCOM", "RTEX> IndustryBlueprintsFragment.onCreateView - " + rtex.getMessage());
	//			rtex.printStackTrace();
	//			this.stopActivity(new RuntimeException("RTEX> IndustryBlueprintsFragment.onCreateView - " + rtex.getMessage()));
	//		}
	//		Log.i("NEOCOM", "<< IndustryBlueprintsFragment.onCreateView");
	//		return theView;
	//	}

	//	@Override
	//	public void onStart() {
	//		Log.i("NEOCOM", ">> IndustryBlueprintsFragment.onStart");
	//		if (!_alreadyInitialized) {
	//			try {
	//				// Create the right blueprint list depending on the parametrized tech.
	//				NeoComModelStore store = NeoComModelStore.getSingleton();
	//				if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechIII)) {
	//					this.setDataSource(new IndustryT3BlueprintsDataSource(store));
	//				}
	//				if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechII)) {
	//					this.setDataSource(new IndustryT2BlueprintsDataSource(store));
	//				}
	//				if (_tech.equalsIgnoreCase(ModelWideConstants.eveglobal.TechI)) {
	//					this.setDataSource(new IndustryT1BlueprintsDataSource(store));
	//				}
	//			} catch (final RuntimeException rtex) {
	//				Log.e("NEOCOM", "RTEX> IndustryBlueprintsFragment.onStart - " + rtex.getMessage());
	//				rtex.printStackTrace();
	//				this.stopActivity(new RuntimeException("RTEX> IndustryBlueprintsFragment.onStart - " + rtex.getMessage()));
	//			}
	//		}
	//		super.onStart();
	//		Log.i("NEOCOM", "<< IndustryBlueprintsFragment.onStart");
	//	}
	//
	//	public AbstractPagerFragment setTechLevel(final String tech) {
	//		_tech = tech;
	//		return this;
	//	}
	@Override
	public void setHeaderContents () {
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class BlueprintsTIIGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String _loginid = null;
	private long _characterid = -1;
	private NeoComCharacter _pilot = null;
	//	private final HashMap<Long, LocationIndustryPart>	locations					= new HashMap<Long, LocationIndustryPart>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public BlueprintsTIIGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
		_loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		_characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		_pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NeoComCharacter getPilot () {
		return _pilot;
	}

	/**
	 * This adapter should generate the model for all the assets aggregated by Location. The Locations are lazy
	 * evaluated so no need to get all the assets by the list of locations and then construct the first two
	 * levels, Regions then Locations. Once the Location is expanded then the model will extend itself to the
	 * new list of assets.<br>
	 * REFACTOR: For testing purposes we are going to download all the assets.
	 */
	public RootNode collaborate2Model () {
		AbstractGenerator.logger.info(">> [BlueprintsTIIGenerator.collaborate2Model]");
		// On cached generators we should check if the model is initialized.
		if ( null == _dataModelRoot ) {
			this.setDataModel(new RootNode());
			// Get the Blueprints Manager active at the character.
			final List<ExtendedLocation> locations = getPilot().getBlueprintsManager().getT2Delegate().getLocations();
			//Hashtable<Long, Region> regs = assetsMan.getRegions();
			for (ExtendedLocation loc : locations) {
				_dataModelRoot.addChild(loc);
				AbstractGenerator.logger.info("-- [BlueprintsTIIGenerator.collaborate2Model]> Adding '"
						+ loc.getName() + "' to the _dataModelRoot");
			}
		}
		AbstractGenerator.logger.info("<< [BlueprintsTIIGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

}

//// - CLASS IMPLEMENTATION ...................................................................................
///**
// * This DataSource will get access to the special IndustryManager to have track of the assets resulting from
// * some other actions. The list of resources is mandatory to be able to check how many blueprints can be
// * manufactured. Manufacture resources are accounted from the location where the blueprint is located with
// * exception to the MANUFACTURE location that will have the resources reduced or augmented depending on
// * scheduled manufacture jobs or by scheduled market buys.<br>
// * Each blueprint is located on a hierarchical tree with locations at the top, then Hangars or Containers and
// * then the blueprints themselves. Blueprints will be stacked and the resulting stack checked against
// * available resources to set the number of blueprints that are manufacturable. This data will be stored on
// * the Part and will use the new IndustryManager.
// *
// * @author Adam Antinoo
// */
//public class IndustryT2BlueprintsDataSource extends AbstractDataSource {
//	// - S T A T I C - S E C T I O N ..........................................................................
//
//	// - F I E L D - S E C T I O N ............................................................................
//
//	// - C O N S T R U C T O R - S E C T I O N ................................................................
//
//	// - M E T H O D - S E C T I O N ..........................................................................
//	/**
//	 * The source data for this data source is the EveChar - AssetsManager - Blueprint CCP API. The first
//	 * element to access if the EveChar that should have access to an AssetsManager. This manager will store and
//	 * cache all Character model data and on some cases the access to the CCP servers to download new and fresh
//	 * versions of the API data. Blueprints are not to be stored at the database and their load on the EveChar
//	 * structures is expected to be performed on background tasks once the Character is active on memory. The
//	 * new Activity data model dependencies may request that same loading multiple times during the life cycle
//	 * of the activities and the local data models that will be created on that cycles. Because of this is quite
//	 * important to perform effective caches of the original source CCP API data that should be stored on disk
//	 * for off link access and app operation.<br>
//	 * The hierarchy contains two levels of elements. The first level are the locations and can be of two types,
//	 * locations where the blueprints are on the floor (hangar) and locations where the blueprint is inside some
//	 * container. They will not have the default name but the name of the container and will behave more like
//	 * them.<br>
//	 * On the second level there are the blueprints themselves.<br>
//	 * <br>
//	 * The process to get this hierarchy is somehow different from other loops. It will get each of the
//	 * blueprints in order and then locate the proper place where to connect it. If the elements do not exist
//	 * they will be created. If the element exists the blueprint will be aggregated to a stack of the same type.
//	 */
//	@Override
//	public void createContentHierarchy() {
//		Log.i("IndustryT2Blueprints", ">> IndustryT2Blueprints.createContentHierarchy");
//		// Clear the current list of elements.
//		_root.clear();
//
//		// Get the AssetsManager through the Store.
//		ArrayList<NeoComBlueprint> bps = _store.getPilot().getAssetsManager().searchT2Blueprints();
//		for (NeoComBlueprint currentbpc : bps) {
//			long locid = currentbpc.getLocationID();
//			NeoComAsset parent = currentbpc.getParentContainer();
//			BlueprintPart bppart = new BlueprintPart(currentbpc);
//			bppart.setActivity(ModelWideConstants.activities.MANUFACTURING);
//			bppart.setRenderMode(AppWideConstants.rendermodes.RENDER_BLUEPRINTINDUSTRY);
//			if (null == parent) {
//				this.add2Location(locid, bppart);
//			} else {
//				this.add2Container(parent, bppart);
//			}
//		}
//		Log.i("IndustryT2Blueprints", "<< IndustryT2Blueprints.createContentHierarchy [" + _root.size() + "]");
//	}
//
//	@Override
//	public ArrayList<AbstractAndroidPart> getBodyParts() {
//		AbstractDataSource.logger.info(">> IndustryT2Blueprints.getPartHierarchy");
//		Collections.sort(_root, NeoComApp.createComparator(AppWideConstants.comparators.COMPARATOR_NAME));
//		ArrayList<AbstractAndroidPart> result = new ArrayList<AbstractAndroidPart>();
//		for (AbstractAndroidPart node : _root)
//			// Check if the node is expanded. Then add its children.
//			if (node.isExpanded()) {
//				result.add(node);
//				result.add(new TerminatorPart(new Separator("")));
//				ArrayList<IPart> grand = node.collaborate2View();
//				// Order the list of blueprints by their profit
//				Collections.sort(grand, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_CARD_RATIO));
//				for (IPart part : grand) {
//					result.add((AbstractAndroidPart) part);
//				}
//				result.add(new TerminatorPart(new Separator("")));
//			} else {
//				result.add(node);
//			}
//		_adapterData = result;
//		AbstractDataSource.logger.info("<< IndustryT2Blueprints.getPartHierarchy");
//		return result;
//	}
//
//	@Override
//	public DataSourceLocator getDataSourceLocator() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public ArrayList<AbstractAndroidPart> getHeaderParts() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void propertyChange(final PropertyChangeEvent event) {
//		if ( SystemWideConstants.events
//				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE) {
//			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
//					event.getNewValue());
//		}
//	}
//
//	/**
//	 * Adding to a container is like adding to a location. The location is the container's location but the name
//	 * changes to the containers name. So create a <code>LocationBlueprintPart</code> but set the container
//	 * field. We also change the aggregation algorithm to locate the corresponding asset container on another
//	 * list.
//	 *
//	 * @param container
//	 * @param part
//	 */
//	private void add2Container(final NeoComAsset container, final BlueprintPart part) {
//		long cid = container.getDAOID();
//		LocationIndustryPart lochit = locations.get(cid);
//		if (null == lochit) {
//			lochit = (LocationIndustryPart) new LocationIndustryPart(part.getCastedModel().getLocation())
//					.setRenderMode(AppWideConstants.rendermodes.RENDER_BLUEPRINTINDUSTRY);
//			lochit.setContainerLocation(true);
//			String containername = container.getUserLabel();
//			if (null == containername) {
//				lochit.setContainerName("#" + container.getAssetID());
//			} else {
//				lochit.setContainerName(containername);
//			}
//			locations.put(cid, lochit);
//			_root.add(lochit);
//		}
//		lochit.addChild(part);
//	}
//
//	/**
//	 * Checks of this locations already exists on the table and if not found then creates a new LocationPart
//	 * branch and adds to it the parameter Part.
//	 *
//	 * @param locationid
//	 *
//	 * @param part
//	 *          part to be added to the locations. May be an asset or a container.
//	 */
//	private void add2Location(final long locationid, final BlueprintPart part) {
//		// Check if the location is already on the array.
//		LocationIndustryPart hit = locations.get(locationid);
//		if (null == hit) {
//			hit = (LocationIndustryPart) new LocationIndustryPart(part.getCastedModel().getLocation())
//					.setRenderMode(AppWideConstants.rendermodes.RENDER_BLUEPRINTINDUSTRY);
//			locations.put(locationid, hit);
//			_root.add(hit);
//		}
//		hit.addChild(part);
//	}
//
//	//	/**
//	//	 * Stacks blueprints that are equal and that are located on the same location. The also should be inside the
//	//	 * same container so the locationID, the parentContainer and the typeID should match to perform the
//	//	 * aggregation.
//	//	 *
//	//	 * @param targetContainer
//	//	 *          the stack that will receive the additional blueprint
//	//	 * @param part
//	//	 *          the blueprint part to be added to the hierarchy
//	//	 */
//	//	private void checkBPCStacking(final LocationIndustryPart targetContainer, final BlueprintPart part) {
//	//		int type = part.getTypeID();
//	//		// Search on the children list for a identical blueprint type.
//	//		Vector<IGEFNode> childs = targetContainer.getChildren();
//	//		for (IGEFNode node : childs) {
//	//			if (node instanceof BlueprintPart) {
//	//				BlueprintPart bpcpart = (BlueprintPart) node;
//	//				int childtype = bpcpart.getTypeID();
//	//				if (childtype == type) {
//	//					bpcpart.incrementStack();
//	//					return;
//	//				}
//	//			}
//	//		}
//	//		targetContainer.addChild(part);
//	//	}
//
//	//	private void taskCreation(final AbstractAndroidPart part) {
//	//		if (part instanceof BlueprintPart) {
//	//			((BlueprintPart) part).generateT2ManufactureTasks();
//	//		} else {
//	//			Vector<IGEFNode> ch = part.getChildren();
//	//			for (IGEFNode node : ch) {
//	//				taskCreation((AbstractAndroidPart) node);
//	//			}
//	//		}
//	//	}
//
//	//	private IndustryManager getIndustryManager() {
//	//		if (null == industryManager) industryManager = new IndustryManager(DataSourceFactory.getPilot());
//	//		return industryManager;
//	//	}
//
//}

// - UNUSED CODE ............................................................................................
