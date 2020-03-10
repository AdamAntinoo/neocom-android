package org.dimensinfin.eveonline.neocom.app.activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.runner.RunWith;

import org.dimensinfin.android.mvc.activity.MVCFragmentPagerAdapter;
import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationService;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;

@RunWith(AndroidJUnit4.class)
public class AppSplashActivityTest {
	private IConfigurationService configurationProvider;
	private IFileSystem fileSystem;
// TODO - This test works but does not have any meaning. The view that I can see has an excetion and no content.
//	@Test
	public void applicationLifecycle() {
		try (ActivityScenario<AppSplashActivity> scenario = ActivityScenario.launch(AppSplashActivity.class)) {
			scenario.onActivity(( activity ) -> {
				NeoComApplicationTest.logger.info("ACCEPTANCE --> [AppSplashActivity]> Creating page...");
				final MVCFragmentPagerAdapter pageAdapter = activity.accessPageAdapter();
				Assert.assertEquals(1, pageAdapter.getCount()); // Check the number of pages
				final MVCPagerFragment fragment = (MVCPagerFragment) pageAdapter.getItem(0);
				Assert.assertEquals( PageNamesType.SPLASH.name(), fragment.getVariant()); // Check the page variant type
			});
		}
	}
}
