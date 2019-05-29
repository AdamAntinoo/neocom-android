package org.dimensinfin.eveonline.neocom.features.planetaryinteraction.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class ExtractorImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	// - C O N S T R U C T O R S
	protected ExtractorImageView( final Context context ) {
		super(context);
	}

	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [ExtractorImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		this.centerCanvas(canvas);
		// Draw the structure ring
		this.drawFacilityRing(canvas);
		this.drawCycleRing(canvas);
		this.drawStructureIcon(canvas);
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
