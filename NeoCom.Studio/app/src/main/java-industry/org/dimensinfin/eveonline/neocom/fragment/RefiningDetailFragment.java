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

import android.support.annotation.WorkerThread;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.sharedcode.EExtras;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.Property;
import org.dimensinfin.eveonline.neocom.enums.EPropertyTypes;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.app.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("RefiningDetailFragment");

	// - F I E L D - S E C T I O N ............................................................................
	private int resourceTypeId = 1230;

	// - M E T H O D - S E C T I O N ..........................................................................
	public RefiningDetailFragment setResourceTypeId( int resourceTypeId ) {
		this.resourceTypeId = resourceTypeId;
		return this;
	}

	// --- P A G E R F R A G M E N T   O V E R R I D E S
	@Override
	public IPartFactory createFactory() {
		return new IndustryPartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		final EveItem item = new AndroidGlobalDataManager().searchItem4Id(this.resourceTypeId);
		if ( null != item )
			return item.getName();
		else return "-";
	}

	@Override
	public String getTitle() {
		return "REFINING ADVICE";
	}

	@WorkerThread
	@Override
	protected List<ICollaboration> registerHeaderSource() {
		logger.info(">> [RefiningDetailFragment.registerHeaderSource]");
		List<ICollaboration> headerContents = new ArrayList<>();

		// Code should be run on other thread than the main. Dispatch it to the executor.
		//		AndroidGlobalDataManager.submitJob2ui(() -> {
		// Create the report. To complete this add the Credential for the owner of the resource, the current location and the
		// preferred refining location from the location roles for the Pilot.
		if ( this.getAppContext() instanceof ICredentialActivity ) {
			int resourceSystemId = this.getExtras().getInt(EExtras.RESOURCESYSTEM_IDENTIFIER.name(), 34);
			try {
				final Credential credential = ((ICredentialActivity) this.getAppContext()).getCredential();
				final PilotV2 pilot = AndroidGlobalDataManager.fastRequestPilotV2(credential);
				final List<Property> locationRoles = pilot.getLocationRoles();
				final EveLocation closestRefiningLocation = searchClosestRefiningStation(resourceSystemId, locationRoles);
				// TODO - Continue with the development
				final RefiningDetailReport report = new RefiningDetailReport(resourceTypeId)
						.setCredential(credential)
						.setResourceLocation(resourceSystemId)
						.setClosestRefiningLocation(closestRefiningLocation);
				headerContents.add(report);
			} catch (NeoComRegisteredException e) {
				e.printStackTrace();
			}
		}
		//		});
		return headerContents;
	}

	@Override
	protected org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		return new EmptyDataSource(new DataSourceLocator().addIdentifier("EMPTY"), getVariant(), getFactory(), getExtras());
	}

	/**
	 * Starting at the location where the pilot has mined the resource, we scan all the locations that have roles as REFINING. We
	 * calculate the jump distance and we get the closest to the mining location. This should also get the refining result percentage
	 * we can get from the ore.
	 *
	 * If there are no REFINING locations or they are farther that a configured jump distance we return the resource location and
	 * expect a refining result of the 35%.
	 * @param resourceLocation
	 * @return
	 */
	protected EveLocation searchClosestRefiningStation( final int resourceLocation, final List<Property> locationRoles ) {
		// Initialize the searcher to the resource location;
		int refiningTarget = resourceLocation;
		int minJumps = 999;
		for (Property role : locationRoles) {
			if ( role.getPropertyType() == EPropertyTypes.LOCATIONROLE ) {
				if ( role.getStringValue().equalsIgnoreCase("REFINING") ) {
//					int jumps = AndroidGlobalDataManager.calculateRouteJumps(((ICredentialActivity) this.getAppContext()).getCredential()
//							, resourceLocation, Double.valueOf(role.getNumericValue()).intValue());
					int jumps = 1;
					if ( jumps < minJumps ) {
						minJumps = jumps;
						refiningTarget = Double.valueOf(role.getNumericValue()).intValue();
					}
				}
			}
		}
		return new AndroidGlobalDataManager().searchLocation4Id(refiningTarget);
	}

	//	public static class RefiningLocation extends EveLocation {
	//		private int locationId = -1;
	//
	//		public RefiningLocation( final int resourceLocation ) {
	//			this.locationId = resourceLocation;
	//		}
	//	}
}
// - UNUSED CODE ............................................................................................
//[01]
