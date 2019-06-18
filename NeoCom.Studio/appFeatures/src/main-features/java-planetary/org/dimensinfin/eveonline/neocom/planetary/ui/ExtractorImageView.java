package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;

public class ExtractorImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	// - C O N S T R U C T O R S
	protected ExtractorImageView( final Context context ) {
		super(context);
	}

	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [ExtractorImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		this.centerCanvas(canvas);
		this.drawFacilityRing(canvas); // Draw the planetaryFacility ring
		this.drawCycleRing(canvas); // Draw the extraction cycle ring
		this.drawFacilityIcon(canvas); // Draw the facility icon
		super.onDraw(canvas);
		logger.info("<< [ExtractorImageView.onDraw]");
	}

	// - B U I L D E R
	public static class Builder extends APlanetaryStructureImageView.BaseBuilder<ExtractorImageView, Builder> {
		public Builder( final Context context ) {
			super(context);
		}

		@Override
		protected ExtractorImageView getActual() {
			return new ExtractorImageView(this.context);
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}
	}
}
