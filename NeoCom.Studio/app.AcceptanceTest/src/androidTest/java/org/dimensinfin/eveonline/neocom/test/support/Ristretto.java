package org.dimensinfin.eveonline.neocom.test.support;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

import org.awaitility.Awaitility;

import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.eveonline.neocom.app.activity.core.NeoComActivity;


import static java.util.concurrent.TimeUnit.SECONDS;

public class Ristretto {
	private static final int WAIT_TIMEOUT = 70;

	// - W O R L D
	private static NeoComWorld world;

	public static void setWorld( final NeoComWorld newworld ) {
		world = newworld;
	}

	// - F I L T E R I N G

	// - A C T I V I T Y   M A N A G E M E N T
	public static int activityPageCount() {
		return ((MVCMultiPageActivity) world.getActiveActivity()).accessPageAdapter().getCount();
	}

	public static List<IAndroidController> accessHeaderPanels( final int page ) {
		final Fragment fragment = Objects.requireNonNull(
				((MVCMultiPageActivity) world.getActiveActivity()).accessPageAdapter().getItem( page )
		);
		return ((MVCPagerFragment) fragment).accessHeaderContents();
	}

	public static List<IAndroidController> accessDataPanels( final int page ) {
		final Fragment fragment = Objects.requireNonNull(
				((MVCMultiPageActivity) world.getActiveActivity()).accessPageAdapter().getItem( page )
		);
		return ((MVCPagerFragment) fragment).accessDataContents();
	}

	public static int headerContentsCount( final int page ) {
		return Ristretto.accessHeaderPanels( page ).size();
	}

	public static int dataContentsCount( final int page ) {
		final Fragment fragment = Objects.requireNonNull(
				((MVCMultiPageActivity) world.getActiveActivity()).accessPageAdapter().getItem( page )
		);
		return ((MVCPagerFragment) fragment).accessDataContents().size();
	}

	// - S Y N C H R O N I Z A T I O N
	public static void waitForCompletion( final Runnable callback ) {
		AcceptanceNeoComLogger.enter();
		// Add the callback behind the latest scheduler run and ui run.
		final Monitor monitor = new Monitor();
		world.getActiveActivity().runOnUiThread( callback );
		world.getActiveActivity().runOnUiThread( monitor::activateTrigger );
		Awaitility.await().atMost( WAIT_TIMEOUT, SECONDS ).until( () -> monitor.isTriggered() );
		AcceptanceNeoComLogger.exit();
	}

	public static void waitForBackground( final Runnable callback ) {
		AcceptanceNeoComLogger.enter();
		// Add the callback behind the latest scheduler run and ui run.
		final Monitor monitor = new Monitor();
		MVCScheduler.backgroundExecutor.submit( () -> callback );
		MVCScheduler.backgroundExecutor.submit( monitor::activateTrigger );
		Awaitility.await().atMost( WAIT_TIMEOUT, SECONDS ).until( () -> monitor.isTriggered() );
		AcceptanceNeoComLogger.exit();
	}

	@Deprecated
	public static void waitForCompletion( final NeoComWorld newworld, final Runnable callback ) {
		world = newworld;
		waitForCompletion( callback );
	}

	public static void updateDisplay() {
		final NeoComActivity activity = (NeoComActivity) world.getActiveActivity();
		final MVCPagerFragment fragment = (MVCPagerFragment) activity.accessPageAdapter().getItem( 0 );
		fragment.updateDisplay();
	}

	// - M O N I T O R
	private static final class Monitor {
		private boolean trigger = false;

		public void activateTrigger() {
			AcceptanceNeoComLogger.enter();
			this.trigger = true;
		}

		public Boolean isTriggered() {
			return this.trigger;
		}
	}
}
