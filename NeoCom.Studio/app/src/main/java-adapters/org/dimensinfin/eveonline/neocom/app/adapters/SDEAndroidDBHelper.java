package org.dimensinfin.eveonline.neocom.app.adapters;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dimensinfin.eveonline.neocom.app.database.AndroidRawStatement;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.domain.EsiLocation;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SDEAndroidDBHelper implements ISDEDatabaseAdapter {
	private static Logger logger = LoggerFactory.getLogger(SDEAndroidDBHelper.class);
	private IFileSystem fileSystemAdapter;

	private String schema = "jdbc:sqlite";
	private String databasePath;
	private String databaseName;
	private int databaseVersion = 0;
	private SQLiteDatabase ccpDatabase;
	private JdbcPooledConnectionSource connectionSource = null;

	private Dao<EsiLocation, Long> locationDao = null;

	private SDEAndroidDBHelper() {
		super();
	}

	public Dao<EsiLocation, Long> getLocationDao() throws SQLException {
		if (null == this.locationDao)
			this.locationDao = DaoManager.createDao(this.connectionSource, EsiLocation.class);
		return this.locationDao;
	}

	protected SQLiteDatabase getSDEDatabase() {
		if (null == this.ccpDatabase) this.openSDEDB();
		return this.ccpDatabase;
	}

	private String getConnectionDescriptor() {
		return this.schema + ":" + this.databasePath + this.databaseName;
	}

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
			final String path = this.fileSystemAdapter.accessResource4Path(databasePath + databaseName);
			this.ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			Objects.requireNonNull(this.ccpDatabase);
			logger.info("-- [SDEAndroidDBHelper.openSDEDB]> Opened database {} successfully with version {}."
					, path, databaseVersion);
		}
		logger.info("<< [SDEAndroidDBHelper.openSDEDB]");
	}
	private void createConnectionSource() throws SQLException {
		this.connectionSource = new JdbcPooledConnectionSource( this.schema + ":" +
				                                                        this.fileSystemAdapter.accessResource4Path(
				                                                        		this.databasePath + this.databaseName));
		this.connectionSource.setMaxConnectionAgeMillis(TimeUnit.MINUTES.toMillis(5)); // Keep the connections open for 5 minutes
		this.connectionSource.setCheckConnectionsEveryMillis(TimeUnit.SECONDS.toMillis(60));
		this.connectionSource.setTestBeforeGet(
				true); // Enable the testing of connections right before they are handed to the user
	}

	protected boolean isDatabaseDefinitionValid() {
		return ((null != this.databasePath) && (null != this.databaseName));
	}

	private void onCreate( final ConnectionSource databaseConnection ) {
		logger.info(">> [SDEDatabaseAdapter.onCreate]");
		// Create the tables that do not exist
		try {
			TableUtils.createTableIfNotExists(databaseConnection, EsiLocation.class);
		} catch (SQLException sqle) {
			logger.warn("SQL [SDEDatabaseAdapter.onCreate]> SQL SDEDatabase: {}", sqle.getMessage());
		}
		logger.info("<< [SDEDatabaseAdapter.onCreate]");
	}

	// - I S D E D A T A B A S E A D A P T E R

	/**
	 * This is the specific SpringBoot implementation for the SDE database adaptation. We can create compatible
	 * <code>RawStatements</code> that can isolate the generic database access code from the platform specific. This
	 * statement uses the database connection to create a generic JDBC Java statement.
	 */
	public AndroidRawStatement constructStatement( final String query, final String[] parameters ) throws SQLException {
		return new AndroidRawStatement(this.getSDEDatabase(), query, parameters);
	}

	// - C O R E
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				       .append("schema", schema)
				       .append("databasePath", databasePath)
				       .append("databaseName", databaseName)
				       .append("databaseVersion", databaseVersion)
				       .toString();
	}

	// - B U I L D E R
	public static class Builder {
		private SDEAndroidDBHelper onConstruction;

		public Builder() {
			this.onConstruction = new SDEAndroidDBHelper();
		}

		public Builder withDatabasePath( final String databasePath ) {
			this.onConstruction.databasePath = databasePath;
			return this;
		}

		public Builder withDatabaseName( final String databaseName ) {
			this.onConstruction.databaseName = databaseName;
			return this;
		}

		public Builder withFileSystemAdapter( final IFileSystem fileSystemAdapter ) {
			this.onConstruction.fileSystemAdapter = fileSystemAdapter;
			return this;
		}

		public ISDEDatabaseAdapter build() {
			Objects.requireNonNull(this.onConstruction.databasePath);
			Objects.requireNonNull(this.onConstruction.databaseName);
			Objects.requireNonNull(this.onConstruction.fileSystemAdapter);
			return this.onConstruction;
		}
	}
}
