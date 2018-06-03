//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.apache.commons.lang3.StringUtils;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */
// - CLASS IMPLEMENTATION ...................................................................................
public class SDEAndroidDBHelper extends SDEDatabaseManager implements ISDEDBHelper {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(SDEAndroidDBHelper.class);
	//	private static final String CCPDATABASE_URL = "jdbc:sqlite:src/main/resources/eve.db-this reference is platform specific-";
	//private static String DEFAULT_CONNECTION_DESCRIPTOR = "jdbc:sqlite:src/main/resources/eve.db";

	// - F I E L D - S E C T I O N ............................................................................
	private String schema = "jdbc:sqlite";
	private String databasePath = "/";
	private String databaseName = "sde.db";
	private int databaseVersion = 0;
	private boolean databaseValid = false;
	private boolean isOpen = false;
	//	private Connection connectionSource = null;
	private SQLiteDatabase ccpDatabase = null;

	//	private final Hashtable<Integer, EveItem> itemCache = new Hashtable<Integer, EveItem>(1000);
	//	private final Hashtable<Long, EveLocation> locationsCache = new Hashtable<Long, EveLocation>(200);

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SDEAndroidDBHelper () {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ISDEDBHelper setDatabaseSchema (final String newschema) {
		this.schema = newschema;
		return this;
	}

	public ISDEDBHelper setDatabasePath (final String newpath) {
		this.databasePath = newpath;
		return this;
	}

	public ISDEDBHelper setDatabaseName (final String instanceName) {
		this.databaseName = instanceName;
		return this;
	}

	public ISDEDBHelper setDatabaseVersion (final int newVersion) {
		this.databaseVersion = newVersion;
		return this;
	}

	public ISDEDBHelper build () throws SQLException {
		if ( StringUtils.isEmpty(schema) )
			throw new SQLException("Cannot create connection: 'schema' is empty.");
		if ( StringUtils.isEmpty(databaseName) )
			throw new SQLException("Cannot create connection: 'databaseName' is empty.");
		databaseValid = true;
		openSDEDB();
		return this;
	}

	public String getConnectionDescriptor () {
		return schema + ":" + databasePath + databaseName;
	}

	public boolean databaseIsValid () {
		if ( isOpen )
			if ( databaseValid )
				if ( null != ccpDatabase ) return true;
		return false;
	}

	/**
	 * Open a new pooled JDBC datasource connection list and stores its reference for use of the whole set of
	 * services. Being a pooled connection it can create as many connections as required to do requests in
	 * parallel to the database instance. This only is effective for MySql databases.
	 *
	 * @return
	 */
	private boolean openSDEDB () {
		logger.info(">> [SDEAndroidDBHelper.openSDEDB]");
		if ( !isOpen ) {
			if ( null == ccpDatabase ) {
				final String path = Environment.getExternalStorageDirectory() + "/" + databasePath + databaseName;
				if ( AndroidGlobalDataManager.sdcardAvailable() ) {
					ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
					logger.info("-- [SDEAndroidDBHelper.openSDEDB]> Opened database {} successfully with version {}."
							, path, databaseVersion);
				} else
					return true;
				return false;
			}
			return true;
		}
		logger.info("<< [SDEAndroidDBHelper.openSDEDB]");
		return isOpen;
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
	 *
	 * @param query
	 * @param parameters
	 * @return
	 */
	protected AndroidRawStatement constructStatement (final String query, final String[] parameters) throws SQLException {
		return new AndroidRawStatement(ccpDatabase, query, parameters);
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("NeoComAndroidDBHelper [");
		final String localConnectionDescriptor = schema + ":" + databasePath + databaseName;
		buffer.append("Descriptor: ").append(localConnectionDescriptor);
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	public static class AndroidRawStatement extends RawStatement {
		private Cursor cursor;

		public AndroidRawStatement (final SQLiteDatabase database, final String query, final String[] parameters) throws
				SQLException {
			if ( null != database ) {
				final Cursor cursor = database.rawQuery(query, parameters);
				if ( null == cursor ) throw new SQLException("Invalid statement when processing query: " + query);
			} else throw new SQLException("No valid connection to database to create statement. " + query);
		}

		@Override
		public boolean moveToFirst () {
			return cursor.moveToFirst();
		}

		@Override
		public boolean moveToLast () {
			return cursor.moveToLast();
		}

		@Override
		public boolean moveToNext () {
			return cursor.moveToNext();
		}

		@Override
		public boolean isFirst () {
			return cursor.isFirst();
		}

		@Override
		public boolean isLast () {
			return cursor.isLast();
		}

		@Override
		public String getString (int colindex) {
			return cursor.getString(colindex);
		}

		@Override
		public short getShort (int colindex) {
			return cursor.getShort(colindex);
		}

		@Override
		public int getInt (int colindex) {
			return cursor.getInt(colindex);
		}

		@Override
		public long getLong (int colindex) {
			return cursor.getLong(colindex);
		}

		@Override
		public float getFloat (int colindex) {
			return cursor.getFloat(colindex);
		}

		@Override
		public double getDouble (int colindex) {
			return cursor.getDouble(colindex);
		}

		public void close () {
			if ( null != cursor ) cursor.close();
		}
	}

	//	public static class SBRawStatement extends RawStatement {
	//		private PreparedStatement prepStmt = null;
	//		private ResultSet cursor = null;
	//
	//		public SBRawStatement (final Connection privateConnection, final String query, final String[] parameters) throws
	//				SQLException {
	//			if ( null != privateConnection ) {
	//				prepStmt = privateConnection.prepareStatement(query);
	//				for (int i = 0; i < parameters.length; i++) {
	//					prepStmt.setString(i + 1, parameters[i]);
	//				}
	//				cursor = prepStmt.executeQuery();
	//				if ( null == cursor ) throw new SQLException("Invalid statement when processing query: " + query);
	//			} else throw new SQLException("No valid connection to database to create statement. {}", query);
	//		}
	//
	//		//		@Override
	//		//		public int getCount() {
	//		//			return 1;
	//		//		}
	//		//
	//		//		@Override
	//		//		public int getPosition() {
	//		//			return 1;
	//		//		}
	//
	//		@Override
	//		public boolean moveToFirst () {
	//			try {
	//				return cursor.first();
	//			} catch (SQLException sqle) {
	//				return false;
	//			}
	//		}
	//
	//		@Override
	//		public boolean moveToLast () {
	//			try {
	//				return cursor.last();
	//			} catch (SQLException sqle) {
	//				return false;
	//			}
	//		}
	//
	//		@Override
	//		public boolean moveToNext () {
	//			try {
	//				return cursor.next();
	//			} catch (SQLException sqle) {
	//				return false;
	//			}
	//		}
	//
	//		@Override
	//		public boolean isFirst () {
	//			try {
	//				return cursor.isFirst();
	//			} catch (SQLException sqle) {
	//				return false;
	//			}
	//		}
	//
	//		@Override
	//		public boolean isLast () {
	//			try {
	//				return cursor.isLast();
	//			} catch (SQLException sqle) {
	//				return false;
	//			}
	//		}
	//
	//		//		@Override
	//		//		public int getColumnIndex( String columnName ) {
	//		//			return prepStmt.getMetaData().get
	//		//		}
	//
	//		@Override
	//		public String getString (int colindex) {
	//			try {
	//				return cursor.getString(colindex);
	//			} catch (SQLException sqle) {
	//				return "";
	//			}
	//		}
	//
	//		@Override
	//		public short getShort (int colindex) {
	//			try {
	//				return cursor.getShort(colindex);
	//			} catch (SQLException sqle) {
	//				return 0;
	//			}
	//		}
	//
	//		@Override
	//		public int getInt (int colindex) {
	//			try {
	//				return cursor.getInt(colindex);
	//			} catch (SQLException sqle) {
	//				return 0;
	//			}
	//		}
	//
	//		@Override
	//		public long getLong (int colindex) {
	//			try {
	//				return cursor.getLong(colindex);
	//			} catch (SQLException sqle) {
	//				return 0;
	//			}
	//		}
	//
	//		@Override
	//		public float getFloat (int colindex) {
	//			try {
	//				return cursor.getFloat(colindex);
	//			} catch (SQLException sqle) {
	//				return 0;
	//			}
	//		}
	//
	//		@Override
	//		public double getDouble (int colindex) {
	//			try {
	//				return cursor.getDouble(colindex);
	//			} catch (SQLException sqle) {
	//				return 0;
	//			}
	//		}
	//
	//		//		@Override
	//		//		public int getType( int colindex ) {
	//		//			return cursor.getType();
	//		//		}
	//
	//		public void close () {
	//			try {
	//				if ( null != cursor ) cursor.close();
	//				if ( null != prepStmt ) prepStmt.close();
	//			} catch (SQLException sqle) {
	//				sqle.printStackTrace();
	//			}
	//		}
	//	}
}

// - UNUSED CODE ............................................................................................
//[01]
