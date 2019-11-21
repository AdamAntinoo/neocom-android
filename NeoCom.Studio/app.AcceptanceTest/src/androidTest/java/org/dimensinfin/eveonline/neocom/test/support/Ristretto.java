package org.dimensinfin.eveonline.neocom.test.support;

import android.view.View;
import androidx.test.espresso.remote.annotation.RemoteMsgConstructor;
import androidx.test.espresso.remote.annotation.RemoteMsgField;

import org.awaitility.Awaitility;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;


import static java.util.concurrent.TimeUnit.SECONDS;

public class Ristretto {
	public enum PanelTypes {
		NOT_SET, REGIONCONTROLLER, STATIONCONTROLLER
	}

	public static Matcher<View> withPanelType( final PanelTypes panelType ) {
		return new WithPanelTypeMatcher( panelType );
	}

	public static final class WithPanelTypeMatcher extends TypeSafeMatcher<View> {
		@RemoteMsgField(order = 0)
		private PanelTypes panelClass = PanelTypes.NOT_SET;

		@RemoteMsgConstructor
		private WithPanelTypeMatcher( final PanelTypes panelType ) {
			this.panelClass = panelType;
		}

		@Override
		protected boolean matchesSafely( final View item ) {
			final Object tag = item.getTag(); // Tag should exist and be a Controller
			if (null != tag)
				switch (this.panelClass) {
					case REGIONCONTROLLER:
						if (tag instanceof RegionController) return true;
						break;
					case STATIONCONTROLLER:
						if (tag instanceof StationController) return true;
						break;
				}
			return false;
		}

		@Override
		public void describeTo( final Description description ) {
			description.appendText( "with panelClass type: " + this.panelClass.name() );
		}
	}

	public static void waitForCompletion( final NeoComWorld world, final Runnable callback ) {
		AcceptanceNeoComLogger.enter();
		// Add the callback behind the latest scheduler run and ui run.
		final Monitor monitor = new Monitor();
		world.getActiveActivity().runOnUiThread( callback );
		world.getActiveActivity().runOnUiThread( monitor::activateTrigger );
		Awaitility.await().atMost( 5, SECONDS ).until( () -> monitor.isTriggered() );
		AcceptanceNeoComLogger.exit();
	}

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
