package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;

public class StorageImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
//	private static final int FACTORY_RING_FILL_COLOR = R.color.pi_storageringfillcolor;

	// - C O N S T R U C T O R S
	protected StorageImageView( final Context context ) {
		super(context);
	}

	// - D R A W I N G
	@Override
	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [StorageImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		this.centerCanvas(canvas);
		this.drawFacilityRing(canvas, R.color.pi_storageringcolor);
		this.drawFacilityFill(canvas);
		this.drawFacilityIcon(canvas);
		super.onDraw(canvas);
		logger.info("<< [StorageImageView.onDraw]");
	}

	protected void drawFacilityFill( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(this.getColor(R.color.pi_storageringfillcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);

		final Float fillLevel = ((StorageFacility) this.planetaryFacility).getTotalVolume();
		final Float angle = fillLevel / ((StorageFacility)this.planetaryFacility).getStorageCapacity() * 360.0F;
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
				, -90.0F, angle, false, paint);
	}

	// - B U I L D E R
	public static class Builder extends APlanetaryStructureImageView.BaseBuilder<StorageImageView, StorageImageView.Builder> {
		public Builder( final Context context ) {
			super(context);
		}

		@Override
		protected StorageImageView getActual() {
			return new StorageImageView(this.context);
		}

		@Override
		protected StorageImageView.Builder getActualBuilder() {
			return this;
		}
	}
}
