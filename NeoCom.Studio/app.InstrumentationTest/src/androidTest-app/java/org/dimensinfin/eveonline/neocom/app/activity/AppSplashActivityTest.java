package org.dimensinfin.eveonline.neocom.app.activity;

import android.view.View;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.R;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AppSplashActivityTest {
	static {
		System.setProperty("org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath());
	}

	@Rule
	public ActivityTestRule<AppSplashActivity> mActivityRule = new ActivityTestRule<>(AppSplashActivity.class);
	@Rule
	public GrantPermissionRule mPermissionExternalStorageRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	@Rule
	public GrantPermissionRule mPermissionInternetRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET);
	@Rule
	public GrantPermissionRule mPermissionAccessNetworkStateRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE);

	//	@Test
	public void onCreate() {

		Espresso.onView(ViewMatchers.withId(R.id.pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
	}

	//	@Test
	public void injectComponents() {
		//		final Application application = Mockito.mock(Application.class);
		//		NeoComComponentFactory.initialiseSingleton(application);
		mActivityRule.getActivity().injectComponents();
		Assert.assertNotNull("Check the injected components are not null.", mActivityRule.getActivity().configurationProvider);
	}

	//	@Test
//	public void generateActionBar() {
//		final View actionBar = mActivityRule.getActivity().generateActionBar();
//		Assert.assertNotNull("Action bar view should not be null.", actionBar);
//	}

	@Test
	public void createAppDir() {
		Assert.assertTrue("The application local directory should exist.", this.mActivityRule.getActivity().createAppDir());
	}

	@Test
	public void createCacheDirectories() {
		Assert.assertTrue("The application cache directory should exist.", this.mActivityRule.getActivity().createCacheDirectories());
	}
}
