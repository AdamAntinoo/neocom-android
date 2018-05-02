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

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class GlobalDataManagerStorage extends GlobalDataManager {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("GlobalDataManagerStorage");
	private static Application application = null;

	// --- A N D R O I D   C O N T E X T   S E C T I O N
	public static Application getApplication () {
		if ( null == application ) throw new RuntimeException("Application not connected");
		else return application;
	}

	public static Context getAppContext () {
		if ( null == application ) throw new RuntimeException("Application not connected");
		else return application.getApplicationContext();
	}

	public static void setApplicationContext (final Application app) {
		application = app;
	}

	// --- S T O R A G E   A C C E S S   S E C T I O N
	public static File accessAppStorage (final String resourceString) {
		if ( null != resourceString )
			return new File(Environment.getExternalStorageDirectory()
					, GlobalAndroidDataManager.getResourceString(R.string.appfoldername) + "/" + resourceString);
		else
			return new File(Environment.getExternalStorageDirectory(),
					GlobalAndroidDataManager.getResourceString(R.string.appfoldername));
	}

	public static AssetManager getAssets () {
		return getAppContext().getAssets();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
