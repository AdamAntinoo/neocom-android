package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.entities.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction;
import org.dimensinfin.eveonline.neocom.database.entities.NeoAsset;
import org.dimensinfin.eveonline.neocom.database.repositories.DatabaseVersionRepository;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;


import static org.dimensinfin.eveonline.neocom.BuildConfig.NEOCOM_DB_VERSION;

/**
 * NeoCom private database connector that will have the same api as the connector to be used on Android. This
 * version already uses the mySql database JDBC implementation instead the SQLite copied from the Android
 * platform.
 * The class will encapsulate all dao and connection access.
 *
 * @author Adam Antinoo
 */
public class AndroidNeoComDBHelper extends OrmLiteSqliteOpenHelper {
	private static Logger logger = LoggerFactory.getLogger( AndroidNeoComDBHelper.class );
	private static AndroidNeoComDBHelper singleton;

	// - F I E L D - S E C T I O N
	private boolean databaseValid = false;
	private boolean isOpen = false;
	private String storageLocation = "";
	private int databaseVersion = 0;
	private DatabaseVersionRepository databaseVersionRepository;

	// - D A O S
	private Dao<DatabaseVersion, Integer> versionDao;
	private Dao<Credential, String> credentialDao;
	private Dao<EsiLocation, String> locationDao;
	private Dao<MiningExtraction, String> miningExtractionDao;
	private Dao<NeoAsset, UUID> assetDao;
//	private Dao<TimeStamp, String> timeStampDao;
//	private Dao<Property, String> propertyDao;
//	private Dao<NeoComBlueprint, String> blueprintDao;
//	private Dao<Job, String> jobDao;
//	private Dao<MarketOrder, String> marketOrderDao;
//	private Dao<FittingRequest, String> fittingRequestDao;
//	private Dao<RefiningData, String> refiningDataDao;

	private DatabaseVersion storedVersion;

	// - C O N S T R U C T O R S
	private AndroidNeoComDBHelper( final Context context, final String databaseName, final int databaseVersion ) {
		super( context, databaseName, null, databaseVersion, R.raw.ormlite_config );
		logger.info( "-- [AndroidNeoComDBHelper.<constructor>]> Initializing instance with configuration: databasePath {}"
				, databaseName );
		logger.info( "-- [AndroidNeoComDBHelper.<constructor>]> Initializing instance with configuration: databaseVersion {}"
				, databaseVersion );
		this.storageLocation = databaseName;
		this.databaseVersion = databaseVersion;
	}

	// -  V E R S I O N
	private DatabaseVersionRepository getDatabaseVersionRepository() throws SQLException {
		if ( null == this.databaseVersionRepository)
			this.databaseVersionRepository = new DatabaseVersionRepository.Builder()
			.withDatabaseVersionDao( this.getVersionDao() )
			.build();
		return this.databaseVersionRepository;
	}
	public int getDatabaseVersion() {
		return this.databaseVersion;
	}

	public int getStoredVersion() {
		if (null == this.storedVersion) {
			// Access the version object persistent on the database.
//			try {
				this.storedVersion= this.databaseVersionRepository.accessVersion();
//				this.storedVersion = version;
//				if (versionList.size() > 0) {
//					this.storedVersion = versionList.get( 0 );
//					return this.storedVersion.getVersionNumber();
//				} else
//					return 0;
//			} catch (final SQLException sqle) {
//				logger.warn( "W- [AndroidNeoComDBHelper.getStoredVersion]> Database exception: " + sqle.getMessage() );
//				return 0;
////			} catch (RuntimeException rtex) {
////				logger.warn( "W- [AndroidNeoComDBHelper.getStoredVersion]> Database exception: " + rtex.getMessage() );
////				return 0;
//			}
		}
		return storedVersion.getVersionNumber();
	}

	// - I N E O C O M D B H E L P E R   I N T E R F A C E
	public boolean isDatabaseValid() {
		return this.databaseValid;
	}

//	public void onCreate( final ConnectionSource databaseConnection ) {
//		// This method is an stub for compatibility with SpringBoot implementations.
//	}

	public void onUpgrade( final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
		// This method is an stub for compatibility with SpringBoot implementations.
	}

	/**
	 * Checks if the key tables had been cleaned and then reinserts the seed data on them.
	 */
	public void loadSeedData() {
		logger.info( ">> [AndroidNeoComDBHelper.loadSeedData]" );
		// Add seed data to the new database is the tables are empty.
		try {
			// - D A T A B A S E    V E R S I O N
			logger.info( "-- [AndroidNeoComDBHelper.loadSeedData]> Loading seed data for DatabaseVersion" );
			final DatabaseVersion version = this.getDatabaseVersionRepository().accessVersion();
			version.setVersionNumber( NEOCOM_DB_VERSION );
			this.getDatabaseVersionRepository().persist( version );
//			Dao<DatabaseVersion, Integer> version = this.getVersionDao();
//			QueryBuilder<DatabaseVersion, String> queryBuilder = version.queryBuilder();
//			queryBuilder.setCountOf( true );
//			// Check that at least one Version record exists on the database. It is a singleton.
//			long records = version.countOf( queryBuilder.prepare() );
//			logger.info( "-- [AndroidNeoComDBHelper.loadSeedData]> DatabaseVersion records: " + records );
//
//			// If the table is empty then insert the seeded Api Keys
//			if (records < 1) {
//				DatabaseVersion key = new DatabaseVersion( this.databaseVersion );
//				version.createOrUpdate( key );
//				logger.info( "-- [AndroidNeoComDBHelper.loadSeedData]> Setting DatabaseVersion to: " + key.getVersionNumber() );
//			}
		} catch (SQLException sqle) {
			logger.error( "E [AndroidNeoComDBHelper.loadSeedData]> Error creating the initial table on the app database." );
			sqle.printStackTrace();
		}
		logger.info( "<< [AndroidNeoComDBHelper.loadSeedData]" );
	}

	public Dao<DatabaseVersion, Integer> getVersionDao() throws SQLException {
		if (null == this.versionDao) {
			this.versionDao = DaoManager.createDao( this.getConnectionSource(), DatabaseVersion.class );
		}
		return this.versionDao;
	}

//	@Override
//	public Dao<TimeStamp, String> getTimeStampDao() throws SQLException {
//		if (null == this.timeStampDao) {
//			this.timeStampDao = DaoManager.createDao(this.getConnectionSource(), TimeStamp.class);
//		}
//		return this.timeStampDao;
//	}

	public Dao<Credential, String> getCredentialDao() throws SQLException {
		if (null == this.credentialDao) {
			this.credentialDao = DaoManager.createDao( this.getConnectionSource(), Credential.class );
		}
		return this.credentialDao;
	}

	public Dao<EsiLocation, String> getLocationDao() throws SQLException {
		if (null == this.locationDao) {
			this.locationDao = DaoManager.createDao( this.getConnectionSource(), EsiLocation.class );
		}
		return this.locationDao;
	}

	public Dao<NeoAsset, UUID> getAssetDao() throws SQLException {
		if (null == this.assetDao) {
			this.assetDao = DaoManager.createDao( this.getConnectionSource(), NeoAsset.class );
		}
		return this.assetDao;
	}

//	@Override
//	public Dao<Property, String> getPropertyDao() throws SQLException {
//		if (null == this.propertyDao) {
//			this.propertyDao = DaoManager.createDao(this.getConnectionSource(), Property.class);
//		}
//		return this.propertyDao;
//	}
//
//
//	@Override
//	public Dao<NeoComBlueprint, String> getBlueprintDao() throws SQLException {
//		if (null == this.blueprintDao) {
//			this.blueprintDao = DaoManager.createDao(this.getConnectionSource(), NeoComBlueprint.class);
//		}
//		return this.blueprintDao;
//	}
//
//	@Override
//	public Dao<Job, String> getJobDao() throws SQLException {
//		if (null == this.jobDao) {
//			this.jobDao = DaoManager.createDao(this.getConnectionSource(), Job.class);
//		}
//		return this.jobDao;
//	}
//
//	@Override
//	public Dao<MarketOrder, String> getMarketOrderDao() throws SQLException {
//		if (null == this.marketOrderDao) {
//			this.marketOrderDao = DaoManager.createDao(this.getConnectionSource(), MarketOrder.class);
//		}
//		return this.marketOrderDao;
//	}
//
//	@Override
//	public Dao<FittingRequest, String> getFittingRequestDao() throws SQLException {
//		if (null == this.fittingRequestDao) {
//			this.fittingRequestDao = DaoManager.createDao(this.getConnectionSource(), FittingRequest.class);
//		}
//		return this.fittingRequestDao;
//	}

	public Dao<MiningExtraction, String> getMiningExtractionDao() throws SQLException {
		if (null == this.miningExtractionDao) {
			this.miningExtractionDao = DaoManager.createDao( this.getConnectionSource(), MiningExtraction.class );
		}
		return this.miningExtractionDao;
	}

//	@Override
//	public Dao<RefiningData, String> getRefiningDataDao() throws SQLException {
//		if (null == this.refiningDataDao) {
//			this.refiningDataDao = DaoManager.createDao(this.getConnectionSource(), RefiningData.class);
//		}
//		return this.refiningDataDao;
//	}

//	/**
//	 * removes from the application database any asset and blueprint that contains the special -1 code as the
//	 * owner identifier. Those records are from older downloads and have to be removed to avoid merging with the
//	 * new download.
//	 */
//	public synchronized void clearInvalidRecords( final long pilotid ) {
//		logger.info(">> [AndroidNeoComDBHelper.clearInvalidRecords]> pilotid", pilotid);
//		synchronized (connectionSource) {
//			try {
//				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
//					public Void call() throws Exception {
//						// Remove all assets that do not have a valid owner.
//						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
//						deleteBuilder.where().eq("ownerId", (pilotid * -1));
//						int count = deleteBuilder.delete();
//						logger.info("-- [AndroidNeoComDBHelper.clearInvalidRecords]> Invalid assets cleared for owner {}: {}",
//						            (pilotid * -1), count);
//
//						// Remove all blueprints that do not have a valid owner.
//						final DeleteBuilder<NeoComBlueprint, String> deleteBuilderBlueprint = getBlueprintDao().deleteBuilder();
//						deleteBuilderBlueprint.where().eq("ownerId", (pilotid * -1));
//						count = deleteBuilderBlueprint.delete();
//						logger.info("-- [AndroidNeoComDBHelper.clearInvalidRecords]> Invalid blueprints cleared for owner {}: {}",
//						            (pilotid * -1),
//						            count);
//						return null;
//					}
//				});
//			} catch (final SQLException ex) {
//				logger.warn(
//						"W> [AndroidNeoComDBHelper.clearInvalidRecords]> Problem clearing invalid records. " + ex.getMessage());
//			} finally {
//				logger.info("<< [AndroidNeoComDBHelper.clearInvalidRecords]");
//			}
//		}
//	}
//
//	/**
//	 * Changes the owner id for all records from a new download with the id of the current character. This
//	 * completes the download and the assignment of the resources to the character without interrupting the
//	 * processing of data by the application.
//	 */
//	public synchronized void replaceAssets( final long pilotid ) {
//		logger.info(">> [AndroidNeoComDBHelper.clearInvalidRecords]> pilotid: {}", pilotid);
//		synchronized (connectionSource) {
//			try {
//				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
//					public Void call() throws Exception {
//						// Remove all assets from this owner before adding the new set.
//						final DeleteBuilder<NeoComAsset, String> deleteBuilder = getAssetDao().deleteBuilder();
//						deleteBuilder.where().eq("ownerId", pilotid);
//						int count = deleteBuilder.delete();
//						logger.info("-- [AndroidNeoComDBHelper.replaceAssets]> Invalid assets cleared for owner {}: {}", pilotid,
//						            count);
//
//						// Replace the owner to vake the assets valid.
//						final UpdateBuilder<NeoComAsset, String> updateBuilder = getAssetDao().updateBuilder();
//						updateBuilder.updateColumnValue("ownerId", pilotid)
//								.where().eq("ownerId", (pilotid * -1));
//						count = updateBuilder.update();
//						logger.info("-- [AndroidNeoComDBHelper.replaceAssets]> Replace owner {} for assets: {}", pilotid, count);
//						return null;
//					}
//				});
//			} catch (final SQLException ex) {
//				logger.warn("W> [AndroidNeoComDBHelper.replaceAssets]> Problem replacing records. " + ex.getMessage());
//			} finally {
//				logger.info("<< [AndroidNeoComDBHelper.replaceAssets]");
//			}
//		}
//	}
//
//	public synchronized void replaceBlueprints( final long pilotid ) {
//		logger.info(">> [AndroidNeoComDBHelper.replaceBlueprints]> pilotid: {}", pilotid);
//		synchronized (connectionSource) {
//			try {
//				TransactionManager.callInTransaction(connectionSource, new Callable<Void>() {
//					public Void call() throws Exception {
//						// Remove all assets that do not have a valid owner.
//						final UpdateBuilder<NeoComBlueprint, String> updateBuilder = getBlueprintDao().updateBuilder();
//						updateBuilder.updateColumnValue("ownerId", pilotid)
//								.where().eq("ownerId", (pilotid * -1));
//						int count = updateBuilder.update();
//						logger.info("-- [AndroidNeoComDBHelper.replaceBlueprints]> Replace owner {} for blueprints: {}", pilotid,
//						            count);
//						return null;
//					}
//				});
//			} catch (final SQLException ex) {
//				logger.warn("W> [AndroidNeoComDBHelper.replaceBlueprints]> Problem replacing records. " + ex.getMessage());
//			} finally {
//				logger.info("<< [AndroidNeoComDBHelper.replaceBlueprints]");
//			}
//		}
//	}

	// - PUBLIC CONNECTION SPECIFIC ACTIONS

	@Override
	public void onCreate( final SQLiteDatabase database, final ConnectionSource databaseConnection ) {
		logger.info( ">> [AndroidNeoComDBHelper.onCreate]" );
		// Create the tables that do not exist
		try {
			TableUtils.createTableIfNotExists( databaseConnection, DatabaseVersion.class );
		} catch (SQLException sqle) {
			logger.warn( "SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage() );
		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, TimeStamp.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
		try {
			TableUtils.createTableIfNotExists( databaseConnection, Credential.class );
		} catch (SQLException sqle) {
			logger.warn( "SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage() );
		}
		try {
			TableUtils.createTableIfNotExists( databaseConnection, NeoAsset.class );
		} catch (SQLException sqle) {
			NeoComLogger.error( "SQL NeoComDatabase.", sqle );
		}
		try {
			TableUtils.createTableIfNotExists( databaseConnection, EsiLocation.class );
		} catch (SQLException sqle) {
			logger.warn( "SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage() );
		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, Property.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, NeoComBlueprint.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, Job.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, MarketOrder.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
//		try {
//			TableUtils.createTableIfNotExists(databaseConnection, FittingRequest.class);
//		} catch (SQLException sqle) {
//			logger.warn("SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage());
//		}
		try {
			TableUtils.createTableIfNotExists( databaseConnection, MiningExtraction.class );
		} catch (SQLException sqle) {
			logger.warn( "SQL [AndroidNeoComDBHelper.onCreate]> SQL NeoComDatabase: {}", sqle.getMessage() );
		}
		this.loadSeedData();
		logger.info( "<< [AndroidNeoComDBHelper.onCreate]" );
	}

	@Override
	public void onUpgrade( final SQLiteDatabase database, final ConnectionSource databaseConnection, final int oldVersion, final int newVersion ) {
		logger.info( ">> [AndroidNeoComDBHelper.onUpgrade]> From: {} -> To: {}"
				, oldVersion, newVersion );
		// Execute different actions depending on the new version.
		try {
			if (oldVersion < 100) {
				this.recreateTable( databaseConnection, DatabaseVersion.class, newVersion ); // Recreate the credential because new fields
				final Dao<DatabaseVersion, Integer> daoVersion = this.getVersionDao();
				daoVersion.createOrUpdate( new DatabaseVersion( NEOCOM_DB_VERSION ) );
				this.recreateTable( databaseConnection, Credential.class, newVersion ); // Recreate the credential because new fields
			}
			if (oldVersion < 121) {
				this.recreateTable( databaseConnection, DatabaseVersion.class, newVersion ); // Recreate the credential because new fields
				final Dao<DatabaseVersion, Integer> daoVersion = this.getVersionDao();
				daoVersion.createOrUpdate( new DatabaseVersion( NEOCOM_DB_VERSION ) );
				this.recreateTable( databaseConnection, Credential.class, newVersion ); // Recreate the credential because new fields
				this.recreateTable( databaseConnection, NeoAsset.class, newVersion );
			}
		} catch (SQLException sqle) {
			logger.warn( "W- [AndroidNeoComDBHelper.readDatabaseVersion]> Database exception: " + sqle.getMessage() );
		}
		this.onCreate( database, databaseConnection );
		logger.info( "<< [AndroidNeoComDBHelper.onUpgrade]" );
	}

	public boolean isOpen() {
		return this.isOpen;
	}

	// -  C O R E
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer( "AndroidNeoComDBHelper [" );
		//		final String localConnectionDescriptor = hostName + "/" + databaseName + "?user=" + databaseUser + "&password=" + databasePassword;
		buffer.append( "Descriptor: " ).append( storageLocation );
		buffer.append( "]" );
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

//	private int readDatabaseVersion() {
//		// Access the version object persistent on the database.
//		try {
//			Dao<DatabaseVersion, Integer> versionDao = this.getVersionDao();
//			QueryBuilder<DatabaseVersion, String> queryBuilder = versionDao.queryBuilder();
//			PreparedQuery<DatabaseVersion> preparedQuery = queryBuilder.prepare();
//			List<DatabaseVersion> versionList = versionDao.query( preparedQuery );
//			if (versionList.size() > 0) {
//				DatabaseVersion version = versionList.get( 0 );
//				return version.getVersionNumber();
//			} else
//				return 0;
//		} catch (SQLException sqle) {
//			logger.warn( "W- [AndroidNeoComDBHelper.readDatabaseVersion]> Database exception: " + sqle.getMessage() );
//			return 0;
//		}
//	}

	private void recreateTable( final ConnectionSource databaseConnection, final Class tableType, final int version ) {
		try {
			TableUtils.dropTable( databaseConnection, tableType, true );
			try {
				TableUtils.createTableIfNotExists( databaseConnection, tableType );
			} catch (SQLException sqle) {
				logger.warn( "SQL [AndroidNeoComDBHelper.recreateTable.{}]> SQL NeoComDatabase: {}",
						tableType.getSimpleName(), sqle.getMessage() );
			}
		} catch (RuntimeException rtex) {
			logger.error( "E [AndroidNeoComDBHelper.recreateTable.{}]> Error upgrading to version {}.",
					tableType.getSimpleName(), version );
			rtex.printStackTrace();
		} catch (SQLException sqle) {
			logger.error( "E [AndroidNeoComDBHelper.recreateTable.{}]> Error upgrading to version {}.",
					tableType.getSimpleName(), version );
			sqle.printStackTrace();
		}
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

		public AndroidNeoComDBHelper build() throws IOException {
			// Check and create only a singleton helper.
			if (null == singleton) {
				Objects.requireNonNull( this.appContext );
				Objects.requireNonNull( this.fileSystem );
				singleton = new AndroidNeoComDBHelper( this.appContext
						, this.fileSystem.accessPublicResource4Path( databaseName )
						, databaseVersion );
				singleton.getWritableDatabase();
			}
			return singleton;
		}
	}
}
