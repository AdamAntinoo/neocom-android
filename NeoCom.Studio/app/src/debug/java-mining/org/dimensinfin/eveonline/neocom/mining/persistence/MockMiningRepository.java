package org.dimensinfin.eveonline.neocom.mining.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class MockMiningRepository extends MiningRepository {
	protected MockMiningRepository() { }

	/**
	 * This other method does the same Mining Extractions processing but only for the records for the current date. The difference is
	 * that today records are aggregated by hour instead of by day. So we will have a record for one ore/system since the hour we did the
	 * extractions until the 23 hours. The first extraction will add to the hour until the next hour starts. Then the accounting for this
	 * new hour will show the new ore totals and so on hour after hour.
	 */
	private List<MiningExtraction> extractions4Test = new ArrayList<>();

	public List<MiningExtraction> accessTodayMiningExtractions4PilotMock( final Credential credential ) throws SQLException {
		this.generateExtractionsT1();
		return extractions4Test;
	}

	private void generateExtractionsT1() {
		extractions4Test.clear();
		// Generate 5 different hours for today.
		extractions4Test.addAll(this.extractionGenerator(34, 10000));
		extractions4Test.addAll(this.extractionGenerator(45498, 10000));
	}

	private List<MiningExtraction> extractionGenerator( final int typeId, final int quantity ) {
		final List<MiningExtraction> todayExtractions = new ArrayList<>();
		// Generate 5 different hours for today.
		for (int i = 5; i > 0; i--) {
			final String date = DateTime.now().toString("YYYY/MM/dd");
			final String id = MiningExtraction.generateRecordId(date, 20 - i, typeId, 30001647, 92223647);
			todayExtractions.add(new MiningExtraction.Builder()
					                     .withTypeId(typeId)
					                     .withSolarSystemId(30001647)
					                     .withQuantity(quantity + i * 100)
					                     .withOwnerId(92223647)
					                     .withExtractionDate(new LocalDate())
					                     .withExtractionHour(20 - i)
					                     .build());
		}
		return todayExtractions;
	}

	// - B U I L D E R
	public static class Builder {
		private MockMiningRepository onConstruction;

		public Builder() {
			this.onConstruction = new MockMiningRepository();
		}

		public Builder withMiningExtractionDao( final Dao<MiningExtraction, String> miningExtractionDao ) {
			this.onConstruction.miningExtractionDao = miningExtractionDao;
			return this;
		}

		public MockMiningRepository build() {
			Objects.requireNonNull(this.onConstruction.miningExtractionDao);
			return this.onConstruction;
		}
	}
}
