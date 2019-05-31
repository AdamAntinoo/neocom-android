package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.graphics.Canvas;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColonyLayoutActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FacilityImageViewTest {
	@Rule
	public ActivityTestRule<PlanetaryColonyLayoutActivity> mActivityRule = new ActivityTestRule<>(PlanetaryColonyLayoutActivity.class);

	/**
	 * Check that the center is properly calculated and that the android drawing calls have the right values.
	 */
	@Test
	public void centerCanvas() {
		final Canvas canvas = Mockito.mock(Canvas.class);
		final ArgumentCaptor<Float> savedCaptor = ArgumentCaptor.forClass(Float.class);
		Mockito.verify(canvas).translate(savedCaptor.capture(), savedCaptor.capture());

//		assertTrue(savedCaptor.getValue().contains("substring I want to find");


	}
}
