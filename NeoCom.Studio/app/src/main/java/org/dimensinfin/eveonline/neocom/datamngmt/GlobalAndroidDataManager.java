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
package org.dimensinfin.eveonline.neocom.datamngmt.manager;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This static class centralizes all the functionalities to access data. It will provide a consistent api to the rest
 * of the application and will hide the internals of how that data is obtained, managed and stored.
 * All thet now are direct database access or cache access or even Model list accesses will be hidden under an api
 * that will decide at any point from where to get the information and if there are more jobs to do to keep
 * that information available and up to date.
 * <p>
 * The initial release will start transferring the ModelFactory functionality.
 *
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class GlobalAndroidDataManager extends GlobalDataManagerStorage {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("GlobalAndroidDataManager");

	//	/**
	//	 * GDM singleton to store all data, caches and references. The use of a singleton will allow to drop all data
	//	 * on a single operation and restart all data caches.
	//	 */
	//	public static final GlobalAndroidDataManager GADM = new GlobalAndroidDataManager();
	//	/** Executor to be used on background activities on the OnClick Part statements. */
	//	private static final ExecutorService onClickExecutor = Executors.newSingleThreadExecutor();
	//	public static void submitOnClick(final Runnable task){
	//		onClickExecutor.submit(task);
	//	}

	// --- G L O B A L   A N D R O I D   D A T A   A C C E S S
	public static String getResourceString (final int resourceid) {
		if ( null != singleton ) return singleton.getResources().getString(resourceid);
		else return "";
	}
}
// - UNUSED CODE ............................................................................................
//[01]
