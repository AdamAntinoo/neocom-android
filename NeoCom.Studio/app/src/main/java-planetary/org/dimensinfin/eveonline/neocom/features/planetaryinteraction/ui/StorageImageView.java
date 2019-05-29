package org.dimensinfin.eveonline.neocom.features.planetaryinteraction.ui;

import android.content.Context;
import android.graphics.Canvas;

import org.dimensinfin.eveonline.neocom.R;

public class StorageImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	private static final int FACTORY_RING_FILL_COLOR = R.color.pi_storageringfillcolor;

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
		this.drawFacilityRing(canvas);
		this.drawFacilityFill(canvas);
		this.drawStructureIcon(canvas);
		super.onDraw(canvas);
		logger.info("<< [StorageImageView.onDraw]");
	}

	protected void drawFacilityFill( final Canvas canvas ) {
		final double fillLevel = this.structure.getContentsVolume();
		final int angle = Double.valueOf((fillLevel / this.structure.getStorageCapacity()) * 360.0).intValue();
		paint.setColor(this.getColor(FACTORY_RING_FILL_COLOR));
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
				, -90.0F, angle, true, paint);
		// Use background color to clean up the drawing that should not be seen.
		paint.setColor(this.getColor(this.structure.getBackgroundColor()));
		canvas.drawCircle(0.0F, 0.0F, DIMENSION - FACILITY_RING_WIDTH, paint);
	}

	/**
	 * Draws each of the 3 facility inputs. If the facility does not have that input we draw nothing but the code
	 * is shared for the 3 facilities.
	 */
	private void drawStorageFillRing( Canvas canvas ) {
		float startAngle = -90.0f;
		float endAngle = 100;
		paint.setColor(getResources().getColor(R.color.pi_storagefill));
		canvas.drawArc(STRUCTURE_INPUTDIMENSION_NEG, STRUCTURE_INPUTDIMENSION_NEG, STRUCTURE_INPUTDIMENSION_POS, STRUCTURE_INPUTDIMENSION_POS, startAngle, endAngle, true, paint);
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
