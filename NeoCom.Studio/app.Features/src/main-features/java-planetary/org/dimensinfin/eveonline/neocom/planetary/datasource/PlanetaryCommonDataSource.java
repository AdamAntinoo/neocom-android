package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;

public abstract class PlanetaryCommonDataSource extends MVCDataSource {
	protected Credential credential;
	protected ESIDataAdapter esiDataAdapter;
	protected PlanetaryRepository planetaryRepository;

	protected ESIDataAdapter getEsiDataAdapter() {
		return this.esiDataAdapter;
	}

	protected Credential getCredential() {
		return credential;
	}

	// - B U I L D E R
	public static abstract  class Builder<T extends MVCDataSource,B extends MVCDataSource.Builder> extends MVCDataSource.Builder<T ,B> {
		private Credential credential;
		private ESIDataAdapter esiDataAdapter;
		private PlanetaryRepository planetaryRepository;

		public B withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.credential = credential;
			return this.actualClassBuilder;
		}

		public B withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.esiDataAdapter = esiDataAdapter;
			return this.actualClassBuilder;
		}

		public B withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.planetaryRepository = planetaryRepository;
			return this.actualClassBuilder;
		}

		public T build() {
			Objects.requireNonNull(this.credential);
			Objects.requireNonNull(this.esiDataAdapter);
			Objects.requireNonNull(this.planetaryRepository);
			((PlanetaryCommonDataSource) this.getActual()).credential = this.credential;
			((PlanetaryCommonDataSource) this.getActual()).esiDataAdapter = this.esiDataAdapter;
			((PlanetaryCommonDataSource) this.getActual()).planetaryRepository = this.planetaryRepository;
			return this.getActual();
		}
	}
}
