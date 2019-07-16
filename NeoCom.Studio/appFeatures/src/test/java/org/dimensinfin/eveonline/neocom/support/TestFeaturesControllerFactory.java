package org.dimensinfin.eveonline.neocom.support;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

public class TestFeaturesControllerFactory extends ControllerFactory {
	public TestFeaturesControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}
	@Override
	public IAndroidController createController( final ICollaboration node ) {
		logger.info("-- [AppControllerFactory.createController]> Node class: {}", node.getClass().getSimpleName());
		if (node instanceof Credential) {
			return new CredentialController((Credential) node, this);
		}
		return super.createController(node);
	}
}
