package org.dimensinfin.eveonline.neocom.mining.controller;

import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IControllerFactory;
import org.dimensinfin.android.mvc.domain.IRender;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.NeoComRenderv2;
import org.dimensinfin.eveonline.neocom.mining.MiningExtractionAggregatorAsPanelTitle;

public class MiningExtractionAggregatorController extends AndroidController<MiningExtractionAggregatorAsPanelTitle> implements PropertyChangeListener {

	// - C O N S T R U C T O R
	public MiningExtractionAggregatorController( @NonNull final MiningExtractionAggregatorAsPanelTitle model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
//		model.addPropertyChangeListener(this);
	}

	// - I A N D R O I D C O N T R O L L E R   I N T E R F A C E
	@Override
	public IRender buildRender( final Context context ) {
		return new MiningExtractionAggregatorRender(this, context);
	}

	@Override
	public long getModelId() {
		return this.getModel().hashCode();
	}

	// - P R O P E R T Y C H A N G E L I S T E N E R
	@Override
	public void propertyChange( final PropertyChangeEvent evt ) {
		if (evt.getPropertyName().equalsIgnoreCase(EEvents.EVENT_REFRESHDATA.name())) {
			this.notifyDataModelChange();
		}
	}

	// - I T E M P A C K R E N D E R
	public static class MiningExtractionAggregatorRender extends NeoComRenderv2 {
		// - U I   F I E L D S
		private TextView title;
		private TextView volume;

		// - C O N S T R U C T O R S
		public MiningExtractionAggregatorRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
			super(controller, context);
		}

		@Override
		public MiningExtractionAggregatorController getController() {
			return (MiningExtractionAggregatorController) super.getController();
		}

		// - I R E N D E R   I N T E R F A C E
		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractionaggregator4title;
		}

		@Override
		public void initializeViews() {
			this.title = this.getView().findViewById(R.id.title);
			this.volume = this.getView().findViewById(R.id.volume);
		}

		@Override
		public void updateContent() {
			this.title.setText(this.getController().getModel().getTitle());
			this.volume.setText(this.accessVolume());
		}

		private String accessVolume() {
			return volumeFormatter.format(this.getController().getModel().getTotalVolume());
		}
	}
}
