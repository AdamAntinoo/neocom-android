package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.app.render.PanelTitleRender;

public class PanelTitleController extends AndroidController<PanelTitle> {
	protected String variant;

	protected PanelTitleController( @NonNull final PanelTitle model, @NonNull final IControllerFactory factory ) {
		super( model, factory );
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PanelTitleRender( this, context );
	}

	// - B U I L D E R
	public static class Builder {
		private PanelTitleController onConstruction;

		public Builder( @NonNull final PanelTitle model, @NonNull final IControllerFactory factory ) {
			Objects.requireNonNull( model );
			Objects.requireNonNull( factory );
			this.onConstruction = new PanelTitleController( model, factory );
		}

		public Builder withVariant( final String variant ) {
			this.onConstruction.variant = variant;
			return this;
		}

		public PanelTitleController build() {
			return this.onConstruction;
		}
	}
}
