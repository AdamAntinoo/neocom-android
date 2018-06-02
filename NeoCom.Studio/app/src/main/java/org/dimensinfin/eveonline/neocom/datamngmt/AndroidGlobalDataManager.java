//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
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
import android.os.Environment;

import org.dimensinfin.eveonline.neocom.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * This static class centralizes all the functionalities to access data. It will provide a consistent api to the rest
 * of the application and will hide the internals of how that data is obtained, managed and stored.
 * All thet now are direct database access or cache access or even Model list accesses will be hidden under an api
 * that will decide at any point from where to get the information and if there are more jobs to do to keep
 * that information available and up to date.
 * <p>
 * The initial release will start transferring the ModelFactory functionality.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidGlobalDataManager extends AndroidGlobalDataManagerCache {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AndroidGlobalDataManager");

	// --- A N D R O I D   C O N T E X T   S E C T I O N
	private static Application application = null;

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

	// --- G L O B A L   A N D R O I D   D A T A   A C C E S S
	// TODO - Call the android api to solve this check.
	public static boolean sdcardAvailable () {
		return true;
	}

	public static String getResourceString (final int resourceid) {
		return getApplication().getResources().getString(resourceid);
	}

	public static String getURLForItem (final int typeID) {
		final String iconUrl = "http://image.eveonline.com/Type/" + typeID + "_64.png";
		return iconUrl;
	}

	public static String getURLForStation (final int typeID) {
		final String iconUrl = "http://image.eveonline.com/Render/" + typeID + "_64.png";
		return iconUrl;
	}

	public static boolean checkAppFile (final String resourceString) {
		return checkStorageResource(AndroidGlobalDataManager.accessAppStorage4Path(resourceString));
	}

	public static File accessAppStorageFile (final String resourceString) {
		if ( null != resourceString )
			return new File(Environment.getExternalStorageDirectory()
					, AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname)
					+ "/" + resourceString);
		else
			return new File(Environment.getExternalStorageDirectory()
					, AndroidGlobalDataManager.getApplication().getResources().getString(R.string.appname));
	}
}
// - UNUSED CODE ............................................................................................
//[01]
