package org.dimensinfin.eveonline.neocom.app.activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.dimensinfin.android.mvc.activity.MVCFragmentPagerAdapter;
import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.eveonline.neocom.adapters.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.adapters.IFileSystem;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;

@RunWith(AndroidJUnit4.class)
public class AppSplashActivityTest {
	private IConfigurationProvider configurationProvider;
	private IFileSystem fileSystem;

	@Test
	public void applicationLifecycle() {
		try (ActivityScenario<AppSplashActivity> scenario = ActivityScenario.launch(AppSplashActivity.class)) {
			scenario.onActivity(( activity ) -> {
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [AppSplashActivity]> Creating page...");
				final MVCFragmentPagerAdapter pageAdapter = activity.accessPageAdapter();
				Assert.assertEquals(1, pageAdapter.getCount()); // Check the number of pages
				final MVCPagerFragment fragment = (MVCPagerFragment) pageAdapter.getItem(0);
				Assert.assertEquals(PageNamesType.SPLASH.name(), fragment.getVariant()); // Check the page variant type
			});
		}
	}
}