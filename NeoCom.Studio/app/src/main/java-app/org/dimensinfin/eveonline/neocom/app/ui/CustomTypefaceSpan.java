//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.app.ui;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class CustomTypefaceSpan extends TypefaceSpan {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("CustomTypefaceSpan");

	// - F I E L D - S E C T I O N ............................................................................
	private final Typeface newType;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public CustomTypefaceSpan( String family, Typeface type ) {
		super(family);
		newType = type;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void updateDrawState( TextPaint ds ) {
		applyCustomTypeFace(ds, newType);
	}

	@Override
	public void updateMeasureState( TextPaint paint ) {
		applyCustomTypeFace(paint, newType);
	}

	private static void applyCustomTypeFace( Paint paint, Typeface tf ) {
		int oldStyle;
		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~tf.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
