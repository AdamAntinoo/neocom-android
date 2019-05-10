package org.dimensinfin.eveonline.neocom.app;

import java.io.File;

import org.dimensinfin.eveonline.neocom.auth.NeoComOAuth20;
import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;

public class ESIAdapter extends ESINetworkManager {
	private IConfigurationProvider configurationProvider;

	public ESIAdapter( final IConfigurationProvider configurationProvider ) {
		this.configurationProvider = configurationProvider;
	}

	private void initialise() {
		logger.info(">> [ESIAdapter.initialize]");
		// Read the configuration and open the ESI requests cache.
		cacheDataFile = new File(GlobalDataManager.accessResource4Path(cacheFilePath));
		// Read the scoped from a resource file
		constructScopes();

		// Initialize global constants from configuration files.
		CLIENT_ID = this.configurationProvider.getResourceString("R.esi.authorization.clientid");
		SECRET_KEY = this.configurationProvider.getResourceString("R.esi.authorization.secretkey");
		CALLBACK = this.configurationProvider.getResourceString("R.esi.authorization.callback");
		AGENT = this.configurationProvider.getResourceString("R.esi.authorization.agent");
		// Verify that the constants have values. Otherwise launch exception.
		if (CLIENT_ID.isEmpty())
			throw new NeoComRuntimeException("RT [ESIAdapter.initialize]> ESI configuration property is empty.");
		if (SECRET_KEY.isEmpty())
			throw new NeoComRuntimeException("RT [ESIAdapter.initialize]> ESI configuration property is empty.");
		if (CALLBACK.isEmpty())
			throw new NeoComRuntimeException("RT [ESIAdapter.initialize]> ESI configuration property is empty.");
		neocomAuth20 = new NeoComOAuth20(CLIENT_ID, SECRET_KEY, CALLBACK, AGENT, STORE, SCOPES);
		neocomRetrofit = NeoComRetrofitHTTP.build(neocomAuth20, AGENT, cacheDataFile, cacheSize, timeout);
		logger.info("<< [ESIAdapter.initialize]");
	}

	// - B U I L D E R
	public static class Builder {
		private ESIAdapter onConstruction;

		public Builder( final IConfigurationProvider configurationProvider ) {
			this.onConstruction = new ESIAdapter(configurationProvider);
		}

		public ESIAdapter build() {
			// Run the initialisation code.
			this.onConstruction.initialise();
			return this.onConstruction;
		}
	}
}
