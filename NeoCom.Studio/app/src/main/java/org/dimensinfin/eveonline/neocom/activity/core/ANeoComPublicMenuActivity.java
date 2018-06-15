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
package org.dimensinfin.eveonline.neocom.activity.core;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.R;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ANeoComPublicMenuActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
//	private static Logger logger = LoggerFactory.getLogger("ANeoComPublicMenuActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				NavUtils.navigateUpFromSameTask(this);
				return true;
						case R.id.action_settings:
							final Intent intent = new Intent(this, SettingsActivity.class);
							this.startActivity(intent);
							return false;
			//			case R.id.action_fullreload:
			//				// Do a reload of the application data. First do a cleanup of the data structures to force a reload.
			//				// This is a hard reset of the application.
			//				NeoComModelStore.getSingleton().cleanModel();
			//				this.startActivity(new Intent(this, NeoComSplashActivity.class));
			//				return true;
			//			case R.id.action_downloadlocations:
			//				// Insert into the download queue the action to download the locations.
			//				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.CITADELUPDATE);
			//				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.OUTPOSTUPDATE);
			//				return true;
			//			case R.id.action_updateaccounts:
			//				// Clean the model store data and force a reload of new data from Database.
			//				// This is equivalent to a soft restart.
			//				ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(new Runnable() {
			//					@Override
			//					public void run () {
			//						NeoComModelStore.getSingleton().initialize();
			//					}
			//				});
			//				return true;
			//			//				new BackgroundMenuAction(this, R.id.action_updateaccounts).execute();
			//			//				break;
			//			case R.id.action_showactivejobs:
			//				this.startActivity(new Intent(this, ActiveJobsActivity.class));
			//				return true;
			//			//							// Create demo fittings to test the save/restore and continue the development.
			//			//							Fitting onConstructionFit = new Fitting();
			//			//							onConstructionFit.setName("Testing - Crusader");
			//			//							onConstructionFit.addHull(11184);
			//			//							onConstructionFit.fitModule(6719, 4);
			//			//							onConstructionFit.fitModule(5973);
			//			//							onConstructionFit.fitModule(5405);
			//			//							onConstructionFit.fitModule(5839);
			//			//							onConstructionFit.fitModule(5849);
			//			//							onConstructionFit.fitModule(11563);
			//			//							onConstructionFit.fitModule(33076);
			//			//							onConstructionFit.fitRig(26929);
			//			//							onConstructionFit.fitRig(26929);
			//			//							onConstructionFit.addCargo(244, 4);
			//			//							onConstructionFit.addCargo(240, 4);
			//			//							NeoComModelStore.getSingleton().addFitting(onConstructionFit, onConstructionFit.getName());
			//			//							return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
