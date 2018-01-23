//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
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
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.PlanetaryPartFactory;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.Hashtable;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryFragment extends AbstractPagerFragment {
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class PlanetaryByLocationGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private long characterid = -1;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public PlanetaryByLocationGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
			super(locator, variant);
			characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This adapter should generate the model for all the assets that belong to the Planetary category and aggregated
		 * by Location. The Locations are completely evaluated so once the list of locations is added to the model root
		 * all the nodes are already calculated and on memory.
		 * The presentation is on 3 levels. The first level are the Regions, then the Locations and finally the Planetary
		 * Resources. The first level can be removed if the number of locations is below a user preference.
		 * <p>
		 * This Generator is cached so on next requests it can found already an initialized Generator. In that cases we
		 * can skip the initialization and processing of the PLanetary resources.
		 */
		public RootNode collaborate2Model () {
			AbstractGenerator.logger.info(">> [PlanetaryByLocationGenerator.collaborate2Model]");
			// On cached generators we should check if the model is initialized.
			if ( cacheIsInvalid() ) {
				// Get the Planetary Manager active at the character.
				PlanetaryManager planetaryMgr = GlobalDataManager.getPlanetaryManager(NeoComModelStore.getCredential4Id
						(characterid));
				Hashtable<Long, Region> regs = planetaryMgr.getRegions();
				for (Long key : regs.keySet()) {
					_dataModelRoot.addChild(regs.get(key));
					AbstractGenerator.logger.info("-- [PlanetaryByLocationGenerator.collaborate2Model]> Adding '"
							+ regs.get(key).getLocations() + "' to the _dataModelRoot");
				}
			}
			AbstractGenerator.logger.info("<< [PlanetaryByLocationGenerator.collaborate2Model]");
			return _dataModelRoot;
		}

//		private boolean cacheIsValid () {
//			if ( null == _dataModelRoot ) {
//				// Initialize the Adapter data structures.
//				this.setDataModel(new RootNode());
//				return false;
//			}
//			else return true;
//		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		PlanetaryFragment.logger.info(">> [PlanetaryFragment.createFactory]");
		this.setFactory(new PlanetaryPartFactory(this.getVariant()));
		PlanetaryFragment.logger.info("<< [PlanetaryFragment.createFactory]");
	}

	@Override
	public String getSubtitle () {
		String st = "";
		if ( this.getVariant() == ENeoComVariants.PLANETARY_BYLOCATION.name() ) {
			st = NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_PlanetaryManagerActivity);
		}
		return st;
	}

	/**
	 * Warning. This value has dependencies. I have to simplify this to reduce the number of global methods.
	 * @return
	 */
	@Override
	public String getTitle () {
		return NeoComModelStore.getActiveCredential().getName();
	}

	@Override
	public void registerDataSource () {
		PlanetaryFragment.logger.info(">> [PlanetaryFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
//		//		EManagerType manager = EManagerType
//		//				.valueOf(this.getExtras().getString(AppWideConstants.EExtras.EXTRA_MANAGERNAME.name()));
//		// TODO Activate the fake login until the code is removed
//		//		NeoComModelStore.getSingleton().activateLoginIdentifier("CaptainHaddock09");
//		NeoComModelStore.getSingleton().activateCredential(characterid);
//		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(characterid)
		                                                   .addIdentifier("PLANETARY-MANAGER");
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.PLANETARY_BYLOCATION.name() ) {
			generator = ModelGeneratorStore.registerGenerator(
					new PlanetaryByLocationGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( null != generator )
			setGenerator(generator);
		PlanetaryFragment.logger.info("<< [PlanetaryFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents () {
	}
}
//- UNUSED CODE ............................................................................................
