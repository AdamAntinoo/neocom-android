package org.dimensinfin.eveonline.neocom.model;

import java.util.ArrayList;
import java.util.List;


import org.dimensinfin.core.interfaces.ICollaboration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */
public class AppVersion extends NeoComNode {
	// - S T A T I C - S E C T I O N
	private static Logger logger = LoggerFactory.getLogger(AppVersion.class);

	// - F I E L D - S E C T I O N
	private String name = "NeoCom";
	private String version = "-";

	// - C O N S T R U C T O R - S E C T I O N
	public AppVersion() {
		super();
		logger.info("-- [AppVersion.<constructor>]> Node class: {}", this.getClass().getSimpleName());
	}

	// - G E T T E R S   &   S E T T E R S
	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public AppVersion setName( final String name ) {
		this.name = name;
		return this;
	}

	public AppVersion setVersion( final String version ) {
		this.version = version;
		return this;
	}

	// - I C O L L A B O R A T I O N   I N T E R F A C E
	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

	// - C O R E

	@Override
	public boolean equals( final Object o ) {
		if (this == o) return true;
		if (!(o instanceof AppVersion)) return false;
		final AppVersion that = (AppVersion) o;
		return new EqualsBuilder()
				       .append(name, that.name)
				       .append(version, that.version)
				       .isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				       .append(name)
				       .append(version)
				       .toHashCode();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AppVersion [ ");
		buffer.append("name: ").append(this.name).append(" ");
		buffer.append("version: ").append(this.version).append(" ");
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	@Override
	public int compareTo( final Object o ) {
		if (o instanceof AppVersion) {
			final AppVersion target = (AppVersion) o;
			return this.getName().compareTo(target.getName());
		} else return 0;
	}
}
