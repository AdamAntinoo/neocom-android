package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.annimon.stream.Stream;

import org.dimensinfin.eveonline.neocom.provider.AConfigurationService;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

/**
 * @author Adam Antinoo
 */
public class AndroidConfigurationService extends AConfigurationService {
	private Context applicationContext;

	// - C O N S T R U C T O R S
	private AndroidConfigurationService() {}
//	private AndroidConfigurationProvider( final String propertiesFolder ) {
//		super(propertiesFolder);
//	}

	public AndroidConfigurationService setApplicationContext( final Context applicationContext ) {
		this.applicationContext = applicationContext;
		return this;
	}

	public void readAllProperties() {
		NeoComLogger.enter();
		// Read all .properties files under the predefined path on the /assets folder.
		final List<String> propertyFiles = getResourceFiles( getResourceLocation() );
		Stream.of( propertyFiles )
				.sorted()
				.forEach( ( fileName ) -> {
					NeoComLogger.info( "Processing file: {}", fileName );
					try {
						Properties properties = new Properties();
						final String filePath = getResourceLocation() + "/" + fileName;
						properties.load( this.applicationContext.getAssets().open( filePath ) );
						// Copy properties to globals.
						configurationProperties.putAll( properties );
					} catch (IOException ioe) {
						NeoComLogger.error( "Exception reading properties file " + fileName, ioe );
						ioe.printStackTrace();
					}
				} );
		NeoComLogger.exit( "Total properties number: {}", contentCount() + "" );
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
			final String[] list = this.applicationContext.getAssets().list( path );
			if (list.length > 0) {
				for (String file : list) {
					if (file.indexOf(
							"." ) < 0) { // <<-- check if filename has a . then it is a file - hopefully directory names dont have .
						//						System.out.println("This is a folder = "+path+"/"+file);
						final List<String> dirList = getResourceFiles(
								file ); // <<-- To get subdirectory files and directories list and check
						// Copy the file list to the results.
						propertyFiles.addAll( dirList );
					} else {
						if (file.toLowerCase().contains( ".properties" ))
							propertyFiles.add( file );
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return propertyFiles;
	}

	// - B U I L D E R
	public static class Builder extends AConfigurationService.Builder<AndroidConfigurationService, AndroidConfigurationService.Builder> {
		private AndroidConfigurationService onConstruction;

		@Override
		protected AndroidConfigurationService getActual() {
			if (null == this.onConstruction) this.onConstruction = new AndroidConfigurationService();
			return this.onConstruction;
		}

		@Override
		protected AndroidConfigurationService.Builder getActualBuilder() {
			return this;
		}

		public AndroidConfigurationService.Builder withApplicationContext( final Context applicationContext ) {
			Objects.requireNonNull( applicationContext );
			this.onConstruction.applicationContext = applicationContext;
			return this;
		}

		public AndroidConfigurationService.Builder withConfigurationDirectory( final String configurationDirectory ) {
			Objects.requireNonNull( configurationDirectory );
			this.onConstruction.configuredPropertiesDirectory = configurationDirectory;
			return this;
		}

		public AndroidConfigurationService build() {
			Objects.requireNonNull( this.onConstruction.applicationContext );
			super.build();
			this.onConstruction.readAllProperties();
			return this.onConstruction;
		}
	}
}
