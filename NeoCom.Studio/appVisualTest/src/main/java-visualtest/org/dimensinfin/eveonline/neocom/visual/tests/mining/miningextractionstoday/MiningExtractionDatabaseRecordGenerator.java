package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.visual.app.support.DataTableToMiningExtractionConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class MiningExtractionDatabaseRecordGenerator {
	/**
	 * Preload on the visual test database the required records to be used on the test data rendering. This replaces the mock
	 * interceptors to give the same functionality but for database based data sources.
	 */
	private static String RECORDS_SOURCE =
			"\t  | id                                    | typeId | solarSystemId | quantity | delta | extractionDateName | extractionHour | ownerId  |\n" +
					"\t  | 2019-08-07:14-30001740-17464-92223647 | 17464  | 30001740      | 843      | 0     | 2019-08-07         | 14             | 92223647 |\n" +
					"\t  | 2019-08-07:14-30001740-17460-92223647 | 17460  | 30001740      | 8266     | 0     | 2019-08-07         | 14             | 92223647 |\n" +
					"\t  | 2019-08-07:14-30001740-17453-92223647 | 17453  | 30001740      | 3345     | 0     | 2019-08-07         | 14             | 92223647 |\n" +
					"\t  | 2019-08-07:13-30001735-17459-92223647 | 17459  | 30001735      | 38087    | 14511 | 2019-08-07         | 13             | 92223647 |\n" +
					"\t  | 2019-08-07:12-30001735-17471-92223647 | 17471  | 30001735      | 25432    | 6156  | 2019-08-07         | 12             | 92223647 |\n" +
					"\t  | 2019-08-07:12-30001735-17464-92223647 | 17464  | 30001735      | 30348    | 0     | 2019-08-07         | 12             | 92223647 |\n" +
					"\t  | 2019-08-07:12-30001735-17459-92223647 | 17459  | 30001735      | 23576    | 17682 | 2019-08-07         | 12             | 92223647 |\n" +
					"\t  | 2019-08-07:11-30001735-17471-92223647 | 17471  | 30001735      | 19276    | 0     | 2019-08-07         | 11             | 92223647 |\n" +
					"\t  | 2019-08-07:11-30001735-17459-92223647 | 17459  | 30001735      | 5894     | 0     | 2019-08-07         | 11             | 92223647 |\n" +
					"\t  | 2019-08-07:10-30001735-17471-92223647 | 17471  | 30001735      | 1566     | 0     | 2019-08-07         | 10             | 92223647 |\n" +
					"\t  | 2019-08-07:10-30001735-17459-92223647 | 17459  | 30001735      | 421      | 0     | 2019-08-07         | 10             | 92223647 |\n";
	private List<Map<String, String>> dataTable;
	private DataTableToMiningExtractionConverter dataTableToMiningExtractionConverter = new DataTableToMiningExtractionConverter();
	private MiningRepository miningRepository;

	public void loadMockData2Repository( final Credential credential ) {
		this.dataTable = this.generateDataTable(RECORDS_SOURCE);
		try {
			this.persistTable(this.dataTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	public List<MiningExtraction> accessTodayMiningExtractions4Pilot( final Credential credential ) {
//		final List<MiningExtraction> records = new ArrayList<>();
//		for (Map<String, String> row : dataTable)
//			records.add(this.dataTableToMiningExtractionConverter.convert(row));
//		return records;
//	}

	private void persistTable(final List<Map<String, String>> dataTable) throws SQLException {
		for (Map<String, String> row : dataTable) {
			final MiningExtraction extraction = this.dataTableToMiningExtractionConverter.convert(row);
			this.miningRepository.persist(extraction);
		}
	}

	private List<Map<String, String>> generateDataTable( final String source ) {
		final List<Map<String, String>> dataTable = new ArrayList<>();
		final String filteredSource = RECORDS_SOURCE.replaceFirst("^\\s*\t\\s*|", "").replaceFirst(Pattern.quote("|"), "");
		String columnRecord = filteredSource.split("\n")[0].trim();
		columnRecord = columnRecord.substring(0, columnRecord.length() - 1);
		final List<String> columNames = Arrays.asList(columnRecord.split(Pattern.quote("|")));
		for (int i = 1; i < 6; i++) {
			final List<String> dataFields = this.extractDataFields(filteredSource.split("\n")[i]);
			int j = 0;
			final Map<String, String> row = new HashMap<>();
			for (String column : columNames)
				row.put(column.trim(), dataFields.get(j++).trim());
			dataTable.add(row);
		}
		return dataTable;
	}

	private List<String> extractDataFields( final String source ) {
		return Arrays.asList(source.replaceFirst("^\\s*\t\\s*|", "")
				                     .replaceFirst(Pattern.quote("|"), "")
				                     .split(Pattern.quote("|"))
		);
	}

	// - B U I L D E R
	public static class Builder {
		private MiningExtractionDatabaseRecordGenerator onConstruction;

		public Builder() {
			this.onConstruction = new MiningExtractionDatabaseRecordGenerator();
		}

		public Builder withMiningRepository( final MiningRepository miningRepository ) {
			Objects.requireNonNull(miningRepository);
			this.onConstruction.miningRepository = miningRepository;
			return this;
		}

		public MiningExtractionDatabaseRecordGenerator build() {
//			Objects.requireNonNull(this.onConstruction.miningRepository);
//			this.onConstruction.dataTable = this.onConstruction.generateDataTable(RECORDS_SOURCE);
//			try {
//				this.onConstruction.persistTable();
//			} catch (SQLException e) {
//				throw new NullPointerException("The internal extractionrecord table cannot be persisted.");
//			}
			return this.onConstruction;
		}
	}
}
