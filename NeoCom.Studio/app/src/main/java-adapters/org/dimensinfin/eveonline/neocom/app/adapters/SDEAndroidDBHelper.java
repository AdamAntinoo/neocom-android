package org.dimensinfin.eveonline.neocom.app.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.adapters.IFileSystem;
import org.dimensinfin.eveonline.neocom.app.database.AndroidRawStatement;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;


import static org.dimensinfin.eveonline.neocom.BuildConfig.SDE_DB_VERSION;

public class SDEAndroidDBHelper extends OrmLiteSqliteOpenHelper implements ISDEDatabaseAdapter {
	private static Logger logger = LoggerFactory.getLogger(SDEAndroidDBHelper.class);
	private static SDEAndroidDBHelper singleton;

	// - C O M P O N E N T S
	private Context appContext;
	private String storageLocation ;
	private int databaseVersion = 0;
//	private IFileSystem fileSystemAdapter;

	//	private String schema = "jdbc:sqlite";
//	private String databasePath;
//	private String databaseName;
	private SQLiteDatabase ccpDatabase;
//	private JdbcPooledConnectionSource connectionSource = null;

	private Dao<EsiLocation, Long> locationDao = null;

	// - C O N S T R U C T O R S
	private SDEAndroidDBHelper( final Context context, final String databaseName, final int databaseVersion ) {
		super(context, databaseName, null, databaseVersion, R.raw.ormlite_config);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databasePath {}"
				, databaseName);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databaseVersion {}"
				, databaseVersion);
		this.appContext = context;
		this.storageLocation = databaseName;
		this.databaseVersion = databaseVersion;
		this.openSDEDB();
	}
	@Override
	public Integer getDatabaseVersion() {
		return this.databaseVersion;
	}
//	private SDEAndroidDBHelper() {
//		super();
//	}

//	private void setFileSystemAdapter( final IFileSystem fileSystemAdapter ) {
//		this.fileSystemAdapter = fileSystemAdapter;
//	}

	protected SQLiteDatabase getSDEDatabase() {
		if (null == this.ccpDatabase) this.openSDEDB();
		return this.ccpDatabase;
	}

//	private String getConnectionDescriptor() {
//		return this.schema + ":" + this.databasePath + this.databaseName;
//	}

	/**
	 * Open a new pooled JDBC datasource connection list and stores its reference for use of the whole set of
	 * services. Being a pooled connection it can create as many connections as required to do requests in
	 * parallel to the database instance. This only is effective for MySql databases.
	 * <p>
	 * Check database definition before trying to open the database.
	 */
	protected void openSDEDB() {
		logger.info(">> [SDEAndroidDBHelper.openSDEDB]");
		if ((null == this.ccpDatabase) && (this.isDatabaseDefinitionValid())) {
			final String path = "jdbc:sqlite" + ":" + this.appContext.getResources().getString(R.string.sdedatabasefilename);
			this.ccpDatabase = SQLiteDatabase.openDatabase(this.storageLocation, null, SQLiteDatabase.OPEN_READWRITE);
			Objects.requireNonNull(this.ccpDatabase);
			logger.info("-- [SDEAndroidDBHelper.openSDEDB]> Opened database {} successfully with version {}."
					, path, databaseVersion);
		}
		logger.info("<< [SDEAndroidDBHelper.openSDEDB]");
	}

	protected boolean isDatabaseDefinitionValid() {
//		return ((null != this.databasePath) && (null != this.databaseName));
		return true;
	}
//	private void createConnectionSource() throws SQLException {
//		this.connectionSource = new JdbcPooledConnectionSource( this.schema + ":" +
//				                                                        this.fileSystemAdapter.accessResource4Path(
//				                                                        		this.databasePath + this.databaseName));
//		this.connectionSource.setMaxConnectionAgeMillis(TimeUnit.MINUTES.toMillis(5)); // Keep the connections open for 5 minutes
//		this.connectionSource.setCheckConnectionsEveryMillis(TimeUnit.SECONDS.toMillis(60));
//		this.connectionSource.setTestBeforeGet(
//				true); // Enable the testing of connections right before they are handed to the user
//	}

	private void onCreate( final ConnectionSource databaseConnection ) {
	}

	/**
	 * This is the specific SpringBoot implementation for the SDE database adaptation. We can create compatible
	 * <code>RawStatements</code> that can isolate the generic database access code from the platform specific. This
	 * statement uses the database connection to create a generic JDBC Java statement.
	 */
	public AndroidRawStatement constructStatement( final String query, final String[] parameters ) throws SQLException {
		return new AndroidRawStatement(this.getSDEDatabase(), query, parameters);
	}

	// - I S D E D A T A B A S E A D A P T E R

	public Dao<EsiLocation, Long> getLocationDao() throws NeoComRuntimeException {
		try {
			if (null == this.locationDao) {
				this.locationDao = DaoManager.createDao(this.getConnectionSource(), EsiLocation.class);
			}
		} catch (final SQLException sqle) {
			throw new NeoComRuntimeException(sqle);
		}
		return this.locationDao;
	}

	@Override
	public void onCreate( final SQLiteDatabase database, final ConnectionSource connectionSource ) {
		logger.info(">> [SDEDatabaseAdapter.onCreate]");
		// Create the tables that do not exist
		try {
			TableUtils.createTableIfNotExists(connectionSource, EsiLocation.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [SDEDatabaseAdapter.onCreate]> SQL SDEDatabase: {}", sqle.getMessage());
		}
		logger.info("<< [SDEDatabaseAdapter.onCreate]");
	}

	@Override
	public void onUpgrade( final SQLiteDatabase database, final ConnectionSource connectionSource, final int oldVersion, final int newVersion ) {

	}

	// - C O R E
//	@Override
//	public String toString() {
//		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
//				       .append("schema", schema)
//				       .append("databasePath", databasePath)
//				       .append("databaseName", databaseName)
//				       .append("databaseVersion", databaseVersion)
//				       .toString();
//	}


	@Override
	public String toString() {
		return new ToStringBuilder(this)
				       .append("storageLocation", this.storageLocation)
				       .append("databaseVersion", this.databaseVersion)
				       .toString();
	}

	// - B U I L D E R

	/**
	 * This is an special Builder because the class inherits an specific Constructor that cannot be changed. That constructor should be used on the
	 * final build so all fields are stored on the Builder to later be copied to the new instance.
	 */
	public static class Builder {
		private Context appContext;
		private String databaseName;

		public Builder() {
		}

		public SDEAndroidDBHelper.Builder withAppContext( final Context appContext ) {
			Objects.requireNonNull(appContext);
			this.appContext = appContext;
			return this;
		}

		public SDEAndroidDBHelper.Builder withDatabaseName( final String databaseName ) {
			Objects.requireNonNull(databaseName);
			this.databaseName = databaseName;
			return this;
		}

		public ISDEDatabaseAdapter build( final IFileSystem fileSystem ) throws IOException {
			// Check and create only a singleton helper.
			if (null == singleton) {
				Objects.requireNonNull(this.appContext);
				Objects.requireNonNull(this.databaseName);
				singleton = new SDEAndroidDBHelper(this.appContext,
						fileSystem.accessResource4Path(databaseName),
						SDE_DB_VERSION);
			}
			return singleton;
		}
	}
}
