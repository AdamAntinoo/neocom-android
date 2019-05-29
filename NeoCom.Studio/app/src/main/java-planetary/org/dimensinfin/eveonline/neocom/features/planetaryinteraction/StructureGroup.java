package org.dimensinfin.eveonline.neocom.features.planetaryinteraction;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;

public class StructureGroup implements ICollaboration {
	private int groupId;
	private int iconReference;
	private String name;
	private int groupCount = 0;

	// - C O N S T R U C T O R
	public StructureGroup( final String name ) {
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
		final StructureGroup target = (StructureGroup) o;
		return this.getName().compareTo(target.getName());
	}

	// - B U I L D E R
	public static class Builder {
		private final StructureGroup object;

		public Builder( final String name ) {
			this.object = new StructureGroup(name);
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

		public StructureGroup build() {
			return this.object;
		}
	}
}
