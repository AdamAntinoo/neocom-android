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
import org.dimensinfin.eveonline.neocom.app.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryProcessor;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryScenery;
import org.dimensinfin.eveonline.neocom.planetary.ProcessingAction;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.Vector;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryOptimizationFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		PlanetaryOptimizationFragment.logger.info(">> [PlanetaryOptimizationFragment.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		PlanetaryOptimizationFragment.logger.info("<< [PlanetaryOptimizationFragment.createFactory]");
	}

	@Override
	public String getSubtitle () {
		return NeoComAppConnector.getSingleton()
		                         .getResourceString(R.string.activity_subtitle_PlanetaryOptimizationActivity);
	}

	@Override
	public String getTitle () {
		return NeoComModelStore.getActiveCredential().getName();
	}

	@Override
	public void registerDataSource () {
		PlanetaryOptimizationFragment.logger.info(">> [PlanetaryOptimizationFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
//		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		long locationid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());

//		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComModelStore.getSingleton().activateCredential(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
//		                                                   .addIdentifier(loginid)
		                                                   .addIdentifier(characterid)
		                                                   .addIdentifier(locationid)
		                                                   .addIdentifier("PLANETARY-OPTIMIZATION");
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.PLANETARY_OPTIMIZATION.name() ) {
			generator = ModelGeneratorStore.registerGenerator(
					new PlanetaryOptimization4LocationGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( null != generator )
			setGenerator(generator);
		PlanetaryOptimizationFragment.logger.info("<< [PlanetaryOptimizationFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents () {
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class PlanetaryOptimization4LocationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
//	private String _loginid = null;
	private long characterid = -1;
//	private NeoComCharacter _pilot = null;
	private long _locationid = -2;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryOptimization4LocationGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
//		_loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		_locationid = extras.getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());
//		_pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(_characterid);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
//	public NeoComCharacter getPilot () {
//		return _pilot;
//	}

	/**
	 * The model generated by this Generator is the result of optimizing the list of Planetary resources located at the
	 * target Location. This list of resources is recursively processed to found what is the transformation that
	 * generates the best benefit scenario.
	 */
	public RootNode collaborate2Model () {
		AbstractGenerator.logger.info(">> [PlanetaryOptimization4LocationGenerator.collaborate2Model]");
		// On cached generators we should check if the model is initialized.
		if ( null == _dataModelRoot ) {
			// Get the Assets Manager active at the character.
			PlanetaryManager planetaryMan = (PlanetaryManager) new PlanetaryManager(NeoComModelStore.getSingleton().activateCredential(characterid)).initialize();
			try {
				// Get the location and then process all its contents to extract the Planetary Resources.
				Vector<Resource> resources = planetaryMan.getLocationContents(Long.valueOf(_locationid).toString());
				// The Planetary Advisor requires a list of Planetary Resources to be stocked to start the profit calculations.
				PlanetaryScenery scenery = new PlanetaryScenery();
				scenery.stock(resources);
				// Create the initial processing point and start the optimization recursively.
				PlanetaryProcessor proc = new PlanetaryProcessor(scenery);
				// Start running the best profit search.
				Vector<ProcessingAction> bestScenario = proc.startProfitSearch(null);

				// Initialize the Adapter data structures.
				this.setDataModel(new RootNode());
				for (ProcessingAction action : bestScenario) {
					_dataModelRoot.addChild(action);
					AbstractGenerator.logger.info("-- [PlanetaryOptimization4LocationGenerator.collaborate2Model]> Adding '"
							+ action.toString() + "' to the _dataModelRoot");
				}
			} catch (RuntimeException rtex) {
				rtex.printStackTrace();
			}
		}
		AbstractGenerator.logger.info("<< [PlanetaryOptimization4LocationGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

}
//- UNUSED CODE ............................................................................................
