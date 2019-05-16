package org.dimensinfin.eveonline.neocom.app.ui;

import java.util.List;
import java.util.Objects;

import android.content.SharedPreferences;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.sharedcode.provider.NeoComAndroid;

public class SplashActionBar implements ICollaboration {
	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";

	private SharedPreferences preferences;

	private SplashActionBar() {}

	public String getServerName() {
		return this.preferences.getString(DATASOURCE_PREFERENCE, NeoComAndroid.DEFAULT_ESI_SERVER);
	}

	public String getServerStatus() {
		return "active";
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return null;
	}

	@Override
	public int compareTo( final Object o ) {
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private SplashActionBar onConstruction;

		public Builder() {
			this.onConstruction = new SplashActionBar();
		}

		public Builder withPreferences( final SharedPreferences preferences ) {
			this.onConstruction.preferences = preferences;
			return this;
		}
		//		public Builder withEsiAdapter( final SharedPreferences preferences ) {
		//			this.onConstruction.preferences = preferences;
		//			return this;
		//		}

		public SplashActionBar build() {
			Objects.requireNonNull(this.onConstruction.preferences);
			// Access the server to get the status and the number of pilots.

			return this.onConstruction;
		}
	}
}
