package org.dimensinfin.eveonline.neocom.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import com.annimon.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */
public class GlobalAndroidConfigurationProvider extends GlobalConfigurationProvider {
	// - S T A T I C - S E C T I O N
//	private static Logger logger = LoggerFactory.getLogger(GlobalAndroidConfigurationProvider.class);

	// - F I E L D - S E C T I O N
	//	private Application app = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public GlobalAndroidConfigurationProvider( final String propertiesFolder , final IFileSystem fileSystemAdapter){
		Stream.of(propertyFiles)
				.sorted()
				.forEach(( fileName ) -> {
					logger.info("-- [GlobalAndroidConfigurationProvider.readAllProperties]> Processing file: {}", fileName);
					try {
						Properties properties = new Properties();
						final String filePath = getResourceLocation() + "/" + fileName;
						properties.load(AndroidGlobalDataManager.getApplication().getAssets().open(filePath));
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
	public List<String> getResourceFiles( String path ) {
		final List<String> propertyFiles = new ArrayList<>();

		try {
			final String[] list = AndroidGlobalDataManager.getApplication().getAssets().list(path);
			if (list.length > 0) {
				for (String file : list) {
					if (file.indexOf(".") < 0) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
						//						System.out.println("This is a folder = "+path+"/"+file);
						final List<String> dirList = getResourceFiles(file); // <<-- To get subdirectory files and directories list and check
						// Copy the file list to the results.
						propertyFiles.addAll(dirList);
					} else {
						if (file.toLowerCase().contains(".properties"))
							propertyFiles.add(file);
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return propertyFiles;
	}

	// - B U I L D E R
	public static class Builder {
		private GlobalAndroidConfigurationProvider onConstruction;

		public Builder( final String propertiesLocation , final IFileSystem fileSystemAdapter ) {
			this.onConstruction = new GlobalAndroidConfigurationProvider(propertiesLocation,fileSystemAdapter);
		}

		public GlobalAndroidConfigurationProvider build() {
			return this.onConstruction;
		}
	}
}
