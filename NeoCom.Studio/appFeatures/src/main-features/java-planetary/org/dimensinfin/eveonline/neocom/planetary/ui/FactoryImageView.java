package org.dimensinfin.eveonline.neocom.planetary.ui;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.planetary.FactoryType;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactoryImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	// - F I E L D S
	private FactoryType facilityClass = FactoryType.BASIC_INDUSTRY;
	private Map<Integer, FactorySection> factorySections = new HashMap<>();
	// - C O N S T R U C T O R S
	protected FactoryImageView( final Context context ) {
		super(context);
	}

	// - C O N T E N T   A P I
	public void setPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
		super.setPlanetaryFacility(planetaryFacility);
		this.facilityClass = ((FactoryFacility) planetaryFacility).getFactoryType();
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = this.planetaryFacility.getContents();
		if (null != contents) {
			for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
				// BUG - Capacity does not come form the Factory type. It should come from the schematics output.
				final FactorySection section = new FactorySection.Builder(resource)
						                               .withCapacity(((FactoryFacility) planetaryFacility).getRequiredQuantity())
						                               .build();
				this.factorySections.put(resource.getTypeId(), section);
			}
		}
	}

	// - D R A W I N G
	@Override
	protected void onDraw( Canvas canvas ) {
		logger.info(">> [FacilityImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		try {
			this.centerCanvas(canvas);
			this.drawFacilityRing(canvas, R.color.pi_factoryringcolor);
			this.drawFacilityFill(canvas);
			this.drawCycleRing(canvas);
			this.drawFacilityIcon(canvas);
		} catch (Exception ex) {
			super.onDraw(canvas);
		}
		super.onDraw(canvas);
		logger.info("<< [FacilityImageView.onDraw]");
	}

	/**
	 * Industrial facilities have some different types depending on the schematic selected. Some require a single input resource, those are the
	 * basic facilities that transform the raw element to Tier 1. Then come the Advanced facility that can do any of the other transformations. This
	 * later facility is able to generate the Tier 2 to Tier 4 resources.
	 *
	 * The drawing should check the second type to draw 2 or 3 segments equally spaced.
	 */
	@Override
	protected void drawFacilityRing( final Canvas canvas, final int colorReference ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(colorReference));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		// Get the facility type.
		logger.info("-- [FacilityImageView.drawFacilityRing]> {}", facilityClass.name());
		switch (facilityClass) {
			case BASIC_INDUSTRY:
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, 0.0F, 360.0F, false, paint);
				break;
			case ADVANCED_INDUSTRY:
				float startAngle = -90.0F + 5.0F;
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 180.0F - 10.0F, false, paint);
				startAngle = -90.0F + 5.0F + 180.0F;
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 180.0F - 10.0F, false, paint);
				break;
			case ADVANCED_INDUSTRY_MULTICOMPONENT:
				startAngle = -90.0F + 5.0F;
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, false, paint);
				startAngle = -90.0F + 5.0F + 120.0F;
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, false, paint);
				startAngle = -90.0F + 5.0F + 120.0F + 120.0F;
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, false, paint);
				break;
		}
	}

	protected void drawFacilityFill( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.pi_factoryringfillcolor));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		// Get the facility type.
		logger.info("-- [FacilityImageView.drawFacilityFill]> {}", facilityClass.name());
		int sectionCounter = 0;
		for (FactorySection section : this.factorySections.values()) {
			switch (facilityClass) {
				case BASIC_INDUSTRY:
					float startAngle = -90.0F;
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(360.0F), false, paint);
					break;
				case ADVANCED_INDUSTRY:
					startAngle = -90.0F + 5.0F + 180.0F * sectionCounter;
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(180.0F) - 10.0F, false, paint);
					break;
				case ADVANCED_INDUSTRY_MULTICOMPONENT:
					startAngle = -90.0F + 5.0F + 120.0F * sectionCounter;
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(120.0F) - 10.0F, false, paint);
					break;
			}
			sectionCounter++;
		}
	}

	/**
	 * Draws the white ring depending on the time elapsed from the cycle start.
	 */
	private void drawCycleRing( Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Initialise the paint.
		paint.setColor(this.getColor(R.color.appwhite));
		paint.setStrokeWidth(CYCLE_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(-CYCLE_DIMENSION, -CYCLE_DIMENSION, CYCLE_DIMENSION, CYCLE_DIMENSION
				, -90.0f, this.getCycleElapsedAngle(), false, paint);
	}

	protected Float getCycleElapsedAngle() {
		final Integer cycle = ((FactoryFacility) this.planetaryFacility).getCycleTime();
		final DateTime lastCycleStart = this.planetaryFacility.getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		return 360.0F * (elapsed.getSeconds() % cycle) / cycle;
	}

	// - B U I L D E R
	public static class Builder extends APlanetaryStructureImageView.BaseBuilder<FactoryImageView, FactoryImageView.Builder> {
		public Builder( final Context context ) {
			super(context);
		}

		@Override
		protected FactoryImageView getActual() {
			return new FactoryImageView(this.context);
		}

		@Override
		protected FactoryImageView.Builder getActualBuilder() {
			return this;
		}
	}

	// - F A C T O R Y S E C T I O N
	private static class FactorySection {
		private GetCharactersCharacterIdPlanetsPlanetIdOkContents content;
		private long capacity = 3000;
		private long fillLevel = 0;

		// - C O N S T R U C T O R S
		private FactorySection( final GetCharactersCharacterIdPlanetsPlanetIdOkContents content ) {
			super();
			this.content = content;
		}

		// - G E T T E R S   &   S E T T E R S
		public long getCapacity() {
			return this.capacity;
		}

		public float getFillAngle( final float maxAngle ) {
			return this.fillLevel * maxAngle / this.capacity;
		}

		// - B U I L D E R
		public static class Builder {
			private FactorySection onConstruction;

			public Builder( final GetCharactersCharacterIdPlanetsPlanetIdOkContents content ) {
				this.onConstruction = new FactorySection(content);
			}

			public Builder withCapacity( final long capacity ) {
				this.onConstruction.capacity = capacity;
				return this;
			}

			public FactorySection build() {
				// Do the internal calculations to set the fill level and the angle percentage.
				this.onConstruction.fillLevel = this.onConstruction.content.getAmount();
				return this.onConstruction;
			}
		}
	}
}
