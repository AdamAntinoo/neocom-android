package org.dimensinfin.eveonline.neocom.mining.activity;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;
import org.dimensinfin.eveonline.neocom.mining.TableHeader;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionAggregatorController;
import org.dimensinfin.eveonline.neocom.mining.controller.MiningExtractionController;
import org.dimensinfin.eveonline.neocom.mining.controller.ResourceController;
import org.dimensinfin.eveonline.neocom.mining.controller.TableHeaderController;

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
		if (node instanceof MiningExtraction) {
			return new MiningExtractionController((MiningExtraction) node, this);
		}
		if (node instanceof TableHeader) {
			return new TableHeaderController((TableHeader) node, this);
		}
		if (node instanceof Resource) {
			return new ResourceController((Resource) node, this).setRenderMode(this.getVariant());
		}
		return super.createController(node);
	}
}
