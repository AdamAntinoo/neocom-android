package org.dimensinfin.eveonline.neocom.mining;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.model.NeoComExpandableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceAggregatorContainer<F> extends NeoComExpandableNode {
	private List<MiningExtraction> contents = new ArrayList<>();
	private F facetDelegate;

	public int addPack( final MiningExtraction itemPack ) {
		this.contents.add(itemPack);
		return this.contents.size();
	}

	public F getFacetDelegate() {
		return this.facetDelegate;
	}

	public List<MiningExtraction> getContentList() {
		return this.contents;
	}

	@Override
	public boolean isEmpty() {
		return this.contents.isEmpty();
	}

	// - B U I L D E R
	public static class Builder<F> {
		private ResourceAggregatorContainer onConstruction;

		public Builder() {
			this.onConstruction = new ResourceAggregatorContainer();
		}

		public ResourceAggregatorContainer.Builder withFacet( final F facet ) {
			Objects.requireNonNull(facet);
			this.onConstruction.facetDelegate = facet;
			return this;
		}

		public ResourceAggregatorContainer<F> build() {
			Objects.requireNonNull(this.onConstruction.facetDelegate);
			return this.onConstruction;
		}
	}
}