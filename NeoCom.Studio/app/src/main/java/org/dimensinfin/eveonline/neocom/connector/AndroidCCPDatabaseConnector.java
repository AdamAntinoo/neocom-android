//	PROJECT:      Neocom.MarketDataService (NEOC-MKDS)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	This project contains a MicroService specially dedicated to get and store the Market Data
//								information. I should be exposed on a new port and should be accesible to all the backend MS to consult
//								Item Market Data.
package org.dimensinfin.eveonline.neocom.connector;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.ModelWideConstants;
import org.dimensinfin.eveonline.neocom.core.AccessStatistics;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.ItemCategory;
import org.dimensinfin.eveonline.neocom.model.ItemGroup;
import org.dimensinfin.eveonline.neocom.planetary.Schematics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidCCPDatabaseConnector implements ICCPDatabaseConnector {
	final private class RawStatement {
		private SQLiteDatabase _database = null;
		private Cursor _cursor = null;

		public RawStatement (SQLiteDatabase database, String query, String[] parameters) {
			logger.info(">> [RawStatement.<constructor>]");
			if ( null == database ) throw new NeocomRuntimeException("Invalid database reference received.");
			_database = database;
			_cursor = _database.rawQuery(query, parameters);
			if ( null == _cursor ) throw new NeocomRuntimeException("Invalid cursor when processing query: " + query);
		}

		public int getCount () {
			return _cursor.getCount();
		}

		public int getPosition () {
			return _cursor.getPosition();
		}

		public boolean moveToFirst () {
			return _cursor.moveToFirst();
		}

		public boolean moveToLast () {
			return _cursor.moveToLast();
		}

		public boolean moveToNext () {
			return _cursor.moveToNext();
		}

		public boolean isFirst () {
			return _cursor.isFirst();
		}

		public boolean isLast () {
			return _cursor.isLast();
		}

		public int getColumnIndex (final String s) {
			return _cursor.getColumnIndex(s);
		}

		public String getString (final int i) {
			return _cursor.getString(i);
		}

		public short getShort (final int i) {
			return _cursor.getShort(i);
		}

		public int getInt (final int i) {
			return _cursor.getInt(i);
		}

		public long getLong (final int i) {
			return _cursor.getLong(i);
		}

		public float getFloat (final int i) {
			return _cursor.getFloat(i);
		}

		public double getDouble (final int i) {
			return _cursor.getDouble(i);
		}

		public int getType (final int i) {
			return _cursor.getType(i);
		}

		public void close () {
			_cursor.close();
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory
			.getLogger(AndroidCCPDatabaseConnector.class);
	private static AccessStatistics locationsCacheStatistics = new AccessStatistics();

	// - F I E L D   I N D E X   D E F I N I T I O N S
	private static int STATIONTYPEID_COLINDEX = 1;

	// - F I E L D   I N D E X   D E F I N I T I O N S


	// - S Q L   C O M M A N D S
	// - I T E M B Y I D
	private static final String SELECT_ITEM_BYID = "SELECT it.typeID AS typeID"
			+ " , it.typeName AS typeName"
			+ " , ig.groupID AS groupID"
			+ " , ig.groupName AS groupName"
			+ " , ic.categoryID AS categoryID"
			+ " , ic.categoryName AS categoryName"
			+ " , it.basePrice AS basePrice"
			+ " , it.volume AS volume"
			+ " , IFNULL(img.metaGroupName, " + '"' + "NOTECH" + '"' + ") AS Tech"
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
	private static final String SELECT_LOCATIONBYSYSTEM = "SELECT solarSystemID FROM mapSolarSystems"
			+ " WHERE solarSystemName = ?";
	private static final String SELECT_STATIONTYPE = "SELECT stationTypeID FROM staStations WHERE stationID = ?";

	// - I T E M G R O U P 4 I D
	private static int ITEMGROUP_GROUPID_COLINDEX = 0;
	private static int ITEMGROUP_CATEGORYID_COLINDEX = 1;
	private static int ITEMGROUP_GROUPNAME_COLINDEX = 2;
	private static int ITEMGROUP_ICONLINKNAME_COLINDEX = 3;
	private static final String SELECT_ITEMGROUP = "SELECT ig.groupID AS groupID"
			+ " , ig.categoryID AS categoryID"
			+ " , ig.groupName AS groupName"
			+ " , ei.iconFile AS iconLinkName"
			+ " FROM invGroups ig"
			+ " LEFT OUTER JOIN eveIcons ei ON ig.iconID = ei.iconID"
			+ " WHERE ig.groupID = ?";

	// - I T E M C A T E G O R Y 4 I D
	private static int ITEMCATEGORY_CATEGORYID_COLINDEX = 0;
	private static int ITEMCATEGORY_CATEGORYNAME_COLINDEX = 1;
	private static int ITEMCATEGORY_ICONLINKNAME_COLINDEX = 2;
	private static final String SELECT_ITEMCATEGORY = "SELECT ic.categoryID AS categoryID"
			+ " , ic.categoryName AS categoryName"
			+ " , ei.iconFile AS iconLinkName"
			+ " FROM invCategories ic"
			+ " LEFT OUTER JOIN eveIcons ei ON ic.iconID = ei.iconID"
			+ " WHERE ic.categoryID = ?";

	// --- S C H E M A T I C S I D
	private static int SCHEMATICSID_SCHEMATICID_COLINDEX = 0;
	private static int SCHEMATICSID_TYPEID_COLINDEX = 1;
	private static int SCHEMATICSID_QUANTITY_COLINDEX = 2;
	private static int SCHEMATICSID_ISINPUT_COLINDEX = 3;
	private static final String SELECT_SCHEMATICSID =
			"SELECT        pstms.schematicID, pstms.typeID, pstms.quantity, pstms.isInput"
					+ " FROM   planetSchematicsTypeMap pstmt, planetSchematicsTypeMap pstms"
					+ " WHERE  pstmt.schematicID = ?"
					+ " AND    pstmt.isInput = 0"
					+ " AND    pstms.schematicID = pstmt.schematicID";


	// STATEMENTS TO CHECK
	private static final String SELECT_MODULE4BLUEPRINT = "SELECT productTypeID FROM industryActivityProducts BT"
			+ " WHERE typeID = ? AND activityID = 1";
	private static final String SELECT_TECH4BLUEPRINT = "SELECT iap.typeID, it.typeName, imt.metaGroupID, img.metaGroupName"
			+ " FROM industryActivityProducts iap, invTypes it, invMetaTypes imt, invMetaGroups img" + " WHERE it.typeID =?"
			+ " AND iap.typeID = it.typeID" + " AND imt.typeID = productTypeID" + " AND img.metaGroupID = imt.metaGroupID"
			+ " AND iap.activityID = 1";
	private static final String SELECT_REFININGASTEROID = "SELECT itm.materialTypeID AS materialTypeID, itm.quantity AS qty"
			+ " , it.typeName AS materialName" + " , ito.portionSize AS portionSize"
			+ " FROM invTypeMaterials itm, invTypes it, invTypes ito" + " WHERE itm.typeID = ?"
			+ " AND it.typeID = itm.materialTypeID" + " AND ito.typeID = itm.typeID" + " ORDER BY itm.materialTypeID";
	private static final String SELECT_BLUEPRINT4MODULE = "SELECT typeID FROM industryActivityProducts BT"
			+ " WHERE productTypeID = ? AND activityID = 1";
	private static final String SELECT_RAW_PRODUCTRESULT = "SELECT pstmo.typeID, pstmo.quantity, pstmo.schematicID"
			+ " FROM   planetSchematicsTypeMap pstmi, planetSchematicsTypeMap pstmo" + " WHERE  pstmi.typeID = ?"
			+ " AND    pstmo.schematicID = pstmi.schematicID" + " AND    pstmo.isInput = 0";
	private static final String SELECT_SCHEMATICS4OUTPUT = "SELECT pstms.typeID, pstms.quantity, pstms.isInput"
			+ " FROM   planetSchematicsTypeMap pstmt, planetSchematicsTypeMap pstms" + " WHERE  pstmt.typeID = ?"
			+ " AND    pstmt.isInput = 0" + " AND    pstms.schematicID = pstmt.schematicID";

	// - F I E L D - S E C T I O N ............................................................................
	private SQLiteDatabase ccpDatabase = null;
	private final Hashtable<Integer, EveItem> itemCache = new Hashtable<Integer, EveItem>();
	private final Hashtable<Long, EveLocation> locationsCache = new Hashtable<Long, EveLocation>(200);

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AndroidCCPDatabaseConnector () {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
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
		}
		return true;
	}

	// --- STATEMENTS VERIFIED

	/**
	 * Search on the eve.db database for the attributes that describe an Item. Items are the lowest data
	 * structure for EVE resources or modules. Everything on Eve is an Item. We detect blueprints that require a
	 * different treatment and also we check for the availability of the item at the current cache if
	 * implemented.
	 */
	public EveItem searchItembyID (final int typeID) {
		// Search the item on the cache.
		EveItem hit = itemCache.get(typeID);
		if ( null == hit ) {
			try {
				hit = new EveItem();
				final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_ITEM_BYID,
						new String[]{Integer.valueOf(typeID).toString()});
				if ( null != cursor ) {
					if ( cursor.moveToFirst() ) {
						hit.setTypeID(cursor.getInt(cursor.getColumnIndex("typeID")));
						hit.setName(cursor.getString(cursor.getColumnIndex("typeName")));
						hit.setGroupId(cursor.getInt(cursor.getColumnIndex("groupID")));
						hit.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryID")));
						//						hit.setGroupname(cursor.getString(2));
						//						hit.setCategory(cursor.getString(3));
						hit.setBasePrice(cursor.getDouble(cursor.getColumnIndex("basePrice")));
						hit.setVolume(cursor.getDouble(cursor.getColumnIndex("volume")));
						// Process the Tech field. The query marks blueprints
						String tech = cursor.getString(cursor.getColumnIndex("Tech"));
						if ( tech.equalsIgnoreCase("NOTECH") ) {
							// Double check it is a Blueprint
							hit.setTech(ModelWideConstants.eveglobal.TechI);
							if ( hit.getName().contains(" II Blueprint") ) {
								//								hit.setBlueprint(true);
								if ( hit.getName().contains(" II Blueprint") ) {
									hit.setTech(ModelWideConstants.eveglobal.TechII);
								}
								if ( hit.getName().contains(" III Blueprint") ) {
									hit.setTech(ModelWideConstants.eveglobal.TechIII);
								}
							}
						} else {
							hit.setTech(tech);
						}
					}
					cursor.close();
					itemCache.put(new Integer(typeID), hit);
				}
			} catch (final Exception ex) {
				AndroidCCPDatabaseConnector.logger
						.warn("W> [AndroidDatabaseConnector.searchItembyID]> Item <" + typeID + "> not found.");
				return new EveItem();
			}
		}
		return hit;
	}
	public Vector<Schematics> selectSchematics4Id (final int schematicId) {
		Vector<Schematics> scheList = new Vector<Schematics>();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_SCHEMATICSID,
					new String[]{Integer.valueOf(schematicId).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					scheList.add(new Schematics().addData(cursor.getInt(cursor.getColumnIndex("typeID")),
							cursor.getInt(cursor.getColumnIndex("quantity")),
							(cursor.getInt(cursor.getColumnIndex("isInput")) == 1) ? true : false));
				}
				cursor.close();
			}
		} catch (Exception ex) {
			AndroidCCPDatabaseConnector.logger
					.warn("W- [SpingDatabaseConnector.searchRawPlanetaryOutput]> Database exception: " + ex.getMessage());
		}
		return scheList;
	}

	//--------------------------------------------------------------------------------------------------------------
	/**
	 * Search on the eve.db database for the item group information. This new select is used to get access to
	 * the icon information that should be stored to be correlated to the resource list.
	 */
	public ItemGroup searchItemGroup4Id (final int targetGroupId) {
		logger.info(">> [AndroidCCPDatabaseConnector.searchItemGroup4Id]> targetGroupId: {}", targetGroupId);
		ItemGroup target = new ItemGroup();
		try {
			final RawStatement cursor = new RawStatement(getCCPDatabase(), SELECT_ITEMGROUP, new String[]{Integer.valueOf(targetGroupId).toString()});
			while (cursor.moveToNext()) {
				target.setGroupId(cursor.getInt(ITEMGROUP_GROUPID_COLINDEX));
				target.setCategoryId(cursor.getInt(ITEMGROUP_CATEGORYID_COLINDEX));
				target.setGroupName(cursor.getString(ITEMGROUP_GROUPNAME_COLINDEX));
				target.setIconLinkName(cursor.getString(ITEMGROUP_ICONLINKNAME_COLINDEX));
			}
			cursor.close();
			logger.info("<< [AndroidCCPDatabaseConnector.searchItemGroup4Id]");
			return target;
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E [AndroidCCPDatabaseConnector.searchItemGroup4Id]> Exception processing statement: {}" + ex.getMessage());
		}
		return target;
	}

	/**
	 * Search on the eve.db database for the item category information. This new select is used to get access to
	 * the icon information that should be stored to be correlated to the resource list.
	 */
	public ItemCategory searchItemCategory4Id (final int targetCategoryId) {
		logger.info(">> [AndroidCCPDatabaseConnector.searchItemCategory4Id]> targetGroupId: {}", targetCategoryId);
		ItemCategory target = new ItemCategory();
		try {
			final RawStatement cursor = new RawStatement(getCCPDatabase(), SELECT_ITEMCATEGORY, new String[]{Integer.valueOf(targetCategoryId).toString()});
			while (cursor.moveToNext()) {
				target.setCategoryId(cursor.getInt(ITEMCATEGORY_CATEGORYID_COLINDEX));
				target.setCategoryName(cursor.getString(ITEMCATEGORY_CATEGORYNAME_COLINDEX));
				target.setIconLinkName(cursor.getString(ITEMCATEGORY_ICONLINKNAME_COLINDEX));
			}
			cursor.close();
			logger.info("<< [AndroidCCPDatabaseConnector.searchItemCategory4Id]");
			return target;
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E [AndroidCCPDatabaseConnector.searchItemCategory4Id]> Exception processing statement: {}" + ex.getMessage());
		}
		return target;
	}

	public int queryBlueprintDependencies (final int bpitemID) {
		final String SQLSTATEMENT = "SELECT parentBlueprintTypeID FROM invBlueprintTypes BT WHERE blueprintTypeID = ?";
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(
					SQLSTATEMENT,
					new String[]{Integer.valueOf(bpitemID).toString()});
			if ( null != cursor ) {
				int parentBlueprintTypeID = -1;
				while (cursor.moveToNext()) {
					parentBlueprintTypeID = cursor.getInt(cursor.getColumnIndex("parentBlueprintTypeID"));
				}
				cursor.close();
				return parentBlueprintTypeID;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error getting T2 BPO for BP <" + bpitemID + ">.");
		}
		return -1;
	}

	public ArrayList<Resource> refineOre (final int oreID) {
		ArrayList<Resource> results = new ArrayList<Resource>();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_REFININGASTEROID,
					new String[]{Integer.valueOf(oreID).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					int resourceID = cursor.getInt(cursor.getColumnIndex("materialTypeID"));
					int qty = cursor.getInt(cursor.getColumnIndex("qty"));
					int portionSize = cursor.getInt(cursor.getColumnIndex("portionSize"));
					Resource resource = new Resource(resourceID, qty);
					resource.setStackSize(portionSize);
					results.add(resource);
				}
				cursor.close();
				return results;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error refining <" + oreID + "> not found.");
		}
		return results;
	}

	/**
	 * Returns the blueprint id that matched this module from the <code>invBlueprintTypes</code> table.
	 */
	public int searchBlueprint4Module (final int moduleID) {
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_BLUEPRINT4MODULE,
					new String[]{Integer.valueOf(moduleID).toString()});
			if ( null != cursor ) {
				int productTypeID = -1;
				while (cursor.moveToNext()) {
					productTypeID = cursor.getInt(cursor.getColumnIndex("typeID"));
				}
				cursor.close();
				return productTypeID;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error locating BPO for module <" + moduleID + ">.");
		}
		return -1;
	}


	/**
	 * Search on the CCP Database or on the Application database for a new location ID that is not already on
	 * the cache. Locations are extended objects on the NeoCom model because to the standard and game defined
	 * locations we should add an external resource with the compilation of user deployed structures (Citadels,
	 * Refineries, etc) that can also store assets and that are becoming the real place where to have the items.
	 * We also have to include another external source that are the Outposts than come fom CCP sources but that
	 * are being slowly phased out.<br>
	 * The process starts searching for locations depending on range, first at the CCP database and then at the
	 * Locations table at the application database.
	 */
	@Override
	public EveLocation searchLocationbyID (final long locationID) {
		// First check if the location is already on the cache table.
		EveLocation hit = locationsCache.get(locationID);
		if ( null != hit ) {
			int access = AndroidCCPDatabaseConnector.locationsCacheStatistics.accountAccess(true);
			int hits = AndroidCCPDatabaseConnector.locationsCacheStatistics.getHits();
			AndroidCCPDatabaseConnector.logger.info(">< [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
					+ access + "] Location " + locationID + ": found at cache.");
			return hit;
		} else {
			// Try to get that id from the cache tables
			int access = AndroidCCPDatabaseConnector.locationsCacheStatistics.accountAccess(false);
			int hits = AndroidCCPDatabaseConnector.locationsCacheStatistics.getHits();
			List<EveLocation> locationList = null;
			try {
				Dao<EveLocation, String> locationDao = NeoComAppConnector.getSingleton().getDBConnector().getLocationDao();
				QueryBuilder<EveLocation, String> queryBuilder = locationDao.queryBuilder();
				Where<EveLocation, String> where = queryBuilder.where();
				where.eq("id", locationID);
				PreparedQuery<EveLocation> preparedQuery = queryBuilder.prepare();
				locationList = locationDao.query(preparedQuery);
			} catch (java.sql.SQLException sqle) {
				sqle.printStackTrace();
				AndroidCCPDatabaseConnector.logger.info(">< [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
						+ access + "] Location " + locationID + ": not found at database. Returning DUMMY.");
				return new EveLocation(locationID);
			}

			// Check list contents. If found we have the location. Else then check if Office
			if ( locationList.size() < 1 ) {
				long fixedLocationID = locationID;
				hit = new EveLocation(fixedLocationID);
				Cursor cursor = null;
				try {
					cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_LOCATIONBYID,
							new String[]{Long.valueOf(fixedLocationID).toString()});
					if ( null != cursor ) {
						boolean detected = false;
						if ( cursor.moveToFirst() ) {
							detected = true;
							AndroidCCPDatabaseConnector.logger.info(
									"-- [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
											+ access + "] Location " + locationID + ": Obtained from CCP database.");
							detected = true;
							// Check returned values when doing the assignments.
							long fragmentID = cursor.getLong(cursor.getColumnIndex("systemID"));
							if ( fragmentID > 0 ) {
								hit.setSystemID(fragmentID);
								hit.setSystem(cursor.getString(cursor.getColumnIndex("system")));
							} else {
								hit.setSystem(cursor.getString(cursor.getColumnIndex("locationName")));
							}
							fragmentID = cursor.getLong(cursor.getColumnIndex("constellationID"));
							if ( fragmentID > 0 ) {
								hit.setConstellationID(fragmentID);
								hit.setConstellation(cursor.getString(cursor.getColumnIndex("constellation")));
							}
							fragmentID = cursor.getLong(cursor.getColumnIndex("regionID"));
							if ( fragmentID > 0 ) {
								hit.setRegionID(fragmentID);
								hit.setRegion(cursor.getString(cursor.getColumnIndex("region")));
							}
							hit.setTypeID(ELocationType.CCPLOCATION);
							hit.setStation(cursor.getString(cursor.getColumnIndex("locationName")));
							hit.setLocationID(cursor.getLong(cursor.getColumnIndex("locationID")));
							hit.setSecurity(cursor.getString(cursor.getColumnIndex("security")));

							// Location found on CCP database.
							hits = AndroidCCPDatabaseConnector.locationsCacheStatistics.getHits();
							AndroidCCPDatabaseConnector.logger.info(">< [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
									+ access + "] Location " + locationID + ": found at CCP Database.");
							locationsCache.put(hit.getID(), hit);
						}
						if ( !detected ) {
							AndroidCCPDatabaseConnector.logger
									.info("-- [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
											+ access + "] Location " + locationID + ": not found on any Database - UNKNOWN-.");
							hit.setSystem("ID>" + Long.valueOf(locationID).toString());
						}
					}
				} catch (final Exception ex) {
					AndroidCCPDatabaseConnector.logger.warn(
							"W- [CCPDatabaseConnector.searchLocationbyID]> Location <" + fixedLocationID + "> not found.");
				} finally {
					try {
						cursor.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				// If the location is not cached nor in the CCP database. Return default location
				return hit;
			} else {
				// Location found on the Application database.
				hits = AndroidCCPDatabaseConnector.locationsCacheStatistics.getHits();
				AndroidCCPDatabaseConnector.logger.info(">< [CCPDatabaseConnector.searchLocationbyID]> [HIT-" + hits + "/"
						+ access + "] Location " + locationID + ": found at Application Database.");
				EveLocation foundLoc = locationList.get(0);
				locationsCache.put(foundLoc.getID(), foundLoc);
				return locationList.get(0);
			}
		}
	}

	public EveLocation searchLocationBySystem (final String name) {
		final EveLocation newLocation = new EveLocation();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_LOCATIONBYSYSTEM,
					new String[]{name});
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int locationID = cursor.getInt(cursor.getColumnIndex("solarSystemID"));
					cursor.close();
					return this.searchLocationbyID(locationID);
				}
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.warning("W> Location <" + name + "> not found.");
		}
		return newLocation;
	}

	public int searchModule4Blueprint (final int bpitemID) {
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_MODULE4BLUEPRINT,
					new String[]{Integer.valueOf(bpitemID).toString()});
			if ( null != cursor ) {
				int productTypeID = -1;
				while (cursor.moveToNext()) {
					productTypeID = cursor.getInt(cursor.getColumnIndex("productTypeID"));
				}
				cursor.close();
				return productTypeID;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error locating module for BPO <" + bpitemID + ">.");
		}
		return -1;
	}

	public int searchRawPlanetaryOutput (final int typeID) {
		int outputResourceId = typeID;
		try {
			Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_RAW_PRODUCTRESULT,
					new String[]{Integer.valueOf(typeID).toString()});
			while (cursor.moveToNext()) {
				outputResourceId = cursor.getInt(cursor.getColumnIndex("typeID"));
			}
			cursor.close();
		} catch (Exception ex) {
			AndroidCCPDatabaseConnector.logger
					.warn("W- [SpingDatabaseConnector.searchRawPlanetaryOutput]> Database exception: " + ex.getMessage());
		}
		return outputResourceId;
	}

	public Vector<Schematics> searchSchematics4Output (final int targetId) {
		Vector<Schematics> scheList = new Vector<Schematics>();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_SCHEMATICS4OUTPUT,
					new String[]{Integer.valueOf(targetId).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					scheList.add(new Schematics().addData(cursor.getInt(cursor.getColumnIndex("typeID")),
							cursor.getInt(cursor.getColumnIndex("quantity")),
							(cursor.getInt(cursor.getColumnIndex("isInput")) == 1) ? true : false));
				}
				cursor.close();
			}
		} catch (Exception ex) {
			AndroidCCPDatabaseConnector.logger
					.warn("W- [SpingDatabaseConnector.searchRawPlanetaryOutput]> Database exception: " + ex.getMessage());
		}
		return scheList;
	}

	/**
	 * Returns the resource identifier of the station class to locate icons or other type related resources.
	 */
	public int searchStationType (final long stationID) {
		//		ArrayList<Resource> inventionJob = new ArrayList<Resource>();
		int stationTypeID = 1529;
		NeoComAppConnector.getSingleton().startChrono();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_STATIONTYPE,
					new String[]{Long.valueOf(stationID).toString()});
			if ( null != cursor ) {
				while (cursor.moveToNext()) {
					stationTypeID = cursor.getInt(cursor.getColumnIndex("stationTypeID"));
				}
				cursor.close();
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error searching for station type <" + stationID + "> not found.");
		}
		// Log.i("AndroidDatabaseConnector", "<<
		// AndroidDatabaseConnector.searchListOfDatacores " +
		// inventionJob.getSize());
		AndroidCCPDatabaseConnector.logger
				.debug("~~ [AndroidCCPDatabaseConnector.searchStationType]> Time lapse for [SELECT STATIONTYPEID " + stationID
						+ "] " + NeoComAppConnector.getSingleton().timeLapse());
		return stationTypeID;
	}

	public String searchTech4Blueprint (final int blueprintID) {
		try {
			if ( null == ccpDatabase ) {
				ccpDatabase = this.getCCPDatabase();
			}
			final Cursor cursor = ccpDatabase.rawQuery(AndroidCCPDatabaseConnector.SELECT_TECH4BLUEPRINT,
					new String[]{Integer.valueOf(blueprintID).toString()});
			if ( null != cursor ) {
				String productTypeID = ModelWideConstants.eveglobal.TechI;
				while (cursor.moveToNext()) {
					productTypeID = cursor.getString(3);
				}
				cursor.close();
				return productTypeID;
			}
		} catch (final Exception ex) {
			AndroidDatabaseConnector.logger.severe("E> Error locating BPO for module <" + blueprintID + ">.");
		}
		return ModelWideConstants.eveglobal.TechI;
	}

	private SQLiteDatabase getCCPDatabase () {
		if ( null == ccpDatabase ) {
			this.openCCPDataBase();
		}
		return ccpDatabase;
	}

}

// - UNUSED CODE ............................................................................................
