package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

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
		this.drawFacilityRing(canvas);
		this.drawCpuInUseRing(canvas);
		this.drawPowerInUseRing(canvas);
		this.drawFacilityFill(canvas);
		this.drawFacilityIcon(canvas);
		super.onDraw(canvas);
		logger.info("<< [CommandCenterImageView.onDraw]");
	}

	protected void centerCanvas4CommandCenter( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
	}

	protected void drawCpuInUseRing( final Canvas canvas ) {
		// Draw the circle indicators.
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterringcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{3, 2}, 5));
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, 90.0F
				, 180.0F, false, paint);
		// Draw the cpu in use.
		paint.setColor(this.getColor(R.color.pi_commandcentercpucolor));
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, 90.0F
				, this.getCpuUsageAngle(), false, paint);
	}

	protected void drawPowerInUseRing( final Canvas canvas ) {
		// Draw the circle indicators.
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterringcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[]{3, 2}, 5));
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, 90.0F
				, -180.0F, false, paint);
		// Draw the cpu in use.
		paint.setColor(this.getColor(R.color.pi_commandcenterpowercolor));
		canvas.drawArc(-DIMENSION + 2 * FACILITY_RING_WIDTH, -DIMENSION + 2 * FACILITY_RING_WIDTH
				, DIMENSION - 2 * FACILITY_RING_WIDTH, DIMENSION - 2 * FACILITY_RING_WIDTH, 90.0F
				, this.getPowerUsageAngle(), false, paint);
	}

	private Float getCpuUsageAngle() {
		final Float cpuUse = new Float(((CommandCenterFacility) this.planetaryFacility).cpuInUse());
		final Float cpuCapacity = new Float(((CommandCenterFacility) this.planetaryFacility).getCpuCapacity());
		return cpuUse / cpuCapacity * 180.0F;
	}

	private Float getPowerUsageAngle() {
		final Float powerUse = Float.valueOf(((CommandCenterFacility) this.planetaryFacility).powerInUse());
		final Float powerOutput = Float.valueOf(((CommandCenterFacility) this.planetaryFacility).getPowerOutput());
		return powerUse / powerOutput * -180.0F;
	}

	protected void drawFacilityFill( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_commandcenterstorageusedcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION, -90.0F, this.getStorageUsageAngle(), false, paint);
		// Use background color to clean up the drawing that should not be seen.
		//		paint.setColor(this.getColor(this.planetaryFacility.getBackgroundColor()));
		//		canvas.drawCircle(0.0F, 0.0F, DIMENSION - FACILITY_RING_WIDTH, paint);
	}

	private Float getStorageUsageAngle() {
		final Float capacity = ((CommandCenterFacility) this.planetaryFacility).getStorageCapacity();
		final Float usage = ((CommandCenterFacility) this.planetaryFacility).getTotalVolume();
		return usage / capacity * 360.0F;
	}

	//	/**
	//	 * Draws each of the 3 facility inputs. If the facility does not have that input we draw nothing but the code
	//	 * is shared for the 3 facilities.
	//	 */
	//	private void drawStorageFillRing( Canvas canvas ) {
	//		float startAngle = -90.0f;
	//		float endAngle = 100;
	//		paint.setColor(getResources().getColor(R.color.pi_storagefill));
	//		canvas.drawArc(STRUCTURE_INPUTDIMENSION_NEG, STRUCTURE_INPUTDIMENSION_NEG, STRUCTURE_INPUTDIMENSION_POS, STRUCTURE_INPUTDIMENSION_POS, startAngle, endAngle, true, paint);
	//	}

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
