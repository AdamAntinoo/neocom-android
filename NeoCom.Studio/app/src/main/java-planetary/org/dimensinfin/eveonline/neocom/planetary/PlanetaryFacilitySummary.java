package org.dimensinfin.eveonline.neocom.planetary;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;

/**
 * This class stores the list of facility dta that should be used on the colony header to show an abstract of the facility contents. This is a summary
 * class similar to an aggregator.
 */
public class PlanetaryFacilitySummary implements ICollaboration {
	private int groupId;
	private int iconReference;
	private String name;
	private int groupCount = 0;

	// - C O N S T R U C T O R
	public PlanetaryFacilitySummary( final String name ) {
		this.name = name;
	}

	// - G E T T E R S   &   S E T T E R S
	public String getName() {
		return name;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public int getIconReference() {
		return iconReference;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variation ) {
		return new ArrayList<>();
	}

//	@Override
	public int compareTo( final Object o ) {
		final PlanetaryFacilitySummary target = (PlanetaryFacilitySummary) o;
		return this.getName().compareTo(target.getName());
	}

	// - B U I L D E R
	public static class Builder {
		private final PlanetaryFacilitySummary object;

		public Builder( final String name ) {
			this.object = new PlanetaryFacilitySummary(name);
		}

		public Builder withGroupId( final int groupId ) {
			this.object.groupId = groupId;
			return this;
		}

		public Builder withIconReference( final int iconReference ) {
			this.object.iconReference = iconReference;
			return this;
		}

		public Builder withName( final String name ) {
			this.object.name = name;
			return this;
		}

		public Builder withGroupCount( final int groupCount ) {
			this.object.groupCount = groupCount;
			return this;
		}

		public PlanetaryFacilitySummary build() {
			return this.object;
		}
	}
}
