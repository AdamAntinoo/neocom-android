package org.dimensinfin.eveonline.neocom.app.ui;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;

/**
 * @author Adam Antinoo
 */
public class NeoComActionBarV2 implements ICollaboration {
	private String titleTopLeft = "";
	private String titleTopRight = "";
	private String subtitle = "";

	private NeoComActionBarV2() {}

	public String getTitleTopLeft() {
		return titleTopLeft;
	}

	public String getTitleTopRight() {
		return titleTopRight;
	}

	public String getSubtitle() {
		return subtitle;
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
		private NeoComActionBarV2 onConstruction;

		public Builder() {
			this.onConstruction = new NeoComActionBarV2();
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

		public NeoComActionBarV2 build() {
			return this.onConstruction;
		}
	}
}
