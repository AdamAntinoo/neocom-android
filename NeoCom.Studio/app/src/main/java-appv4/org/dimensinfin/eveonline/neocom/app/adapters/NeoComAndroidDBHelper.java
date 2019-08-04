package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.INeoComDBHelper;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.entities.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.entities.FittingRequest;
import org.dimensinfin.eveonline.neocom.entities.Job;
import org.dimensinfin.eveonline.neocom.entities.MarketOrder;
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.entities.NeoComBlueprint;
import org.dimensinfin.eveonline.neocom.entities.Property;
import org.dimensinfin.eveonline.neocom.entities.RefiningData;
import org.dimensinfin.eveonline.neocom.entities.TimeStamp;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * NeoCom private database connector that will have the same api as the connector to be used on Android. This
 * version already uses the mySql database JDBC implementation instead the SQLite copied from the Android
 * platform.
 * The class will encapsulate all dao and connection access.
 *
 * @author Adam Antinoo
 */
public class NeoComAndroidDBHelper extends OrmLiteSqliteOpenHelper implements INeoComDBHelper {
	private static Logger logger = LoggerFactory.getLogger(NeoComAndroidDBHelper.class);
	//	private static String DEFAULT_CONNECTION_DESCRIPTOR = "jdbc:mysql://localhost:3306/neocom?user=NEOCOMTEST&password=01.Alpha";
	private static NeoComAndroidDBHelper singleton;

	// - F I E L D - S E C T I O N
	private boolean databaseValid = false;
	private boolean isOpen = false;
	private String storageLocation = "";
	private int databaseVersion = 0;

	private Dao<DatabaseVersion, String> versionDao;
	private Dao<TimeStamp, String> timeStampDao;
	private Dao<Credential, String> credentialDao;
	private Dao<EveLocation, String> locationDao;
	private Dao<Property, String> propertyDao;
	private Dao<NeoComAsset, String> assetDao;
	private Dao<NeoComBlueprint, String> blueprintDao;
	private Dao<Job, String> jobDao;
	private Dao<MarketOrder, String> marketOrderDao;
	private Dao<FittingRequest, String> fittingRequestDao;
	private Dao<MiningExtraction, String> miningExtractionDao;
	private Dao<RefiningData, String> refiningDataDao;

	private DatabaseVersion storedVersion;

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

	// - I N E O C O M D B H E L P E R   I N T E R F A C E
	public boolean isDatabaseValid() {
		return databaseValid;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public int getDatabaseVersion() {
		return databaseVersion;
	}

	public int getStoredVersion() {
		if (null == this.storedVersion) {
			// Access the version object persistent on the database.
			try {
				List<DatabaseVersion> versionList = this.getVersionDao().queryForAll();
				if (versionList.size() > 0) {
					this.storedVersion = versionList.get(0);
					return this.storedVersion.getVersionNumber();
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

	public void onCreate( final ConnectionSource databaseConnection ) {
		// This method is an stub for compatibility with SpringBoot implementations.
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
			TableUtils.createTableIfNotExists(databaseConnection, EveLocation.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, Property.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, NeoComAsset.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, NeoComBlueprint.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, Job.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, MarketOrder.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
		}
		try {
			TableUtils.createTableIfNotExists(databaseConnection, FittingRequest.class);
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

	public void onUpgrade( final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
		// This method is an stub for compatibility with SpringBoot implementations.
	}

	@Override
	public void onUpgrade( final SQLiteDatabase database, final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
		logger.info(">> [NeoComAndroidDBHelper.onUpgrade]> From: {} -> To: {}"
				, oldVersion, newVersion);
		// Execute different actions depending on the new version.
		if (oldVersion < 109) {
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, DatabaseVersion.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, DatabaseVersion.class);
					DatabaseVersion version = new DatabaseVersion(newVersion);
//							                          .store();
					Dao<DatabaseVersion, String> versionDao = this.getVersionDao();
					versionDao.update(version);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, TimeStamp.class, true);
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, Credential.class, true);
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 110) {
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, MiningExtraction.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, MiningExtraction.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table MiningExtraction on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table MiningExtraction on Database new version.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 113) {
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, MiningExtraction.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, MiningExtraction.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table MiningExtraction on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table MiningExtraction on Database new version.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 116) {
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, Credential.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, Credential.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table Credential on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table Credential on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, EveLocation.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, EveLocation.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table EveLocation on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table EveLocation on Database new version.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 118) {
			try {
				// Drop all the tables to force a new update from the latest SQLite version.
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, NeoComAsset.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E> Error dropping table NeoComAsset on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E> Error dropping table NeoComAsset on Database new version.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 119) {
			// Drop Credential table because reconstruction.
			try {
				TableUtils.dropTable(databaseConnection, Credential.class, true);
				try {
					TableUtils.createTableIfNotExists(databaseConnection, Credential.class);
				} catch (SQLException sqle) {
					logger.warn("SQL [NeoComAndroidDBHelper.onUpgrade]> SQL NeoComDatabase: {}", sqle.getMessage());
				}
			} catch (RuntimeException rtex) {
				logger.error("E [NeoComAndroidDBHelper.onUpgrade]> Error upgrading to version 119.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				logger.error("E [NeoComAndroidDBHelper.onUpgrade]> Error upgrading to version 119.");
				sqle.printStackTrace();
			}
		}
		if (oldVersion < 120) {
			this.recreateTable(databaseConnection, Credential.class, newVersion); // Recreate the credential because new fields
		}
		this.onCreate(databaseConnection);
		logger.info("<< [NeoComAndroidDBHelper.onUpgrade]");
	}

	/**
	 * Checks if the key tables had been cleaned and then reinserts the seed data on them.
	 */
	public void loadSeedData() {
		logger.info(">> [NeoComAndroidDBHelper.loadSeedData]");
		// Add seed data to the new database is the tables are empty.
		try {
			//---  D A T A B A S E    V E R S I O N
			logger.info("-- [NeoComAndroidDBHelper.loadSeedData]> Loading seed data for DatabaseVersion");
			Dao<DatabaseVersion, String> version = this.getVersionDao();
			QueryBuilder<DatabaseVersion, String> queryBuilder = version.queryBuilder();
			queryBuilder.setCountOf(true);
			// Check that at least one Version record exists on the database. It is a singleton.
			long records = version.countOf(queryBuilder.prepare());
			logger.info("-- [NeoComAndroidDBHelper.loadSeedData]> DatabaseVersion records: " + records);

			// If the table is empty then insert the seeded Api Keys
			if (records < 1) {
				DatabaseVersion key = new DatabaseVersion(this.databaseVersion);
				version.createOrUpdate(key);
				logger.info("-- [NeoComAndroidDBHelper.loadSeedData]> Setting DatabaseVersion to: " + key.getVersionNumber());
			}
		} catch (SQLException sqle) {
			logger.error("E [NeoComAndroidDBHelper.loadSeedData]> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}
		logger.info("<< [NeoComAndroidDBHelper.loadSeedData]");
	}

	@Override
	public Dao<DatabaseVersion, String> getVersionDao() throws SQLException {
		if (null == versionDao) {
			versionDao = DaoManager.createDao(this.getConnectionSource(), DatabaseVersion.class);
		}
		return versionDao;
	}

	@Override
	public Dao<TimeStamp, String> getTimeStampDao() throws SQLException {
		if (null == timeStampDao) {
			timeStampDao = DaoManager.createDao(this.getConnectionSource(), TimeStamp.class);
		}
		return timeStampDao;
	}

	@Override
	public Dao<Credential, String> getCredentialDao() throws SQLException {
		if (null == credentialDao) {
			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
		}
		return credentialDao;
	}

	@Override
	public Dao<EveLocation, String> getLocationDao() throws SQLException {
		if (null == locationDao) {
			locationDao = DaoManager.createDao(this.getConnectionSource(), EveLocation.class);
		}
		return locationDao;
	}

	@Override
	public Dao<Property, String> getPropertyDao() throws SQLException {
		if (null == propertyDao) {
			propertyDao = DaoManager.createDao(this.getConnectionSource(), Property.class);
		}
		return propertyDao;
	}

	@Override
	public Dao<NeoComAsset, String> getAssetDao() throws SQLException {
		if (null == assetDao) {
			assetDao = DaoManager.createDao(this.getConnectionSource(), NeoComAsset.class);
		}
		return assetDao;
	}

	@Override
	public Dao<NeoComBlueprint, String> getBlueprintDao() throws SQLException {
		if (null == blueprintDao) {
			blueprintDao = DaoManager.createDao(this.getConnectionSource(), NeoComBlueprint.class);
		}
		return blueprintDao;
	}

	@Override
	public Dao<Job, String> getJobDao() throws SQLException {
		if (null == jobDao) {
			jobDao = DaoManager.createDao(this.getConnectionSource(), Job.class);
		}
		return jobDao;
	}

	@Override
	public Dao<MarketOrder, String> getMarketOrderDao() throws SQLException {
		if (null == marketOrderDao) {
			marketOrderDao = DaoManager.createDao(this.getConnectionSource(), MarketOrder.class);
		}
		return marketOrderDao;
	}

	@Override
	public Dao<FittingRequest, String> getFittingRequestDao() throws SQLException {
		if (null == fittingRequestDao) {
			fittingRequestDao = DaoManager.createDao(this.getConnectionSource(), FittingRequest.class);
		}
		return fittingRequestDao;
	}

	@Override
	public Dao<MiningExtraction, String> getMiningExtractionDao() throws SQLException {
		if (null == miningExtractionDao) {
			miningExtractionDao = DaoManager.createDao(this.getConnectionSource(), MiningExtraction.class);
		}
		return miningExtractionDao;
	}

	@Override
	public Dao<RefiningData, String> getRefiningDataDao() throws SQLException {
		if (null == refiningDataDao) {
			refiningDataDao = DaoManager.createDao(this.getConnectionSource(), RefiningData.class);
		}
		return refiningDataDao;
	}

//	@Override
//	public Dao<Colony, String> getColonyDao() throws SQLException {
//		if (null == colonyDao) {
//			colonyDao = DaoManager.createDao(this.getConnectionSource(), Colony.class);
//		}
//		return colonyDao;
//	}

	//	@Override
	//	public Dao<ColonyStorage, String> getColonyStorageDao() throws SQLException {
	//		if (null == colonyStorageDao) {
	//			colonyStorageDao = DaoManager.createDao(this.getConnectionSource(), ColonyStorage.class);
	//		}
	//		return colonyStorageDao;
	//	}
	//
	//	public Dao<ColonySerialized, String> getColonySerializedDao() throws SQLException {
	//		if (null == colonySerializedDao) {
	//			colonySerializedDao = DaoManager.createDao(this.getConnectionSource(), ColonySerialized.class);
	//		}
	//		return colonySerializedDao;
	//	}

	// --- PUBLIC CONNECTION SPECIFIC ACTIONS

	/**
	 * removes from the application database any asset and blueprint that contains the special -1 code as the
	 * owner identifier. Those records are from older downloads and have to be removed to avoid merging with the
	 * new download.
	 */
	public synchronized void clearInvalidRecords( final long pilotid ) {
		logger.info(">> [NeoComAndroidDBHelper.clearInvalidRecords]> pilotid", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets that do not have a valid owner.
						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
						deleteBuilder.where().eq("ownerId", (pilotid * -1));
						int count = deleteBuilder.delete();
						logger.info("-- [NeoComAndroidDBHelper.clearInvalidRecords]> Invalid assets cleared for owner {}: {}", (pilotid * -1), count);

						// Remove all blueprints that do not have a valid owner.
						final DeleteBuilder<NeoComBlueprint, String> deleteBuilderBlueprint = getBlueprintDao().deleteBuilder();
						deleteBuilderBlueprint.where().eq("ownerId", (pilotid * -1));
						count = deleteBuilderBlueprint.delete();
						logger.info("-- [NeoComAndroidDBHelper.clearInvalidRecords]> Invalid blueprints cleared for owner {}: {}", (pilotid * -1),
								count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComAndroidDBHelper.clearInvalidRecords]> Problem clearing invalid records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComAndroidDBHelper.clearInvalidRecords]");
			}
		}
	}

	/**
	 * Changes the owner id for all records from a new download with the id of the current character. This
	 * completes the download and the assignment of the resources to the character without interrupting the
	 * processing of data by the application.
	 */
	public synchronized void replaceAssets( final long pilotid ) {
		logger.info(">> [NeoComAndroidDBHelper.clearInvalidRecords]> pilotid: {}", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets from this owner before adding the new set.
						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
						deleteBuilder.where().eq("ownerId", pilotid);
						int count = deleteBuilder.delete();
						logger.info("-- [NeoComAndroidDBHelper.replaceAssets]> Invalid assets cleared for owner {}: {}", pilotid, count);

						// Replace the owner to vake the assets valid.
						final UpdateBuilder<NeoComAsset, String> updateBuilder = getAssetDao().updateBuilder();
						updateBuilder.updateColumnValue("ownerId", pilotid)
						             .where().eq("ownerId", (pilotid * -1));
						count = updateBuilder.update();
						logger.info("-- [NeoComAndroidDBHelper.replaceAssets]> Replace owner {} for assets: {}", pilotid, count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComAndroidDBHelper.replaceAssets]> Problem replacing records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComAndroidDBHelper.replaceAssets]");
			}
		}
	}

	public synchronized void replaceBlueprints( final long pilotid ) {
		logger.info(">> [NeoComAndroidDBHelper.replaceBlueprints]> pilotid: {}", pilotid);
		synchronized (connectionSource) {
			try {
				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
					public Void call() throws Exception {
						// Remove all assets that do not have a valid owner.
						final UpdateBuilder<NeoComBlueprint, String> updateBuilder = getBlueprintDao().updateBuilder();
						updateBuilder.updateColumnValue("ownerId", pilotid)
						             .where().eq("ownerId", (pilotid * -1));
						int count = updateBuilder.update();
						logger.info("-- [NeoComAndroidDBHelper.replaceBlueprints]> Replace owner {} for blueprints: {}", pilotid, count);
						return null;
					}
				});
			} catch (final SQLException ex) {
				logger.warn("W> [NeoComAndroidDBHelper.replaceBlueprints]> Problem replacing records. " + ex.getMessage());
			} finally {
				logger.info("<< [NeoComAndroidDBHelper.replaceBlueprints]");
			}
		}
	}

	private int readDatabaseVersion() {
		// Access the version object persistent on the database.
		try {
			Dao<DatabaseVersion, String> versionDao = this.getVersionDao();
			QueryBuilder<DatabaseVersion, String> queryBuilder = versionDao.queryBuilder();
			PreparedQuery<DatabaseVersion> preparedQuery = queryBuilder.prepare();
			List<DatabaseVersion> versionList = versionDao.query(preparedQuery);
			if (versionList.size() > 0) {
				DatabaseVersion version = versionList.get(0);
				return version.getVersionNumber();
			} else
				return 0;
		} catch (SQLException sqle) {
			logger.warn("W- [NeoComAndroidDBHelper.readDatabaseVersion]> Database exception: " + sqle.getMessage());
			return 0;
		}
	}

	// -  C O R E
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NeoComAndroidDBHelper [");
		//		final String localConnectionDescriptor = hostName + "/" + databaseName + "?user=" + databaseUser + "&password=" + databasePassword;
		buffer.append("Descriptor: ").append(storageLocation);
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
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

	private void recreateTable( final ConnectionSource databaseConnection, final Class tableType, final int version ) {
		try {
			TableUtils.dropTable(databaseConnection, tableType, true);
			try {
				TableUtils.createTableIfNotExists(databaseConnection, tableType);
			} catch (SQLException sqle) {
				logger.warn("SQL [NeoComAndroidDBHelper.recreateTable.{}]> SQL NeoComDatabase: {}",
						tableType.getSimpleName(), sqle.getMessage());
			}
		} catch (RuntimeException rtex) {
			logger.error("E [NeoComAndroidDBHelper.recreateTable.{}]> Error upgrading to version {}.",
					tableType.getSimpleName(), version);
			rtex.printStackTrace();
		} catch (SQLException sqle) {
			logger.error("E [NeoComAndroidDBHelper.recreateTable.{}]> Error upgrading to version {}.",
					tableType.getSimpleName(), version);
			sqle.printStackTrace();
		}
	}
}
