package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.annimon.stream.Stream;

import org.dimensinfin.eveonline.neocom.adapter.AConfigurationProvider;

/**
 * @author Adam Antinoo
 */
public class AndroidConfigurationProvider extends AConfigurationProvider {
	private Context applicationContext;

	// - C O N S T R U C T O R S
	private AndroidConfigurationProvider() {}
//	private AndroidConfigurationProvider( final String propertiesFolder ) {
//		super(propertiesFolder);
//	}

	public AndroidConfigurationProvider setApplicationContext( final Context applicationContext ) {
		this.applicationContext = applicationContext;
		return this;
	}

	protected void readAllProperties() {
		logger.info(">> [AndroidConfigurationProvider.readAllProperties]");
		// Read all .properties files under the predefined path on the /assets folder.
		final List<String> propertyFiles = getResourceFiles(getResourceLocation());
		Stream.of(propertyFiles)
		      .sorted()
		      .forEach(( fileName ) -> {
			      logger.info("-- [AndroidConfigurationProvider.readAllProperties]> Processing file: {}", fileName);
			      try {
				      Properties properties = new Properties();
				      final String filePath = getResourceLocation() + "/" + fileName;
				      properties.load(this.applicationContext.getAssets().open(filePath));
				      // Copy properties to globals.
				      configurationProperties.putAll(properties);
			      } catch (IOException ioe) {
				      logger.error(
						      "E [AndroidConfigurationProvider.readAllProperties]> Exception reading properties file {}. {}",
						      fileName, ioe.getMessage());
				      ioe.printStackTrace();
			      }
		      });
		logger.info("<< [AndroidConfigurationProvider.readAllProperties]> Total properties number: {}", contentCount());
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
			final String[] list = this.applicationContext.getAssets().list(path);
			if (list.length > 0) {
				for (String file : list) {
					if (file.indexOf(
							".") < 0) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
						//						System.out.println("This is a folder = "+path+"/"+file);
						final List<String> dirList = getResourceFiles(
								file); // <<-- To get subdirectory files and directories list and check
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
	public static class Builder extends AConfigurationProvider.Builder {
		private AndroidConfigurationProvider onConstruction;

		@Override
		protected AConfigurationProvider getActual() {
			if (null == this.onConstruction) this.onConstruction = new AndroidConfigurationProvider();
			return this.onConstruction;
		}

		@Override
		protected AConfigurationProvider.Builder getActualBuilder() {
			return this;
		}

		public AndroidConfigurationProvider.Builder withApplicationContext( final Context applicationContext ) {
			Objects.requireNonNull(applicationContext);
			((AndroidConfigurationProvider)	this.getActual()).setApplicationContext(applicationContext);
			return this;
		}

		public AndroidConfigurationProvider.Builder withConfigurationDirectory( final String configurationDirectory ) {
			Objects.requireNonNull(configurationDirectory);
			this.getActual().setConfiguredPropertiesDirectory(configurationDirectory);
			return this;
		}

		public AndroidConfigurationProvider build() throws IOException {
			Objects.requireNonNull(this.onConstruction.applicationContext);
			final AndroidConfigurationProvider provider = (AndroidConfigurationProvider) super.build();
			provider.readAllProperties();
			return provider;
		}
	}
}
