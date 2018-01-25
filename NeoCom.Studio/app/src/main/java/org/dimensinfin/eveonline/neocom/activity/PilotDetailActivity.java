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
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.PilotDetailsFragment;

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * This Activity will show a detailed list of properties of the selected Pilot or Corporation on the header.
 * And then on the body list it will list the Managers with some summary information about them like the
 * number of assets on the Assets Manager or the value of all the Planetary stuff on the Planetary Manager.
 *
 * @author Adam Antinoo
 */
public class PilotDetailActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private int characterid = -1;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Return the name of this activity
	 *
	 * @return
	 */
	public String getName () {
		return "Pilot Detailed Information";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		AbstractPagerActivity.logger.info(">> [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			Bundle extras = null;
			if ( null != savedInstanceState ) {
				extras = savedInstanceState;
				characterid = savedInstanceState.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			} else {
				extras = this.getIntent().getExtras();
				characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			}
			int page = 0;
			this.addPage(new PilotDetailsFragment().setVariant(ENeoComVariants.PILOT_DETAILS.name()).setExtras(extras),
					page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger
					.severe("RTEX [PilotDetailActivity.onCreate]> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
	}

	@Override
	protected void onRestoreInstanceState (final Bundle savedInstanceState) {
		characterid = savedInstanceState.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 */
	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
	}
}


// - UNUSED CODE ............................................................................................
