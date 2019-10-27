package org.dimensinfin.eveonline.neocom.app.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

public class AppVersion extends NeoComNode {
	private String appName = "NeoCom";
	private String appVersion = "-";

	// - C O N S T R U C T O R S
	private AppVersion() {
		super();
		logger.info("-- [AppVersion.<constructor>]> Node class: {}", this.getClass().getSimpleName());
	}

	// - G E T T E R S   &   S E T T E R S
	public String getAppName() {
		return appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public AppVersion setAppName( final String appName ) {
		this.appName = appName;
		return this;
	}

	public AppVersion setAppVersion( final String appVersion ) {
		this.appVersion = appVersion;
		return this;
	}

	// - I C O L L A B O R A T I O N   I N T E R F A C E
//	@Override
//	public List<ICollaboration> collaborate2Model( final String variation ) {
//		return new ArrayList<>();
//	}

	// - C O R E

	@Override
	public boolean equals( final Object o ) {
		if (this == o) return true;
		if (!(o instanceof AppVersion)) return false;
		final AppVersion that = (AppVersion) o;
		return new EqualsBuilder()
				       .append(this.appName, that.appName)
				       .append(this.appVersion, that.appVersion)
				       .isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(this.appName)
				       .append(this.appVersion)
				       .toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				       .append("name", appName)
				       .append("version", appVersion)
				       .toString();
	}

	@Override
	public int compareTo( final Object o ) {
		if (o instanceof AppVersion) {
			final AppVersion target = (AppVersion) o;
			return this.getAppName().compareTo(target.getAppName());
		} else return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private AppVersion onConstruction;

		public Builder() {
			this.onConstruction = new AppVersion();
		}

		public AppVersion.Builder withAppName( final String appName ) {
			this.onConstruction.appName = appName;
			return this;
		}

		public AppVersion.Builder withAppVersion( final String appVersion ) {
			this.onConstruction.appVersion = appVersion;
			return this;
		}

		public AppVersion build() {
			return this.onConstruction;
		}
	}
}
