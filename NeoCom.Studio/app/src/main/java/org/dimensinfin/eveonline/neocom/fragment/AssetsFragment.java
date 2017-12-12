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

import android.os.Bundle;

import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.model.shared.Asset;
import com.beimin.eveapi.model.shared.Location;
import com.beimin.eveapi.parser.pilot.LocationsParser;
import com.beimin.eveapi.parser.pilot.PilotAssetListParser;
import com.beimin.eveapi.response.shared.AssetListResponse;
import com.beimin.eveapi.response.shared.LocationsResponse;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.ModelAppConnector;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

//- CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		AssetsFragment.logger.info(">> [AssetsFragment.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		AssetsFragment.logger.info("<< [AssetsFragment.createFactory]");
	}

	@Override
	public String getSubtitle () {
		String st = "";
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			st = NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_AssetsByLocation);
		}
		if ( this.getVariant() == ENeoComVariants.ASSETS_MATERIALS.name() ) {
			st = NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_AssetsByCategory);
		}
		return st;
	}

	@Override
	public String getTitle () {
		return this.getPilotName();
	}

	@Override
	public void registerDataSource () {
		AssetsFragment.logger.info(">> [AssetsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		//		EManagerType manager = EManagerType
		//				.valueOf(this.getExtras().getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(loginid)
		                                                   .addIdentifier(characterid)
		                                                   .addIdentifier("ASSETS-MANAGER");
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name() ) {
			generator = ModelGeneratorStore
					.registerGenerator(new AssetsByLocationGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYCATEGORY.name() ) {
			generator = ModelGeneratorStore
					.registerGenerator(new AssetsByCategoryGenerator(locator, this.getVariant(), getExtras()));
		}
		setGenerator(generator);
		//		// Register the datasource. If this same datasource is already at the manager we get it instead creating a new one.
		//		SpecialDataSource ds = new MVCDataSource(locator, this.getFactory(), generator);
		//		ds.setVariant(this.getVariant());
		//		//	ds.addParameter(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
		//		this.setDataSource(DataSourceManager.registerDataSource(ds));
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
	public void setHeaderContents () {
	}

	private String getPilotName () {
		return NeoComModelStore.getSingleton().getCurrentPilot().getName();
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsByLocationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String _loginid = null;
	private long _characterid = -1;
	//	private EManagerType	_manager			= EManagerType.ASSETS_MANAGER;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsByLocationGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
		_loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		_characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
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

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsByCategoryGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String _loginid = null;
	private long _characterid = -1;
	private final Vector<NeoComAsset> unlocatedAssets = new Vector<NeoComAsset>();
	private NeoComCharacter _pilot = null;

	private Hashtable<String, Hashtable<String, Vector<Asset>>> assetGroups = new Hashtable();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsByCategoryGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
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
	 * This adapter will use another approach to obtain the assets. It will call the CCP service and get the complete
	 * list of this character assets and then process them fast, without adding more data that the strictly necessary just
	 * to classify them into their categories.
	 */
	public RootNode collaborate2Model () {
		AbstractGenerator.logger.info(">> [AssetsByLocationGenerator.collaborate2Model]");
		// Get the Assets Manager active at the character.
		//		NeoComCharacter pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
		AssetsManager assetsMan = getPilot().getAssetsManager();
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

	public void downloadPilotAssets () {
		logger.info(">> [AssetsByLocationGenerator.downloadPilotAssets]");
		try {
			//			// Clear any previous record with owner -1 from database.
			//			INeoComModelDatabase dbConn = ModelAppConnector.getSingleton().getDBConnector();
			//			synchronized (dbConn) {
			//				dbConn.clearInvalidRecords(this.getPilot().getCharacterID());
			//			}
			// Parse the CCP data to a list of assets
			//			NeoComCharacter pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
			PilotAssetListParser parser = new PilotAssetListParser();
			AssetListResponse response = parser.getResponse(getPilot().getAuthorization());
			if ( null != response ) {
				unlocatedAssets.clear();
				List<Asset> assets = response.getAll();
				// Get the Group and Category to store the assets into its right hierarchy level.
				for (final Asset eveAsset : assets) {
					//					try {
					this.classifyAssets(eveAsset);
					//					} catch (final RuntimeException ex) {
					//						ex.printStackTrace();
					//					}
				}
				//				// Second pass. All the assets in unknown locations should be readjusted for hierarchy changes.
				//				for (NeoComAsset asset : unlocatedAssets) {
				//					this.validateLocation(asset);
				//				}
				//				// Assign the assets to the pilot.
				//				synchronized (dbConn) {
				//					dbConn.replaceAssets(this.getPilot().getCharacterID());
				//				}
				//				// Update the caching time to the time set by the eveapi.
				//				String reference = this.getPilot().getCharacterID() + ".ASSETS";
				//				if ( null == assetsCacheTime ) {
				//					assetsCacheTime = new TimeStamp(reference, new Instant(response.getCachedUntil()));
				//				} else {
				//					assetsCacheTime.updateTimeStamp(new Instant(response.getCachedUntil()));
				//				}
			}
		} catch (final ApiException apie) {
			apie.printStackTrace();
		} catch (final RuntimeException rtex) {
			rtex.printStackTrace();
		}
		logger.info("<< [AssetsByLocationGenerator.downloadPilotAssets]");
	}

	private void classifyAssets (final Asset eveAsset) {
		// Get the Group and the Category.
		final int typeID = eveAsset.getTypeID();
		final EveItem item = ModelAppConnector.getSingleton().getCCPDBConnector().searchItembyID(typeID);
		final String group = item.getGroupName();
		final String category = item.getCategory();

		// Search for the Group.
		Hashtable<String, Vector<Asset>> targetGroup = assetGroups.get(group);
		if ( null == targetGroup ) {
			targetGroup = new Hashtable<String, Vector<Asset>>();
			assetGroups.put(group, targetGroup);
		}

		// Search for the Category
		Vector<Asset> targetCategory = targetGroup.get(category);
		if ( null == targetCategory ) {
			targetCategory = new Vector<Asset>();
			targetGroup.put(category, targetCategory);
		}

		targetCategory.add(eveAsset);
	}

	protected String downloadAssetEveName (final long assetID) {
		// Wait up to one second to avoid request rejections from CCP.
		try {
			Thread.sleep(500); // 500 milliseconds is half second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		final Vector<Long> ids = new Vector<Long>();
		ids.add(assetID);
		try {
			final LocationsParser parser = new LocationsParser();
			final LocationsResponse response = parser.getResponse(this.getPilot().getAuthorization(), ids);
			if ( null != response ) {
				Set<Location> userNames = response.getAll();
				if ( userNames.size() > 0 ) return userNames.iterator().next().getItemName();
			}
		} catch (final ApiException e) {
			logger.info("W- EveChar.downloadAssetEveName - asset has no user name defined: " + assetID);
			//			e.printStackTrace();
		}
		return null;
	}

}

//- UNUSED CODE ............................................................................................
