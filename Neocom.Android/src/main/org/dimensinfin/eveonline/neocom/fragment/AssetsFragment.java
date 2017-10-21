//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.Hashtable;

import org.dimensinfin.activity.AbstractPagerFragment;
import org.dimensinfin.android.datasource.AbstractGenerator;
import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.datasource.ModelGeneratorStore;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.datasource.DataSourceManager;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datasource.NeoComDataSource;
import org.dimensinfin.eveonline.neocom.enums.EManagerType;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.manager.AssetsManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class AssetsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

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
		if (this.getVariant() == ENeoComVariants.ASSETS_BYLOCATION.name()) {
			st = "ASSETS - By Location";
		}
		if (this.getVariant() == ENeoComVariants.ASSETS_MATERIALS.name()) {
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
		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
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
		AssetsFragment.logger.info("<< [AssetsFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents() {
	}

	private String getPilotName() {
		return NeoComModelStore.getSingleton().getCurrentPilot().getName();
	}
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
//- UNUSED CODE ............................................................................................
