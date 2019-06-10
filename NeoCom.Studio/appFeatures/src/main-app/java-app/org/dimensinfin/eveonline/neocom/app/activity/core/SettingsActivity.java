package org.dimensinfin.eveonline.neocom.app.activity.core;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.dimensinfin.eveonline.neocom.R;

//public class SettingsActivity extends PreferenceActivity {
//	public void onCreate(final Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		addPreferencesFromResource(R.xml.preferences);
//	}
//}
public class SettingsActivity extends Activity {
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment())
				.commit();
	}

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate( Bundle savedInstanceState ) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}
