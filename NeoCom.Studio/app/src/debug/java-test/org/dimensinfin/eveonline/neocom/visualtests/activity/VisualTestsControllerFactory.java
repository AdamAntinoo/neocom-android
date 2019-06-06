package org.dimensinfin.eveonline.neocom.visualtests.activity;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryFacilityController;

public class VisualTestsControllerFactory extends ControllerFactory {
	public VisualTestsControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		if (node instanceof Credential)
			return new CredentialController((Credential) node, this);
		if (node instanceof CommandCenterFacility)
			return new PlanetaryFacilityController((CommandCenterFacility) node, this);
		return super.createController(node);
	}
}
