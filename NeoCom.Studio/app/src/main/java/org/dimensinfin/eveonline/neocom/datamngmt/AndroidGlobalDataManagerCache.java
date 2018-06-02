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
package org.dimensinfin.eveonline.neocom.datamngmt;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidGlobalDataManagerCache extends GlobalDataManager {
	private static Map<String, Drawable> drawablesCache = new Hashtable<>();

	public static Future<Drawable> getCacheDrawable (final String url, final ImageView targetImage, final Activity
			context) {
		// Download the image on a background task and send it to the target.
		final Future<?> fut = AndroidGlobalDataManagerCache.submitJob2ui(() -> {
			//			ImageView targetImage = null;
			//			Drawable source = null;
			InputStream is = null;
			URLConnection urlConn = null;
			try {
				urlConn = new URL(url).openConnection();
				is = urlConn.getInputStream();
				final Drawable source = Drawable.createFromStream(is, "src");
				drawablesCache.put(url, source);
				context.runOnUiThread(() -> {
					targetImage.setImageDrawable(source);
					targetImage.invalidate();
				});
			} catch (final Exception ex) {
			} finally {
				try {
					if ( is != null ) {
						is.close();
					}
				} catch (final IOException e) {
				}
			}
		});
		return null;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
