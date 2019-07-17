package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;

public class CommandCenterImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	private static final int FACTORY_RING_FILL_COLOR = R.color.pi_commandcenterringcolor;
	private static final int CPU_FILL_COLOR = R.color.pi_commandcentercpucolor;
	private static final int POWER_FILL_COLOR = R.color.pi_commandcenterpowercolor;

	// - C O N S T R U C T O R S
	protected CommandCenterImageView( final Context context ) {
		super(context);
	}

	// - D R A W I N G
	@Override
	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [CommandCenterImageView.onDraw]");
		this.centerCanvas4CommandCenter(canvas); // Command center goes to the center of the canvas
		this.drawFacilityRing(canvas, R.color.pi_commandcenterringcolor);
		this.drawGeneratorRing(canvas);
		this.drawCpuInUseRing(canvas);
		this.drawPowerInUseRing(canvas);
		this.drawStorageUsed(canvas);
		this.drawFacilityIcon(canvas);
		super.onDraw(canvas);
		logger.info("<< [CommandCenterImageView.onDraw]");
	}

	protected void centerCanvas4CommandCenter( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
	}

	protected void drawGeneratorRing( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterringcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{ 3, 2 }, 5));
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, -90.0F
				, 360.0F, false, paint);
	}

	protected void drawCpuInUseRing( final Canvas canvas ) {
		// Draw the circle indicators.
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcentercpucolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{ 3, 2 }, 5));
		// Draw the cpu in use.
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, 90.0F
				, this.getCpuUsageAngle(), false, paint);
	}

	protected void drawPowerInUseRing( final Canvas canvas ) {
		// Draw the circle indicators.
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterpowercolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{ 3, 2 }, 5));
		// Draw the power in use.
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, -90.0F
				, this.getPowerUsageAngle(), false, paint);
	}

	private Float getCpuUsageAngle() {
		final Float cpuUse = new Float(((CommandCenterFacility) this.planetaryFacility).cpuInUse());
		final Float cpuCapacity = new Float(((CommandCenterFacility) this.planetaryFacility).getCpuCapacity());
		return cpuUse / cpuCapacity * 180.0F;
	}

	private Float getPowerUsageAngle() {
		final Float powerUse = ((CommandCenterFacility) this.planetaryFacility).powerInUse().floatValue();
		final Float powerOutput = ((CommandCenterFacility) this.planetaryFacility).getPowerOutput().floatValue();
		return powerUse / powerOutput * 180.0F;
	}

	protected void drawStorageUsed( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterstorageusedcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION, -90.0F,
				this.getStorageUsageAngle(), false, paint);
	}

	protected void drawFacilityIcon( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.appblack));
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawArc(-40, -40, 40, 40, -90.0F,
				360.0F, false, paint); // Draw the black circle background

//		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
		Bitmap structureIconBitmap = BitmapFactory.decodeResource(this.getResources(), this.planetaryFacility.getIconReferenceId());
		structureIconBitmap = this.changeImageColor(structureIconBitmap, this.getColor(this.planetaryFacility.getIconColorReference()));
		final Rect structureIconRect = new Rect(0, 0, structureIconBitmap.getWidth(), structureIconBitmap.getHeight());
		final Rect structureIconDestination = new Rect(-STRUCTURE_ICON_DIMENSION, -STRUCTURE_ICON_DIMENSION, STRUCTURE_ICON_DIMENSION, STRUCTURE_ICON_DIMENSION);
		canvas.drawBitmap(structureIconBitmap, structureIconRect, structureIconDestination, paint);
	}

	private Float getStorageUsageAngle() {
		final Float capacity = ((CommandCenterFacility) this.planetaryFacility).getStorageCapacity();
		final Float usage = ((CommandCenterFacility) this.planetaryFacility).getTotalVolume();
		return usage / capacity * 360.0F;
	}

	// - B U I L D E R
	public static class Builder extends BaseBuilder<CommandCenterImageView, CommandCenterImageView.Builder> {
		public Builder( final Context context ) {
			super(context);
		}

		@Override
		protected CommandCenterImageView getActual() {
			return new CommandCenterImageView(this.context);
		}

		@Override
		protected CommandCenterImageView.Builder getActualBuilder() {
			return this;
		}
	}
}
