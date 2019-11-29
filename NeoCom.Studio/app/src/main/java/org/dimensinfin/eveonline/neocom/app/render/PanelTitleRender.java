package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PanelTitleController;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;

public class PanelTitleRender extends NeoComRender {
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
