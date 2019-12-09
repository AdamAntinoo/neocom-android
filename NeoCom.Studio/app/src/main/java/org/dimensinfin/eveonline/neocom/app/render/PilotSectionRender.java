package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class PilotSectionRender extends NeoComRender {
	// - U I   F I E L D S
	protected ImageView sectionIcon;
	protected TextView sectionTitle;
	protected TextView sectionData;
	protected TextView sectionLabel;
	protected ViewGroup clickBlock;
	protected ImageView clickIcon;

	public PilotSectionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
	}

	@Override
	public PilotSectionController getController() {
		return (PilotSectionController) super.getController();
	}

	@Override
	public int accessLayoutReference() {
		return R.layout.pilotsection4list;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.sectionIcon = Objects.requireNonNull( this.getView().findViewById( R.id.sectionIcon ) );
		this.sectionTitle = Objects.requireNonNull( this.getView().findViewById( R.id.sectionTitle ) );
		this.sectionData = Objects.requireNonNull( this.getView().findViewById( R.id.sectionData ) );
		this.sectionLabel = Objects.requireNonNull( this.getView().findViewById( R.id.sectionLabel ) );
		this.clickBlock = Objects.requireNonNull( this.getView().findViewById( R.id.clickBlock ) );
		this.clickIcon = Objects.requireNonNull( this.getView().findViewById( R.id.clickIcon ) );
		this.clickBlock.setVisibility( View.INVISIBLE );
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.sectionIcon.setImageDrawable( this.getContext().getDrawable( this.getController().getModel().getIconReference() ) );
		this.sectionTitle.setText( this.getController().getModel().getTitle() );
		this.sectionLabel.setText( this.getController().getModel().getLabel() );
		this.sectionData.setText( "-" );
		MVCScheduler.backgroundExecutor.submit( () -> {
			NeoComLogger.info( "Accessing sectionData. {}", this.getController().getModel().getDataFormatted() );
			final String data = this.getController().getModel().getDataFormatted();
			handler.post( () -> {
				this.sectionData.setText( data );
				this.getController().sendChangeEvent( EEvents.EVENT_REFRESHDATA.name() );
			} );
		} );
		this.activateActivityLauncher();
	}

	private void activateActivityLauncher() {
		if (null != this.getController().getModel().getClickTarget()) {
			this.clickBlock.setVisibility( View.VISIBLE );
			if (!this.getController().getModel().isCompleted()) {
				this.clickIcon.setImageDrawable( getDrawable( R.drawable.progress_spinner_white ) );
				this.clickIcon.setTag( R.drawable.progress_spinner_white );
			} else {
				this.clickIcon.setImageDrawable( getDrawable( R.drawable.multiarrowright ) );
				this.clickIcon.setTag( R.drawable.multiarrowright );
				this.getController().sendChangeEvent( EEvents.EVENT_REFRESHDATA.name() );
			}
		} else {
			this.clickBlock.setVisibility( View.INVISIBLE );
			this.clickIcon.setImageDrawable( getDrawable( R.drawable.multiarrowright ) );
			this.clickIcon.setTag( R.drawable.multiarrowright );
		}
	}
}
