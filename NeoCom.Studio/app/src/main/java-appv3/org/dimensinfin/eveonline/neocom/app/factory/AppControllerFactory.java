package org.dimensinfin.eveonline.neocom.app.factory;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.app.controller.*;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;
import org.dimensinfin.eveonline.neocom.app.pilot.PilotV3;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComActionBarV2;
import org.dimensinfin.eveonline.neocom.app.ui.PlanetaryActionBar;
import org.dimensinfin.eveonline.neocom.app.ui.SplashActionBar;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.NewLoginAction;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.ui.NeoComMenuBar;
import org.dimensinfin.eveonline.neocom.planetary.controller.PlanetaryActionBarController;

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
		if (node instanceof AppVersion) {
			return new AppVersionController((AppVersion) node, this);
		}
		if (node instanceof Credential) {
			return new CredentialController((Credential) node, this);
		}
		if (node instanceof NewLoginAction) {
			return new NewLoginController((NewLoginAction) node, this);
		}
		if (node instanceof InitialisationTask) {
			return new InitialisationTaskController((InitialisationTask) node, this);
		}
		if (node instanceof PanelTitle) {
			return new PanelTitleController.Builder((PanelTitle) node, this)
					       .withVariant(this.getVariant())
					       .build();
		}

		if (node instanceof PilotV3) {
			return new PilotV3Controller.Builder((PilotV3) node, this)
					       .withVariant(this.getVariant())
					       .build();
		}
		if (node instanceof NeoComMenuBar) {
			return new NeoComDashboardMenuController.Builder((NeoComMenuBar) node, this)
					       .withVariant(this.getVariant())
					       .build();
		}

		// - A C T I O N B A R S
		if (node instanceof SplashActionBar) {
			return new SplashActionBarController((SplashActionBar) node, this);
		}
		if (node instanceof NeoComActionBarV2) {
			return new NeoComActionBarController((NeoComActionBarV2) node, this);
		}
		if (node instanceof PlanetaryActionBar) {
			return new PlanetaryActionBarController((PlanetaryActionBar) node, this);
		}
		return super.createController(node);
	}
}
