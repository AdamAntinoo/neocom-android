package org.dimensinfin.eveonline.neocom.app.factory;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;

public class AppControllerFactory extends ControllerFactory {
	public AppControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	/**
	 * This method returns the matching Part for an input node class. Depending on the class and the variant we can generate a different
	 * set of controllers to build up our model-view-controller structures.
	 *
	 * @param node the application node for what we should find a Part and create an instance.
	 * @return new <code>IPart</code> instance that is able to manage this class of node.
	 */
	@Override
	public IAndroidController createController( final ICollaboration node ) {
		logger.info("-- [AppDashboardFactory.createPart]> Node class: {}", node.getClass().getSimpleName());
		return super.createController(node);
	}
}
