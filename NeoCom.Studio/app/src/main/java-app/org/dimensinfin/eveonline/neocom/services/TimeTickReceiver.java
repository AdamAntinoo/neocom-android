//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
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
package org.dimensinfin.eveonline.neocom.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.enums.PreferenceKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * This class is a one minute timer activated at startup. It will search for data obsolete and fire the
 * services required to update that data by downloading from CCP servers using the required API calls.
 * @author Adam Antinoo
 */
public class TimeTickReceiver extends BroadcastReceiver {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("TimeTickReceiver");

	// - F I E L D - S E C T I O N ............................................................................
	private Context _context = null;
	private int countdown = 0;
	private int countdownTarget = 1;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public TimeTickReceiver( final Context context ) {
		_context = context;
		countdownTarget = AndroidGlobalDataManager.getResourceInt("R.runtime.downloadTick.cycleTime", 5);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Receive an intent on every minute tick.
	 *
	 * On each tick fire the Credential scan searching for Pilot or Corporation expired data downloads. If so then start a new download
	 * job.
	 *
	 * Current code runs on all platforms so there is no need to have specific code for the Android platform.
	 */
	@Override
	public void onReceive( final Context context, final Intent intent ) {
		logger.info(">> [TimeTickReceiver.onReceive]");
		try {
			// Run only if the network is active.
			if (!AndroidGlobalDataManager.checkNetworkAccess()) return;
			// Or if the service is disables by configuration.
			if (this.blockedDownload()) return;

			// Count down configured minutes between consecutive ticker firings. This Receiver is called every minute but the download check
			// should be done every 5 minutes or any other minute count configured.
			countdown--;
			if (countdown <= 0) {
				// Reset the counter and fire the tick.
				countdown = countdownTarget;
				NeoComApplication.runTicker();
				// Write down cached data.
				NeoComApplication.mdServer.writeMarketDataCacheToStorage();
				//			AndroidGlobalDataManager.writeLocationsDatacache();
			}
		} finally {
			logger.info("<< [TimeTickReceiver.onReceive]> [{}]", countdown);
		}
	}

	private boolean blockedDownload() {
		// Read the flag values from the preferences.
		boolean blockDownload = AndroidGlobalDataManager.getDefaultSharedPreferences()
				.getBooleanPreference(PreferenceKeys.prefkey_BlockDownloads.name(), true);
		return blockDownload;
	}
}

// - UNUSED CODE ............................................................................................
//[01]
