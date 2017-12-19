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
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.BaseManagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.IndustryBlueprintsFragment;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * Activity to display the different list of Blueprints and allow action with them, like get information to
 * lauch jobs or see the coverage of material requirements and create a goddies list.
 * 
 * @author Adam Antinoo
 */
public class IndustryDirectorActivity extends BaseManagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("IndustryDirectorActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private int	_T3Count	= 0;
	private int	_T2Count	= 0;
	private int	_T1Count	= 0;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
//	/**
//	 * Checks if there are the conditions to activate this particular manager. Each one will have it different
//	 * rules to reach the activation point.<br>
//	 * The BPOManager need that there are at least one BPO on the list of assets of the pilot.
//	 */
//	public boolean checkActivation(final NeoComCharacter checkPilot) {
//		final ArrayList<NeoComBlueprint> bps = checkPilot.getAssetsManager().getBlueprints();
//		if (bps.size() > 0) {
//			// Get the counts of the different blueprint categories.
//			for (final NeoComBlueprint blueprint : bps) {
//				if (blueprint.getTech().equalsIgnoreCase(ModelWideConstants.eveglobal.TechI)) {
//					_T1Count++;
//				}
//				if (blueprint.getTech().equalsIgnoreCase(ModelWideConstants.eveglobal.TechII)) {
//					_T2Count++;
//				}
//				if (blueprint.getTech().equalsIgnoreCase(ModelWideConstants.eveglobal.TechIII)) {
//					_T3Count++;
//				}
//			}
//			return true;
//		} else
//			return false;
//	}
//
//	public int getIconReferenceActive() {
//		return R.drawable.industry;
//	}
//
//	public int getIconReferenceInactive() {
//		return R.drawable.industrydimmed;
//	}

	public String getName() {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_title_IndustryManagerActivity);
	}

	/**
	 * During the creation of a new IndustryDirector we have to check for the Industry Store data. If we found
	 * that data we can restore a previous state. Otherwise we can createa new set of data so next time we enter
	 * this Director we can resume the activity with all that data loaded and ready. During the load of the data
	 * we need to display the Industry pages we need ready access to the list of blueprints, both the T2 and the
	 * T1 blueprints. Since the blueprints are tied to assets we can assume that we have a cached copy of the
	 * XML source file we downloaded when we did the assets download. Instead reading the bleuprint information
	 * from the database with the DAO we can parse the cached XML with the eveapi and process directly the list
	 * of blueprints.<br>
	 * Afterwards we can store all that data relations because they will not change until the assets are
	 * processed again.<br>
	 * In case we have no network we can fall back again to the last XML file blueprint list but if that file is
	 * not present then we can assume we have no ona blueprint and deactivate this Director returning to the
	 * Pilot Information page.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		logger.info(">> [IndustryDirectorActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new IndustryBlueprintsFragment().setVariant(ENeoComVariants.BLUEPRINTS_TII.name()).setExtras(extras), page++);
//			this.addPage(new AssetsFragment().setVariant(ENeoComVariants.ASSETS_BYCATEGORY.name()).setExtras(extras), page++);
		} catch (RuntimeException rtex) {
			AssetsManagerActivity.logger.warning("RTEX [IndustryDirectorActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [IndustryDirectorActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		logger.info( "<< [IndustryDirectorActivity.onCreate]"); //$NON-NLS-1$
	}
}
// - UNUSED CODE
// ............................................................................................
