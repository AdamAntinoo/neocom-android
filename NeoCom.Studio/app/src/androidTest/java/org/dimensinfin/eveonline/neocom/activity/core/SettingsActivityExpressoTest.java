package org.dimensinfin.eveonline.neocom.activity.core;

import android.preference.Preference;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class SettingsActivityExpressoTest {

	@Rule
	public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(SettingsActivity.class);

	//	@Test
	//	public void onCreate() {
	//		final boolean scenario = launchActivity<SettingsActivity>();
	//		onView(withId(R.id.finish_button)).perform(click())
	//
	//		onView(withId(R.id.name_field)).perform(typeText("Steve"));
	//		onView(withId(R.id.greet_button)).perform(click());
	//		onView(withText("Hello Steve!")).check(matches(isDisplayed()));
	//	}

	//	@Test
	public void onCreate_add() {
		onData(allOf(
				is(instanceOf(Preference.class)),
				withId(R.id.DevelopmentBlockDownloads)))
				.check(matches(isCompletelyDisplayed()));
	}
}
