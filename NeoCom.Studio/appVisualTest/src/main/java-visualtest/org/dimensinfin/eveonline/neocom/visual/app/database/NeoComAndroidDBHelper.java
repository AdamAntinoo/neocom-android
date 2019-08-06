package org.dimensinfin.eveonline.neocom.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.entities.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.entities.TimeStamp;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class NeoComAndroidDBHelper extends OrmLiteSqliteOpenHelper /*implements INeoComDBHelper*/ {
	private static Logger logger = LoggerFactory.getLogger(NeoComAndroidDBHelper.class);
	private static String DEFAULT_CONNECTION_DESCRIPTOR = "jdbc:mysql://localhost:3306/neocom?user=NEOCOMTEST&password=01.Alpha";
	private static NeoComAndroidDBHelper singleton;

	// - F I E L D - S E C T I O N
	private boolean databaseValid = false;
	private boolean isOpen = false;
	private String storageLocation = "";
	private int databaseVersion = 0;
	private DatabaseVersion storedVersion;

	private Dao<DatabaseVersion, String> versionDao;
	private Dao<TimeStamp, String> timeStampDao;
	private Dao<Credential, String> credentialDao;
	private Dao<MiningExtraction, String> miningExtractionDao;

	// - C O N S T R U C T O R S
	private NeoComAndroidDBHelper( final Context context, final String databaseName, final int databaseVersion ) {
		super(context, databaseName, null, databaseVersion, R.raw.ormlite_config);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databasePath {}"
				, databaseName);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databaseVersion {}"
				, databaseVersion);
		this.storageLocation = databaseName;
		this.databaseVersion = databaseVersion;
	}

	public int getDatabaseVersion() {
		return databaseVersion;
	}

	public int getStoredVersion() {
		if (null == storedVersion) {
			// Access the version object persistent on the database.
			try {
				List<DatabaseVersion> versionList = this.getVersionDao().queryForAll();
				if (versionList.size() > 0) {
					storedVersion = versionList.get(0);
					return storedVersion.getVersionNumber();
				} else
					return 0;
			} catch (SQLException sqle) {
				logger.warn("W- [NeoComAndroidDBHelper.getStoredVersion]> Database exception: " + sqle.getMessage());
				return 0;
			} catch (RuntimeException rtex) {
				logger.warn("W- [NeoComAndroidDBHelper.getStoredVersion]> Database exception: " + rtex.getMessage());
				return 0;
			}
		} else return storedVersion.getVersionNumber();
	}

	// - I N E O C O M D B H E L P E R   I N T E R F A C E
	public boolean isDatabaseValid() {
		return databaseValid;
	}

	public void onCreate( final ConnectionSource databaseConnection ) {
	}

	public void onUpgrade( final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
	}

	@Override
	public void onCreate( final SQLiteDatabase database, final ConnectionSource databaseConnection ) {
		logger.info(">> [NeoComAndroidDBHelper.onCreate]");
		// Create the tables that do not exist
		try {
			TableUtils.createTableIfNotExists(databaseConnection, DatabaseVersion.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, TimeStamp.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, Credential.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, MiningExtraction.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		this.loadSeedData();
		logger.info("<< [NeoComAndroidDBHelper.onCreate]");
	}

	@Override
	public void onUpgrade( final SQLiteDatabase database, final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
		logger.info(">> [NeoComAndroidDBHelper.onUpgrade]> From: {} -> To: {}"
				, oldVersion, newVersion);
		this.onCreate(databaseConnection);
		logger.info("<< [NeoComAndroidDBHelper.onUpgrade]");
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void loadSeedData() {
		logger.info(">> [NeoComAndroidDBHelper.loadSeedData]");
		logger.info("<< [NeoComAndroidDBHelper.loadSeedData]");
	}

	public Dao<DatabaseVersion, String> getVersionDao() throws SQLException {
		if (null == versionDao) {
			versionDao = DaoManager.createDao(this.getConnectionSource(), DatabaseVersion.class);
		}
		return versionDao;
	}

	public Dao<TimeStamp, String> getTimeStampDao() throws SQLException {
		if (null == timeStampDao) {
			timeStampDao = DaoManager.createDao(this.getConnectionSource(), TimeStamp.class);
		}
		return timeStampDao;
	}

	public Dao<Credential, String> getCredentialDao() throws SQLException {
		if (null == credentialDao) {
			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
		}
		return credentialDao;
	}

	public Dao<MiningExtraction, String> getMiningExtractionDao() throws SQLException {
		if (null == this.miningExtractionDao) {
			this.miningExtractionDao = DaoManager.createDao(this.getConnectionSource(), MiningExtraction.class);
		}
		return this.miningExtractionDao;
	}

	// -  B U I L D E R
	public static class Builder {
		private Context appContext;
		private IFileSystem fileSystem;
		private String storageLocation;
		private String databaseName;
		private int databaseVersion = 0;

		public Builder() {
		}

		public Builder withAppContext( final Context appContext ) {
			this.appContext = appContext;
			return this;
		}

		public Builder withFileSystem( final IFileSystem fileSystem ) {
			this.fileSystem = fileSystem;
			return this;
		}

		public Builder setDatabaseLocation( final String location ) {
			this.storageLocation = location;
			return this;
		}

		public Builder setDatabaseName( final String instanceName ) {
			this.databaseName = instanceName;
			return this;
		}

		public Builder setDatabaseVersion( final int newVersion ) {
			this.databaseVersion = newVersion;
			return this;
		}

		public NeoComAndroidDBHelper build() throws SQLException {
			// Check and create only a singleton helper.
			if (null == singleton) {
				Objects.requireNonNull(this.appContext);
				Objects.requireNonNull(this.fileSystem);
				singleton = new NeoComAndroidDBHelper(this.appContext
						, this.fileSystem.accessResource4Path(databaseName)
						, databaseVersion);
			}
			return singleton;
		}
	}
}
