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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.ExceptionDataSource;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.core.NeoComException;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MineralResourcesFragment extends MiningLedgerFragment{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MineralResourcesFragment");

	// - F I E L D - S E C T I O N ............................................................................
	private int pilotIdentifier = -1;

	// - M E T H O D - S E C T I O N ..........................................................................
	public int getPilotIdentifier() {
		return pilotIdentifier;
	}

	public MineralResourcesFragment setPilotIdentifier( final int pilotIdentifier ) {
		this.pilotIdentifier = pilotIdentifier;
		return this;
	}

	@Override
	public IPartFactory createFactory() {
		return new IndustryPartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "Mineral Resources by Location";
	}

	@Override
	public String getTitle() {
		return "Pilot";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected IDataSource registerDataSource() {
		logger.info(">< [MineralResourcesFragment.registerDataSource]");
		if ( pilotIdentifier < 0 ) {
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(new NeoComException("[MineralResourcesFragment.registerDataSource]> Pilot identifier is not valid."));
		}
		try {
			return new MineralResourcesDataSource(new DataSourceLocator()
					.addIdentifier("MINERAL-RESOURCES")
					.addIdentifier(getVariant())
					.addIdentifier(pilotIdentifier)
					, getVariant()
					, createFactory()
					, getExtras())
					.setCacheable(true)
					.setVariant(getVariant());
		} catch (NeoComException neoe) {
			neoe.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(neoe);
		}
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class MineralResourcesDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private int pilotIdentifier = -1;
		private Credential credential = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MineralResourcesDataSource( final DataSourceLocator locator
				, final String variant
				, final IPartFactory factory
				, final Bundle extras ) throws NeoComException {
			super(locator, variant, factory, extras);
			// Extract the pilot identifier from the extras.
			pilotIdentifier = extras.getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), -1);
			if ( pilotIdentifier < 0 )
				throw new NeoComException("X> [MineralResourcesDataSource.<constructor>]> Data source has not received the Pilot Identifier mandatory parameter.");
			else {
				// Search the matching credential.
				final List<Credential> credentials = AndroidGlobalDataManager.accessAllCredentials();
				for (Credential cred : credentials) {
					if ( cred.getAccountId() == pilotIdentifier ) credential = cred;
				}
				// We have not found the credential. Fire another exception.
				if ( null == credential )
					throw new NeoComException("X> [MineralResourcesDataSource.<constructor>]> Data source has not located a valid Credential " +
							"for identifier " + pilotIdentifier + ".");
			}
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This is the method that creates the model nodes represent the list of current registered credentials. In addition to this list
		 * there is another item that is the actuator button to initiate the process to register a new credential.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [MineralResourcesDataSource.collaborate2Model]");
			// Check if the data is already on the cache so we should not have to process again the model generation.
			if ( !isCached() ) {
				try {
					// Add the panel title up front of the Credential list.
					this.addModelContents(new PanelTitle("MINERALS BY LOCATION"));

					// Get the list of assets of Mineral type.
					HashMap<String, Object> where = new HashMap<String, Object>();
					where.put("ownerId", credential.getAccountId());
					where.put("category", "Material");
					where.put("groupName", "Mineral");
					final List<NeoComAsset> miningResources = new AndroidGlobalDataManager().getNeocomDBHelper().getAssetDao()
					                                                                              .queryForFieldValues(where);
					// Get the list of assets of Ore type.
					where = new HashMap<String, Object>();
					where.put("ownerId", credential.getAccountId());
					where.put("category", "Asteroid");
					miningResources.addAll(new AndroidGlobalDataManager().getNeocomDBHelper().getAssetDao()
					                                                           .queryForFieldValues(where));
					// Get ell the Mineral resources and classify them by location.
	// TODO - Copy in the classification code. Now I will display the complete list.
					for (NeoComAsset asset : miningResources) {
						this.addModelContents(asset);
						logger.info("-- [MineralResourcesDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
								, asset.getName());
					}
				} catch (SQLException sqle) {
					AndroidGlobalDataManager.registerException("MineralResourcesDataSource.collaborate2Model"
							, sqle, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					sqle.printStackTrace();
				} catch (RuntimeException rtex) {
					AndroidGlobalDataManager.registerRuntimeException("MineralResourcesDataSource.collaborate2Model"
							, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					rtex.printStackTrace();
				}
			}
			logger.info("<< [MineralResourcesDataSource.collaborate2Model]");
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
