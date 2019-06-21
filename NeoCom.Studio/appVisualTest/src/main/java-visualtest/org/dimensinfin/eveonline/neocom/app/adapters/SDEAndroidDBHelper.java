package org.dimensinfin.eveonline.neocom.app.adapters;

import java.util.Objects;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.dimensinfin.eveonline.neocom.database.ISDEDBHelper;
import org.dimensinfin.eveonline.neocom.database.ISDEDatabaseAdapter;
import org.dimensinfin.eveonline.neocom.database.RawStatement;
import org.dimensinfin.eveonline.neocom.database.SDEDatabaseManager;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */
public class SDEAndroidDBHelper extends SDEDatabaseManager implements ISDEDatabaseAdapter {
	private static Logger logger = LoggerFactory.getLogger(SDEAndroidDBHelper.class);

	private IFileSystem fileSystemAdapter;
	private SQLiteDatabase ccpDatabase;
	private boolean isOpen = false;
	private String databasePath = "";
	private String databaseName = "sde.db";
	//	private static final String CCPDATABASE_URL = "jdbc:sqlite:src/main/resources/eve.db-this reference is platform specific-";
	//private static String DEFAULT_CONNECTION_DESCRIPTOR = "jdbc:sqlite:src/main/resources/eve.db";

	// - F I E L D - S E C T I O N ............................................................................
	private String schema = "jdbc:sqlite";
	private int databaseVersion = 0;
	private boolean databaseValid = false;
	//	private Connection connectionSource = null;

	//	private final Hashtable<Integer, EveItem> itemCache = new Hashtable<Integer, EveItem>(1000);
	//	private final Hashtable<Long, EveLocation> locationsCache = new Hashtable<Long, EveLocation>(200);

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SDEAndroidDBHelper() {
		super();
	}

	protected SQLiteDatabase getSDEDatabase() {
		if (null == this.ccpDatabase) {
			//			if (this.databaseIsValid())
			this.openSDEDB();
		}
		return this.ccpDatabase;
	}

	public ISDEDBHelper setDatabaseSchema( final String newschema ) {
		this.schema = newschema;
		return this;
	}

	public ISDEDBHelper setDatabasePath( final String newpath ) {
		this.databasePath = newpath;
		return this;
	}

	public ISDEDBHelper setDatabaseName( final String instanceName ) {
		this.databaseName = instanceName;
		return this;
	}

	public ISDEDBHelper setDatabaseVersion( final int newVersion ) {
		this.databaseVersion = newVersion;
		return this;
	}

	public ISDEDBHelper build() throws SQLException {
		if (StringUtils.isEmpty(schema))
			throw new SQLException("Cannot create connection: 'schema' is empty.");
		if (StringUtils.isEmpty(databaseName))
			throw new SQLException("Cannot create connection: 'databaseName' is empty.");
		databaseValid = true;
		openSDEDB();
		return this;
	}

	public String getConnectionDescriptor() {
		return schema + ":" + databasePath + databaseName;
	}

	public boolean databaseIsValid() {
		if (this.isOpen)
			if (databaseValid)
				if (null != ccpDatabase) return true;
		return false;
	}

	/**
	 * Open a new pooled JDBC datasource connection list and stores its reference for use of the whole set of
	 * services. Being a pooled connection it can create as many connections as required to do requests in
	 * parallel to the database instance. This only is effective for MySql databases.
	 *
	 * Check database definition before trying to open the database.
	 */
	protected boolean openSDEDB() {
		logger.info(">> [SDEAndroidDBHelper.openSDEDB]");
		if (!this.isOpen) {
			if (null == this.ccpDatabase) {
				if (this.isDatabaseDefinitionValid()) {
					final String path = this.fileSystemAdapter.accessResource4Path(databasePath + databaseName);
					// TODO - Network and card validation should be done at a higher level.
					//					if (AndroidGlobalDataManager.sdcardAvailable()) {
					this.ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
					Objects.requireNonNull(this.ccpDatabase);
					logger.info("-- [SDEAndroidDBHelper.openSDEDB]> Opened database {} successfully with version {}."
							, path, databaseVersion);
					//					}
				} else
					return true;
				return false;
			}
			return true;
		}
		logger.info("<< [SDEAndroidDBHelper.openSDEDB]");
		return this.isOpen;
	}

	protected boolean isDatabaseDefinitionValid() {
		if ((null != databasePath) && (null != this.databaseName)) return true;
		return false;
	}
	//	private void createConnectionSource() throws SQLException {
	////		final String localConnectionDescriptor = schema + ":" + databasePath + databaseName;
	//		if (databaseValid) {
	//			try {
	//				Class.forName("org.sqlite.JDBC");
	//			} catch (ClassNotFoundException cnfe) {
	//				throw new SQLException("Cannot create connection. {}.", cnfe.getMessage());
	//			}
	//			connectionSource = DriverManager.getConnection(getConnectionDescriptor());
	//			connectionSource.setAutoCommit(false);
	//		} else throw new SQLException("Cannot create connection, database validation not passed.");
	//	}

	//	private Connection getSDEConnection() throws SQLException {
	//		if (null != connectionSource) return connectionSource;
	//		else throw new SQLException("Cannot create connection, database validation not passed.");
	//	}

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

	//- CLASS IMPLEMENTATION ...................................................................................

	/**
	 * This is an special class to do the adaptation between the differences between the ormlite implementation on Java and on Android. For
	 * example on Java the column index starts at 1 while on Android the column index starts at 0.
	 */
	public static class AndroidRawStatement extends RawStatement {
		private Cursor cursor;

		public AndroidRawStatement( final SQLiteDatabase database, final String query, final String[] parameters ) throws
				SQLException {
			if (null != database) {
				cursor = database.rawQuery(query, parameters);
				if (null == cursor) throw new SQLException("Invalid statement when processing query: " + query);
			} else throw new SQLException("No valid connection to database to create statement. " + query);
		}

		@Override
		public boolean moveToFirst() {
			return cursor.moveToFirst();
		}

		@Override
		public boolean moveToLast() {
			return cursor.moveToLast();
		}

		@Override
		public boolean moveToNext() {
			return cursor.moveToNext();
		}

		@Override
		public boolean isFirst() {
			return cursor.isFirst();
		}

		@Override
		public boolean isLast() {
			return cursor.isLast();
		}

		@Override
		public String getString( int colindex ) {
			return cursor.getString(colindex - 1);
		}

		@Override
		public short getShort( int colindex ) {
			return cursor.getShort(colindex - 1);
		}

		@Override
		public int getInt( int colindex ) {
			return cursor.getInt(colindex - 1);
		}

		@Override
		public long getLong( int colindex ) {
			return cursor.getLong(colindex - 1);
		}

		@Override
		public float getFloat( int colindex ) {
			return cursor.getFloat(colindex - 1);
		}

		@Override
		public double getDouble( int colindex ) {
			return cursor.getDouble(colindex - 1);
		}

		public void close() {
			if (null != cursor) cursor.close();
		}
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
