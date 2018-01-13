//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//                 the game data to help capsuleers organize and prioritize activities. The strong points are
//                 help at the Industry level tracking and calculating costs and benefits. Also the market
//                 information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//                 from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//                 designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//                 rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.BaseManagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.ColonyFragment;
import org.dimensinfin.eveonline.neocom.fragment.PlanetaryFragment;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryManagerActivity extends BaseManagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getName() {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_PlanetaryManagerActivity);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		PlanetaryManagerActivity.logger.info(">> [PlanetaryManagerActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(true);
		try {
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new PlanetaryFragment().setVariant(ENeoComVariants.PLANETARY_BYLOCATION.name()).setExtras(extras),
					page++);
			this.addPage(new ColonyFragment().setVariant(ENeoComVariants.PLANETARY_COLONIES.name()).setExtras(extras),
					page++);
		} catch (Exception rtex) {
			PlanetaryManagerActivity.logger.warning("RTEX> [PlanetaryManagerActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX> [PlanetaryManagerActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		PlanetaryManagerActivity.logger.info("<< [PlanetaryManagerActivity.onCreate]"); //$NON-NLS-1$
	}
}
//- UNUSED CODE ............................................................................................
