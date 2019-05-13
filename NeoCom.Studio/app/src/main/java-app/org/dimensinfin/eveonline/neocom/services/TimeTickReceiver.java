package org.dimensinfin.eveonline.neocom.services;

import java.util.Objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.enums.PreferenceKeys;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a one minute timer activated at startup. It will search for data obsolete and fire the
 * services required to update that data by downloading from CCP servers using the required API calls.
 *
 * @author Adam Antinoo
 */
public class TimeTickReceiver extends BroadcastReceiver {
	private static Logger logger = LoggerFactory.getLogger(TimeTickReceiver.class);

	// - F I E L D - S E C T I O N
	private Context context = null;
	private boolean activationState = true;
	private int timer = 1;

	private int countdown = 0;
	private int countdownTarget = 1;

	// - C O N S T R U C T O R S
	public TimeTickReceiver( final Context context ) {
		this.context = context;
//		countdownTarget = context.getResources().getInteger("R.runtime.downloadTick.cycleTime", 5);
	}

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
				                        .getBooleanPreference(PreferenceKeys.prefkey_BlockDownloads.name());
		return blockDownload;
	}

	// - B U I L D E R
	public static class Builder {
		private TimeTickReceiver onConstruction;

		public Builder( final Context context ) {
			this.onConstruction = new TimeTickReceiver(context);
		}

		public Builder withTickActivate( final boolean activationState ) {
			this.onConstruction.activationState = activationState;
			return this;
		}

		public Builder withTickTime( final int timer ) {
			this.onConstruction.timer = timer;
			return this;
		}

		public TimeTickReceiver build() {
			Objects.requireNonNull(this.onConstruction.context);
			return this.onConstruction;
		}
	}
}
