package org.dimensinfin.eveonline.neocom.sharedcode.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.entities.MiningExtraction;

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
//		final Dao<MiningExtraction, String> miningExtractionDao = new GlobalDataManager().getNeocomDBHelper().getMiningExtractionDao();
		final QueryBuilder<MiningExtraction, String> builder = miningExtractionDao.queryBuilder();
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
