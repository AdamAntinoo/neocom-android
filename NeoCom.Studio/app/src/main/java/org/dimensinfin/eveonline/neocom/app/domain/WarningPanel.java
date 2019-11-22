package org.dimensinfin.eveonline.neocom.app.domain;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.domain.NeoComNode;

public class WarningPanel extends NeoComNode {
	private String type;
	private String description;

	private WarningPanel() { }

	// - B U I L D E R
	public static class Builder {
		private WarningPanel onConstruction;

		public Builder() {
			this.onConstruction = new WarningPanel();
		}

		public Builder withType( final String type ) {
			Objects.requireNonNull( type );
			this.onConstruction.type = type;
			return this;
		}

		public Builder withDescription( final String description ) {
			Objects.requireNonNull( description );
			this.onConstruction.description = description;
			return this;
		}

		public WarningPanel build() {
			Objects.requireNonNull( this.onConstruction.description );
			return this.onConstruction;
		}
	}
}
