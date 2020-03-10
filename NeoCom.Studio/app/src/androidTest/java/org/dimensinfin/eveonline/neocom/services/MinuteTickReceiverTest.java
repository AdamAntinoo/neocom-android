package org.dimensinfin.eveonline.neocom.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.service.scheduler.domain.Job;
import org.dimensinfin.eveonline.neocom.ui.PreferenceKeys;


@RunWith(AndroidJUnit4.class)
public class MinuteTickReceiverTest {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private MinuteTickReceiver receiver4Test;
	private Context context;
	private Sensor sensorSpy;

	// Executes each task synchronously using Architecture Components.
	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	@Before
	public void setUp() throws Exception {
		this.receiver4Test = new MinuteTickReceiver();
		this.context = InstrumentationRegistry.getInstrumentation().getContext();
		final Sensor sensor = new Sensor();
		this.sensorSpy = Mockito.spy( sensor );
		JobScheduler.getJobScheduler().clear();
		JobScheduler.getJobScheduler().registerJob( new Job() {
			@Override
			public int getUniqueIdentifier() {
				return 0;
			}

			@Override
			public Boolean call() throws Exception {
				return sensorSpy.signalCallExecuted();
			}
		} );
		Assert.assertEquals( 1, JobScheduler.getJobScheduler().getJobCount() );
//		Assert.assertTrue( JobScheduler.getJobScheduler().needsStorage() );
	}

	@Test
	public void onReceiveSuccess() {
		// Prepare preferences
		final SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences( context ).edit();
		preferencesEditor.putBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), true );
		preferencesEditor.commit();
		Intent intent = new Intent( Intent.ACTION_TIME_TICK );
		this.receiver4Test.onReceive( context, intent );

		AsyncTask.execute( () -> {
			final boolean block = PreferenceManager.getDefaultSharedPreferences( context )
					                      .getBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), true );
			Mockito.verify( sensorSpy, Mockito.times( 1 ) ).signalCallExecuted();
		} );
	}

//	@Test
	public void onReceiveBlocked() {
		// Prepare preferences
		final SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences( context ).edit();
		preferencesEditor.putBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), false );
		preferencesEditor.commit();
		Intent intent = new Intent( Intent.ACTION_TIME_TICK );
		this.receiver4Test.onReceive( context, intent );

		AsyncTask.execute( () -> {
			final boolean block = PreferenceManager.getDefaultSharedPreferences( context )
					                      .getBoolean( PreferenceKeys.prefkey_AllowScheduler.name(), true );
			NeoComLogger.info( "Current block state: {}", block + "" );
			Mockito.verify( sensorSpy, Mockito.times( 1 ) ).signalCallExecuted();
		} );
	}

	public static class Sensor {
		public boolean signalCallExecuted() {
			return true;
		}
	}
}
