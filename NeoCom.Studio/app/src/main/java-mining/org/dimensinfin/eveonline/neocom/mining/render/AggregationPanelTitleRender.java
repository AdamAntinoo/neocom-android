package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;
import org.dimensinfin.eveonline.neocom.mining.controller.AggregationPanelTitleController;

public class AggregationPanelTitleRender extends NeoComRender {
	private TextView title;
	private TextView volume;
	private TextView value;

	// - C O N S T R U C T O R S
	public AggregationPanelTitleRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super( controller, context );
	}

	@Override
	public AggregationPanelTitleController getController() {
		return (AggregationPanelTitleController) super.getController();
	}

	// - I R E N D E R   I N T E R F A C E
	@Override
	public int accessLayoutReference() {
		return R.layout.aggregationpanelheader;
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.title = this.getView().findViewById( R.id.title );
		this.volume = this.getView().findViewById( R.id.volume );
		this.value = this.getView().findViewById( R.id.marketValue );
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.title.setText( this.getController().getModel().getTitle() );
		this.volume.setText( this.accessVolume() );
		this.value.setText( this.accessMarketValue() );
	}

	private String accessVolume() {
		return volumeFormatter.format( this.getController().getModel().getTotalVolume() );
	}

	private String accessMarketValue() {
		return this.generatePriceString( this.getController().getModel().getTotalValue() );
	}
}
