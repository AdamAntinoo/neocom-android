//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.os.Bundle;
import android.view.MenuItem;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.LoginListFragment;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class LoginListActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("LoginListActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Return the name of this activity
	 *
	 * @return
	 */
	public String getName() {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_title_LoginListActivity);
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		LoginListActivity.logger.info(">> [LoginListActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Process the parameters into the context. This initial Activity is the only one with no parameters.
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			int page = 0;
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			this.addPage(new LoginListFragment().setVariant(ENeoComVariants.CAPSULEER_LIST.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger.severe("[LoginListActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [LoginListActivity.onCreate]"); //$NON-NLS-1$
	}

	/**
	 * THis code should override the generic navigation code. Do this for this Activity because this is the first and there are no other parents
	 * for this.
	 * @param item menu item selected.
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. For the top of the parent chain just do nothing.1
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
//- UNUSED CODE ............................................................................................
