package org.dimensinfin.eveonline.neocom.app;

import java.util.Objects;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.DataDownloaderService;
import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManagerZBase;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

/**
 * @author Adam Antinoo
 */
public class NeoComAndroid {
	private static NeoComAndroid singleton;

	private Application applicationContext;
	private IFileSystem fileSystemAdapter;
	private IConfigurationProvider configurationProvider;
	private IGlobalPreferencesManager preferencesProvider;
	private ESINetworkManagerZBase esiAdapter;

	private DataDownloaderService downloaderService;

	//	private static IFileSystem fsAdapter = null;

	//	public static IFileSystem getFileSystemAdapter() {
	//		return fsAdapter;
	//	}
	//
	//	public NeoComAndroid() {
	//		fsAdapter = null;
	//	}
	public static NeoComAndroid getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException("NeoCom gloabl singleton is not instantiated. Please complete initilisation.");
		return singleton;
	}
	public static <T> T assertNotNull(final T target) {
		if (null == target) throw new NullPointerException("Target object is not valid. Found to be null.");
		return target;
	}

	public static DataDownloaderService getDataDownloaderService() {
		return getSingleton().getDownloaderService();
	}

	public DataDownloaderService getDownloaderService() {
		return this.downloaderService;
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

		public Builder withESIAdapter( final ESINetworkManagerZBase esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}
		public Builder withDataDownloaderService( final DataDownloaderService downloaderService ) {
			this.onConstruction.downloaderService = downloaderService;
			return this;
		}

		public NeoComAndroid build() {
			singleton = this.onConstruction;
			NeoComAndroid.assertNotNull(this.onConstruction.downloaderService);
			return this.onConstruction;
		}
	}
}
