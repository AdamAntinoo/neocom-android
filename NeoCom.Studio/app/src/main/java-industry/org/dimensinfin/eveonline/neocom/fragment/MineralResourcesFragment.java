package org.dimensinfin.eveonline.neocom.fragment;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.datasource.ExceptionDataSource;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.exception.NeoComException;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerFragment;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.InformationPanel;
import org.dimensinfin.eveonline.neocom.model.LocationFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MineralResourcesFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("MineralResourcesFragment");

	// - F I E L D - S E C T I O N ............................................................................

	/** Credential to be used on all database requests and data retrievals. */
	//	protected Credential credential = null;

	// - M E T H O D - S E C T I O N ..........................................................................
	//	public MiningLedgerFragment setCredential( final Credential credential ) {
	//		this.credential = credential;
	//		return this;
	//	}
	//
	// --- P A G E R F R A G M E N T   O V E R R I D E S
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
		final Credential credential = ((ICredentialActivity) this.getAppContext()).getCredential();
		if (null != credential)
			return credential.getAccountName();
		else return "-";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		List<ICollaboration> headerList = new ArrayList<>();
		headerList.add(new PanelTitle("MINERALS BY LOCATION"));
		return headerList;
	}

	@Override
	protected org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		logger.info(">< [MineralResourcesFragment.registerDataSource]");
		final DataSourceLocator identifier = new DataSourceLocator().addIdentifier("MINERAL-RESOURCES")
				.addIdentifier(getVariant())
				.addIdentifier(((ICredentialActivity) this.getAppContext()).getCredential().getAccountId());
		try {
			return new MineralResourcesDataSource(identifier
					, getVariant()
					, createFactory()
					, getExtras())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setCacheable(true)
					.setVariant(getVariant());
		} catch (NeoComException neoe) {
			neoe.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(neoe);
		} catch (RuntimeException rte) {
			rte.printStackTrace();
			// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION"), getVariant()
					, new PartFactory(getVariant()), getExtras())
					.registerException(rte);
		}
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class MineralResourcesDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		/** Credential to be used on all database requests and data retrievals. */
		protected Credential credential = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MineralResourcesDataSource( final DataSourceLocator locator
				, final String variant
				, final IPartFactory factory
				, final Bundle extras ) throws NeoComException {
			super(locator, variant, factory, extras);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		public MineralResourcesDataSource setCredential( final Credential credential ) {
			this.credential = credential;
			return this;
		}
		// --- I D A T A S O U R C E   I N T E R F A C E

		/**
		 * This is the method that creates the model nodes represent the list of current registered credentials. In addition to this list
		 * there is another item that is the actuator button to initiate the process to register a new credential.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [MineralResourcesDataSource.collaborate2Model]");
			// Check if the data is already on the cache so we should not have to process again the model generation.
			if (!isCached()) {
				if (null != credential) {
					try {
						// Initialize the data structures to be used for classification.
						final HashMap<Long, LocationFacetedAssetsContainer> locations = new HashMap<>();
						final LocationFacetedAssetsContainer collectorLocation = new LocationFacetedAssetsContainer(
								new EveLocation().setSystem("UNKWNOWN DEEP SPACE")
						);
						this.addModelContents(collectorLocation);
						logger.info("-- [MineralResourcesDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
								, collectorLocation.getStation());
						locations.put(-1l, collectorLocation);

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

						//--- L O O P   S T A R T
						for (NeoComAsset asset : miningResources) {
							// Check the asset location. If of type UNKNOWN then it should be processed later.
							final EveLocation loc = asset.getLocation();
							if (loc.getTypeId() == ELocationType.UNKNOWN) {
								collectorLocation.addAsset(asset);
								continue;
							}

							//--- L O C A T I O N
							// Check if this Location is already present the cache.
							LocationFacetedAssetsContainer locationHit = locations.get(loc.getId());
							if (null == locationHit) {
								locationHit = new LocationFacetedAssetsContainer(loc);
								locations.put(loc.getId(), locationHit);
								this.addModelContents(locationHit);
								logger.info("-- [MineralResourcesDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
										, locationHit.getStation());
							}

							//--- A S S E T
							// The Region and the Location were already registered. Add the asset to the Location and check if this
							// affects the UI.
							locationHit.addAsset(asset);
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
				} else
					this.addModelContents(new InformationPanel("The Credential is not available."));
			}
			logger.info("<< [MineralResourcesDataSource.collaborate2Model]");
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
