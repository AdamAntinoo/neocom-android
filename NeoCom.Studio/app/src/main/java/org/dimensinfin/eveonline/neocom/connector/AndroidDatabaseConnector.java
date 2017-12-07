//	PROJECT:      NeoCom.Databases (NEOC.D)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//								environment limits.
//								Database and model adaptations for storage model independency.
package org.dimensinfin.eveonline.neocom.connector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.ModelWideConstants;
import org.dimensinfin.eveonline.neocom.database.NeoComBaseDatabase;
import org.dimensinfin.eveonline.neocom.database.NeocomAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.market.MarketDataSet;
import org.dimensinfin.eveonline.neocom.model.ApiKey;
import org.dimensinfin.eveonline.neocom.model.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComBlueprint;
import org.dimensinfin.eveonline.neocom.model.Property;
import org.dimensinfin.eveonline.neocom.model.TimeStamp;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceList;
import org.dimensinfin.eveonline.neocom.planetary.Schematics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidDatabaseConnector extends NeoComBaseDatabase implements INeoComDatabase {

	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("AndroidDatabaseConnector");

	//	protected static String												DATABASE_NAME							= "neocom-v.r.db";
	//	protected static int													DATABASE_VERSION					= 010;

	private static final String SELECT_SCHEMATICS_INFO = "SELECT pstms.typeID, pstms.quantity, pstms.isInput"
			+ " FROM   planetSchematicsTypeMap pstmt, planetSchematicsTypeMap pstms" + " WHERE  pstmt.typeID = ?"
			+ " AND    pstmt.isInput = 0" + " AND    pstms.schematicID = pstmt.schematicID";
	private static final String SELECT_ITEM_BYID = "SELECT it.typeID AS typeID, it.typeName AS typeName"
			+ " , ig.groupName AS groupName" + " , ic.categoryName AS categoryName" + " , it.basePrice AS basePrice"
			+ " , it.volume AS volume" + " , IFNULL(img.metaGroupName, " + '"' + "NOTECH" + '"' + ") AS Tech"
			+ " FROM invTypes it" + " LEFT OUTER JOIN invGroups ig ON ig.groupID = it.groupID"
			+ " LEFT OUTER JOIN invCategories ic ON ic.categoryID = ig.categoryID"
			+ " LEFT OUTER JOIN invMetaTypes imt ON imt.typeID = it.typeID"
			+ " LEFT OUTER JOIN invMetaGroups img ON img.metaGroupID = imt.metaGroupID" + " WHERE it.typeID = ?";

	private static final String SELECT_LOCATIONBYID = "SELECT md.itemID AS locationID, md.typeID AS typeID, md.itemName AS locationName, md.security AS security"
			+ " , IFNULL(md.solarSystemID, -1) AS systemID, ms.solarSystemName AS system"
			+ " , IFNULL(md.constellationID, -1) AS constellationID, mc.constellationName AS constellation"
			+ " , IFNULL(md.regionID, -1) AS regionID, mr.regionName AS region" + " FROM mapDenormalize md"
			+ " LEFT OUTER JOIN mapRegions mr ON mr.regionID = md.regionID"
			+ " LEFT OUTER JOIN mapConstellations mc ON mc.constellationID = md.constellationID"
			+ " LEFT OUTER JOIN mapSolarSystems ms ON ms.solarSystemID = md.solarSystemID" + " WHERE itemID = ?";
	private static final String SELECT_MATERIAL_USAGE = "SELECT typeID, materialTypeID, quantity FROM industryActivityMaterials WHERE typeID = ? AND activityID = 1";
	private static final String LOM4BLUEPRINT = "SELECT iam.typeID, itb.typeName, iam.materialTypeID, it.typeName, ig.groupName, ic.categoryName, iam.quantity, iam.consume"
			+ " FROM industryActivityMaterials iam, invTypes itb, invTypes it, invGroups ig, invCategories ic"
			+ " WHERE iam.typeID = ?" + " AND iam.activityID = 1" + " AND itb.typeID = iam.typeID"
			+ " AND it.typeID = iam.materialTypeID" + " AND ig.groupID = it.groupID" + " AND ic.categoryID = ig.categoryID";

	private static final String INDUSTRYACTIVITYMATERIALS = "SELECT materialTypeID, quantity, consume FROM industryActivityMaterials WHERE typeID = ? AND activityID = 8";
	private static final String STATIONTYPE = "SELECT stationTypeID FROM staStations WHERE stationID = ?";
	private static final String JOB_COMPLETION_TIME = "SELECT typeID, time FROM industryActivity WHERE typeID = ? AND activityID = ?";
	private static final String CHECK_INVENTION = "SELECT count(*) AS counter"
			+ " FROM industryActivityProducts iap" + " WHERE iap.typeID = ?" + " AND iap.activityID = 8";
	private static final String INVENTION_PRODUCT = "SELECT productTypeID FROM industryActivityProducts WHERE typeID = ? AND activityID = 8";
	private static final String CHECK_MANUFACTURABLE = "SELECT count(*) AS counter FROM industryActivityProducts iap WHERE iap.productTypeID = ? AND iap.activityID = 1";
	private static final String CHECK_REACTIONABLE = "SELECT count(*) AS counter FROM industryActivityProducts iap WHERE iap.productTypeID = ? AND iap.activityID = 1";
	private static final String CHECK_PLANETARYPRODUCED = "SELECT count(*) AS counter FROM industryActivityProducts iap WHERE iap.productTypeID = ? AND iap.activityID = 1";
	private static final String REACTION_COMPONENTS = "SELECT" + "   invTypeReactions.reactionTypeID"
			+ " , invTypes.typeID, invTypes.typeName" + " , invTypeReactions.input"
			+ " , COALESCE(dgmTypeAttributes.valueInt, dgmTypeAttributes.valueFloat) * invTypeReactions.quantity AS quantity"
			+ " FROM invTypeReactions, dgmTypeAttributes, invTypes" + " WHERE"
			+ " invTypes.typeId = invTypeReactions.typeID AND" + " invTypeReactions.reactionTypeID IN ("
			+ "    SELECT reactionTypeID" + "    FROM invTypeReactions" + "    WHERE typeID = ? ) AND"
			+ " dgmTypeAttributes.typeID = invTypeReactions.typeID";

	private static final String SEARCH_LISTOFMATERIALS = "SELECT ais.typeID, ais.skillID, ais.level, it.typeName "
			+ " FROM industryActivitySkills ais, invTypes it "
			+ " WHERE ais.typeID = ? "
			+ " AND ais.activityID = 1 "
			+ " AND it.typeID=ais.skillID";
	//	public static MyLocation getLocation(final long locationID, final MyAsset parentAsset) {
	//		// Offices
	//		long fixedLocationID = locationID;
	//		if (fixedLocationID >= 66000000) {
	//			if (fixedLocationID < 66014933) {
	//				fixedLocationID = fixedLocationID - 6000001;
	//			} else {
	//				fixedLocationID = fixedLocationID - 6000000;
	//			}
	//		}
	//		MyLocation location = StaticData.get().getLocations().get(fixedLocationID);
	//		if (location != null) return location;
	//		if (parentAsset != null) {
	//			location = parentAsset.getLocation();
	//			if (location != null) return location;
	//		}
	//		location = CitadelGetter.get(locationID);
	//		if (location != null) return location;
	//		return new MyLocation(locationID);
	//	}

	// - F I E L D - S E C T I O N ............................................................................
	private NeocomAndroidDBHelper neocomDBHelper = null;
	protected Context _context = null;
	private SQLiteDatabase staticDatabase = null;
	private SQLiteDatabase ccpDatabase = null;

	private final SparseArray<EveItem> itemCache = new SparseArray<EveItem>();
	private final SparseArray<MarketDataSet> buyMarketDataCache = new SparseArray<MarketDataSet>();
	private final SparseArray<MarketDataSet> sellMarketDataCache = new SparseArray<MarketDataSet>();
	//	private final SparseArray<Outpost>				outpostsCache							= new SparseArray<Outpost>();;

	private final HashMap<Long, NeoComAsset> containerCache = new HashMap<Long, NeoComAsset>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AndroidDatabaseConnector (final Context app) {
		_context = app;
	}

	/**
	 * Get the complete list of assets that are Planetary Materials.
	 *
	 * @return
	 */
	public ArrayList<NeoComAsset> accessAllPlanetaryAssets (final long characterID) {
		// Select assets for each one of the Planetary categories.
		ArrayList<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		assetList.addAll(this.searchAsset4Category(characterID, "Planetary Commodities"));
		assetList.addAll(this.searchAsset4Category(characterID, "Planetary Resources"));
		return assetList;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public boolean checkInvention (final int typeID) {
		return this.checkRecordExistence(typeID, AndroidDatabaseConnector.CHECK_INVENTION);
	}

	public boolean checkManufacturable (final int typeID) {
		return this.checkRecordExistence(typeID, AndroidDatabaseConnector.CHECK_MANUFACTURABLE);
	}

	/**
	 * removes from the application database any asset and blueprint that contains the special -1 code as the
	 * owner identifier. Those records are from older downloads and have to be removed to avoid merging with the
	 * new download.
	 */
	public synchronized void clearInvalidRecords (final long pilotid) {
		SQLiteDatabase database = null;
		try {
			database = this.getAppDatabase();
			synchronized (database) {
				database.beginTransaction();
				AndroidDatabaseConnector.logger.info("-- clearInvalidAssets rows deleted ASSETS [OWNERID = -1] - "
						+ database.delete("Assets", "ownerID" + "=" + (pilotid * -1), null));
				AndroidDatabaseConnector.logger.info("-- clearInvalidAssets rows deleted BLUEPRINTS [OWNERID = -1] - "
						+ database.delete("Blueprints", "ownerID" + "=" + (pilotid * -1), null));
				database.setTransactionSuccessful();
			}
		} catch (final SQLiteException ex) {
			AndroidDatabaseConnector.logger.warning("W> Problem clearing invalid assets. " + ex.getMessage());
		} finally {
			if ( null != database ) {
				database.endTransaction();
			}
		}
	}

	public void closeDatabases () {
		//		appDatabaseHelper.close();
		if ( null != staticDatabase ) {
			staticDatabase.close();
		}
		//		if (null != ccpDatabase) {
		//			ccpDatabase.close();
		//		}
		staticDatabase = null;
		//	ccpDatabase = null;
	}

	public Dao<ApiKey, String> getApiKeysDao () throws SQLException {
		return this.getNeocomDBHelper().getApiKeysDao();
	}

	public List<ApiKey> getApiList4Login (final String login) {
		AndroidDatabaseConnector.logger.info(">> [AndroidDatabaseConnector.getApiList4Login] login=" + login);
		// Access the database to get the list of keys. From that point on we can retrieve the characters easily.
		List<ApiKey> apilist = null;
		try {
			Dao<ApiKey, String> keyDao = NeoComAppConnector.getSingleton().getDBConnector().getApiKeysDao();
			QueryBuilder<ApiKey, String> queryBuilder = keyDao.queryBuilder();
			Where<ApiKey, String> where = queryBuilder.where();
			where.eq("login", login);
			PreparedQuery<ApiKey> preparedQuery = queryBuilder.prepare();
			apilist = keyDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
		}
		AndroidDatabaseConnector.logger.info("<< [AndroidDatabaseConnector.getApiList4Login]");
		return apilist;
	}

	public Dao<NeoComAsset, String> getAssetDAO () throws SQLException {
		return this.getNeocomDBHelper().getAssetDAO();
	}

	public Dao<NeoComBlueprint, String> getBlueprintDAO () throws SQLException {
		return this.getNeocomDBHelper().getBlueprintDAO();
	}

	//	public Dao<ApiKey, String> getApiKeysDao() throws SQLException {
	//		return this.getNeocomDBHelper().getApiKeysDao();
	//	}

	//	public Dao<Job, String> getJobDAO() throws SQLException {
	//		return this.getNeocomDBHelper().getJobDAO();
	//	}

	public Dao<EveLocation, String> getLocationDAO () throws SQLException {
		return this.getNeocomDBHelper().getLocationDAO();
	}

	//	public Dao<NeoComMarketOrder, String> getMarketOrderDAO() throws SQLException {
	//		return this.getNeocomDBHelper().getMarketOrderDAO();
	//	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao () throws SQLException {
		return this.getNeocomDBHelper().getPlanetaryResourceDao();
	}

	public Dao<Property, String> getPropertyDAO () throws SQLException {
		return this.getNeocomDBHelper().getPropertyDAO();
	}

	public Dao<ResourceList, String> getResourceListDao () throws SQLException {
		return this.getNeocomDBHelper().getResourceListDao();
	}

	public SQLiteDatabase getStaticDatabase () throws SQLException {
		if ( null == staticDatabase ) {
			this.openAppDataBase();
		}
		return staticDatabase;
	}

	public Dao<TimeStamp, String> getTimeStampDAO () throws SQLException {
		return this.getNeocomDBHelper().getTimeStampDAO();
	}

	public Dao<DatabaseVersion, String> getVersionDao () throws SQLException {
		return this.getNeocomDBHelper().getVersionDao();
	}

	public void loadSeedData () {
		neocomDBHelper.loadSeedData();
	}

	/**
	 * Opens a SQLite database to get access to the Items and locations. If there is any problem the method
	 * returns true.
	 *
	 * @return <code>true</code> if there was any problem.
	 */
	public boolean openAppDataBase () {
		if ( null == staticDatabase ) {
			final String database = NeoComAppConnector.getSingleton().getAppFilePath(R.string.appdatabasefilename);
			final String path = Environment.getExternalStorageDirectory() + "/" + database;
			//			try {
			if ( NeoComAppConnector.getSingleton().sdcardAvailable() ) {
				// Open the helper to check version and availability.
				// appDatabaseHelper = new EveDroidDBHelper(_context);
				staticDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
			} else
				return true;
			return false;
			//			} catch (final SQLException sqle) {
			//				AndroidDatabaseConnector.logger.severe("E> Failed to open database: " + database);
			//				AndroidDatabaseConnector.logger.severe("E> " + sqle.getMessage());
			//				throw new RuntimeException("E> Failed to open database: " + database + ". " + sqle.getMessage());
			//			}
		}
		return true;
	}

	public boolean openCCPDataBase () {
		if ( null == ccpDatabase ) {
			final String database = NeoComAppConnector.getSingleton().getAppFilePath(R.string.ccpdatabasefilename);
			final String path = Environment.getExternalStorageDirectory() + "/" + database;
			//			try {
			// InputStream dbStream =
			// _context.getResources().openRawResource(R.raw.eve);
			if ( NeoComAppConnector.getSingleton().sdcardAvailable() ) {
				ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			} else
				return true;
			return false;
			//			} catch (final SQLException sqle) {
			//				AndroidDatabaseConnector.logger.severe("E> Failed to open database: " + database);
			//				AndroidDatabaseConnector.logger.severe("E> " + sqle.getMessage());
			//			}
		}
		return true;
	}
	//	/**
	//	 * Opens a SQLite database to get access to the Items and locations. If there is any problem the method
	//	 * returns true.
	//	 *
	//	 * @return <code>true</code> if there was any problem.
	//	 */
	//	public boolean openAppDataBase() {
	//		if (null == staticDatabase) {
	//			final String database = NeoComAppConnector.getSingleton().getAppFilePath(R.string.appdatabasefilename);
	//			final String path = Environment.getExternalStorageDirectory() + "/" + database;
	//			//			try {
	//			if (AppConnector.sdcardAvailable()) {
	//				// Open the helper to check version and availability.
	//				// appDatabaseHelper = new EveDroidDBHelper(_context);
	//				staticDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
	//			} else
	//				return true;
	//			return false;
	//			//			} catch (final SQLException sqle) {
	//			//				AndroidDatabaseConnector.logger.severe("E> Failed to open database: " + database);
	//			//				AndroidDatabaseConnector.logger.severe("E> " + sqle.getMessage());
	//			//				throw new RuntimeException("E> Failed to open database: " + database + ". " + sqle.getMessage());
	//			//			}
	//		}
	//		return true;
	//	}
	//
	//	@Override
	//	public boolean openCCPDataBase() {
	//		if (null == ccpDatabase) {
	//			final String database = NeoComAppConnector.getSingleton().getAppFilePath(R.string.ccpdatabasefilename);
	//			final String path = Environment.getExternalStorageDirectory() + "/" + database;
	//			//			try {
	//			// InputStream dbStream =
	//			// _context.getResources().openRawResource(R.raw.eve);
	//			if (AppConnector.sdcardAvailable()) {
	//				ccpDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	//			} else
	//				return true;
	//			return false;
	//			//			} catch (final SQLException sqle) {
	//			//				AndroidDatabaseConnector.logger.severe("E> Failed to open database: " + database);
	//			//				AndroidDatabaseConnector.logger.severe("E> " + sqle.getMessage());
	//			//			}
	//		}
	//		return true;
	//	}

	/**
	 * Returns the list of distinct identifiers for parentAssetId that should represent the container where an
	 * asset is located. That list can represent known Locations, Assets and unknown locations that should
	 * represent other Corporation assets like the Customs or not listed Space Structures.
	 */
	public List<NeoComAsset> queryAllAssetContainers (final long identifier) {
		// Get access to one assets with a distinct location. Discard the rest of the data and only process the Location id
		List<NeoComAsset> uniqueContainers = new Vector<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder().distinct()
			                                                         .selectColumns("parentAssetID");
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", identifier);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			uniqueContainers = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W [SpringDatabaseConnector.queryAllLogins]> Excpetion reading all Logins" + sqle.getMessage());
		}
		return uniqueContainers;
	}

	/**
	 * Return the list of all the distinct Locations where a Character has assets. There is one location with a
	 * minus value id that is the <code>undefined</code> that contains all the items inside another elements
	 * (before assets processing) and the items on stations or space locations that do not belong to this
	 * character.<br>
	 * Once the assets are processed inside a location this list can change because it may contain other
	 * elements that are Ships or Containers. When processing the list we should connect them properly on the
	 * new hierarchy.<br>
	 *
	 * @return
	 */
	public List<NeoComAsset> queryAllAssetLocations (final long identifier) {
		// Get access to one assets with a distinct location. Discard the rest of the data and only process the Location id
		List<NeoComAsset> uniqueLocations = new Vector<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder().distinct().selectColumns("locationID");
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", identifier);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			uniqueLocations = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W [SpringDatabaseConnector.queryAllLogins]> Excpetion reading all Logins" + sqle.getMessage());
		}
		return uniqueLocations;
	}

	//	/**
	//	 * Reads all the keys stored at the database and classified them into a set of Login names.
	//	 */
	//	public Hashtable<String, Login> queryAllLogins() {
	//		// Get access to all ApiKey registers
	//		List<ApiKey> keyList = new Vector<ApiKey>();
	//		try {
	//			Dao<ApiKey, String> keysDao = this.getApiKeysDao();
	//			QueryBuilder<ApiKey, String> queryBuilder = keysDao.queryBuilder();
	//			PreparedQuery<ApiKey> preparedQuery = queryBuilder.prepare();
	//			keyList = keysDao.query(preparedQuery);
	//		} catch (java.sql.SQLException sqle) {
	//			sqle.printStackTrace();
	//			AndroidDatabaseConnector.logger
	//					.warning("W [SpringDatabaseConnector.queryAllLogins]> Exception reading all Logins. " + sqle.getMessage());
	//		}
	//		// Classify the keys on they matching Logins.
	//		Hashtable<String, Login> loginList = new Hashtable<String, Login>();
	//		for (ApiKey apiKey : keyList) {
	//			String name = apiKey.getLogin();
	//			// Search for this on the list before creating a new Login.
	//			Login hit = loginList.get(name);
	//			if (null == hit) {
	//				Login login = new Login(name).addKey(apiKey);
	//				loginList.put(name, login);
	//			} else {
	//				hit.addKey(apiKey);
	//			}
	//		}
	//		return loginList;
	//	}

	//	public int queryBlueprintDependencies(final int bpitemID) {
	//		try {
	//			if (null == ccpDatabase) {
	//				ccpDatabase = this.getCCPDatabase();
	//			}
	//			final Cursor cursor = ccpDatabase.rawQuery(
	//					"SELECT parentBlueprintTypeID FROM invBlueprintTypes BT WHERE blueprintTypeID = ?",
	//					new String[] { Integer.valueOf(bpitemID).toString() });
	//			if (null != cursor) {
	//				int parentBlueprintTypeID = -1;
	//				while (cursor.moveToNext()) {
	//					parentBlueprintTypeID = cursor.getInt(cursor.getColumnIndex("parentBlueprintTypeID"));
	//				}
	//				cursor.close();
	//				return parentBlueprintTypeID;
	//			}
	//		} catch (final Exception ex) {
	//			AndroidDatabaseConnector.logger.severe("E> Error getting T2 BPO for BP <" + bpitemID + ">.");
	//		}
	//		return -1;
	//	}

	public List<NeoComAsset> queryContainerContents (final long identifier) {
		// Get access to one assets with a distinct location. Discard the rest of the data and only process the Location id
		List<NeoComAsset> contents = new Vector<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			//			where.eq("ownerID", identifier);
			where.eq("parentAssetID", identifier);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			contents = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W [SpringDatabaseConnector.queryContainerContents]> Exception reading Location contents"
							+ sqle.getMessage());
		}
		return contents;
	}

	@Override
	public List<NeoComAsset> searchAssetsAtLocation (final long id) {
		return null;
	}

	public List<NeoComAsset> queryLocationContents (final long identifier) {
		// Get access to one assets with a distinct location. Discard the rest of the data and only process the Location id
		List<NeoComAsset> contents = new Vector<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			//			where.eq("ownerID", identifier);
			where.eq("locationID", identifier);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			contents = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			AndroidDatabaseConnector.logger.warning(
					"W [SpringDatabaseConnector.queryLocationContents]> Exception reading Location contents" + sqle.getMessage());
		}
		return contents;
	}

	public List<NeoComAsset> queryLocationPlanetaryContents (final long identifier) {
		// Get access to one assets with a distinct location. Discard the rest of the data and only process the Location id
		List<NeoComAsset> contents = new Vector<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("category", "Planetary Commodities");
			where.or().eq("category", "Planetary Resources");
			where.and().eq("locationID", identifier);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			contents = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			AndroidDatabaseConnector.logger.warning(
					"W [SpringDatabaseConnector.queryLocationContents]> Exception reading Location contents" + sqle.getMessage());
		}
		return contents;
	}

	/**
	 * Changes the owner id for all records from a new download with the id of the current character. This
	 * completes the download and the assignment of the resources to the character without interrupting the
	 * processing of data by the application.
	 */
	public synchronized void replaceAssets (final long characterID) {
		SQLiteDatabase database = null;
		try {
			database = this.getAppDatabase();
			synchronized (database) {
				database.beginTransaction();
				Log.i("", "-- replaceAssets rows deleted ASSETS [OWNERID = " + characterID + "] - "
						+ database.delete("Assets", "ownerID" + "=" + characterID, null));
				ContentValues values = new ContentValues();
				values.put("ownerID", characterID);
				database.update("Assets", values, "ownerID=" + (characterID * -1), null);
				database.setTransactionSuccessful();
			}
		} catch (final SQLiteException ex) {
			ex.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W> Problem replacing assets for " + characterID + ". " + ex.getMessage());
		} finally {
			if ( null != database ) {
				database.endTransaction();
			}
		}
	}

	public synchronized void replaceBlueprints (final long characterID) {
		SQLiteDatabase database = null;
		try {
			database = this.getAppDatabase();
			synchronized (database) {
				database.beginTransaction();
				Log.i("", "-- replaceBlueprints rows deleted BLUEPRINTS [OWNERID = " + characterID + "] - "
						+ database.delete("Blueprints", "ownerID" + "=" + characterID, null));
				ContentValues values = new ContentValues();
				values.put("ownerID", characterID);
				database.update("Blueprints", values, "ownerID" + "=-1", null);
				database.setTransactionSuccessful();
			}
		} catch (final SQLiteException ex) {
			ex.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W> Problem replacing Blueprints for " + characterID + ". " + ex.getMessage());
		} finally {
			if ( null != database ) {
				database.endTransaction();
			}
		}
	}

	public synchronized void replaceJobs (final long characterID) {
		SQLiteDatabase database = null;
		try {
			database = this.getAppDatabase();
			synchronized (database) {
				database.beginTransaction();
				Log.i("", "-- replaceJobs rows deleted JOBS [OWNERID = " + characterID + "] - "
						+ database.delete("Jobs", "ownerID" + "=" + characterID + " AND jobType='CCP'", null));
				ContentValues values = new ContentValues();
				values.put("ownerID", characterID);
				database.update("Jobs", values, "ownerID=-1", null);
				database.setTransactionSuccessful();
			}
		} catch (final SQLiteException ex) {
			ex.printStackTrace();
			AndroidDatabaseConnector.logger
					.warning("W> Problem replacing assets for " + characterID + ". " + ex.getMessage());
		} finally {
			if ( null != database ) {
				database.endTransaction();
			}
		}
	}

	/**
	 * Get the list of all the Blueprints for a given Character. It can ge a Pilot or a Corporation and the
	 * differences should be none.
	 *
	 * @param characterID
	 * @return
	 */
	public ArrayList<NeoComAsset> searchAllBlueprintAssets (final long characterID) {
		// Select assets for each one of the Planetary categories.
		ArrayList<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		assetList.addAll(this.searchAsset4Category(characterID, "Blueprint"));
		return assetList;
	}

	/**
	 * Gets the list of assets of a select Category for the identified owner.
	 *
	 * @param characterID  - owner of the assets.
	 * @param categoryName - the category name to search for.
	 * @return
	 */
	public ArrayList<NeoComAsset> searchAsset4Category (final long characterID, final String categoryName) {
		// Select assets for the owner and with an specific type id.
		List<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		try {
			ModelAppConnector.getSingleton().startChrono();
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", characterID);
			where.and();
			where.eq("category", categoryName);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			assetList = assetDao.query(preparedQuery);
			AndroidDatabaseConnector.logger
					.info("~~ [AndroidDatabaseConnector.searchAsset4Category]> Time lapse for [SELECT CATEGORY=" + categoryName
							+ " OWNERID = " + characterID + "] - " + ModelAppConnector.getSingleton().timeLapse());
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
		}
		return (ArrayList<NeoComAsset>) assetList;
	}

	public ArrayList<NeoComAsset> searchAsset4Type (final long characterID, final int typeID) {
		// Select assets for the owner and with an specific type id.
		List<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", characterID);
			where.and();
			where.eq("typeID", typeID);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			assetList = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
		}
		return (ArrayList<NeoComAsset>) assetList;
	}

	public NeoComAsset searchAssetByID (final long assetID) {
		// search for the asset on the cache. Usually searching for containers.
		NeoComAsset hit = containerCache.get(assetID);
		if ( null == hit ) {
			// Select assets for the owner and with an specific type id.
			List<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
			try {
				Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
				QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
				Where<NeoComAsset, String> where = queryBuilder.where();
				where.eq("assetID", assetID);
				PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
				assetList = assetDao.query(preparedQuery);
				if ( assetList.size() > 0 ) {
					hit = assetList.get(0);
					containerCache.put(hit.getAssetID(), hit);
				}
			} catch (java.sql.SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return hit;
	}

	public ArrayList<NeoComAsset> searchAssetContainedAt (final long characterID, final long containerId) {
		// Select assets for the owner and with an specific type id.
		List<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", characterID);
			where.and();
			where.eq("parentAssetID", containerId);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			assetList = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
		}
		return (ArrayList<NeoComAsset>) assetList;
	}

	//	/**
	//	 * Returns the blueprint id that matched this module from the <code>invBlueprintTypes</code> table.
	//	 */
	//	public int searchBlueprint4Module(final int moduleID) {
	//		try {
	//			if (null == ccpDatabase) {
	//				ccpDatabase = this.getCCPDatabase();
	//			}
	//			final Cursor cursor = ccpDatabase.rawQuery(
	//					"SELECT typeID FROM industryActivityProducts BT WHERE productTypeID = ? AND activityID = 1",
	//					new String[] { Integer.valueOf(moduleID).toString() });
	//			if (null != cursor) {
	//				int productTypeID = -1;
	//				while (cursor.moveToNext()) {
	//					productTypeID = cursor.getInt(cursor.getColumnIndex("typeID"));
	//				}
	//				cursor.close();
	//				return productTypeID;
	//			}
	//		} catch (final Exception ex) {
	//			AndroidDatabaseConnector.logger.severe("E> Error locating BPO for module <" + moduleID + ">.");
	//		}
	//		return -1;
	//	}

	//	/**
	//	 * Like the other initialization methods this return a true if there was any problem during the process.
	//	 *
	//	 * @return
	//	 */
	//	@Override
	//	public boolean openDAO() {
	//		if (null == appDatabaseHelper) {
	//			appDatabaseHelper = OpenHelperManager.getHelper(_context, EveDroidDBHelper.class);
	//			return true;
	//		}
	//		return false;
	//	}

	/**
	 * Gets the list of type ids for the required resources to get an output batch of the former planetary
	 * resource.
	 *
	 * @param typeID
	 * @return
	 */
	public Vector<Integer> searchInputResources (final int typeID) {
		Vector<Integer> result = new Vector<Integer>();
		//		PreparedStatement prepStmt = null;
		//		ResultSet cursor = null;
		//		try {
		//			prepStmt = this.getCCPDatabase().prepareStatement(SELECT_TIER2_INPUTS);
		//			prepStmt.setString(1, Integer.valueOf(typeID).toString());
		//			cursor = prepStmt.executeQuery();
		//			while (cursor.next()) {
		//				result.add(cursor.getInt(1));
		//			}
		//		} catch (Exception ex) {
		//			AndroidDatabaseConnector.logger
		//					.warning("W- [SpingDatabaseConnector.searchRawPlanetaryOutput]> Database exception: " + ex.getMessage());
		//		} finally {
		//			try {
		//				if (cursor != null) {
		//					cursor.close();
		//				}
		//			} catch (SQLException ex) {
		//				ex.printStackTrace();
		//			}
		//			try {
		//				if (prepStmt != null) {
		//					prepStmt.close();
		//				}
		//			} catch (SQLException ex) {
		//				ex.printStackTrace();
		//			}
		//		}
		return result;
	}

	public ArrayList<Integer> searchInventionableBlueprints (final String idList) {
		Log.i("DBQUERY", ">> AndroidDatabaseConnector.searchInventionableBlueprints");
		ArrayList<Integer> blueprintIds = new ArrayList<Integer>();
		NeoComAppConnector.getSingleton().startChrono();
		try {
			// if (null == ccpDatabase) ccpDatabase = getCCPDatabase();
			final Cursor cursor = this.getCCPDatabase().rawQuery(
					"SELECT typeID FROM industryActivityProducts WHERE productTypeID in ( " + idList + " ) AND activityID = 8",
					null);
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					blueprintIds.add(new Integer(cursor.getInt(cursor.getColumnIndex("typeID"))));
				}
				// We have collected the required blueprint. Add it to the list
				// of resources.
				cursor.close();
				Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT PRODUCTTYPEID IN " + idList + "] - "
						+ NeoComAppConnector.getSingleton().timeLapse());
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for  blueprints.");
		}
		Log.i("DBQUERY", "<< AndroidDatabaseConnector.searchInventionableBlueprints " + blueprintIds.size());
		return blueprintIds;
	}

	public int searchInventionProduct (final int typeID) {
		int product = -1;
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidDatabaseConnector.INVENTION_PRODUCT,
					new String[]{Integer.valueOf(typeID).toString()});
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					product = cursor.getInt(0);
				}
				cursor.close();
			}
		} catch (final Exception ex) {
			Log.w("EVEI", "W> AndroidDatabaseConnector.checkInvention -- Exception:" + ex.getMessage());
		}
		return product;
	}

	//	public ArrayList<Job> searchJob4Class(final long characterID, final String classname) {
	//		// Select assets for the owner and woth an specific type id.
	//		List<Job> joblist = new ArrayList<Job>();
	//		try {
	//			Dao<Job, String> jobDao = NeoComAppConnector.getSingleton().getDBConnector().getJobDAO();
	//			QueryBuilder<Job, String> queryBuilder = jobDao.queryBuilder();
	//			Where<Job, String> where = queryBuilder.where();
	//			where.eq("ownerID", characterID);
	//			where.and();
	//			where.eq("jobType", classname);
	//			PreparedQuery<Job> preparedQuery = queryBuilder.prepare();
	//			joblist = jobDao.query(preparedQuery);
	//		} catch (java.sql.SQLException sqle) {
	//			sqle.printStackTrace();
	//		}
	//		return (ArrayList<Job>) joblist;
	//
	//	}

	//	/**
	//	 * Search on the eve.db database for the attributes that describe an Item. Items are the lowest data
	//	 * structure for EVE resources or modules. Everything on Eve is an Item. We detect blueprints that require a
	//	 * different treatment and also we check for the availability of the item at the current cache if
	//	 * implemented.
	//	 */
	//	public EveItem searchItembyID(final int typeID) {
	//		// Search the item on the cache.
	//		EveItem hit = itemCache.get(typeID);
	//		if (null == hit) {
	//			try {
	//				hit = new EveItem();
	//				final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidDatabaseConnector.SELECT_ITEM_BYID,
	//						new String[] { Integer.valueOf(typeID).toString() });
	//				if (null != cursor) {
	//					if (cursor.moveToFirst()) {
	//						hit.setTypeID(cursor.getInt(0));
	//						hit.setName(cursor.getString(1));
	//						hit.setGroupname(cursor.getString(2));
	//						hit.setCategory(cursor.getString(3));
	//						hit.setBasePrice(cursor.getDouble(4));
	//						hit.setVolume(cursor.getDouble(5));
	//						// Process the Tech field. The query marks blueprints
	//						String tech = cursor.getString(6);
	//						if (tech.equalsIgnoreCase("NOTECH")) {
	//							// Double check it is a Blueprint
	//							hit.setTech(ModelWideConstants.eveglobal.TechI);
	//							if (hit.getName().contains(" II Blueprint")) {
	//								hit.setBlueprint(true);
	//								if (hit.getName().contains(" II Blueprint")) {
	//									hit.setTech(ModelWideConstants.eveglobal.TechII);
	//								}
	//								if (hit.getName().contains(" III Blueprint")) {
	//									hit.setTech(ModelWideConstants.eveglobal.TechIII);
	//								}
	//							}
	//						} else {
	//							hit.setTech(tech);
	//						}
	//					}
	//					cursor.close();
	//					itemCache.put(new Integer(typeID), hit);
	//				}
	//			} catch (final Exception ex) {
	//				Log.w("EVEI", "W> AndroidDatabaseConnector.searchItembyID -- Item <" + typeID + "> not found.");
	//				return new EveItem();
	//			}
	//		}
	//		return hit;
	//	}

	public int searchJobExecutionTime (final int typeID, final int activityID) {
		Log.i("EVEI", ">> AndroidDatabaseConnector.searchJobExecutionTime");
		int jobTime = ModelWideConstants.HOURS2 / 1000;
		try {
			if ( null == ccpDatabase ) {
				ccpDatabase = this.getCCPDatabase();
			}
			final Cursor cursor = ccpDatabase.rawQuery(AndroidDatabaseConnector.JOB_COMPLETION_TIME,
					new String[]{Integer.valueOf(typeID).toString(), Integer.valueOf(activityID).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					jobTime = cursor.getInt(cursor.getColumnIndex("time"));
				}
				// Log.i("EVEI", "<<
				// AndroidDatabaseConnector.searchJobExecutionTime");
				// return jobTime;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for job time <" + typeID + "> not found.");
		}
		Log.i("EVEI", "<< AndroidDatabaseConnector.searchJobExecutionTime");
		return jobTime;
	}

	public ArrayList<Resource> searchListOfDatacores (final int itemID) {
		// Log.i("AndroidDatabaseConnector", ">>
		// AndroidDatabaseConnector.searchListOfDatacores");
		ArrayList<Resource> inventionJob = new ArrayList<Resource>();
		NeoComAppConnector.getSingleton().startChrono();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidDatabaseConnector.INDUSTRYACTIVITYMATERIALS,
					new String[]{Integer.valueOf(itemID).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					// The the data of the resource. Check for blueprints.
					int resourceID = cursor.getInt(cursor.getColumnIndex("materialTypeID"));
					int qty = cursor.getInt(cursor.getColumnIndex("quantity"));
					// EveItem newItem = searchItembyID(resourceID);
					// Resource resource = ;
					inventionJob.add(new Resource(resourceID, qty));
				}
				cursor.close();
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for material <" + itemID + "> not found.");
		}
		// Log.i("AndroidDatabaseConnector", "<<
		// AndroidDatabaseConnector.searchListOfDatacores " +
		// inventionJob.getSize());
		Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT INVENTION LOM " + itemID + "] - [" + inventionJob.size() + "] "
				+ NeoComAppConnector.getSingleton().timeLapse());
		return inventionJob;
	}

	public ArrayList<Resource> searchListOfMaterials (final int itemID) {
		// Log.i("AndroidDatabaseConnector", ">>
		// AndroidDatabaseConnector.searchListOfMaterials");
		ArrayList<Resource> buildJob = new ArrayList<Resource>();
		NeoComAppConnector.getSingleton().startChrono();
		try {
			if ( null == ccpDatabase ) {
				ccpDatabase = this.getCCPDatabase();
			}
			Cursor cursor = ccpDatabase.rawQuery(AndroidDatabaseConnector.SELECT_MATERIAL_USAGE,
					new String[]{Integer.valueOf(itemID).toString()});
			int blueprintId = -1;
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					// The the data of the resource. Check for blueprints.
					int materialTypeID = cursor.getInt(cursor.getColumnIndex("materialTypeID"));
					int qty = cursor.getInt(cursor.getColumnIndex("quantity"));
					blueprintId = cursor.getInt(cursor.getColumnIndex("typeID"));
					// int consume =
					// cursor.getInt(cursor.getColumnIndex("consume"));
					Resource resource = new Resource(materialTypeID, qty);
					buildJob.add(resource);
				}
				cursor.close();
				// We have collected the required blueprint. Add it to the list
				// of resources.
				if ( blueprintId != -1 ) {
					buildJob.add(new Resource(blueprintId, 1));
				}

				// Add the skills to the list of resources
				cursor = ccpDatabase.rawQuery(SEARCH_LISTOFMATERIALS,
						new String[]{Integer.valueOf(itemID).toString()});
				if ( null == cursor ) throw new Exception("E> Invalid cursor or empty.");
				while (cursor.moveToNext()) {
					// The the data of the resource. Check for blueprints.
					int skillID = cursor.getInt(cursor.getColumnIndex("skillID"));
					int level = cursor.getInt(cursor.getColumnIndex("level"));
					Resource resource = new Resource(skillID, level);
					buildJob.add(resource);
				}
				cursor.close();

				// Log.i("AndroidDatabaseConnector", "<<
				// AndroidDatabaseConnector.searchListOfMaterials " +
				// buildJob.size());
				// return buildJob;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for material <" + itemID + "> not found.");
		}
		Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT LOM " + itemID + "] - [" + buildJob.size() + "] "
				+ NeoComAppConnector.getSingleton().timeLapse());
		// Log.i("AndroidDatabaseConnector", "<<
		// AndroidDatabaseConnector.searchListOfMaterials " + buildJob.size());
		return buildJob;
	}

	public ArrayList<Resource> searchListOfReaction (final int itemID) {
		ArrayList<Resource> buildJob = new ArrayList<Resource>();
		NeoComAppConnector.getSingleton().startChrono();
		try {
			if ( null == ccpDatabase ) {
				ccpDatabase = this.getCCPDatabase();
			}
			Cursor cursor = ccpDatabase.rawQuery(AndroidDatabaseConnector.REACTION_COMPONENTS,
					new String[]{Integer.valueOf(itemID).toString()});
			// int blueprintId = -1;
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					// The the data of the resource. Check for blueprints.
					int materialTypeID = cursor.getInt(cursor.getColumnIndex("typeID"));
					int input = cursor.getInt(cursor.getColumnIndex("input"));
					int qty = cursor.getInt(cursor.getColumnIndex("quantity"));
					// blueprintId =
					// cursor.getInt(cursor.getColumnIndex("typeID"));
					// int consume =
					// cursor.getInt(cursor.getColumnIndex("consume"));
					if ( input == 1 ) {
						Resource resource = new Resource(materialTypeID, qty);
						buildJob.add(resource);
					}
				}
				cursor.close();
				// // We have collected the required blueprint. Add it to the
				// list of resources.
				// if (blueprintId != -1) buildJob.add(new Resource(blueprintId,
				// 1));
				//
				// // Add the skills to the list of resources
				// cursor = ccpDatabase
				// .rawQuery(
				// "SELECT ais.typeID, ais.skillID, ais.level, it.typeName FROM
				// industryActivitySkills ais, invTypes it WHERE ais.typeID = ?
				// AND ais.activityID = 1 AND it.typeID=ais.skillID",
				// new String[] { Integer.valueOf(itemID).toString() });
				// if (null == cursor) throw new Exception("E> Invalid cursor or
				// empty.");
				// while (cursor.moveToNext()) {
				// // The the data of the resource. Check for blueprints.
				// int skillID =
				// cursor.getInt(cursor.getColumnIndex("skillID"));
				// int level = cursor.getInt(cursor.getColumnIndex("level"));
				// Resource resource = new Resource(skillID, level);
				// buildJob.add(resource);
				// }
				// cursor.close();

				// Log.i("AndroidDatabaseConnector", "<<
				// AndroidDatabaseConnector.searchListOfMaterials " +
				// buildJob.size());
				// return buildJob;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for material <" + itemID + "> not found.");
		}
		Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT LOM " + itemID + "] - [" + buildJob.size() + "] "
				+ NeoComAppConnector.getSingleton().timeLapse());
		// Log.i("AndroidDatabaseConnector", "<<
		// AndroidDatabaseConnector.searchListOfMaterials " + buildJob.size());
		return buildJob;
	}

	public int searchReactionOutputMultiplier (final int itemID) {
		int multiplier = 200;
		NeoComAppConnector.getSingleton().startChrono();
		try {
			if ( null == ccpDatabase ) {
				ccpDatabase = this.getCCPDatabase();
			}
			Cursor cursor = ccpDatabase.rawQuery(AndroidDatabaseConnector.REACTION_COMPONENTS,
					new String[]{Integer.valueOf(itemID).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					// Search for the itemid at the reaction and then return the
					// multiplier.
					int materialTypeID = cursor.getInt(cursor.getColumnIndex("typeID"));
					if ( materialTypeID == itemID ) {
						multiplier = cursor.getInt(cursor.getColumnIndex("quantity"));
						// int input =
						// cursor.getInt(cursor.getColumnIndex("input"));
						// int qty =
						// cursor.getInt(cursor.getColumnIndex("quantity"));
						// // blueprintId =
						// cursor.getInt(cursor.getColumnIndex("typeID"));
						// // int consume =
						// cursor.getInt(cursor.getColumnIndex("consume"));
						// if (input == 1) {
						// Resource resource = new Resource(materialTypeID,
						// qty);
						// buildJob.add(resource);
						// }
					}
				}
				cursor.close();
				// // We have collected the required blueprint. Add it to the
				// list of resources.
				// if (blueprintId != -1) buildJob.add(new Resource(blueprintId,
				// 1));
				//
				// // Add the skills to the list of resources
				// cursor = ccpDatabase
				// .rawQuery(
				// "SELECT ais.typeID, ais.skillID, ais.level, it.typeName FROM
				// industryActivitySkills ais, invTypes it WHERE ais.typeID = ?
				// AND ais.activityID = 1 AND it.typeID=ais.skillID",
				// new String[] { Integer.valueOf(itemID).toString() });
				// if (null == cursor) throw new Exception("E> Invalid cursor or
				// empty.");
				// while (cursor.moveToNext()) {
				// // The the data of the resource. Check for blueprints.
				// int skillID =
				// cursor.getInt(cursor.getColumnIndex("skillID"));
				// int level = cursor.getInt(cursor.getColumnIndex("level"));
				// Resource resource = new Resource(skillID, level);
				// buildJob.add(resource);
				// }
				// cursor.close();

				// Log.i("AndroidDatabaseConnector", "<<
				// AndroidDatabaseConnector.searchListOfMaterials " +
				// buildJob.size());
				// return buildJob;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for material <" + itemID + "> not found.");
		}
		Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT LOM " + itemID + "] - [" + multiplier + "] "
				+ NeoComAppConnector.getSingleton().timeLapse());
		// Log.i("AndroidDatabaseConnector", "<<
		// AndroidDatabaseConnector.searchListOfMaterials " + buildJob.size());
		return multiplier;
	}

	public Vector<Schematics> searchSchematics4Output (final int targetId) {
		Vector<Schematics> scheList = new Vector<Schematics>();
		PreparedStatement prepStmt = null;
		Cursor cursor = null;
		try {
			cursor = this.getCCPDatabase().rawQuery(AndroidDatabaseConnector.SELECT_SCHEMATICS_INFO,
					new String[]{Integer.valueOf(targetId).toString()});
			while (cursor.moveToNext()) {
				boolean flag = true;
				if ( cursor.getInt(3) == 0 ) {
					flag = false;
				} else {
					flag = true;
				}
				scheList.add(new Schematics().addData(cursor.getInt(1), cursor.getInt(2), flag));
				cursor.close();
			}
		} catch (Exception ex) {
			AndroidDatabaseConnector.logger
					.warning("W- [SpingDatabaseConnector.searchRawPlanetaryOutput]> Database exception: " + ex.getMessage());
			//		} finally {
			//			try {
			//				if (cursor != null) {
			//					cursor.close();
			//				}
			////			} catch (SQLException ex) {
			////				ex.printStackTrace();
			////			}
			//			try {
			//				if (prepStmt != null) {
			//					prepStmt.close();
			//				}
			////			} catch (SQLException ex) {
			////				ex.printStackTrace();
			////			}
		}
		return scheList;
	}

	//	/**
	//	 * Returns the resource identifier of the station class to locate icons or other type related resources.
	//	 *
	//	 * @param stationID
	//	 * @return
	//	 */
	//	public int searchStationType(final long stationID) {
	//		ArrayList<Resource> inventionJob = new ArrayList<Resource>();
	//		int stationTypeID = 1529;
	//		AppConnector.startChrono();
	//		try {
	//			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidDatabaseConnector.STATIONTYPE,
	//					new String[] { Long.valueOf(stationID).toString() });
	//			if (null != cursor) {
	//				while (cursor.moveToNext()) {
	//					stationTypeID = cursor.getInt(cursor.getColumnIndex("stationTypeID"));
	//				}
	//				cursor.close();
	//			}
	//		} catch (final Exception ex) {
	//			AndroidDatabaseConnector.logger.severe("E> Error searching for station type <" + stationID + "> not found.");
	//		}
	//		// Log.i("AndroidDatabaseConnector", "<<
	//		// AndroidDatabaseConnector.searchListOfDatacores " +
	//		// inventionJob.getSize());
	//		Log.i("DBQUERY.TIME", "~~ Time lapse for [SELECT STATIONTYPEID " + stationID + "] " + NeoComAppConnector.getSingleton().timeLapse());
	//		return stationTypeID;
	//	}

	//	public int totalLocationContentCount(final long identifier) {
	//		try {
	//			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
	//			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
	//			queryBuilder.setCountOf(true).where().eq("locationID", identifier);
	//			long totalAssets = assetDao.countOf(queryBuilder.prepare());
	//			return Long.valueOf(totalAssets).intValue();
	//		} catch (java.sql.SQLException sqle) {
	//			sqle.printStackTrace();
	//			AndroidDatabaseConnector.logger
	//					.warning("W [SpringDatabaseConnector.getLocationContentCount]> Exception reading Location contents count."
	//							+ sqle.getMessage());
	//			return 0;
	//		}
	//	}

	protected SQLiteDatabase getCCPDatabase () {
		if ( null == ccpDatabase ) {
			this.openCCPDataBase();
		}
		return ccpDatabase;
	}

	private boolean checkRecordExistence (final int typeID, final String query) {
		int count = 0;
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(query, new String[]{Integer.valueOf(typeID).toString()});
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					count = cursor.getInt(0);
				}
				cursor.close();
			}
		} catch (final Exception ex) {
			Log.w("EVEI", "W> AndroidDatabaseConnector.checkRecordExistence -- Exception:" + ex.getMessage());
			return false;
		}
		if ( count > 0 )
			return true;
		else
			return false;
	}

	private SQLiteDatabase getAppDatabase () {
		if ( null == staticDatabase ) {
			this.openAppDataBase();
		}
		return staticDatabase;
	}

	private NeocomAndroidDBHelper getNeocomDBHelper () {
		if ( null == neocomDBHelper ) {
			AndroidDatabaseConnector.logger.warning(
					"W> [NeocomDatabaseConnector.getNeocomDBHelper]> helper not found. Creating a new one with default database.");
			neocomDBHelper = OpenHelperManager.getHelper(_context, NeocomAndroidDBHelper.class);
		}
		return neocomDBHelper;
	}

	//	private NeocomAndroidDBHelper getNeocomDBHelper() {
	//		if (null == neocomDBHelper) {
	//			AndroidDatabaseConnector.logger.warning(
	//					"W> [NeocomDatabaseConnector.getNeocomDBHelper]> helper not found. Creating a new one with default database.");
	//			neocomDBHelper = OpenHelperManager.getHelper(_context, NeocomAndroidDBHelper.class);
	//		}
	//		return neocomDBHelper;
	//	}

	private String readJsonData () {
		StringBuffer data = new StringBuffer();
		try {
			String str = "";
			InputStream is = NeoComAppConnector.getSingleton().getStorageConnector().accessInternalStorage("outposts.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			if ( is != null ) {
				while ((str = reader.readLine()) != null) {
					data.append(str);
				}
			}
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toString();
	}

	//	private EveLocation searchOutpostbyID(final long locationID) {
	//		// Check if the outpotst already loaded.
	//		if ((null == outpostsCache) || (outpostsCache.size() < 1)) {
	//			// Making a request to url and getting response
	//			String jsonStr = this.readJsonData();
	//			try {
	//				JSONObject jsonObj = new JSONObject(jsonStr);
	//				// Getting JSON Array node
	//				JSONArray outposts = jsonObj.getJSONArray("items");
	//				// Looping through all outposts
	//				int counter = 1;
	//				for (int i = 0; i < outposts.length(); i++) {
	//					Outpost o = new Outpost();
	//					JSONObject post = outposts.getJSONObject(i);
	//					int id = post.getInt("facilityID");
	//					o.setFacilityID(id);
	//					JSONObject intermediate = post.getJSONObject("solarSystem");
	//					o.setSolarSystem(intermediate.getLong("id"));
	//					o.setName(post.getString("name"));
	//					intermediate = post.getJSONObject("region");
	//					o.setRegion(intermediate.getLong("id"));
	//					intermediate = post.getJSONObject("owner");
	//					o.setOwner(intermediate.getLong("id"));
	//					intermediate = post.getJSONObject("type");
	//					o.setType(intermediate.getLong("id"));
	//
	//					// Create the part with the Outpost
	//					outpostsCache.put(id, o);
	//					Log.i("DataSource", ".. Part counter " + counter++);
	//				}
	//			} catch (JSONException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//		// Search for the item
	//		Outpost hit = outpostsCache.get(Long.valueOf(locationID).intValue());
	//		EveLocation location = new EveLocation(locationID);
	//		if (null != hit) {
	//			EveLocation systemLocation = NeoComAppConnector.getSingleton().getCCPDBConnector()
	//					.searchLocationbyID(hit.getSolarSystem());
	//			location.setStation(hit.getName());
	//			location.setSystemID(hit.getSolarSystem());
	//			location.setSystem(systemLocation.getSystem());
	//			location.setConstellationID(systemLocation.getConstellationID());
	//			location.setConstellation(systemLocation.getConstellation());
	//			location.setRegionID(systemLocation.getRegionID());
	//			location.setRegion(systemLocation.getRegion());
	//			location.setSecurity(systemLocation.getSecurity());
	//		}
	//		return location;
	//	}
}

// - UNUSED CODE ............................................................................................
