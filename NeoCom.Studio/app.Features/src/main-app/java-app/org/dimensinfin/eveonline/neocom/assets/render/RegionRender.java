package org.dimensinfin.eveonline.neocom.assets.render;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NodeBaseDecoratedRender;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class RegionRender extends NodeBaseDecoratedRender {
	// - U I   F I E L D S
	private TextView regionName;
	private TextView stationCount;

	public RegionRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
		NeoComLogger.enter();
	}

	@Override
	public RegionController getController() {
		return (RegionController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.region4assets;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.regionName = this.getView().findViewById( R.id.regionName );
		this.stationCount = this.getView().findViewById( R.id.stationCount );
		Objects.requireNonNull( this.regionName );
		Objects.requireNonNull( this.stationCount );
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.nodeIcon.setImageResource( R.drawable.region );
		this.regionName.setText( this.getController().getModel().getRegionName() );
		this.stationCount.setText( this.accessContentCount() );
	}

	private String accessContentCount() {
		return qtyFormatter.format( this.getController().getModel().getContentCount() );
	}
}
