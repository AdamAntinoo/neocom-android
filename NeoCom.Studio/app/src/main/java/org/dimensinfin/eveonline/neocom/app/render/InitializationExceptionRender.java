package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.controller.InitializationExceptionController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class InitializationExceptionRender extends MVCRender implements View.OnClickListener {
	private TextView exceptionMessage;
//	private ImageView retryButton;

	// - C O N S T R U C T O R S
	public InitializationExceptionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
		NeoComLogger.enter();
	}

	@Override
	public InitializationExceptionController getController() {
		return (InitializationExceptionController) super.getController();
	}

	// - O N C L I C K L I S T E N E R
	@Override
	public void onClick( final View v ) {
		this.getController().sendChangeEvent(AppInitializationActivity.RETRY_INITIALIZATION_EVENT);
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.initializationexception4list;
	}

	@Override
	public void initializeViews() {
		// TODO - The layout needs to include more elements. Enough at the current moment.
		this.exceptionMessage = this.getView().findViewById(R.id.message);
//		this.retryButton = this.getView().findViewById(R.id.stateIcon);
		Objects.requireNonNull(this.exceptionMessage);
//		Objects.requireNonNull(this.retryButton);
//		this.retryButton.setVisibility(View.INVISIBLE);
//		this.retryButton.setOnClickListener(this);
	}

	@Override
	public void updateContent() {
		this.exceptionMessage.setText(this.ifNotEmpty(this.getController().getModel().getExceptionMessage()));
//		if (this.getController().getModel().isRetryable())
//			this.retryButton.setVisibility(View.VISIBLE);
	}

	private String ifNotEmpty( final String input ) {
		if (null != input) return input;
		else return "-";
	}
}
