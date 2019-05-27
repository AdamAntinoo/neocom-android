package org.dimensinfin.eveonline.neocom.planetary.activity;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;

/**
 * @author Adam Antinoo
 */
public class PlanetaryControllerFactory extends AppControllerFactory {
	public PlanetaryControllerFactory( final String selectedVariant ) {
		super(selectedVariant);
	}

	@Override
	public IAndroidController createController( final ICollaboration node ) {
		return super.createController(node);
	}
}
