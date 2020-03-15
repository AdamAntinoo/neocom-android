package org.dimensinfin.eveonline.neocom.mining.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.annotation.RequiresNetwork;
import org.dimensinfin.eveonline.neocom.domain.NeoComNode;
import org.dimensinfin.eveonline.neocom.domain.space.SpaceSystem;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.miningextraction.domain.MiningExtraction;

public class MiningExtractionAggregatorContainer<F> extends NeoComNode {
	private static final long serialVersionUID = 6968783170787567490L;
	private transient List<MiningExtraction> contents = new ArrayList<>();
	private transient F facetDelegate;

	public List<MiningExtraction> getContentList() {
		return this.contents;
	}

	public F getFacetDelegate() {
		return this.facetDelegate;
	}

	public Double getResourcesValue() {
		Double value = 0.0;
		for (MiningExtraction extraction : this.contents)
			value += extraction.getPrice() * extraction.getQuantity();
		return value;
	}

	public Double getResourcesVolume() {
		Double volume = 0.0;
		for (MiningExtraction extraction : this.contents)
			volume += extraction.getVolume() * extraction.getQuantity();
		return volume;
	}

	public int addPack( final MiningExtraction itemPack ) {
		this.contents.add( itemPack );
		return this.contents.size();
	}

	// - I C O L L A B O R A T I O N
	@RequiresNetwork
	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		if (this.facetDelegate instanceof SpaceSystem) {
			Map<Integer, MiningExtractionAggregatorContainer<Integer>> hourlyResources = new HashMap<>();
			for (MiningExtraction extraction : this.contents) {
				MiningExtractionAggregatorContainer<Integer> hit = hourlyResources.get( extraction.getExtractionHour() );
				if (null == hit) {
					hit = new MiningExtractionAggregatorContainer.Builder<Integer>()
							      .withFacet( extraction.getExtractionHour() )
							      .build();
					hourlyResources.put( extraction.getExtractionHour(), hit );
				}
				hit.addPack( extraction );
			}
			final List<ICollaboration> collaboration = new ArrayList<>();
			for (ICollaboration node : hourlyResources.values())
				collaboration.addAll( node.collaborate2Model( variant ) );
			return collaboration;
		}
		if (this.facetDelegate instanceof Integer) {
			final List<ICollaboration> collaboration = new ArrayList<>();
			for (MiningExtraction extraction : this.contents) {
				collaboration.add( new Resource( extraction.getTypeId(), (int) extraction.getQuantity() ) );
			}
			return collaboration;
		} else return super.collaborate2Model( variant );
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder( 17, 37 )
				       .appendSuper( super.hashCode() )
				       .append( this.facetDelegate )
				       .toHashCode();
	}

	// - C O R E
	@Override
	public boolean equals( final Object o ) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final MiningExtractionAggregatorContainer<?> that = (MiningExtractionAggregatorContainer<?>) o;
		return new EqualsBuilder()
				       .appendSuper( super.equals( o ) )
				       .append( this.facetDelegate, that.facetDelegate )
				       .isEquals();
	}

	// - B U I L D E R
	public static class Builder<F> {
		private MiningExtractionAggregatorContainer<F> onConstruction;

		public Builder() {
			this.onConstruction = new MiningExtractionAggregatorContainer<>();
		}

		public MiningExtractionAggregatorContainer.Builder<F> withFacet( final F facet ) {
			Objects.requireNonNull( facet );
			this.onConstruction.facetDelegate = facet;
			return this;
		}

		public MiningExtractionAggregatorContainer<F> build() {
			Objects.requireNonNull( this.onConstruction.facetDelegate );
			return this.onConstruction;
		}
	}
}
