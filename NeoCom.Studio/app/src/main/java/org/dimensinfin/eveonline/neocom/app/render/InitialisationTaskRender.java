package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.AMVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.InitialisationTaskController;
import org.dimensinfin.eveonline.neocom.app.domain.InitialisationTask;

import org.w3c.dom.Text;

public class InitialisationTaskRender extends AMVCRender {
	// - U I   F I E L D S
	private TextView taskName;
	private TextView elapsedTime;
	private ImageView spinner;

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
		return R.layout.initialisationtask4list;
	}

	@Override
	public void initializeViews() {
		taskName = this.getView().findViewById(R.id.taskName);
		elapsedTime = this.getView().findViewById(R.id.elapsedTime);
		spinner = this.getView().findViewById(R.id.spinner);
		spinner.setVisibility(View.INVISIBLE);
	}

	@Override
	public void updateContent() {
		taskName.setText(this.getController().getModel().getTaskName());
		taskName.setText(this.getController().getModel().getTaskName());
		final InitialisationTask.ETaskStatus status = this.getController().getModel().getStatus();
		if ( status == InitialisationTask.ETaskStatus.SCHEDULED)
			spinner.setVisibility(View.VISIBLE);
		if ( status == InitialisationTask.ETaskStatus.EXECUTING) {
			spinner.setVisibility(View.VISIBLE);
			Animation rotation = AnimationUtils.loadAnimation(getContext(), org.dimensinfin.android.mvc.R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);
			spinner.startAnimation(rotation);
		}
	}
}
