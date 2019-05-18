package org.dimensinfin.eveonline.neocom.sharedcode.provider;

import java.util.Objects;

import android.app.Application;

import org.dimensinfin.eveonline.neocom.conf.IGlobalPreferencesManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
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
	private ESIGlobalAdapter esiAdapter;
	private DataDownloaderService downloaderService;
	private MiningRepository miningRepository;
	private EveItemProvider eveItemProvider;

	private Application applicationContext;
	private IFileSystem fileSystemAdapter;
	private IGlobalPreferencesManager preferencesProvider;


	public static NeoComAndroid getSingleton() {
		if (null == singleton)
			throw new NeoComRuntimeException("NeoCom gloabl singleton is not instantiated. Please complete initilisation.");
		return singleton;
	}

	public IConfigurationProvider getConfigurationProvider() {
		return this.configurationProvider;
	}

	public ESIGlobalAdapter getEsiAdapter() {
		return this.esiAdapter;
	}

	public DataDownloaderService getDownloaderService() {
		return this.downloaderService;
	}

	public MiningRepository getMiningRepository() {
		return this.miningRepository;
	}

	public EveItemProvider getEveItemProvider() {
		return this.eveItemProvider;
	}

	// - B U I L D E R
	public static class Singletoner {
		private NeoComAndroid onConstruction;

		public Singletoner() {
			this.onConstruction = new NeoComAndroid();
		}

		public Singletoner withApplicationContext( final Application application ) {
			this.onConstruction.applicationContext = application;
			return this;
		}

		public Singletoner withFileSystemAdapter( final IFileSystem fileSystemAdapter ) {
			this.onConstruction.fileSystemAdapter = fileSystemAdapter;
			return this;
		}

		public Singletoner withConfigurationProvider( final IConfigurationProvider configurationProvider ) {
			this.onConstruction.configurationProvider = configurationProvider;
			return this;
		}

		public Singletoner withPreferencesProvider( final IGlobalPreferencesManager preferencesProvider ) {
			this.onConstruction.preferencesProvider = preferencesProvider;
			return this;
		}

		public Singletoner withESIAdapter( final ESIGlobalAdapter esiAdapter ) {
			this.onConstruction.esiAdapter = esiAdapter;
			return this;
		}

		public Singletoner withDataDownloaderService( final DataDownloaderService downloaderService ) {
			this.onConstruction.downloaderService = downloaderService;
			return this;
		}

		public Singletoner withMiningRepository( final MiningRepository miningRepository ) {
			this.onConstruction.miningRepository = miningRepository;
			return this;
		}

		public Singletoner withEveItemProvider( final EveItemProvider eveItemProvider ) {
			this.onConstruction.eveItemProvider = eveItemProvider;
			return this;
		}

		public NeoComAndroid build() {
			singleton = this.onConstruction;
			Objects.requireNonNull(this.onConstruction.configurationProvider);
			Objects.requireNonNull(this.onConstruction.esiAdapter);
			Objects.requireNonNull(this.onConstruction.downloaderService);
			Objects.requireNonNull(this.onConstruction.miningRepository);
			Objects.requireNonNull(this.onConstruction.eveItemProvider);
			return this.onConstruction;
		}
	}
}
