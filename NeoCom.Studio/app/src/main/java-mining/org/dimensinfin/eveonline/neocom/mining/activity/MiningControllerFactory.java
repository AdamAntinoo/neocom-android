package org.dimensinfin.eveonline.neocom.mining.activity;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionAggregatorController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningResourceController;
import org.dimensinfin.eveonline.neocom.mining.domain.MiningExtraction4View;
import org.dimensinfin.eveonline.neocom.mining.domain.MiningExtractionAggregatorAsPanelTitle;

/**
 * @author Adam Antinoo
 */
public class MiningControllerFactory extends AppControllerFactory {
	public MiningControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		if (node instanceof MiningExtractionAggregatorAsPanelTitle) {
			return new MiningExtractionAggregatorController((MiningExtractionAggregatorAsPanelTitle) node, this);
		}
		if (node instanceof MiningExtraction4View) {
			return new MiningExtractionController((MiningExtraction4View) node, this);
		}
//		if (node instanceof DailyExtractionResourcesContainer) {
//			return new DailyExtractionResourcesContainerController((DailyExtractionResourcesContainer) node, this)
//					       .setRenderMode(this.getVariant());
//		}
		if (node instanceof Resource) {
			return new MiningResourceController((Resource) node, this).setRenderMode(this.getVariant());
		}
//		if (node instanceof MiningExtractionAggregatorController) {
//			if ( ((MiningExtractionAggregatorContainer<?>) node).getFacetDelegate() instanceof EsiLocation)
//				return new ResourceAggregatorContainer4EsiLocationFacetController((ResourceAggregatorContainer) node, this);
//			if ( ((ResourceAggregatorContainer) node).getFacetDelegate() instanceof Integer)
//				return new ResourceAggregatorContainer4LocalTimeFacetController((ResourceAggregatorContainer) node, this);
//		}

		return super.createController(node);
	}
}
