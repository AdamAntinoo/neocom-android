package org.dimensinfin.eveonline.neocom.app.render.core;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;

public abstract class NodeBaseDecoratedRender extends NeoComRender {
	// - U I   F I E L D S
	protected FrameLayout nodeIconBlock;
	protected ImageView nodeIcon;
	protected ImageView toprightDecorator;
	protected ImageView topleftDecorator;
	protected ImageView bottomrightDecorator;
	protected ImageView bottomleftDecorator;

	public NodeBaseDecoratedRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.nodeIconBlock = this.getView().findViewById( R.id.nodeIconBlock );
		this.nodeIcon = this.getView().findViewById( R.id.nodeIcon );
		this.toprightDecorator = this.getView().findViewById( R.id.toprightDecorator );
		this.topleftDecorator = this.getView().findViewById( R.id.topleftDecorator );
		this.bottomrightDecorator = this.getView().findViewById( R.id.bottomrightDecorator );
		this.bottomleftDecorator = this.getView().findViewById( R.id.bottomleftDecorator );
	}

	@Override
	public void updateContent() {
		super.updateContent();
	}
}
