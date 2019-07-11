package org.dimensinfin.eveonline.neocom.app.database;

import android.database.sqlite.SQLiteDatabase;

import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Adam Antinoo
 */
public class SDEAndroidDBHelper /*extends SDEDatabaseManager*/ implements ISDEDatabaseAdapter {
	private static Logger logger = LoggerFactory.getLogger(SDEAndroidDBHelper.class);
	private IFileSystem fileSystemAdapter;

	private String schema = "jdbc:sqlite";
	private String databasePath;
	private String databaseName;
	private int databaseVersion = 0;
	private SQLiteDatabase ccpDatabase;

	private SDEAndroidDBHelper() {
		super();
	}

	protected SQLiteDatabase getSDEDatabase() throws java.sql.SQLException {
		if (null == this.ccpDatabase) this.openSDEDB();
		return this.ccpDatabase;
	}

	/**
	 * Open a new pooled JDBC datasource connection list and stores its reference for use of the whole set of
	 * services. Being a pooled connection it can create as many connections as required to do requests in
	 * parallel to the database instance. This only is effective for MySql databases.
	 * <p>
	 * Check database definition before trying to open the database.
	 */
	protected void openSDEDB() throws SQLException {
		logger.info(">> [SDEAndroidDBHelper.openSDEDB]");
			if (null == this.ccpDatabase) {
				if (this.isDatabaseDefinitionValid()) {
					final String path = this.fileSystemAdapter.accessResource4Path(databasePath + databaseName);
					this.ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
					Objects.requireNonNull(this.ccpDatabase);
					logger.info("-- [SDEAndroidDBHelper.openSDEDB]> Opened database {} successfully with version {}."
							, path, databaseVersion);
				}
			}
		logger.info("<< [SDEAndroidDBHelper.openSDEDB]");
	}

	protected boolean isDatabaseDefinitionValid() {
		if ((null != this.databasePath) && (null != this.databaseName)) return true;
		return false;
	}

	/**
	 * This is the specific SpringBoot implementation for the SDE database adaptation. We can create compatible
	 * <code>RawStatements</code> that can isolate the generic database access code from the platform specific. This
	 * stetement uses the database connection to create a generic JDBC Java statement.
	 */
	public AndroidRawStatement constructStatement( final String query, final String[] parameters ) throws SQLException {
		return new AndroidRawStatement(this.getSDEDatabase(), query, parameters);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("NeoComAndroidDBHelper [");
		final String localConnectionDescriptor = schema + ":" + databasePath + databaseName;
		buffer.append("Descriptor: ").append(localConnectionDescriptor);
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
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
