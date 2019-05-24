package org.dimensinfin.eveonline.neocom.app.domain;

public class WarningPanel extends PanelTitle {
	private String type;

	public WarningPanel( final String title ) {
		super(title);
	}

	// - B U I L D E R
	public static class Builder {
		private WarningPanel onConstruction;

		public Builder() {
			this.onConstruction = new WarningPanel("");
		}

		public Builder withType( final String type ) {
			this.onConstruction.type = type;
			return this;

		}
		public Builder withDescription( final String description ) {
			this.onConstruction.setTitle(description);
			return this;

		}

		public WarningPanel build() {
			return this.onConstruction;
		}
	}
}
