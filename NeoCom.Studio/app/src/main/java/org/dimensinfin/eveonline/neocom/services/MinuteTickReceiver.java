package org.dimensinfin.eveonline.neocom.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.joda.time.LocalTime;

import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.annotation.Singleton;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;

/**
 * This class is a one minute timer activated at startup. It will call upon the list of schedulers configured if the minute counter matches the
 * scheduler filtering time. For example if a scheduler used a 5 min utes beat then this timer activated each minute will only call that
 * scheduler every 5 calls.
 *
 * @author Adam Antinoo
 */
@Singleton
public class MinuteTickReceiver extends BroadcastReceiver {
	private static final String ALLOW_SCHEDULER_PREFERENCE_KEY = "prefkey_AllowScheduler";

	/**
	 * Receive an intent on every minute tick.
	 * On each tick check for the list of configured scheduler which of them should be fired. This checks each scheduler timer counter and if the
	 * scheduler is activated or not.
	 */
	@LogEnterExit
	@Override
	public void onReceive( final Context context, final Intent intent ) {
		NeoComLogger.enter( "{}:{}",
				String.format( Integer.toString( LocalTime.now().getHourOfDay() ), "%d02" ),
				String.format( Integer.toString( LocalTime.now().getMinuteOfHour() ), "%d02" ) );
		if (this.allowed2Run( context )) {
			JobScheduler.getJobScheduler().runSchedule();
		} else return;
		NeoComLogger.exit();
	}

	protected boolean allowed2Run( final Context context ) {
		return PreferenceManager.getDefaultSharedPreferences( context )
				       .getBoolean( ALLOW_SCHEDULER_PREFERENCE_KEY, true );
	}
}
