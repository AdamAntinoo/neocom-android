package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.core.AppCompatibilityUtils;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class APlanetaryStructureImageView extends ImageView {
	protected static Logger logger = LoggerFactory.getLogger(APlanetaryStructureImageView.class);

	// - D R A W I N G   C O N S T A N T S
	protected static final int STRUCTURE_SIZE = 120;
	protected static final int DIMENSION = STRUCTURE_SIZE / 2;
	protected static final Float LATITUDE_MULTIPLIER = (1.0F / 0.005608293F) * DIMENSION;
	protected static final Float LONGITUDE_MULTIPLIER = (1.0F / 0.013409427F) * STRUCTURE_SIZE;
	protected static final int FACILITY_RING_WIDTH = 10;
	protected static final int CYCLE_WIDTH = 5;
	protected static final int STRUCTURE_ICON_DIMENSION = 30;
	protected static final int CYCLE_RADIUS = STRUCTURE_SIZE / 2 - FACILITY_RING_WIDTH - FACILITY_RING_WIDTH;
	protected static final int CYCLE_BORDER_DISTANCE = (STRUCTURE_SIZE / 2) - CYCLE_RADIUS;
	protected static final int CYCLE_DIMENSION = STRUCTURE_SIZE / 2 - CYCLE_BORDER_DISTANCE;

	// - F I E L D S
	/** This is the planetaryFacility to be rendered. This data should contain all the data required for the rendering. */
	protected IPlanetaryFacility planetaryFacility;
	protected double scale = 1.0;
	protected Bitmap bitmap;

	// - C O N S T R U C T O R S
	protected APlanetaryStructureImageView( final Context context ) {
		super(context);
	}

	// - C O N T E N T   A P I

	/**
	 * Get the model from where to get all the parameters and calculations instead having a lot of methods
	 * to enter that data. This way we are sure we have all the parameter set before the <code>onDraw</code>
	 * is called.
	 */
	public void setPlanetaryFacility( final IPlanetaryFacility planetaryFacility ) {
		this.planetaryFacility = planetaryFacility;
		this.setImageBitmap(this.createBitmap());
		//		this.drawableId = this.planetaryFacility.getIconReferenceId();
	}

	public void setScale( final double newscale ) {
		this.scale = newscale;
	}

	// - D R A W I N G
	protected void centerCanvas( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
		// Convert the facility location to pixel location being the Command Center the center of coordinates.
		final float longitudeDelta = this.planetaryFacility.getGeoPosition().getLongitude() - this.planetaryFacility.getCommandCenterPosition().getLongitude();
		final float latitudeDelta = this.planetaryFacility.getGeoPosition().getLatitude() - this.planetaryFacility.getCommandCenterPosition().getLatitude();
		final int posx = -1 * Math.round(longitudeDelta * LONGITUDE_MULTIPLIER);
		final int posy = Math.round(latitudeDelta * LATITUDE_MULTIPLIER);
		canvas.translate(posx, posy);
	}

	/**
	 * Draw the facility color ring. Has no data information, just a ring to set the facility coverage area.
	 */
	protected void drawFacilityRing( final Canvas canvas, final int colorReference ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(this.getColor(colorReference));
		paint.setStrokeWidth(FACILITY_RING_WIDTH);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.BUTT);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(-DIMENSION, -DIMENSION, DIMENSION, DIMENSION, -90.0F
				, 360.0F, false, paint);
	}

	protected void drawFacilityIcon( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		Bitmap structureIconBitmap = BitmapFactory.decodeResource(this.getResources(), this.planetaryFacility.getIconReferenceId());
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

	//	/** Return the number of seconds elapsed on the current cycle. */
	//	protected long getCycleElapsed() {
	//		return Instant.now().getMillis() / 1000 - this.planetaryFacility.getLastCycleStart().getMillis() / 1000;
	//	}

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

		//		public B withDeltaPosition( @NonNull final FacilityGeoPosition deltaPosition ) {
		//			this.actualClass.setDeltaPosition(deltaPosition);
		//			return this.actualClassBuilder;
		//		}

		public B withFacility( @NonNull final IPlanetaryFacility planetaryFacility ) {
			this.actualClass.setPlanetaryFacility(planetaryFacility);
			return this.actualClassBuilder;
		}

		//		public B withScale( @NonNull final double scale ) {
		//			this.actualClass.setScale(scale);
		//			return this.actualClassBuilder;
		//		}

		public T build() {
			// Do any other validations. If failed then launch an exception.
			//			AppCompatibilityUtils.assertNotNull(this.actualClass.deltaPosition);
			AppCompatibilityUtils.assertNotNull(this.actualClass.planetaryFacility);
			return actualClass;
		}
	}
}
