package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.widget.ImageView;

import org.dimensinfin.eveonline.neocom.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageProgressImageView extends ImageView {
	protected static Logger logger = LoggerFactory.getLogger(StorageProgressImageView.class);
	private static final int MARGIN_WIDTH = 20;

	private Context context;
	private Float inUse;
	private Float capacity;
	private Paint paint;

	public StorageProgressImageView( final Context context ) {
		super(context);
	}

	@Override
	protected void onDraw( Canvas canvas ) {
		logger.info(">> [StorageProgressImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		try {
			this.centerCanvas(canvas);
			this.drawCycleRing(canvas);
		} catch (Exception ex) {
			super.onDraw(canvas);
		}
		super.onDraw(canvas);
		logger.info("<< [StorageProgressImageView.onDraw]");
	}

	// - D R A W I N G
	protected void centerCanvas( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	protected void drawCycleRing( Canvas canvas ) {
		final Integer dimension = (canvas.getWidth() - MARGIN_WIDTH * 3) / 2;
		paint.setColor(getResources().getColor(R.color.appblue));
		paint.setStrokeWidth(MARGIN_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{5, 3}, 5));
		// Draw blue zone
		final Float angle = this.getCycleAngle();
		canvas.drawArc(-dimension, -dimension, dimension, dimension, -90.0F
				, Math.min(270.0F, angle), false, paint);
		if (angle > 270.0F) {
			// Draw red zone
			paint.setColor(getResources().getColor(R.color.appred));
			canvas.drawArc(-dimension, -dimension, dimension, dimension, 270.0F-90.0F
					, angle - 270.0F, false, paint);
		}
	}

	public Float getCycleAngle() {
		final float angle = this.inUse / this.capacity * 360.0F;
		logger.info("-- [StorageProgressImageView.getCycleAngle]> Angle: {}", angle);
		if (angle > 360.0F) return 360.0F;
		else return angle;
	}

	// - B U I L D E R
	public static class Builder {
		private StorageProgressImageView onConstruction;

		public Builder( final Context context ) {
			this.onConstruction = new StorageProgressImageView(context);
			this.onConstruction.context = context;
		}

		public Builder withUsedVolume( final Float used ) {
			this.onConstruction.inUse = used;
			return this;
		}

		public Builder withCapacity( final Float capacity ) {
			this.onConstruction.capacity = capacity;
			return this;
		}

		public StorageProgressImageView build() {
			return this.onConstruction;
		}
	}
}
