package org.dimensinfin.eveonline.neocom.factory;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.controller.AppVersionController;
import org.dimensinfin.eveonline.neocom.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.controller.NewLoginController;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;

public class CoreControllerFactory extends ControllerFactory {
	public CoreControllerFactory( final String selectedVariant ) {
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
		if (node instanceof AppVersion) {
			return new AppVersionController((AppVersion) node, this);
		}
		if (node instanceof Credential) {
			return new CredentialController((Credential) node, this);
		}
		if (node instanceof NewLoginAction) {
			return new NewLoginController((NewLoginAction) node, this);
		}
		return super.createController(node);
	}
}
