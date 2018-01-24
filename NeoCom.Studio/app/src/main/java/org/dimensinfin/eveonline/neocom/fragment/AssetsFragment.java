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
import android.util.SparseArray;

import com.beimin.eveapi.exception.ApiException;
import com.beimin.eveapi.model.shared.Asset;
import com.beimin.eveapi.model.shared.Location;
import com.beimin.eveapi.parser.ApiAuthorization;
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
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.AssetCategory;
import org.dimensinfin.eveonline.neocom.model.NeoComSimpleAsset;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(AssetsFragment.class);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
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
		if ( this.getVariant() == ENeoComVariants.ASSETS_BYCATEGORY.name() ) {
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
		int characterid = this.getExtras().getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
//		NeoComModelStore.getSingleton().activateCredential(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
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
		if ( null != generator )
			setGenerator(generator);
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents () {
	}

	private String getPilotName () {
		return NeoComModelStore.getActiveCredential().getAccountName();
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsByLocationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private int characterid = -1;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsByLocationGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
		characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
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
		// On cached generators we should check if the model is initialized.
		if ( null == _dataModelRoot ) {
			this.setDataModel(new RootNode());
			// Get the Assets Manager active at the character.
			AssetsManager assetsMan = GlobalAndroidDataManager.getAssetsManager(NeoComModelStore.getCredential4Id(characterid));
			// Initialize the Adapter data structures.
//			this.setDataModel(new RootNode());
			// Add the first level that are the Regions.
			Hashtable<Long, Region> regs = assetsMan.getRegions();
			for (Long key : regs.keySet()) {
				_dataModelRoot.addChild(regs.get(key));
				AbstractGenerator.logger.info("-- [AssetsByLocationGenerator.collaborate2Model]> Adding '"
						+ regs.get(key).getLocations() + "' to the _dataModelRoot");
			}
		}
		AbstractGenerator.logger.info("<< [AssetsByLocationGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetsByCategoryGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(AssetsByCategoryGenerator.class);

	// - F I E L D - S E C T I O N ............................................................................
	private int characterid = -1;

	private SparseArray<AssetCategory> _categories = new SparseArray();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetsByCategoryGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
		characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This adapter will use another approach to obtain the assets. It will call the CCP service and get the complete
	 * list of this character assets and then process them fast, without adding more data that the strictly necessary just
	 * to classify them into their categories.
	 */
	public RootNode collaborate2Model () {
		AbstractGenerator.logger.info(">> [AssetsByCategoryGenerator.collaborate2Model]");
		// On cached generators we should check if the model is initialized.
		if ( null == _dataModelRoot )
			this.setDataModel(new RootNode());
		else return _dataModelRoot;

		// Download and process all the user assets into the list of Categories.
		_categories.clear();
		NeoComAppConnector.getSingleton().startChrono();
		downloadPilotAssets();
		for (int i = 0; i < _categories.size(); i++) {
			_dataModelRoot.addChild(_categories.valueAt(i));
		}
		logger.info("-- [AssetsByCategoryGenerator.collaborate2Model]> Adding {} to the _dataModelRoot", _categories.size());
		AbstractGenerator.logger.info("<< [AssetsByCategoryGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

	public void downloadPilotAssets () {
		logger.info(">> [AssetsByCategoryGenerator.downloadPilotAssets]");
		try {
			final Credential credential = NeoComModelStore.getCredential4Id(characterid);
			final ApiAuthorization auth = new ApiAuthorization(credential.getKeyCode(), characterid, credential.getValidationCode());
			PilotAssetListParser parser = new PilotAssetListParser();
			AssetListResponse response = parser.getResponse(auth);
			logger.info(">> [AssetsByCategoryGenerator.downloadPilotAssets]");
			if ( null != response ) {
				List<Asset> assets = response.getAll();
				// Get the Group and Category to store the assets into its right hierarchy level.
				for (final Asset eveAsset : assets) {
					this.classifyAsset(eveAsset);
				}
			}
		} catch (final ApiException apie) {
			apie.printStackTrace();
		} catch (final RuntimeException rtex) {
			rtex.printStackTrace();
		}
		logger.info("<< [AssetsByCategoryGenerator.downloadPilotAssets]");
	}

	private void classifyAsset (final Asset eveAsset) {
		// Convert the eveapi aplin asset to my new simple NeoComSimpleAsset.
		NeoComSimpleAsset newasset = NeoComSimpleAsset.createFrom(eveAsset);
		// Get the Group and the Category.
		final int typeID = newasset.getTypeID();
		final int categoryid = newasset.getCategoryId();

		// Check if the category is already on the list.
		final AssetCategory hit = _categories.get(categoryid);
		if ( null == hit ) {
			final AssetCategory newcategory = new AssetCategory(categoryid);
			newcategory.addAsset(newasset);
			_categories.put(categoryid, newcategory);
		} else hit.addAsset(newasset);
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
			final Credential credential = NeoComModelStore.getCredential4Id(characterid);
			final ApiAuthorization auth = new ApiAuthorization(credential.getKeyCode(), characterid, credential.getValidationCode());
			final LocationsParser parser = new LocationsParser();
			final LocationsResponse response = parser.getResponse(auth, ids);
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
