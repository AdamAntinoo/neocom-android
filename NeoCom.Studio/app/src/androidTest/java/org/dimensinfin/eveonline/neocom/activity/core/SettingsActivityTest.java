package org.dimensinfin.eveonline.neocom.activity.core;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SettingsActivityTest {

	@Rule
	public ActivityTestRule<SettingsActivity> mActivityRule = new ActivityTestRule<>(SettingsActivity.class);

	@Test
	public void readBooleanSetting() {
		final SettingsActivity context = mActivityRule.getActivity();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final boolean obtained = preferences.getBoolean("prefkey_BlockDownloads", true);

		assertFalse(obtained);
	}
}
