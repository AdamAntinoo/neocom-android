package org.dimensinfin.eveonline.neocom.app.render;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;

public class PilotSectionRender extends NeoComRenderv2 {
	// - U I   F I E L D S
	protected ImageView sectionIcon;
	protected TextView sectionTitle;
	protected TextView sectionData;
	protected TextView sectionLabel;
	protected ViewGroup clickBlock;

	public PilotSectionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
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
		this.sectionIcon = this.getView().findViewById(R.id.sectionIcon);
		this.sectionTitle = this.getView().findViewById(R.id.sectionTitle);
		this.sectionData = this.getView().findViewById(R.id.sectionData);
		this.sectionLabel = this.getView().findViewById(R.id.sectionLabel);
		this.clickBlock = this.getView().findViewById(R.id.clickBlock);
		this.clickBlock.setVisibility(View.INVISIBLE);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.sectionIcon.setImageDrawable(this.getContext().getDrawable(this.getController().getModel().getIconReference()));
		this.sectionTitle.setText(this.getController().getModel().getTitle());
		this.sectionLabel.setText(this.getController().getModel().getLabel());
		this.sectionData.setText("-");
		MVCScheduler.backgroundExecutor.submit(() -> {
			final String data = this.getController().getModel().getDataFormatted();
			handler.post(() -> this.sectionData.setText(data));
		});
		this.activateActivityLauncher();
	}

	private void activateActivityLauncher() {
		if (null != this.getController().getModel().getClickTarget())
			this.clickBlock.setVisibility(View.VISIBLE);
	}
}
