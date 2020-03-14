package org.dimensinfin.eveonline.neocom.test.support.adapters;

import java.util.Objects;

import com.j256.ormlite.dao.Dao;

import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtractionEntity;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;

public class SupportMiningRepository extends MiningRepository {
	// - B U I L D E R
	public static class Builder {
		private SupportMiningRepository onConstruction;

		public Builder() {
			this.onConstruction = new SupportMiningRepository();
		}

		public SupportMiningRepository build() {
			Objects.requireNonNull( this.onConstruction.miningExtractionDao );
			Objects.requireNonNull( this.onConstruction.locationCatalogService );
			return this.onConstruction;
		}

		public SupportMiningRepository.Builder withLocationCatalogService( final LocationCatalogService locationCatalogService ) {
			Objects.requireNonNull( locationCatalogService );
			this.onConstruction.locationCatalogService = locationCatalogService;
			return this;
		}

		public SupportMiningRepository.Builder withMiningExtractionDao( final Dao<MiningExtractionEntity, String> miningExtractionDao ) {
			Objects.requireNonNull( miningExtractionDao );
			this.onConstruction.miningExtractionDao = miningExtractionDao;
			return this;
		}
	}
}
