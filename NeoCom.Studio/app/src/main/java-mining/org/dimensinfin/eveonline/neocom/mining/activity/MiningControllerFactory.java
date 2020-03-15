package org.dimensinfin.eveonline.neocom.mining.activity;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.DailyExtractionResourcesContainer;
import org.dimensinfin.eveonline.neocom.mining.controller.DailyExtractionResourcesContainerController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionAggregatorController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
import org.dimensinfin.eveonline.neocom.mining.converter.MiningExtractionAggregatorContainerToAggregationPanelTitleConverter;
import org.dimensinfin.eveonline.neocom.mining.domain.AggregationPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.domain.MiningExtractionAggregatorContainer;

/**
 * @author Adam Antinoo
 */
public class MiningControllerFactory extends AppControllerFactory {
	public MiningControllerFactory( final String selectedVariant ) {
		super( selectedVariant );
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		if (node instanceof DailyExtractionResourcesContainer) {
			return new DailyExtractionResourcesContainerController( (DailyExtractionResourcesContainer) node, this )
					       .setRenderMode( this.getVariant() );
		}
		if (node instanceof Resource) {
			return new MiningResourceController( (Resource) node, this ).setRenderMode( this.getVariant() );
		}
		if (node instanceof MiningExtractionAggregatorContainer) {
			return new MiningExtractionAggregatorController(
					Objects.requireNonNull( new MiningExtractionAggregatorContainerToAggregationPanelTitleConverter()
							                        .convert( (MiningExtractionAggregatorContainer) node )
					), this )
					       .setRenderMode( this.getVariant() );
		}
//		if (node instanceof MiningExtractionAggregatorContainer) {
//			final Object delegate = ((MiningExtractionAggregatorContainer) node).getFacetDelegate();
//			if ( delegate instanceof SpaceSystem)
//				return new ResourceAggregatorContainer4EsiLocationFacetController((ResourceAggregatorContainer) node, this);
//			if ( ((ResourceAggregatorContainer) node).getFacetDelegate() instanceof Integer)
//				return new ResourceAggregatorContainer4LocalTimeFacetController((ResourceAggregatorContainer) node, this);
//		}
//		if (node instanceof MiningExtractionAggregatorAsPanelTitle) {
//			return new MiningExtractionAggregatorController((MiningExtractionAggregatorAsPanelTitle) node, this)
//					       .setRenderMode(this.getVariant());
//		}
//		if (node instanceof MiningExtraction4View) {
//			return new MiningExtractionController((MiningExtraction4View) node, this)
//					       .setRenderMode(this.getVariant());
//		}

		return super.createController( node );
	}
}
