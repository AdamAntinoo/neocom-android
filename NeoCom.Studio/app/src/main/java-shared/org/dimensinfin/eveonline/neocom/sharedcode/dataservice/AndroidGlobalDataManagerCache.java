package org.dimensinfin.eveonline.neocom.sharedcode.dataservice;

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

import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;

/**
 * @author Adam Antinoo
 */
public class AndroidGlobalDataManagerCache extends GlobalDataManager {
	private static Map<String, Drawable> drawablesCache = new Hashtable<>();

	public Future<Drawable> getCacheDrawable ( final String url, final ImageView targetImage, final Activity
			context) {
		// Download the image on a background task and send it to the target.
		final Future<?> fut = AndroidGlobalDataManagerCache.getSingleton().submitJob(() -> {
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
