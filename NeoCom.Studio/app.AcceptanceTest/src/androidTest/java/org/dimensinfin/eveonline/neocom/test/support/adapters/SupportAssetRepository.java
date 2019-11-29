package org.dimensinfin.eveonline.neocom.test.support.adapters;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;

public class SupportAssetRepository extends AssetRepository {
	protected SupportAssetRepository() {}

	public int deleteAll() {
		try {
			final DeleteBuilder<NeoAsset, UUID> deleteBuilder = this.assetDao.deleteBuilder();
			deleteBuilder.where().isNotNull( "uniqueCredential" );
			final int recordsDeleted = deleteBuilder.delete();
			NeoComLogger.info( "Deleted {} records", recordsDeleted + "" );
			return recordsDeleted;
		} catch (SQLException sqle) {
			NeoComLogger.info( "SQL exception while deleting records: {}", sqle.getMessage() );
			return 0;
		}
	}

	// - B U I L D E R
	public static class Builder {
		protected SupportAssetRepository onConstruction;

		public Builder() {
			this.onConstruction = new SupportAssetRepository();
		}

		public SupportAssetRepository.Builder withAssetDao( final Dao<NeoAsset, UUID> assetDao ) {
			this.onConstruction.assetDao = assetDao;
			return this;
		}

		public SupportAssetRepository build() {
			Objects.requireNonNull( this.onConstruction.assetDao );
			return this.onConstruction;
		}
	}
}
