package org.dimensinfin.eveonline.neocom.app.activity.core;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.mining.activity.MiningLedgerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MiningExtractionRenderTest {
//	private static final String stringToBeTyped = "Espresso";

	@Rule
	public ActivityTestRule<MiningLedgerActivity> activityRule = new ActivityTestRule<>(MiningLedgerActivity.class);

	@Test
	public void changeText_sameActivity() {
		// Type text and then press the button.
		onView(withId(R.id.extractionHour)).perform(typeText("01"));
		//		onView(withId(R.id.changeTextBt)).perform(click());
		// Check that the text was changed.
		onView(withId(R.id.extractionHour)).check(matches(withText("01")));
	}
}
