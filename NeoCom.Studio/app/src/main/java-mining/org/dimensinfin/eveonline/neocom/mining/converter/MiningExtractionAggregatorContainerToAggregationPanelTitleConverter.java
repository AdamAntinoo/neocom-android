package org.dimensinfin.eveonline.neocom.mining.converter;

import javax.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.domain.space.SpaceSystem;
import org.dimensinfin.eveonline.neocom.mining.domain.AggregationPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.domain.MiningExtractionAggregatorContainer;

import retrofit2.Converter;

public class MiningExtractionAggregatorContainerToAggregationPanelTitleConverter
		implements Converter<MiningExtractionAggregatorContainer, AggregationPanelTitle> {

	@Nullable
	@Override
	public AggregationPanelTitle convert( final MiningExtractionAggregatorContainer value ) {
		final Object delegate = value.getFacetDelegate();
		if (delegate instanceof SpaceSystem)
			return new AggregationPanelTitle.Builder()
					       .withTitle( ((SpaceSystem) delegate).getSolarSystemName() )
					       .withValue( value.getResourcesValue() )
					       .withVolume( value.getResourcesVolume() )
					       .build();
		if (delegate instanceof Integer)
			return new AggregationPanelTitle.Builder()
					       .withTitle( delegate.toString() )
					       .build();
		return new AggregationPanelTitle.Builder()
				       .withTitle( "-UNDEFINED-" )
				       .build();
	}
}
