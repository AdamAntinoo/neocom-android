package org.dimensinfin.eveonline.neocom.app.domain;

import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import java.util.Objects;

public class PilotSection extends NeoComNode {
	private int iconReference;
	private String title;
	private String label;
	private Integer pilotIdentifier;
	private IDataProvider dataProvider;
	private PageNamesType clickTarget;

	// - G E T T E R S   &   S E T T E R S
	public int getIconReference() {
		return this.iconReference;
	}

	public String getTitle() {
		return this.title;
	}

	public String getLabel() {
		return this.label;
	}

	public Integer getPilotIdentifier() {
		return this.pilotIdentifier;
	}

	public String getDataFormatted() {return this.dataProvider.getDataFormatted();}

	public PageNamesType getClickTarget() {
		return this.clickTarget;
	}

	// - B U I L D E R
	public static class Builder {
		private PilotSection onConstruction;

		public Builder() {
			this.onConstruction = new PilotSection();
		}

		public Builder withSectionIcon( final int iconReference ) {
			this.onConstruction.iconReference = iconReference;
			return this;
		}

		public Builder withTitle( final String title ) {
			this.onConstruction.title = title;
			return this;
		}

		public Builder withLabel( final String label ) {
			this.onConstruction.label = label;
			return this;
		}

		public Builder withPilotIdentifier( final Integer pilotIdentifier ) {
			Objects.requireNonNull(pilotIdentifier);
			this.onConstruction.pilotIdentifier = pilotIdentifier;
			return this;
		}

		public Builder withDataProvider( final IDataProvider dataProvider ) {
			this.onConstruction.dataProvider = dataProvider;
			return this;
		}

		public Builder withClickTarget( final PageNamesType clickTarget ) {
			this.onConstruction.clickTarget = clickTarget;
			return this;
		}

		public PilotSection build() {
			Objects.requireNonNull(this.onConstruction.pilotIdentifier);
			Objects.requireNonNull(this.onConstruction.dataProvider);
			return this.onConstruction;
		}
	}
}
