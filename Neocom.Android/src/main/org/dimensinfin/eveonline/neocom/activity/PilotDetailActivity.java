//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import org.dimensinfin.eveonline.neocom.activity.core.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.constant.EActivityVariants;
import org.dimensinfin.eveonline.neocom.fragment.PilotDetailedFragment;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

import android.os.Bundle;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This Activity will show a detailed list of properties of the selected Pilot or Corporation on the header.
 * And then on the body list it will list the Managers with some summary information about them like the
 * number of assets on the Assets Manager or the value of all the Planetary stuff on the Planetary Manager.
 * 
 * @author Adam Antinoo
 */
public class PilotDetailActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Return the name of this activity
	 * 
	 * @return
	 */
	public String getName() {
		return "Pilot Detailed Information";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		AbstractPagerActivity.logger.info(">> [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Register this Activity as the current active Activity.
			AppModelStore.getSingleton().activateActivity(this);
			int page = 0;
			this.addPage(new PilotDetailedFragment().setVariant(EActivityVariants.PILOT_DETAILS.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger
					.severe("[PilotDetailActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
	}
}
// - UNUSED CODE ............................................................................................
