package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class ExtractorImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	// - C O N S T R U C T O R S
	protected ExtractorImageView( final Context context ) {
		super(context);
	}

	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [ExtractorImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		this.centerCanvas(canvas);
		this.drawFacilityRing(canvas, R.color.pi_extractorringcolor); // Draw the planetaryFacility ring
		this.drawCycleRing(canvas); // Draw the extraction cycle ring
		this.drawFacilityIcon(canvas); // Draw the facility icon
		super.onDraw(canvas);
		logger.info("<< [ExtractorImageView.onDraw]");
	}

	/**
	 * Draws the white ring depending on the time elapsed from the cycle start.
	 */
	protected void drawCycleRing( Canvas canvas ) {
		final Integer cycle = ((ExtractorFacility) this.planetaryFacility).getCycleTime();
		if (cycle > 0) {
			final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(CYCLE_WIDTH);
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.BUTT);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(-CYCLE_DIMENSION, -CYCLE_DIMENSION, CYCLE_DIMENSION, CYCLE_DIMENSION, -90.0F
					, this.getCycleAngle(), false, paint);
		} else {
			// The extractor is not active
			final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(this.getColor(R.color.appred));
			paint.setStrokeWidth(FACILITY_RING_WIDTH);
			paint.setAntiAlias(true);
			paint.setStrokeCap(Paint.Cap.BUTT);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION, -90.0F
					, 360.0F, false, paint);
		}
	}

	/** Return the angle for the current cycle from -90.0 and for 360.0 degrees and being -90.0 pointing up. */
	protected float getCycleAngle() {
		final Integer cycle = ((ExtractorFacility) this.planetaryFacility).getCycleTime();
		final DateTime lastCycleStart = this.planetaryFacility.getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 360.0F * (elapsed.getSeconds() % cycle) / cycle;
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
