package org.dimensinfin.eveonline.neocom.assets.render;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.Objects;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NodeBaseDecoratedRender;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class StationRender extends NodeBaseDecoratedRender {
	// - U I   F I E L D S
	private TextView locationRegion;
	private TextView locationSystem;
	private TextView locationIdentifier;
	private TextView locationStation;
	private TextView contentsCount;

	public StationRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
		NeoComLogger.enter();
	}

	@Override
	public StationController getController() {
		return (StationController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.location4assetclassification;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.locationRegion = this.getView().findViewById( R.id.locationRegion );
		this.locationSystem = this.getView().findViewById( R.id.locationSystem );
		this.locationIdentifier = this.getView().findViewById( R.id.locationIdentifier );
		this.locationStation = this.getView().findViewById( R.id.locationStation );
		this.contentsCount = this.getView().findViewById( R.id.contentsCount );
		Objects.requireNonNull( this.locationRegion );
		Objects.requireNonNull( this.locationSystem );
		Objects.requireNonNull( this.locationIdentifier );
		Objects.requireNonNull( this.locationStation );
		Objects.requireNonNull( this.contentsCount );
	}

	@Override
	public void updateContent() {
		super.updateContent();
//		this.nodeIcon.setImageResource( R.drawable.region );
		this.locationRegion.setText( this.getController().getModel().getRegionName() );
		this.locationSystem.setText( this.getController().getModel().getSolarSystem().getName() );
		this.locationIdentifier.setText( this.accessLocationIdentifier() );
		this.locationStation.setText( this.getController().getModel().getStationName() );
		this.contentsCount.setText( this.accessContentCount() );
	}

	private String accessLocationIdentifier() {
		return "[" + this.getController().getModel().getSolarSystem().getStarId() + "]";
	}
	private String accessContentCount() {
		return qtyFormatter.format( this.getController().getModel().getContentCount() );
	}
}
