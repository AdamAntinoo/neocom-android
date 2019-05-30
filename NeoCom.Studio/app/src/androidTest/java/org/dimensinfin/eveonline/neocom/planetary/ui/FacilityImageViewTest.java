package org.dimensinfin.eveonline.neocom.planetary.ui;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.dimensinfin.eveonline.neocom.app.activity.core.SettingsActivity;
import org.dimensinfin.eveonline.neocom.planetary.activity.PlanetaryColonyLayoutActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;

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

		ArgumentCaptor<String> savedCaptor = ArgumentCaptor.forClass(String.class);
		verify(mock_contractsDao).save(savedCaptor.capture());
		assertTrue(savedCaptor.getValue().contains("substring I want to find");


	}
}
