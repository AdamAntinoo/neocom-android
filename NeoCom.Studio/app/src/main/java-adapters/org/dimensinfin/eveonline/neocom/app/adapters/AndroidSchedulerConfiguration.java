package org.dimensinfin.eveonline.neocom.app.adapters;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.provider.IPreferencesProvider;
import org.dimensinfin.eveonline.neocom.service.scheduler.conf.ISchedulerConfiguration;

public class AndroidSchedulerConfiguration implements ISchedulerConfiguration {
	private static final String ALLOWED_TORUN_SETTING = "S.scheduler.allowedtorun";
	private static final String ALLOWED_ASSETS_SETTING = "S.scheduler.allowedassets";
	private static final String ALLOWED_MININGEXTRACTIONS_SETTING = "S.scheduler.allowedminingextractions";
	private IPreferencesProvider preferencesProvider;

	private AndroidSchedulerConfiguration() {}

	// - I S C H E D U L E R C O N F I G U R A T I O N
	@Override
	public Boolean getAllowedToRun() {
		return this.preferencesProvider.getBooleanPreference( ALLOWED_TORUN_SETTING );
	}

	@Override
	public Boolean getAllowedMiningExtractions() {
		return this.preferencesProvider.getBooleanPreference( ALLOWED_MININGEXTRACTIONS_SETTING );
	}

	@Override
	public Boolean getAllowedAssets() {
		return this.preferencesProvider.getBooleanPreference( ALLOWED_ASSETS_SETTING );
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidSchedulerConfiguration onConstruction;

		public Builder() {
			this.onConstruction = new AndroidSchedulerConfiguration();
		}

		public AndroidSchedulerConfiguration.Builder withPreferencesProvider( final IPreferencesProvider preferencesProvider ) {
			Objects.requireNonNull( preferencesProvider );
			this.onConstruction.preferencesProvider = preferencesProvider;
			return this;
		}

		public AndroidSchedulerConfiguration build() {
			Objects.requireNonNull( this.onConstruction.preferencesProvider );
			return this.onConstruction;
		}
	}
}
