package org.dimensinfin.eveonline.neocom.sharedcode.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;

import org.joda.time.DateTime;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class MiningRepository {
	private static MiningRepository singleton;

	static MiningRepository getInstance() {
		return singleton;
	}

	private Dao<MiningExtraction, String> miningExtractionDao;

	private MiningRepository() { }

	/**
	 * This other method does the same Mining Extractions processing but only for the records for the current date. The difference is
	 * that today records are aggregated by hour instead of by day. So we will have a record for one ore/system since the hour we did the
	 * extractions until the 23 hours. The first extraction will add to the hour until the next hour starts. Then the accounting for this
	 * new hour will show the new ore totals and so on hour after hour.
	 */
	public List<MiningExtraction> accessTodayMiningExtractions4Pilot( final Credential credential ) throws SQLException {
		final QueryBuilder<MiningExtraction, String> builder = this.miningExtractionDao.queryBuilder();
		builder.where().eq("ownerId", credential.getAccountId());
		builder.orderBy("extractionDateName", true)
				.orderBy("extractionHour", true)
				.orderBy("solarSystemId", true)
				.orderBy("typeId", true);
		final PreparedQuery<MiningExtraction> preparedQuery = builder.prepare();
		final List<MiningExtraction> dataList = miningExtractionDao.query(preparedQuery);
		List<MiningExtraction> results = new ArrayList<>();
		final String filterDate = DateTime.now().toString("YYYY/MM/dd");
		// Filter out all records not belonging to today.
		for (MiningExtraction extraction : dataList) {
			final String date = extraction.getExtractionDate().split(":")[0];
			if (date.equalsIgnoreCase(filterDate)) results.add(extraction);
		}
		return results;
	}

	/**
	 * Get the list of Mining Extractions that are registered on the database. This can be a lot of records that need sorting and also
	 * grouping previously to rendering. This method can do the sorting but the grouping is not one of its features.
	 * The mining operations for a single day aggregate all the ore for a single type, but have different records for different systems
	 * and for different ores. So for a single day we can have around 6-8 records. The mining ledger information at the neocom database has
	 * to expiration time so the number of days is still not predetermined.
	 */
	public static List<MiningExtraction> accessMiningExtractions4Pilot( final Credential credential ) throws SQLException {
		final Dao<MiningExtraction, String> dao = GlobalDataManager.getSingleton().getNeocomDBHelper().getMiningExtractionDao();
		final QueryBuilder<MiningExtraction, String> builder = dao.queryBuilder();
		builder.where().eq("ownerId", credential.getAccountId());
		builder.orderBy("id", false);
		final PreparedQuery<MiningExtraction> preparedQuery = builder.prepare();
		return dao.query(preparedQuery);
	}



	// - B U I L D E R
	public static class Builder {
		private MiningRepository onConstruction;

		public Builder() {
			this.onConstruction = new MiningRepository();
		}

		public Builder withMiningExtractionDao( final Dao<MiningExtraction, String> miningExtractionDao ) {
			this.onConstruction.miningExtractionDao = miningExtractionDao;
			return this;
		}

		public MiningRepository build() {
			Objects.requireNonNull(this.onConstruction.miningExtractionDao);
			singleton = this.onConstruction;
			return singleton;
		}
	}
}
