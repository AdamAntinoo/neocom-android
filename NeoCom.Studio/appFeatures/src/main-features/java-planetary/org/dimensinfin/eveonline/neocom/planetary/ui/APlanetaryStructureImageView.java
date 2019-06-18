package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.core.AppCompatibilityUtils;
import org.dimensinfin.eveonline.neocom.planetary.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class APlanetaryStructureImageView extends ImageView {
	protected static Logger logger = LoggerFactory.getLogger(APlanetaryStructureImageView.class);

	// - D R A W I N G   C O N S T A N T S
	protected static final Float CONVERSION_LONGITUDE_MULTIPLIER = 10500.0F / 9.0F * 7.0F;
	protected static final Float CONVERSION_LATITUDE_MULTIPLIER = 10500.0F;
	protected static final int DEFAULT_ICON_WIDTH = 32;
	protected static final int DEFAULT_ICON_HEIGHT = 32;
	protected static final int STRUCTURE_SIZE = 110;
	protected static final int DIMENSION = Float.valueOf(STRUCTURE_SIZE / 2).intValue();
	protected static final int FACILITY_RING_WIDTH = 10;
	protected static final int CYCLE_WIDTH = 5;
	protected static final int ICON_SIZE = FACILITY_RING_WIDTH + FACILITY_RING_WIDTH + FACILITY_RING_WIDTH + CYCLE_WIDTH;
	protected static final int DIMENSION_NEG = -DIMENSION + ICON_SIZE;
	protected static final int DIMENSION_POS = DIMENSION - ICON_SIZE;
	protected static final int STRUCTURE_RING_WIDTH = 12;
	protected static final int STRUCTURE_ICON_DIMENSION = 30;
	protected static final float STRUCTURE_INPUTDIMENSION_NEG = Integer.valueOf(-STRUCTURE_SIZE / 2).floatValue();
	protected static final float STRUCTURE_INPUTDIMENSION_POS = Integer.valueOf(STRUCTURE_SIZE / 2).floatValue();
	protected static final int CYCLE_RADIUS = STRUCTURE_SIZE / 2 - FACILITY_RING_WIDTH - FACILITY_RING_WIDTH;
	protected static final int CYCLE_BORDER_DISTANCE = (STRUCTURE_SIZE / 2) - CYCLE_RADIUS;
	protected static final float CYCLE_DIMENSION_NEG = Integer.valueOf(-STRUCTURE_SIZE / 2).floatValue() + CYCLE_BORDER_DISTANCE;
	protected static final float CYCLE_DIMENSION_POS = Integer.valueOf(STRUCTURE_SIZE / 2).floatValue() - CYCLE_BORDER_DISTANCE;
	protected static final Rect STRUCTURE_ICON_RECT = new Rect(0, 0, DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
	protected static final Rect STRUCTURE_ICON_DESTINATION_RECT = new Rect(DIMENSION_NEG, DIMENSION_POS, DIMENSION_POS, DIMENSION_NEG);

	// - F I E L D S
	protected FacilityGeoPosition deltaPosition = new FacilityGeoPosition();
	/** Delta location for the current planetaryFacility from the command center. */
	protected double longitudeDelta = 0.0;
	protected double latitudeDelta = 0.0;
	/** Position of the delta to the Control Cebter in pixels. */
	protected long posx = 0;
	protected long posy = 0;
	/** This is the planetaryFacility to be rendered. This data should contain all the data required for the rendering. */
	protected IPlanetaryFacility planetaryFacility;
	/** The resource identifier for the icon that represents the planetaryFacility. */
	protected int drawableId = PlanetaryFacilityType.DEFAULT.getIconReferenceId();
	/** Set the default to a Processing Facility. */
	protected Bitmap structureIconBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
	protected double scale = 1.0;
	protected Bitmap bitmap = null;
	/** The color to be used to simulate transparency by using the same background color. */
	//	protected int eraserColor = this.getResources().getColor(R.color.pi_barren_backgroundcolor);
	protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // The painting brush.

	// - C O N S T R U C T O R S
	protected APlanetaryStructureImageView( final Context context ) {
		super(context);
	}

	// - C O N T E N T   A P I

	/**
	 * This should be called before setting the planetaryFacility. This method calculates the displacement to set this facility relative
	 * to the Command Center position so the command center is centered and this facility is draw close to it.
	 * There are some constants to adjust for the facility drawing size so they are touching without overlapping like the real coordinates
	 * on the planetary interaction game.
	 */
	public void setDeltaPosition( final FacilityGeoPosition deltaPosition ) {
		this.deltaPosition = deltaPosition;
		// Calculate canvas location from delta location that is referenced to center.
		this.longitudeDelta = this.deltaPosition.getLongitude();
		this.latitudeDelta = this.deltaPosition.getLatitude();
		this.posx = Math.round(this.longitudeDelta * CONVERSION_LONGITUDE_MULTIPLIER);
		this.posy = Math.round(this.latitudeDelta * CONVERSION_LATITUDE_MULTIPLIER);
	}

	/**
	 * Get the model from where to get all the parameters and calculations instead having a lot of methods
	 * to enter that data. This way we are sure we have all the parameter set before the <code>onDraw</code>
	 * is called.
	 */
	public void setPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
		this.planetaryFacility = planetaryFacility;
		this.setImageBitmap(this.createBitmap());
		this.drawableId = this.planetaryFacility.getIconReferenceId();
	}

	public void setScale( final double newscale ) {
		this.scale = newscale;
	}

	// - D R A W I N G
	protected void centerCanvas( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
		canvas.translate(posx, posy);
//		canvas.drawColor(0x00000000);
		// Restart the paint.
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * Draw the facility color ring. Has no data information, just a ring to set the facility coverage area.
	 * @param canvas
	 */
	protected void drawFacilityRing( final Canvas canvas ) {
		paint.setColor(this.getColor(this.planetaryFacility.getStructureColorReference()));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION, -90.0F
				, 360.0F, false, paint);
	}

	protected void drawFacilityIcon( final Canvas canvas ) {
		//		paint.setColor(this.getColor(R.color.pi_extractoriconcolor));
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		Bitmap structureIconBitmap = BitmapFactory.decodeResource(this.getResources(), this.drawableId);
		structureIconBitmap = this.changeImageColor(structureIconBitmap, this.getColor(this.planetaryFacility.getIconColorReference()));
		final Rect structureIconRect = new Rect(0, 0, structureIconBitmap.getWidth(), structureIconBitmap.getHeight());
		final Rect structureIconDestination = new Rect(-STRUCTURE_ICON_DIMENSION, -STRUCTURE_ICON_DIMENSION, STRUCTURE_ICON_DIMENSION, STRUCTURE_ICON_DIMENSION);
		canvas.drawBitmap(structureIconBitmap, structureIconRect, structureIconDestination, paint);
	}

	protected Bitmap createBitmap() {
		this.bitmap = Bitmap.createBitmap(STRUCTURE_SIZE, STRUCTURE_SIZE, Bitmap.Config.ARGB_8888);
		return bitmap;
	}

	protected int getColor( final int colorReference ) {
		return this.getResources().getColor(colorReference);
	}

	protected Bitmap changeImageColor( final Bitmap sourceBitmap, int color ) {
		Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
				sourceBitmap.getWidth() - 1,
				sourceBitmap.getHeight() - 1);
		Paint p = new Paint();
		ColorFilter filter = new LightingColorFilter(color, 1);
		p.setColorFilter(filter);
		Canvas canvas = new Canvas(resultBitmap);
		canvas.drawBitmap(resultBitmap, 0, 0, p);
		return resultBitmap;
	}

	/** Return the number of seconds elapsed on the current cycle. */
	protected long getCycleElapsed() {
		return Instant.now().getMillis() / 1000 - this.planetaryFacility.getLastCycleStart().getMillis() / 1000;
	}

	// - B U I L D E R
	protected static abstract class BaseBuilder<T extends APlanetaryStructureImageView, B extends APlanetaryStructureImageView.BaseBuilder> {
		protected final Context context;
		protected T actualClass;
		protected B actualClassBuilder;

		protected abstract T getActual();

		protected abstract B getActualBuilder();

		public BaseBuilder( final Context context ) {
			this.context = context;
			this.actualClass = getActual();
			this.actualClassBuilder = getActualBuilder();
		}

		public B withDeltaPosition( @NonNull final FacilityGeoPosition deltaPosition ) {
			this.actualClass.setDeltaPosition(deltaPosition);
			return this.actualClassBuilder;
		}

		public B withFacility( @NonNull final IPlanetaryFacility planetaryFacility ) {
			this.actualClass.setPlanetaryFacility(planetaryFacility);
			return this.actualClassBuilder;
		}

		public B withScale( @NonNull final double scale ) {
			this.actualClass.setScale(scale);
			return this.actualClassBuilder;
		}

		public T build() {
			// Do any other validations. If failed then launch an exception.
			AppCompatibilityUtils.assertNotNull(this.actualClass.deltaPosition);
			AppCompatibilityUtils.assertNotNull(this.actualClass.planetaryFacility);
			return actualClass;
		}
	}
}
