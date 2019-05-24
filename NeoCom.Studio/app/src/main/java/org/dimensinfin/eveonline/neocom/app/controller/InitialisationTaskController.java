package org.dimensinfin.eveonline.neocom.app.controller;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AAndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.interfaces.IRender;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;

public class InitialisationTaskController extends AAndroidController<InitialisationTask> {
	public InitialisationTaskController( @NonNull final InitialisationTask model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new InitialisationTaskRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - I N I T I A L I S A T I O N T A S K R E N D E R
	public static class InitialisationTaskRender extends AMVCRender {
		// - U I   F I E L D S
		private TextView taskName;

		public InitialisationTaskRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public InitialisationTaskController getController() {
			return (InitialisationTaskController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.paneltitle4list;
		}

		@Override
		public void initializeViews() {
			taskName = this.getView().findViewById(R.id.title);
		}

		@Override
		public void updateContent() {
			taskName.setText(this.getController().getModel().getTaskName());
		}
	}

}
