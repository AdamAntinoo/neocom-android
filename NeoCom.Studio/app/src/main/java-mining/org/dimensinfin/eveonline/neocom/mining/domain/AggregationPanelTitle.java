package org.dimensinfin.eveonline.neocom.mining.domain;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.domain.NeoComNode;

public class AggregationPanelTitle extends NeoComNode {
	private String title;
	private double totalVolume = 0.0;
	private double totalValue=0.0;

	private AggregationPanelTitle() {}

	public String getTitle() {
		return this.title;
	}

	public double getTotalVolume() {
		return this.totalVolume;
	}

	public double getTotalValue() {
		return this.totalValue;
	}

	// - B U I L D E R
	public static class Builder {
		private AggregationPanelTitle onConstruction;

		public Builder() {
			this.onConstruction = new AggregationPanelTitle();
		}

		public AggregationPanelTitle.Builder withTitle( final String title ) {
			Objects.requireNonNull( title );
			this.onConstruction.title = title;
			return this;
		}

		public AggregationPanelTitle.Builder withVolume( final Double volume ) {
			if (null == volume) this.onConstruction.totalVolume = 0.0;
			else this.onConstruction.totalVolume = volume;
			return this;
		}
		public AggregationPanelTitle.Builder withValue( final Double value ) {
			if (null == value) this.onConstruction.totalValue = 0.0;
			else this.onConstruction.totalValue = value;
			return this;
		}

		public AggregationPanelTitle build() {
			Objects.requireNonNull( this.onConstruction.title );
			return this.onConstruction;
		}
	}
}