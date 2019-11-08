package org.dimensinfin.eveonline.neocom.provider;

import java.util.Objects;

import org.dimensinfin.eveonline.neocom.adapters.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.adapters.StoreCacheManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;


import io.reactivex.Single;

public class AndroidLocationCatalogService extends LocationCatalogService {
	// - C O M P O N E N T S
	private StoreCacheManager cacheManager;

	// - C O N S T R U C T O R S
	private AndroidLocationCatalogService() {}

	public SystemLocation searchSpaceLocation4Id( final Integer spaceIdentifier ) {
		return this.cacheManager.accessSpaceLocation( spaceIdentifier );
	}

	// - B U I L D E R
	public static class Builder {
		private AndroidLocationCatalogService onConstruction;

		public Builder() {
			this.onConstruction = new AndroidLocationCatalogService();
		}

		public AndroidLocationCatalogService.Builder withStoreCacheManager( final StoreCacheManager cacheManager ) {
			Objects.requireNonNull(cacheManager);
			this.onConstruction.cacheManager = cacheManager;
			return this;
		}

		public AndroidLocationCatalogService build() {
			Objects.requireNonNull( this.onConstruction.cacheManager );
			// Register on the manager the caches required by this component.
			this.onConstruction.cacheManager.registerCache();
			return this.onConstruction;
		}
	}
}