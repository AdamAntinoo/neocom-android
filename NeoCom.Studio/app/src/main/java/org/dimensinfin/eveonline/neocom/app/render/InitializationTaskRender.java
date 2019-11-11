package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.InitializationTaskController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class InitializationTaskRender extends MVCRender {
	private ImageView stateIcon;
	private TextView message;

	// - C O N S T R U C T O R S
	public InitializationTaskRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
		NeoComLogger.enter();
	}

	@Override
	public InitializationTaskController getController() {
		return (InitializationTaskController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.initializationtask4list;
	}

	@Override
	public void initializeViews() {
		this.stateIcon = this.getView().findViewById(R.id.stateIcon);
		this.message = this.getView().findViewById(R.id.message);
		Objects.requireNonNull(this.stateIcon);
		Objects.requireNonNull(this.message);
	}

	@Override
	public void updateContent() {
		this.message.setText(this.ifNotEmpty(this.getController().getModel().getMessage()));
		switch (this.getController().getModel().getState()) {
			case RUNNING:
				this.stateIcon.setImageDrawable(this.getContext().getDrawable(R.drawable.twotone_directions_run_black_18));
				this.stateIcon.setColorFilter(this.getContext().getResources().getColor(R.color.appbluedark));
				break;
			case COMPLETED:
				this.stateIcon
						.setImageDrawable(this.getContext().getDrawable(R.drawable.twotone_check_circle_outline_black_18));
				this.stateIcon.setColorFilter(this.getContext().getResources().getColor(R.color.appgreenalien));
				break;
			case EXCEPTION:
				this.stateIcon.setImageDrawable(this.getContext().getDrawable(R.drawable.twotone_error_outline_black_18));
				this.stateIcon.setColorFilter(this.getContext().getResources().getColor(R.color.appred));
				break;
		}
	}

	private String ifNotEmpty( final String input ) {
		if (null != input) return input;
		else return "-";
	}
}
