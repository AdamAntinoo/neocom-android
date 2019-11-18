package org.dimensinfin.eveonline.neocom.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.joda.time.LocalTime;

import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.annotation.Singleton;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.ui.PreferenceKeys;

/**
 * This class is a one minute timer activated at startup. It will call upon the list of schedulers configured if the minute counter matches the
 * scheduler filtering time. For example if a scheduler used a 5 min utes beat then this timer activated each minute will only call that
 * scheduler every 5 calls.
 *
 * @author Adam Antinoo
 */
@Singleton
public class MinuteTickReceiver extends BroadcastReceiver {
//	private SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences();
//	private Context context;

	// - C O N S T R U C T O R S
//	public MinuteTickReceiver( final Context context ) {
//		this.context = context;
//	}

	/**
	 * Receive an intent on every minute tick.
	 * On each tick check for the list of configured scheduler which of them should be fired. This checks each scheduler timer counter and if the
	 * scheduler is activated or not.
	 */
	@LogEnterExit
	@Override
	public void onReceive( final Context context, final Intent intent ) {
		NeoComLogger.enter( "{}:{}", LocalTime.now().getHourOfDay() + "", LocalTime.now().getMinuteOfHour() + "" );
		if (this.allowed2Run( context )) {
			if (JobScheduler.getJobScheduler().needsNetwork())
				if (!NeoComApplication.checkNetworkAccess()) return;
			if (JobScheduler.getJobScheduler().needsStorage())
				if (!NeoComApplication.checkSDCardAvailability()) return;
		}
		JobScheduler.getJobScheduler().runSchedule();
		NeoComLogger.exit();
	}

	protected boolean allowed2Run( final Context context ) {
		return PreferenceManager.getDefaultSharedPreferences( context )
				       .getBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), true );
	}

	// - B U I L D E R
//	public static class Builder {
//		private MinuteTickReceiver onConstruction;
//
//		public Builder( final Context context ) {
//			this.onConstruction = new MinuteTickReceiver( context );
////			this.onConstruction.preferences = PreferenceManager.getDefaultSharedPreferences(
////					NeoComComponentFactory.getSingleton().getApplication()
////			);
//		}
//
//		public MinuteTickReceiver build() {
////			Objects.requireNonNull(this.onConstruction.context);
//			//			Objects.requireNonNull(this.onConstruction.configurationProvider);
//			this.onConstruction.preferences = PreferenceManager.getDefaultSharedPreferences(
//					NeoComComponentFactory.getSingleton().getApplication()
//			);
//			return this.onConstruction;
//		}
//	}
}
