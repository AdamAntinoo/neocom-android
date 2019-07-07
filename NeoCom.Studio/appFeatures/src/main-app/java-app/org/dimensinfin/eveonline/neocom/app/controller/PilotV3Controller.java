package org.dimensinfin.eveonline.neocom.app.controller;

import java.util.Objects;

import android.content.Context;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.domain.PilotV3;
import org.dimensinfin.eveonline.neocom.app.render.PilotRender;

public class PilotV3Controller extends AAndroidController<PilotV3> {
	protected String variant;

	protected PilotV3Controller( @NonNull final PilotV3 model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PilotRender(this, context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private PilotV3Controller onConstruction;

		public Builder( @NonNull final PilotV3 model, @NonNull final IControllerFactory factory ) {
			Objects.requireNonNull(model);
			Objects.requireNonNull(factory);
			this.onConstruction = new PilotV3Controller(model, factory);
		}

		public Builder withVariant( final String variant ) {
			this.onConstruction.variant = variant;
			return this;
		}

		public PilotV3Controller build() {
			return this.onConstruction;
		}
	}
}
