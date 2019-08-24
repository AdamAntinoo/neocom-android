package org.dimensinfin.eveonline.neocom.mining;

import android.util.SparseArray;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
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

	public Double getResourcesValue() {
		Double value = new Double(0.0);
		for (MiningExtraction extraction : this.contents)
			value += extraction.getPrice() * extraction.getQuantity();
		return value;
	}

	@Override
	public boolean isEmpty() {
		return this.contents.isEmpty();
	}

	@Override
	public boolean isExpanded() {
		return true;
	}

	@Override
	public List<ICollaboration> collaborate2Model( final String variant ) {
		if (this.facetDelegate instanceof EsiLocation) {
			SparseArray<ResourceAggregatorContainer<Integer>> hourlyResources = new SparseArray<>();
			for (MiningExtraction extraction : this.contents) {
				final ResourceAggregatorContainer<Integer> hit = hourlyResources.get(extraction.getExtractionHour());
				if (null == hit) {
					final ResourceAggregatorContainer<Integer> resources = new ResourceAggregatorContainer.Builder<Integer>()
							                                                       .withFacet(new Integer(
									                                                       extraction.getExtractionHour()))
							                                                       .build();
					resources.addPack(extraction);
					hourlyResources.put(extraction.getExtractionHour(), resources);
				} else {
					hit.addPack(extraction);
				}
			}
			final List<ICollaboration> collaboration = new ArrayList<>();
			for (int i = 0; i < hourlyResources.size(); i++)
				collaboration.add(hourlyResources.get(hourlyResources.keyAt(i)));
			return collaboration;
		}
		if (this.facetDelegate instanceof Integer)
			return super.collaborate2Model(variant);
		else return super.collaborate2Model(variant);
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