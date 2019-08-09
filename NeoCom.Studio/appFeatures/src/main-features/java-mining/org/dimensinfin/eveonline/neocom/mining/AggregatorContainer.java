package org.dimensinfin.eveonline.neocom.mining;

import org.dimensinfin.eveonline.neocom.interfaces.IAggregableItem;
import org.dimensinfin.eveonline.neocom.model.NeoComExpandableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AggregatorContainer<T extends IAggregableItem> extends NeoComExpandableNode {
	private List<T> contents = new ArrayList<>();
	private ExtractionSystem facetDelegate;

	public int addPack( final T itemPack ) {
		this.contents.add(itemPack);
		return this.contents.size();
	}

	public ExtractionSystem getFacetDelegate() {
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

		public Builder withFacet( final ExtractionSystem facet ) {
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
