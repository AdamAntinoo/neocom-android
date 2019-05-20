package org.dimensinfin.eveonline.neocom.sharedcode.controller;

import java.util.Objects;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.ControllerAdapter;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.SplashActionBarController;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.sharedcode.PanelTitle;

public class PanelTitleController extends AAndroidController<PanelTitle> {
	protected String variant;

	protected PanelTitleController( @NonNull final PanelTitle model, @NonNull final IControllerFactory factory ) {
		super(new ControllerAdapter<>(model), factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new PanelTitleRender(this,context);
	}

	@Override
	public long getModelId() {
		return 0;
	}

	// - B U I L D E R
	public static class Builder {
		private PanelTitleController onConstruction;
		private String variant;

		public Builder( @NonNull final PanelTitle model, @NonNull final IControllerFactory factory ) {
			Objects.requireNonNull(model);
			Objects.requireNonNull(factory);
			this.onConstruction = new PanelTitleController(model, factory);
		}

		public Builder withVariant( final String variant ) {
			this.onConstruction.variant = variant;
			return this;
		}

		public PanelTitleController build() {
			return this.onConstruction;
		}
	}

	// - P A N E L T I T L E R E N D E R
	public static class PanelTitleRender extends NeoComRenderv2{
		private TextView title;

		// - C O N S T R U C T O R S
		public PanelTitleRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public PanelTitleController getController() {
			return (PanelTitleController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.paneltitle4list;
		}
		@Override
		public void initializeViews() {
			super.initializeViews();
			title = this.getView().findViewById(R.id.title);
		}
		@Override
		public void updateContent() {
			super.updateContent();
			title.setText(this.getController().getModel().getTitle());
		}
	}
}
