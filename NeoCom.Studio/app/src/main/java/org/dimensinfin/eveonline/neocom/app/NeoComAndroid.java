package org.dimensinfin.eveonline.neocom.app;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

/**
 * @author Adam Antinoo
 */
public class NeoComAndroid {
	private Application applicationContext;
	private IFileSystem fileSystemAdapter;
	private IConfigurationProvider configurationProvider;
	private IGlobalPreferencesManager preferencesProvider;
	private ESIAdapter esiAdapter;

	private static IFileSystem fsAdapter = null;

	public static IFileSystem getFileSystemAdapter() {
		return fsAdapter;
	}

	public NeoComAndroid() {
		fsAdapter = null;
	}

	// - B U I L D E R
	public static class Builder {
		private NeoComAndroid onConstruction;

		public Builder() {
			this.onConstruction = new NeoComAndroid();
		}

		public Builder withApplicationContext( final Application application ) {
			this.onConstruction.applicationContext = application;
			return this;
		}

		public Builder withFileSystemAdapter( final IFileSystem fileSystemAdapter ) {
			this.onConstruction.fileSystemAdapter = fileSystemAdapter;
			return this;
		}

		public Builder withConfigurationProvider( final IConfigurationProvider configurationProvider ) {
			this.onConstruction.configurationProvider = configurationProvider;
			return this;
		}

		public Builder withPreferencesProvider( final IGlobalPreferencesManager preferencesProvider ) {
			this.onConstruction.preferencesProvider = preferencesProvider;
			return this;
		}

		public Builder withESIAdapter( final ESIAdapter esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}

		public NeoComAndroid build() {
			return this.onConstruction;
		}
	}
}
