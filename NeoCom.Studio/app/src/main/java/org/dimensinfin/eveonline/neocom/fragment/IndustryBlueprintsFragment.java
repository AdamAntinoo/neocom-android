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
import org.dimensinfin.eveonline.neocom.manager.BlueprintManager;
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
	@Override
	public void setHeaderContents () {
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class BlueprintsTIIGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
//	private String _loginid = null;
	private long characterid = -1;
	private NeoComCharacter _pilot = null;
	//	private final HashMap<Long, LocationIndustryPart>	locations					= new HashMap<Long, LocationIndustryPart>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public BlueprintsTIIGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
//		_loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
//		_pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
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
			final BlueprintManager blueprintmanager = new BlueprintManager(NeoComModelStore.getSingleton().activateCredential(characterid)).initialize();
			final List<ExtendedLocation> locations = blueprintmanager.getT2Delegate().getLocations();
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
// - UNUSED CODE ............................................................................................
