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

import android.os.Environment;

import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class FileSystemAndroidImplementation implements IFileSystem {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("FileSystemAndroidImplementation");

	// - F I E L D - S E C T I O N ............................................................................
	private String applicationFolder = "NeoCom";

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public FileSystemAndroidImplementation (final String applicationStoreDirectory) {
		logger.info(">< [FileSystemAndroidImplementation.constructor]> applicationStoreDirectory: {}", applicationStoreDirectory);
		if ( null != applicationStoreDirectory )
			this.applicationFolder = applicationStoreDirectory;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public InputStream openResource4Input (final String filePath) throws IOException {
		return new FileInputStream(new File(applicationFolder + "/" + filePath));
	}

	@Override
	public OutputStream openResource4Output (final String filePath) throws IOException {
		return new FileOutputStream(new File(applicationFolder + "/" + filePath));
	}

	@Override
	public InputStream openAsset4Input (final String filePath) throws IOException {
		return AndroidGlobalDataManager.getApplication().getAssets().open(filePath);
	}

	/**
	 * The is no direct access to the root path for the assets. The way to get it is to get the first record of the
	 * list of assets and then remove the asset name. This should return the equivalent string for the root for the
	 * assets location.
	 * @param filePath filepath for the asset to get the whole asset path.
	 * @return
	 * @throws IOException
	 */
	@Override
	public String accessAsset4Path (final String filePath) throws IOException {
		try {
			final String[] assetFileList = AndroidGlobalDataManager.getApplication().getAssets().list(".");
			return assetFileList[0] + filePath;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return "./" + filePath;
		}
	}

	@Override
	public String accessResource4Path (final String filePath) {
		return applicationFolder + "/" + filePath;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
