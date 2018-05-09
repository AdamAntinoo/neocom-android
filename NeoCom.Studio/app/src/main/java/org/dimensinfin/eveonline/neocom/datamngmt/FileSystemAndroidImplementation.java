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

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class FileSystemAndroidImplementation implements IFileSystem {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("FileSystemAndroidImplementation");

	// - F I E L D - S E C T I O N ............................................................................
	private String applicationFolderName = "NeoCom";

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public FileSystemAndroidImplementation (final String applicationName) {
		super();
		this.applicationFolderName=applicationName;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public InputStream openAsset4Input (final String filePath) throws IOException {
		return GlobalAndroidDataManager.getApplication().getAssets().open(filePath);
	}

	@Override
	public InputStream openResource4Input (final String filePath) throws IOException {
		// Target file.
		final File file = accessAppStorageFile(filePath);
		return new FileInputStream(file);
	}

	@Override
	public File accessAppStorageFile (final String resourceString) {
		if ( null != resourceString )
			return new File(Environment.getExternalStorageDirectory()
					, applicationFolderName + "/" + resourceString);
		else
			return new File(Environment.getExternalStorageDirectory(), applicationFolderName);
	}
}
// - UNUSED CODE ............................................................................................
//[01]
