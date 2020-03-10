package org.dimensinfin.eveonline.neocom.app.adapters;

import org.dimensinfin.eveonline.neocom.provider.IPreferencesProvider;
import org.dimensinfin.eveonline.neocom.scheduler.ISchedulerConfiguration;

public class AndroidSchedulerConfiguration implements ISchedulerConfiguration {
	private static final String ALLOWED_TORUN_SETTING = "S.scheduler.allowedtorun";
	private static final String ALLOWED_MININGEXTRACTIONS_SETTING = "S.scheduler.allowedminingextractions";
	private IPreferencesProvider preferencesProvider;

	private AndroidSchedulerConfiguration() {}

	public Boolean getAllowedToRun() {
		return this.preferencesProvider.getBooleanPreference( ALLOWED_TORUN_SETTING );
	}

	public Boolean getAllowedMiningExtractions() {
		return this.preferencesProvider.getBooleanPreference( ALLOWED_MININGEXTRACTIONS_SETTING );
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidSchedulerConfiguration onConstruction;

		public Builder() {
			this.onConstruction = new AndroidSchedulerConfiguration();
		}

		public AndroidSchedulerConfiguration.Builder withPreferencesProvider( final IPreferencesProvider preferencesProvider ) {
			this.onConstruction.preferencesProvider = preferencesProvider;
			return this;
		}

		public AndroidSchedulerConfiguration build() {
			return this.onConstruction;
		}
	}
}
