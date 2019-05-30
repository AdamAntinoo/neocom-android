package org.dimensinfin.eveonline.neocom.planetary.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Canvas;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.planetary.FacilityStructureInfo;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;

public class FacilityImageView extends APlanetaryStructureImageView implements IPlanetaryImageView {
	private static final int FACTORY_RING_FILL_COLOR = R.color.pi_factoryringfillcolor;

	private static class FactorySection {
		private GetCharactersCharacterIdPlanetsPlanetIdOkContents content;
		private long capacity = 3000;
		private long fillLevel = 0;
		//		private double angle = 0.0;

		// - C O N S T R U C T O R S
		private FactorySection( final GetCharactersCharacterIdPlanetsPlanetIdOkContents content ) {
			super();
			this.content = content;
		}

		// - G E T T E R S   &   S E T T E R S
		public long getCapacity() {
			return capacity;
		}

		//		public FactorySection setCapacity( final double capacity ) {
		//			this.capacity = capacity;
		//			return this;
		//		}

		public long getFillLevel() {
			return fillLevel;
		}

		public float getFillAngle( final float maxAngle ) {
			return this.fillLevel * maxAngle / this.capacity;
		}

		//		public FactorySection setFillLevel( final double fillLevel ) {
		//			this.fillLevel = fillLevel;
		//			return this;
		//		}

		//		/**
		//		 * Do the angle calculation every time I request the data.
		//		 * @return
		//		 */
		//		public int getAngle() {
		//			int angle =
		//			return angle;
		//		}

		//		public FactorySection setAngle( final double angle ) {
		//			this.angle = angle;
		//			return this;
		//		}

		// - B U I L D E R
		public static class Builder {
			private FactorySection object;

			public Builder( final GetCharactersCharacterIdPlanetsPlanetIdOkContents content ) {
				this.object = new FactorySection(content);
			}

			public Builder withCapacity( final long capacity ) {
				this.object.capacity = capacity;
				return this;
			}

			public Builder withFillLevel( final long fillLevel ) {
				this.object.fillLevel = fillLevel;
				return this;
			}

			//			public Builder withAngle( final int angle ) {
			//				this.object.angle = angle;
			//				return this;
			//			}

			public FactorySection build() {
				// Do the internal calculations to set the fill level and the angle percentage.
				this.withFillLevel(this.object.content.getAmount());
				return this.object;
			}
		}
	}

	//	private static final int FACTORY_COLOR = R.color.pi_factoryiconcolor;
	//	private static final int facilityring_width = 10;
	//	private static final float facilityInputDimensionNeg = Integer.valueOf(-STRUCTURE_SIZE / 2).floatValue();
	//	private static final float facilityInputDimensionPos = Integer.valueOf(STRUCTURE_SIZE / 2).floatValue();
	//	private static final int cycle_radius = STRUCTURE_SIZE / 2 - facilityring_width - facilityring_width;
	//	private static final int cycle_border_distance = (STRUCTURE_SIZE / 2) - cycle_radius;
	//	private static final float cycleDimensionNeg = Integer.valueOf(-STRUCTURE_SIZE / 2).floatValue() + cycle_border_distance;
	//	private static final float cycleDimensionPos = Integer.valueOf(STRUCTURE_SIZE / 2).floatValue() - cycle_border_distance;
	//	private static final int icon_size = facilityring_width + facilityring_width + facilityring_width + CYCLE_WIDTH;

	// - F I E L D S
	private FacilityStructureInfo facilityClass = FacilityStructureInfo.BASIC_INDUSTRY;
	private Map<Integer, FactorySection> factorySections = new HashMap<>();

	//	private final FactorySection factoryProductionSectionA = new FactorySection();
	//	private final FactorySection factoryProductionSectionB = new FactorySection();
	//	private final FactorySection factoryProductionSectionC = new FactorySection();
	//	private FacilityGeoPosition deltaPosition = new FacilityGeoPosition ( );

	//	private double capacityA = 3000.0;
	//	private double capacityB = 3000.0;
	//	private double capacityC = 3000.0;
	//	private double fillLevelA = 0.0;
	//	private double fillLevelB = 0.0;
	//	private double fillLevelC = 0.0;
	private float endAngleA = 0.0f;
	private float endAngleB = 120.0f;
	private float endAngleC = 240.0f;
	private long cycleDuration = TimeUnit.MINUTES.toMillis(30);
	private long cycleElapsed = 1;
	private float cycleAngle = 0.0f;
	//	private int structureIconColor = Color.WHITE;
	//	private Rect structureIconDestination = null;
	//	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


	// - C O N S T R U C T O R S
	protected FacilityImageView( final Context context ) {
		super(context);
	}

	// - C O N T E N T   A P I
	@Override
	public void setPlanetaryFacility( final PlanetaryFacility planetaryFacility ) {
		super.setPlanetaryFacility(planetaryFacility);
		// Detect the right facility type to setup the different graphic variants.
		if (this.planetaryFacility.getStructureType() == PlanetaryFacilityType.BASIC_INDUSTRY)
			this.facilityClass = FacilityStructureInfo.BASIC_INDUSTRY;
		if (this.planetaryFacility.getStructureType() == PlanetaryFacilityType.ADVANCED_INDUSTRY)
			this.facilityClass = FacilityStructureInfo.facility4Schematics(this.planetaryFacility.getSchematicId());
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> contents = this.planetaryFacility.getContents();
		if (null != contents) {
			for (GetCharactersCharacterIdPlanetsPlanetIdOkContents resource : contents) {
				final FactorySection section = new FactorySection.Builder(resource)
						                               .withCapacity(this.facilityClass.getCapacity())
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
		this.centerCanvas(canvas);
		this.drawFacilityRing(canvas);
		this.drawFacilityFill(canvas);
		this.drawCycleRing(canvas);
		this.drawStructureIcon(canvas);
		super.onDraw(canvas);
		logger.info("<< [FacilityImageView.onDraw]");
	}

	/**
	 * Industrial facilities have some different types depending on the swchematic selected. Some require a single input resource, those are the
	 * basic facilities that transform the raw element to Tier 1. Then come the Advanced facility that can do any of the other transformations. This
	 * later facility is able to generate the Tier 2 to Tier 4 resources.
	 *
	 * The drawing should check the second type to draw 2 or 3 segments equally spaced.
	 *
	 * @param canvas
	 */
	@Override
	protected void drawFacilityRing( final Canvas canvas ) {
		paint.setColor(this.getColor(this.planetaryFacility.getStructureColorReference()));
		// Get the facility type.
		logger.info("-- [FacilityImageView.drawFacilityRing]> {}", facilityClass.name());
		switch (facilityClass) {
			case BASIC_INDUSTRY:
				canvas.drawCircle(0.0f, 0.0f, DIMENSION, paint);
				break;
			case ADVANCED_INDUSTRY:
				float startAngle = -90.0F + 5.0F;
//				paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 180.0F - 10.0F, true, paint);
				startAngle = -90.0F + 5.0F + 180.0F;
//				paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 180.0F - 10.0F, true, paint);
				break;
			case ADVANCED_INDUSTRY_MULTICOMPONENT:
				startAngle = -90.0F + 5.0F;
//				paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, true, paint);
				startAngle = -90.0F + 5.0F + 120.0F;
//				paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, true, paint);
				startAngle = -90.0F + 5.0F + 120.0F + 120.0F;
//				paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
				canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
						, startAngle, 120.0F - 10.0F, true, paint);
				break;
		}
		// Use background color to clean up the drawing that should not be seen.
		paint.setColor(this.getColor(this.planetaryFacility.getBackgroundColor()));
		canvas.drawCircle(0.0F, 0.0F, DIMENSION - FACILITY_RING_WIDTH, paint);
	}

	protected void drawFacilityFill( final Canvas canvas ) {
		paint.setColor(this.getColor(FACTORY_RING_FILL_COLOR));
		// Get the facility type.
		logger.info("-- [FacilityImageView.drawFacilityFill]> {}", facilityClass.name());
		int sectionCounter = 0;
		for (FactorySection section : this.factorySections.values()) {
			switch (facilityClass) {
				case BASIC_INDUSTRY:
					float startAngle = -90.0F;
//					paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(360.0F), true, paint);
					break;
				case ADVANCED_INDUSTRY:
					startAngle = -90.0F + 5.0F + 180.0F * sectionCounter;
//					paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(180.0F) - 10.0F, true, paint);
					break;
				case ADVANCED_INDUSTRY_MULTICOMPONENT:
					startAngle = -90.0F + 5.0F + 120.0F * sectionCounter;
//					paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
					canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
							, startAngle, section.getFillAngle(120.0F) - 10.0F, true, paint);
					break;
			}
			sectionCounter++;
		}
		// Use background color to clean up the drawing that should not be seen.
		paint.setColor(this.getColor(this.planetaryFacility.getBackgroundColor()));
		canvas.drawCircle(0.0F, 0.0F, DIMENSION - FACILITY_RING_WIDTH, paint);
	}


	//	private double getContentIndexed( final int index ) {
	//		if (null != planetaryFacility) {
	//			// TODO Analyze and replace by operative code
	//			//			List<PinContent> contentList = planetaryFacility.getContent();
	//			List<PinContent> contentList = null;
	//			if (null != contentList) {
	//				PinContent content = contentList.get(0);
	//				if (null != content) return content.getAmount();
	//			}
	//		}
	//		return 0.0;
	//	}

	//	private void drawFacilityCapacityRing( Canvas canvas ) {
	//		int radius = STRUCTURE_SIZE / 2;
	//		paint.setColor(getResources().getColor(R.color.pi_facilityinputbackground));
	//		canvas.drawCircle(0.0f, 0.0f, radius, paint);
	//	}

	/**
	 * Draws each of the 3 facility inputs. If the facility does not have that input we draw nothing but the code
	 * is shared for the 3 facilities.
	 */
	private void drawFacilityInputRing( Canvas canvas ) {
		// Draw for input A
		float startAngle = -90.0F;
		paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
				, startAngle, 30.0F, true, paint);

		// Draw for input B
		startAngle = -90.0F + 120.0F - 5.0F;
		paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
				, startAngle, 40.0F, true, paint);

		// Draw for input C
		startAngle = -90.0F + 120.0F - 5.0F + 120.0F - 5.0F;
		paint.setColor(getResources().getColor(R.color.pi_facilityinputfill));
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION
				, startAngle, 50.0F, true, paint);
	}

	//	/**
	//	 * Draws the white ring depending on the time elapsed from the cycle start.
	//	 */
	//	private void drawCycleRing( Canvas canvas ) {
	//		paint.setColor(Color.WHITE);
	//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
	//		canvas.drawArc(cycleDimensionNeg, cycleDimensionNeg, cycleDimensionPos, cycleDimensionPos, -90.0f, cycleAngle, true, paint);
	//	}

	// - B U I L D E R
	public static class Builder extends APlanetaryStructureImageView.BaseBuilder<FacilityImageView, FacilityImageView.Builder> {
		public Builder( final Context context ) {
			super(context);
		}

		@Override
		protected FacilityImageView getActual() {
			return new FacilityImageView(this.context);
		}

		@Override
		protected FacilityImageView.Builder getActualBuilder() {
			return this;
		}
	}

}
