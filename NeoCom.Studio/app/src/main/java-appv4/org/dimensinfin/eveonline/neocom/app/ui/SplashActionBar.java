package org.dimensinfin.eveonline.neocom.app.ui;

import android.content.SharedPreferences;

import org.dimensinfin.core.interfaces.ICollaboration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SplashActionBar implements ICollaboration {
	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
	private static final String DEFAULT_ESI_SERVER = "tranquility".toUpperCase();

	private SharedPreferences preferences;

	private SplashActionBar() {}

	public String getServerName() {
		return this.preferences.getString(DATASOURCE_PREFERENCE, DEFAULT_ESI_SERVER);
	}

	public String getServerStatus() {
		return "active".toUpperCase();
	}

	// - I C O L L A B O R A T I O N
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
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

		public SplashActionBar build() {
			Objects.requireNonNull(this.onConstruction.preferences);
			return this.onConstruction;
		}
	}
}
