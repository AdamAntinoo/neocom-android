package org.dimensinfin.eveonline.neocom.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;

public class AssetsRepository {
	// - C O M P O N E N T S
	protected Dao<NeoAsset, String> assetsDao;

	/**
	 * Get the complete list of the assets that belong to this owner.
	 */
	public List<NeoAsset> findAllByOwnerId( final Integer ownerId ) {
		Objects.requireNonNull( ownerId );
		try {
			QueryBuilder<NeoAsset, String> queryBuilder = this.assetsDao.queryBuilder();
			Where<NeoAsset, String> where = queryBuilder.where();
			where.eq( "ownerID", ownerId );
			final List<NeoAsset> assetList = assetsDao.query( queryBuilder.prepare() );
			NeoComApplication.logger.info( "-- Assets read: " + assetList.size() );
			return assetList;
		} catch (java.sql.SQLException sqle) {
			NeoComApplication.logger.error( "SQL [AssetsRepository.findAllByOwnerId]> SQL Exception: {}", sqle.getMessage() );
			return new ArrayList<>();
		}
	}

	// - B U I L D E R
	public static class Builder {
		private AssetsRepository onConstruction;

		public Builder() {
			this.onConstruction = new AssetsRepository();
		}

		public AssetsRepository.Builder withAssetDao( final Dao<NeoAsset, String> assetsDao ) {
			Objects.requireNonNull( assetsDao );
			this.onConstruction.assetsDao = assetsDao;
			return this;
		}

		public AssetsRepository build() {
			Objects.requireNonNull( this.onConstruction.assetsDao );
			return this.onConstruction;
		}
	}
}