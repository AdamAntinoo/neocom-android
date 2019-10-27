package org.dimensinfin.eveonline.neocom.mining;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.core.IAggregableItem;
import org.dimensinfin.eveonline.neocom.model.NeoComExpandableNode;

public class AggregatorContainer<T> extends NeoComExpandableNode {
	private List<T> contents = new ArrayList<>();
	private IFacet facetDelegate;

	public int addPack( final T itemPack ) {
		this.contents.add(itemPack);
		return this.contents.size();
	}

	public IFacet getFacetDelegate() {
		return this.facetDelegate;
	}

	@Override
	public boolean isEmpty() {
		return this.contents.isEmpty();
	}

	// - B U I L D E R
	public static class Builder<T extends IAggregableItem> {
		private AggregatorContainer<T> onConstruction;

		public Builder() {
			this.onConstruction = new AggregatorContainer<>();
		}

		public Builder withFacet( final IFacet facet ) {
			Objects.requireNonNull(facet);
			this.onConstruction.facetDelegate = facet;
			return this;
		}

		public AggregatorContainer<T> build() {
			Objects.requireNonNull(this.onConstruction.facetDelegate);
			return this.onConstruction;
		}
	}
}
