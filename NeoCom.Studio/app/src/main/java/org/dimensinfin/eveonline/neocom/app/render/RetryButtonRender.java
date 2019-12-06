package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.controller.RetryButtonController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RetryButtonRender extends MVCRender {
	// - U I   F I E L D S
	protected TextView buttonText;

	public RetryButtonRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
		NeoComLogger.enter();
	}

	@Override
	public RetryButtonController getController() {
		return (RetryButtonController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.buttonretry;
	}

	@Override
	public void initializeViews() {
		this.buttonText = Objects.requireNonNull( this.getView().findViewById( R.id.buttonLabel ) );
		final Intent intent = new Intent( this.getView().getContext(), AppInitializationActivity.class );
		intent.addFlags( FLAG_ACTIVITY_NEW_TASK );
		this.getView().setOnClickListener( view -> view.getContext().startActivity( intent ) );
	}

	@Override
	public void updateContent() {
		this.buttonText.setText( this.getController().getModel().getMessage() );
	}
}
