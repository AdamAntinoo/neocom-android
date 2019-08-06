package org.dimensinfin.eveonline.neocom.visual.tests.ui;

import org.dimensinfin.core.interfaces.ICollaboration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisualTestActionBar implements ICollaboration {
	private String pageName;
	private int activityIconReference;

	private VisualTestActionBar() {}

	public String getPageName() {
		return this.pageName;
	}

	public int getActivityIconReference() {
		return activityIconReference;
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
		private VisualTestActionBar onConstruction;

		public Builder() {
			this.onConstruction = new VisualTestActionBar();
		}

		public Builder withPageName( final String pageName ) {
			this.onConstruction.pageName = pageName;
			return this;
		}

		public Builder withActivityIconReference( final int activityIconReference ) {
			this.onConstruction.activityIconReference = activityIconReference;
			return this;
		}

		public VisualTestActionBar build() {
			Objects.requireNonNull(this.onConstruction.pageName);
			return this.onConstruction;
		}
	}
}
