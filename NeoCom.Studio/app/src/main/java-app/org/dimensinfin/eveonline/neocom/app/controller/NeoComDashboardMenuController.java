package org.dimensinfin.eveonline.neocom.app.controller;

import java.util.Objects;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.app.pilot.PilotV3;
import org.dimensinfin.eveonline.neocom.app.render.NeoComDashboardMenuRender;
import org.dimensinfin.eveonline.neocom.app.render.PilotV3Render;
import org.dimensinfin.eveonline.neocom.ui.NeoComMenuBar;

public class NeoComDashboardMenuController extends AAndroidController<NeoComMenuBar> {
	protected String variant;

	protected NeoComDashboardMenuController( @NonNull final NeoComMenuBar model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new NeoComDashboardMenuRender(this, context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private NeoComDashboardMenuController onConstruction;

		public Builder( @NonNull final NeoComMenuBar model, @NonNull final IControllerFactory factory ) {
			Objects.requireNonNull(model);
			Objects.requireNonNull(factory);
			this.onConstruction = new NeoComDashboardMenuController(model, factory);
		}

		public Builder withVariant( final String variant ) {
			this.onConstruction.variant = variant;
			return this;
		}

		public NeoComDashboardMenuController build() {
			return this.onConstruction;
		}
	}
}
