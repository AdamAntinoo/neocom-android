package org.dimensinfin.eveonline.neocom.app.adapters;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.dimensinfin.eveonline.neocom.provider.IPreferencesProvider;

/**
 * This class is specific for the Data Management and the pure java instances. It will replicate the Preferences interface
 * found in Android so the code should be compatible with the Preferences Manager implemented in Android platform.
 *
 * The java and Spring Boot implementation for the preferences will export all the data found at the Properties so it would not
 * need another data source for exporting the information.
 *
 * @author Adam Antinoo
 */
public class AndroidPreferencesProvider implements IPreferencesProvider {
	private Application application;

	public String getStringPreference( final String preferenceName, final String defaultValue ) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this.application );
		return settings.getString( preferenceName, defaultValue );
	}

	public boolean getBooleanPreference( final String preferenceName ) {
		return this.getBooleanPreference( preferenceName, false );
	}

	public boolean getBooleanPreference( final String preferenceName, final boolean defaultValue ) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( this.application );
		return preferences.getBoolean( preferenceName, defaultValue );
	}

	public float getFloatPreference( final String preferenceName, final float defaultValue ) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( this.application );
		return preferences.getFloat( preferenceName, defaultValue );
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidPreferencesProvider onConstruction;

		public Builder() {
			this.onConstruction = new AndroidPreferencesProvider();
		}

		public Builder withApplication( final Application application ) {
			this.onConstruction.application = application;
			return this;
		}

		public AndroidPreferencesProvider build() {
			return this.onConstruction;
		}
	}
}
