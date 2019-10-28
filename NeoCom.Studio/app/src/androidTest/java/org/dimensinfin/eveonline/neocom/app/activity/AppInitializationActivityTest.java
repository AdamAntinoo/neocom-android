package org.dimensinfin.eveonline.neocom.app.activity;

import android.content.Context;
import android.widget.LinearLayout;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.R;


import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AppInitializationActivityTest {
	protected static Logger logger = LoggerFactory.getLogger(AppInitializationActivityTest.class);

	@Test
	public void contextName() {
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		assertEquals("org.dimensinfin.eveonline.neocom", appContext.getPackageName());
	}

	@Test
	public void applicationLifecycle() {
		try (ActivityScenario<AppInitializationActivity> scenario = ActivityScenario.launch(AppInitializationActivity.class)) {
//			logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Moving to state CREATED");
//			scenario.moveToState(Lifecycle.State.CREATED);
			scenario.onActivity(( activity ) -> {
//				final AppInitializationActivity activitySpy = Mockito.spy(activity);
				logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Entering onActivity");
				final LinearLayout layout = activity.findViewById(R.id.initializationLayout);
				logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Checking asserts");
				Assert.assertNotNull(layout);
				logger.info("ACCEPTANCE --> [we_have_an_empty_view_layout]> Waiting to complete the page...");
				try {
					Thread.sleep(TimeUnit.SECONDS.toMillis(5));
				} catch (InterruptedException e) { }
				Assert.assertTrue(layout.getChildCount() > 0);
//				Mockito.verify(activitySpy,Mockito.times(1)).launchInitialization();
			});
		}
	}
}