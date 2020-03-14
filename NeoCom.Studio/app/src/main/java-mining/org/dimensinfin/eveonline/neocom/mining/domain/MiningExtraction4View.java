package org.dimensinfin.eveonline.neocom.mining.domain;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.dimensinfin.eveonline.neocom.annotation.RequiresNetwork;
import org.dimensinfin.eveonline.neocom.domain.NeoComNode;
import org.dimensinfin.eveonline.neocom.domain.NeoItem;

public class MiningExtraction4View extends NeoComNode {
	private static final long serialVersionUID = 4205329340460518327L;

	private long quantity;
	private long delta;
	private int extractionHour;
//	private double extractionValue = 0.0;
	private transient NeoItem item;

	private MiningExtraction4View() {}

	public int getTypeId() {return this.item.getTypeId();}

	@RequiresNetwork
	public String getName() {return this.item.getName();}

	@RequiresNetwork
	public double getPrice() {return this.item.getPrice();}

	public long getDelta() {
		return this.delta;
	}

	public long getQuantity() {
		return this.quantity;
	}

	public int getExtractionHour() {
		return this.extractionHour;
	}

	// - C O R E
	@Override
	public boolean equals( final Object o ) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final MiningExtraction4View that = (MiningExtraction4View) o;
		return new EqualsBuilder()
				       .appendSuper( super.equals( o ) )
				       .append( this.quantity, that.quantity )
				       .append( this.delta, that.delta )
				       .append( this.extractionHour, that.extractionHour )
				       .append( this.item, that.item )
				       .isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder( 17, 37 )
				       .appendSuper( super.hashCode() )
				       .append( this.quantity )
				       .append( this.delta )
				       .append( this.extractionHour )
				       .append( this.item )
				       .toHashCode();
	}

	// - B U I L D E R
	public static class Builder {
		private MiningExtraction4View onConstruction;

		public Builder() {
			this.onConstruction = new MiningExtraction4View();
		}

		public MiningExtraction4View.Builder withDelta( final Long delta ) {
			Objects.requireNonNull( delta );
			this.onConstruction.delta = delta;
			return this;
		}

		public MiningExtraction4View.Builder withExtractionHour( final Integer extractionHour ) {
			Objects.requireNonNull( extractionHour );
			this.onConstruction.extractionHour = extractionHour;
			return this;
		}

		public MiningExtraction4View.Builder withQuantity( final Integer quantity ) {
			Objects.requireNonNull( quantity );
			this.onConstruction.quantity = quantity;
			return this;
		}

		public MiningExtraction4View.Builder withNeoItem( final NeoItem item ) {
			Objects.requireNonNull( item );
			this.onConstruction.item = item;
			return this;
		}

		public MiningExtraction4View build() {
			return this.onConstruction;
		}
	}
}
