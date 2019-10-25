package org.dimensinfin.eveonline.neocom.app.ui;

import org.dimensinfin.core.interfaces.ICollaboration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */
public class MiningActionBar implements ICollaboration {
	private String titleTopLeft = "";
	private String titleTopRight = "";
	private String subtitle = "";
	private int activityIconReference = -1;
	private int pilotIdentifier = -1;

	private MiningActionBar() {}

	public String getTitleTopLeft() {
		return this.titleTopLeft;
	}

	public String getTitleTopRight() {
		return this.titleTopRight;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public int getActivityIconReference() {
		return this.activityIconReference;
	}

	public int getPilotIdentifier() {
		return this.pilotIdentifier;
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
		private MiningActionBar onConstruction;

		public Builder() {
			this.onConstruction = new MiningActionBar();
		}

		public Builder withTitleTopLeft( final String titleTopLeft ) {
			this.onConstruction.titleTopLeft = titleTopLeft;
			return this;
		}

		public Builder withTitleTopRight( final String titleTopRight ) {
			this.onConstruction.titleTopRight = titleTopRight;
			return this;
		}

		public Builder withSubtitle( final String subtitle ) {
			this.onConstruction.subtitle = subtitle;
			return this;
		}

		public Builder withActivityIconRef( final int activityIconReference ) {
			this.onConstruction.activityIconReference = activityIconReference;
			return this;
		}

		public Builder withPilotIdentifier( final int pilotIdentifier ) {
			this.onConstruction.pilotIdentifier = pilotIdentifier;
			return this;
		}

		public MiningActionBar build() {
			return this.onConstruction;
		}
	}
}
