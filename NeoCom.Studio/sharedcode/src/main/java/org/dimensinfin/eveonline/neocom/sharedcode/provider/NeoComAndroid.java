package org.dimensinfin.eveonline.neocom.sharedcode.provider;

import java.util.Objects;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManagerZBase;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.DataDownloaderService;
import org.dimensinfin.eveonline.neocom.sharedcode.persistence.MiningRepository;

/**
 * @author Adam Antinoo
 */
public class NeoComAndroid {
	public static final String DEFAULT_ESI_SERVER = "Tranquility";

	private static NeoComAndroid singleton;

	private IConfigurationProvider configurationProvider;
	private ESINetworkManagerZBase esiAdapter;
	private DataDownloaderService downloaderService;
	private MiningRepository miningRepository;

	private Application applicationContext;
	private IFileSystem fileSystemAdapter;
	private IGlobalPreferencesManager preferencesProvider;


	public static NeoComAndroid getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException("NeoCom gloabl singleton is not instantiated. Please complete initilisation.");
		return singleton;
	}
	public static <T> T assertNotNull(final T target) {
		if (null == target) throw new NullPointerException("Target object is not valid. Found to be null.");
		return target;
	}

	public IConfigurationProvider getConfigurationProvider() {
		return this.configurationProvider;
	}

	public ESINetworkManagerZBase getEsiAdapter() {
		return esiAdapter;
	}

	public DataDownloaderService getDownloaderService() {
		return this.downloaderService;
	}

	public MiningRepository getMiningRepository() {
		return this.miningRepository;
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
		public Builder withMiningRepository( final MiningRepository miningRepository ) {
			this.onConstruction.miningRepository = miningRepository;
			return this;
		}

		public NeoComAndroid build() {
			singleton = this.onConstruction;
			Objects.requireNonNull(this.onConstruction.configurationProvider);
			Objects.requireNonNull(this.onConstruction.esiAdapter);
			Objects.requireNonNull(this.onConstruction.downloaderService);
			Objects.requireNonNull(this.onConstruction.miningRepository);
			return this.onConstruction;
		}
	}
}
