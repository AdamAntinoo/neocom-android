package org.dimensinfin.eveonline.neocom.app.activity;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;


import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NeoComApplicationTest {
	public static final Logger logger = LoggerFactory.getLogger(NeoComApplicationTest.class);

	@Test
	public void contextName() {
		Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		assertEquals("org.dimensinfin.eveonline.neocom", appContext.getPackageName());
	}

	@Test
	public void applicationCreated() {
		final NeoComApplication application = NeoComComponentFactory.getSingleton().getApplication();
		Assert.assertNotNull(application);
	}

	@Test
	public void checkNetworkAccess() {
		Assert.assertTrue(NeoComApplication.checkNetworkAccess());
	}
}