package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.render.AppVersionRender;
import org.dimensinfin.eveonline.neocom.app.render.NeoComActionBarRender;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;

public class AppVersionController extends AndroidController<AppVersion> {
	// - C O N S T R U C T O R S
	public AppVersionController( @NonNull final AppVersion model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	// - I A N D R O I D C O N T R O L L E R

	/**
	 * There are two renders for the AppVersion. The most common is for a header panel to be shown on initialization, error or the splash
	 * activities. The secondary usage is for the simple Action Bar that shows the application name and version.
	 *
	 * @param context the activity context to be used for UI interaction.
	 * @return a new render to be used to generate the render view.
	 */
	@Override
	public IRender buildRender( final Context context ) {
		if (this.getRenderMode().equalsIgnoreCase( PageNamesType.INITIALIZATION_ACTIONBAR.name() ))
			return new NeoComActionBarRender( this, context );
		return new AppVersionRender( this, context );
	}
}
