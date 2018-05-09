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
package org.dimensinfin.eveonline.neocom.conf;

import com.annimon.stream.Stream;

import org.dimensinfin.eveonline.neocom.datamngmt.GlobalAndroidDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class GlobalAndroidConfigurationProvider extends GlobalConfigurationProvider {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("GlobalAndroidConfigurationProvider");

	// - F I E L D - S E C T I O N ............................................................................
	//	private Application app = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public GlobalAndroidConfigurationProvider (final String propertiesFolder) {
		super(propertiesFolder);
		//		this.app = app;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	protected void readAllProperties () throws IOException {
		logger.info(">> [GlobalAndroidConfigurationProvider.readAllProperties]");
		// Read all .properties files under the predefined path on the /resources folder.
		final List<String> propertyFiles = getResourceFiles(getResourceLocation());
		Stream.of(propertyFiles)
		      .sorted()
		      .forEach((fileName) -> {
			      logger.info("-- [GlobalAndroidConfigurationProvider.readAllProperties]> Processing file: {}", fileName);
			      try {
				      Properties properties = new Properties();
				      final String filePath = getResourceLocation() + "/" + fileName;
				      properties.load(GlobalAndroidDataManager.getApplication().getAssets().open(filePath));
				      // Copy properties to globals.
				      globalConfigurationProperties.putAll(properties);
			      } catch (IOException ioe) {
				      logger.error("E [GlobalAndroidConfigurationProvider.readAllProperties]> Exception reading properties file {}. {}",
						      fileName, ioe.getMessage());
				      ioe.printStackTrace();
			      }
		      });
		logger.info("<< [GlobalAndroidConfigurationProvider.readAllProperties]> Total properties number: {}", contentCount());
	}

	/**
	 * This function generates a list for all the property files inside the selected directory and all inside
	 * directories. We only process <b>.properties</b> files.
	 *
	 * @param path the path where to look for files. This path is based on <b>assets/</b>
	 * @return list of pathnames for property files.
	 */
	public List<String> getResourceFiles (String path) {
		final List<String> propertyFiles = new ArrayList<>();

		try {
			final String[] list = GlobalAndroidDataManager.getApplication().getAssets().list(path);
			if ( list.length > 0 ) {
				for (String file : list) {
					if ( file.indexOf(".") < 0 ) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
						//						System.out.println("This is a folder = "+path+"/"+file);
						final List<String> dirList = getResourceFiles(file); // <<-- To get subdirectory files and directories list and check
						// Copy the file list to the results.
						propertyFiles.addAll(dirList);
					} else {
						if ( file.toLowerCase().contains(".properties") )
							propertyFiles.add(file);
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return propertyFiles;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
