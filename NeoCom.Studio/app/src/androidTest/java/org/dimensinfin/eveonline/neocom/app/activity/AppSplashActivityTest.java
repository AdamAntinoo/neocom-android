package org.dimensinfin.eveonline.neocom.app.activity;

import android.view.View;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.activity.core.ANeoComActivity;
import org.dimensinfin.eveonline.neocom.app.controller.SplashActionBarController;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AppSplashActivityTest {
	//	private AppSplashActivity mActivityRule;

	@Rule
	public ActivityTestRule<AppSplashActivity> mActivityRule = new ActivityTestRule<>(AppSplashActivity.class);

	@Test
	public void onCreate() throws Exception {
		final ANeoComActivity parent = Mockito.mock(ANeoComActivity.class);
		final SplashActionBar splashActionbar = Mockito.mock(SplashActionBar.class);
		final SplashActionBarController splashActionBarController = Mockito.mock(SplashActionBarController.class);
		final IRender holder = Mockito.mock(SplashActionBarController.SplashActionBarRender.class);
		final View splashActionbarView = Mockito.mock(View.class);
		final AppSplashFragment fragment = Mockito.mock(AppSplashFragment.class);
//		PowerMockito.whenNew(AppSplashFragment.class).withNoArguments().thenReturn(fragment);
		Mockito.when(splashActionBarController.buildRender(mActivityRule.getActivity())).thenReturn(holder);
		Mockito.doAnswer(( call ) -> {
			return null;
		}).when(holder).updateContent();
	}

	@Test
	public void onStart() {
	}
}
