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
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.activity.core.NewPagerActivity;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.SplashTasksFragment;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This is the application launcher Activity. It is the first Activity to show to the user and its responsibility is to
 * show the initialization progress of the Application data structures. It is based on the generic MVC Activity that
 * shown Views on a Header section and a list of Views on the List section. The elements to show on the list are the
 * different initialization tasks to execute or pending execution and composed in the standard list way. We start with a
 * model node that that gets converted to a Part on the Factory and that Part manages the Renderer View that is finally
 * shown on the screen.
 *
 * @author Adam Antinoo
 */
public class NeoComSplashActivity extends NewPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("NeoComSplashActivity");

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		logger.info(">> [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(false);
		// Hide the Actionbar because this activity should not show it.
		_actionBar.hide();
		try {
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			//		// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			//			Bundle extras = null;
			//			if (null != savedInstanceState) {
			//				extras = savedInstanceState;
			//				loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
			//				characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			//			} else {
			//				extras = this.getIntent().getExtras();
			//				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
			//				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			//			}
			int page = 0;
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			this.addPage(new SplashTasksFragment().setVariant(ENeoComVariants.SPLASH_SCREEN.name()), page++);
		} catch (Exception rtex) {
			logger.warning("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		logger.info("<< [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
	}
}
