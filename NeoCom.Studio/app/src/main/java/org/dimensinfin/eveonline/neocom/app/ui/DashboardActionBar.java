package org.dimensinfin.eveonline.neocom.app.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.core.interfaces.ICollaboration;

/**
 * @author Adam Antinoo
 */
public class DashboardActionBar implements ICollaboration {
	private String appName = "";
	private String appVersion = "";
	private String pilotName = "";

	private DashboardActionBar() {}

	public String getAppName() {
		return this.appName;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public String getPilotName() {
		return this.pilotName;
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
		private DashboardActionBar onConstruction;

		public Builder() {
			this.onConstruction = new DashboardActionBar();
		}

		public Builder withAppName( final String appName ) {
			Objects.requireNonNull( appName );
			this.onConstruction.appName = appName;
			return this;
		}

		public Builder withAppVersion( final String appVersion ) {
			Objects.requireNonNull( appVersion );
			this.onConstruction.appVersion = appVersion;
			return this;
		}

		public Builder withPilotName( final String pilotName ) {
			Objects.requireNonNull( pilotName );
			this.onConstruction.pilotName = pilotName;
			return this;
		}

		public DashboardActionBar build() {
			return this.onConstruction;
		}
	}
}
