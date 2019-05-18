package org.dimensinfin.eveonline.neocom.app;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.app.mock.ESINetworkManagerMock;
import org.dimensinfin.eveonline.neocom.app.mock.NeoComRetrofitMock;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.jetbrains.annotations.NotNull;

public class ESIAndroidAdapter extends ESINetworkManagerMock {
	// - C O N S T R U C T O R S
	protected ESIAndroidAdapter( final IConfigurationProvider configurationProvider, final IFileSystem fileSystemAdapter ) {
		this.configurationProvider = configurationProvider;
		this.fileSystemAdapter = fileSystemAdapter;
	}

	public void initialise() {
		super.initialise();
		neocomRetrofitMock = new NeoComRetrofitMock.Builder()
				                     //				                     .withNeoComOAuth20(this.getConfiguredOAuth("Tranquility"))
				                     .withEsiServerLocation(this.configurationProvider.getResourceString("P.esi.data.server.location.mock"
						                     , "http://localhost:9000/"))
				                     //				                     .withAgent(AGENT)
				                     .withCacheDataFile(cacheDataFile)
				                     .withCacheSize(CACHE_SIZE)
				                     .withTimeout(TIMEOUT)
				                     .build();

	}

	// - B U I L D E R
	public static class Builder {
		protected ESIGlobalAdapter onConstruction;

		public Builder( @NotNull final IConfigurationProvider configurationProvider, @NotNull final IFileSystem fileSystemAdapter ) {
			Objects.requireNonNull(configurationProvider);
			Objects.requireNonNull(fileSystemAdapter);
			this.onConstruction = new ESIAndroidAdapter(configurationProvider, fileSystemAdapter);
		}

		public ESIGlobalAdapter build() {
			// Run the initialisation code.
			this.onConstruction.initialise();
			singleton = this.onConstruction;
			return this.onConstruction;
		}
	}
}
