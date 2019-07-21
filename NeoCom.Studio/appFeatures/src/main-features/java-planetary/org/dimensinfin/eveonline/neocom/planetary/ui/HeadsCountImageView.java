package org.dimensinfin.eveonline.neocom.planetary.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadsCountImageView extends ImageView {
	protected static Logger logger = LoggerFactory.getLogger(HeadsCountImageView.class);
	private int headsCount = 0;

	public HeadsCountImageView( final Context context ) {
		super(context);
	}

	public HeadsCountImageView( final Context context, @Nullable final AttributeSet attrs ) {
		super(context, attrs);
	}

	public HeadsCountImageView( final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr ) {
		super(context, attrs, defStyleAttr);
	}

	public HeadsCountImageView( final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes ) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public HeadsCountImageView setHeadsCount( final int headsCount ) {
		this.headsCount = headsCount;
		return this;
	}

	@Override
	protected void onDraw( final Canvas canvas ) {
		logger.info(">> [HeadsCountImageView.onDraw]");
		// Move canvas to center to allow drawing symmetries. Paint all area transparent.
		try {
			this.centerCanvas(canvas);
			this.drawHeadCountNumber(canvas);
		} catch (Exception ex) {
			super.onDraw(canvas);
		}
		super.onDraw(canvas);
		logger.info("<< [HeadsCountImageView.onDraw]");
	}

	// - D R A W I N G
	protected void centerCanvas( final Canvas canvas ) {
		canvas.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
	}

	private void drawHeadCountNumber( final Canvas canvas ) {
		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(40);
		paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/alte-din-1451-mittelschrift.bold.ttf"));
		canvas.drawText(Integer.valueOf(this.headsCount).toString(), 0, 5, paint);
	}
}
