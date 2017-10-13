//	PROJECT:      Neocom.MarketDataService (NEOC-MKDS)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	This project contains a MicroService specially dedicated to get and store the Market Data
//								information. I should be exposed on a new port and should be accesible to all the backend MS to consult
//								Item Market Data.
package org.dimensinfin.eveonline.neocom.connector;

import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.ModelWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ELocationType;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.EveLocation;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

// - CLASS IMPLEMENTATION ...................................................................................
public class AndroidCCPDatabaseConnector implements ICCPDatabaseConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger											logger									= Logger.getLogger("CCPDatabaseConnector.java");
	private static final String								CCPDATABASE_URL					= "jdbc:sqlite:src/main/resources/eve.db";

	// - F I E L D   I N D E X   D E F I N I T I O N S
	private static int												STATIONTYPEID_COLINDEX	= 1;

	// - S Q L   C O M M A N D S
	private static final String								SELECT_ITEM_BYID				= "SELECT it.typeID AS typeID, it.typeName AS typeName"
			+ " , ig.groupName AS groupName" + " , ic.categoryName AS categoryName" + " , it.basePrice AS basePrice"
			+ " , it.volume AS volume" + " , IFNULL(img.metaGroupName, " + '"' + "NOTECH" + '"' + ") AS Tech"
			+ " FROM invTypes it" + " LEFT OUTER JOIN invGroups ig ON ig.groupID = it.groupID"
			+ " LEFT OUTER JOIN invCategories ic ON ic.categoryID = ig.categoryID"
			+ " LEFT OUTER JOIN invMetaTypes imt ON imt.typeID = it.typeID"
			+ " LEFT OUTER JOIN invMetaGroups img ON img.metaGroupID = imt.metaGroupID" + " WHERE it.typeID = ?";
	private static final String								SELECT_LOCATIONBYID			= "SELECT md.itemID AS locationID, md.typeID AS typeID, md.itemName AS locationName, md.security AS security"
			+ " , IFNULL(md.solarSystemID, -1) AS systemID, ms.solarSystemName AS system"
			+ " , IFNULL(md.constellationID, -1) AS constellationID, mc.constellationName AS constellation"
			+ " , IFNULL(md.regionID, -1) AS regionID, mr.regionName AS region" + " FROM mapDenormalize md"
			+ " LEFT OUTER JOIN mapRegions mr ON mr.regionID = md.regionID"
			+ " LEFT OUTER JOIN mapConstellations mc ON mc.constellationID = md.constellationID"
			+ " LEFT OUTER JOIN mapSolarSystems ms ON ms.solarSystemID = md.solarSystemID" + " WHERE itemID = ?";
	private static final String								SELECT_LOCATIONBYSYSTEM	= "SELECT solarSystemID from mapSolarSystems WHERE solarSystemName = ?";
	private static final String								STATIONTYPE							= "SELECT stationTypeID FROM staStations WHERE stationID = ?";

	// - F I E L D - S E C T I O N ............................................................................
	private SQLiteDatabase										ccpDatabase							= null;
	private final Hashtable<Integer, EveItem>	itemCache								= new Hashtable<Integer, EveItem>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AndroidCCPDatabaseConnector() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public boolean openCCPDataBase() {
		if (null == ccpDatabase) {
			final String database = NeoComAppConnector.getSingleton().getAppFilePath(R.string.ccpdatabasefilename);
			final String path = Environment.getExternalStorageDirectory() + "/" + database;
			//			try {
			// InputStream dbStream =
			// _context.getResources().openRawResource(R.raw.eve);
			if (NeoComAppConnector.getSingleton().sdcardAvailable()) {
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

	public int queryBlueprintDependencies(final int bpitemID) {
		try {
			if (null == ccpDatabase) {
				ccpDatabase = this.getCCPDatabase();
			}
			final Cursor cursor = ccpDatabase.rawQuery(
					"SELECT parentBlueprintTypeID FROM invBlueprintTypes BT WHERE blueprintTypeID = ?",
					new String[] { Integer.valueOf(bpitemID).toString() });
			if (null != cursor) {
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

	/**
	 * Search on the eve.db database for the attributes that describe an Item. Items are the lowest data
	 * structure for EVE resources or modules. Everything on Eve is an Item. We detect blueprints that require a
	 * different treatment and also we check for the availability of the item at the current cache if
	 * implemented.
	 */
	public EveItem searchItembyID(final int typeID) {
		// Search the item on the cache.
		EveItem hit = itemCache.get(typeID);
		if (null == hit) {
			try {
				hit = new EveItem();
				final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_ITEM_BYID,
						new String[] { Integer.valueOf(typeID).toString() });
				if (null != cursor) {
					if (cursor.moveToFirst()) {
						hit.setTypeID(cursor.getInt(0));
						hit.setName(cursor.getString(1));
						hit.setGroupname(cursor.getString(2));
						hit.setCategory(cursor.getString(3));
						hit.setBasePrice(cursor.getDouble(4));
						hit.setVolume(cursor.getDouble(5));
						// Process the Tech field. The query marks blueprints
						String tech = cursor.getString(6);
						if (tech.equalsIgnoreCase("NOTECH")) {
							// Double check it is a Blueprint
							hit.setTech(ModelWideConstants.eveglobal.TechI);
							if (hit.getName().contains(" II Blueprint")) {
								hit.setBlueprint(true);
								if (hit.getName().contains(" II Blueprint")) {
									hit.setTech(ModelWideConstants.eveglobal.TechII);
								}
								if (hit.getName().contains(" III Blueprint")) {
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
						.warning("W> AndroidDatabaseConnector.searchItembyID -- Item <" + typeID + "> not found.");
				return new EveItem();
			}
		}
		return hit;
	}

	//	public EveLocation searchLocationbyID(final long locationID) {
	//		EveLocation hit = new EveLocation(locationID);
	//		//	 prepStmt = null;
	//		Cursor cursor = null;
	//		try {
	//			cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_LOCATIONBYID,
	//					new String[] { Long.valueOf(locationID).toString() });
	//			//		cursor = prepStmt.executeQuery();
	//			boolean detected = false;
	//			while (cursor.moveToNext()) {
	//				//				if (cursor.moveToFirst()) {
	//				detected = true;
	//				// Check returned values when doing the assignments.
	//				long fragmentID = cursor.getLong(5);
	//				if (fragmentID > 0) {
	//					hit.setSystemID(fragmentID);
	//					hit.setSystem(cursor.getString(6));
	//				} else {
	//					hit.setSystem(cursor.getString(3));
	//				}
	//				fragmentID = cursor.getLong(7);
	//				if (fragmentID > 0) {
	//					hit.setConstellationID(fragmentID);
	//					hit.setConstellation(cursor.getString(8));
	//				}
	//				fragmentID = cursor.getLong(9);
	//				if (fragmentID > 0) {
	//					hit.setRegionID(fragmentID);
	//					hit.setRegion(cursor.getString(10));
	//				}
	//				hit.setTypeID(cursor.getInt(2));
	//				hit.setStation(cursor.getString(3));
	//				hit.setLocationID(cursor.getLong(1));
	//				hit.setSecurity(cursor.getString(4));
	//				// Update the final ID
	//				hit.getID();
	//				detected = true;
	//			}
	//			//			if (!detected) // Search the location on the list of outposts.
	//			//				hit = searchOutpostbyID(locationID);
	//			//	}
	//		} catch (final Exception ex) {
	//			AndroidCCPDatabaseConnector.logger.warning("Location <" + locationID + "> not found.");
	//		} finally {
	//			cursor.close();
	//		}
	//		return hit;
	//	}

	/**
	 * Loads the system and other information for a location based on the ID received as a parameter. New
	 * implementation use this ID to calculate if this matches a corporation outpost or a corporation office
	 * hangar.
	 */
	public EveLocation searchLocationbyID(final long locationID) {
		// Try to get that id from the cache tables
		try {
			Dao<EveLocation, String> locationDao = NeoComAppConnector.getSingleton().getDBConnector().getLocationDAO();
			QueryBuilder<EveLocation, String> queryBuilder = locationDao.queryBuilder();
			Where<EveLocation, String> where = queryBuilder.where();
			where.eq("id", locationID);
			PreparedQuery<EveLocation> preparedQuery = queryBuilder.prepare();
			List<EveLocation> locationList = locationDao.query(preparedQuery);

			// Check list contents. If found we have the location. Else then check if Office
			if (locationList.size() < 1) {
				AndroidDatabaseConnector.logger
						.info("-- [searchLocationbyID]> Location: " + locationID + " not found on cache.");
				// Offices
				long fixedLocationID = locationID;
				if (fixedLocationID >= 66000000) {
					if (fixedLocationID < 66014933) {
						fixedLocationID = fixedLocationID - 6000001;
					} else {
						fixedLocationID = fixedLocationID - 6000000;
					}
				}
				EveLocation hit = new EveLocation(fixedLocationID);
				try {
					final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_LOCATIONBYID,
							new String[] { Long.valueOf(fixedLocationID).toString() });
					if (null != cursor) {
						boolean detected = false;
						if (cursor.moveToFirst()) {
							AndroidDatabaseConnector.logger
									.info("-- [searchLocationbyID]> Location: " + locationID + " Obtained from CCP data.");
							detected = true;
							// Check returned values when doing the assignments.
							long fragmentID = cursor.getLong(cursor.getColumnIndex("systemID"));
							if (fragmentID > 0) {
								hit.setSystemID(fragmentID);
								hit.setSystem(cursor.getString(cursor.getColumnIndex("system")));
							} else {
								hit.setSystem(cursor.getString(cursor.getColumnIndex("locationName")));
							}
							fragmentID = cursor.getLong(cursor.getColumnIndex("constellationID"));
							if (fragmentID > 0) {
								hit.setConstellationID(fragmentID);
								hit.setConstellation(cursor.getString(cursor.getColumnIndex("constellation")));
							}
							fragmentID = cursor.getLong(cursor.getColumnIndex("regionID"));
							if (fragmentID > 0) {
								hit.setRegionID(fragmentID);
								hit.setRegion(cursor.getString(cursor.getColumnIndex("region")));
							}
							hit.setTypeID(ELocationType.CCPLOCATION);
							hit.setStation(cursor.getString(cursor.getColumnIndex("locationName")));
							hit.setLocationID(cursor.getLong(cursor.getColumnIndex("locationID")));
							hit.setSecurity(cursor.getString(cursor.getColumnIndex("security")));
							cursor.close();
						}
						if (!detected) {
							AndroidDatabaseConnector.logger
									.info("-- [searchLocationbyID]> Location: " + locationID + " not found on CCP data.");
							hit.setSystem("ID>" + Long.valueOf(locationID).toString());
						}
					}
				} catch (final Exception ex) {
					AndroidDatabaseConnector.logger.warning(
							"W- [AndroidDatabaseConnector.searchLocationbyID]> Location <" + fixedLocationID + "> not found.");
				}
				// If the location is not cached nor in the CCP database. Return default location
				return hit;
			}
			return locationList.get(0);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			return new EveLocation(locationID);
		}
	}

	public EveLocation searchLocationBySystem(final String name) {
		final EveLocation newLocation = new EveLocation();
		try {
			final Cursor cursor = this.getCCPDatabase()
					.rawQuery("SELECT solarSystemID from mapSolarSystems WHERE solarSystemName = ?", new String[] { name });
			if (null != cursor) {
				if (cursor.moveToFirst()) {
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

	//	public EveLocation searchLocationBySystem(final String name) {
	//		EveLocation hit = new EveLocation();
	//		//		PreparedStatement prepStmt = null;
	//		Cursor cursor = null;
	//		try {
	//			cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.SELECT_LOCATIONBYSYSTEM,
	//					new String[] { name });
	//			//		cursor = prepStmt.executeQuery();
	//			boolean detected = false;
	//			//	while (cursor.moveToFirst()) {
	//			if (cursor.moveToFirst()) {
	//				detected = true;
	//				// Check returned values when doing the assignments.
	//				long fragmentID = cursor.getInt(1);
	//				if (fragmentID > 0) {
	//					hit.setSystemID(fragmentID);
	//					//					hit.setSystem(cursor.getString(6));
	//					//				} else {
	//					//					hit.setSystem(cursor.getString(3));
	//				}
	//				//				fragmentID = cursor.getLong(7);
	//				//				if (fragmentID > 0) {
	//				//					hit.setConstellationID(fragmentID);
	//				//					hit.setConstellation(cursor.getString(8));
	//				//				}
	//				//				fragmentID = cursor.getLong(9);
	//				//				if (fragmentID > 0) {
	//				//					hit.setRegionID(fragmentID);
	//				//					hit.setRegion(cursor.getString(10));
	//				//				}
	//				//				hit.setTypeID(cursor.getInt(2));
	//				//				hit.setStation(cursor.getString(3));
	//				//				hit.setLocationID(cursor.getLong(1));
	//				//				hit.setSecurity(cursor.getString(4));
	//				//				// Update the final ID
	//				//				hit.getID();
	//				detected = true;
	//			}
	//			//			if (!detected) // Search the location on the list of outposts.
	//			//				hit = searchOutpostbyID(locationID);
	//			//	}
	//		} catch (final Exception ex) {
	//			AndroidCCPDatabaseConnector.logger.warning("Location <" + name + "> not found.");
	//		} finally {
	//			cursor.close();
	//		}
	//		return this.searchLocationbyID(hit.getSystemID());
	//	}

	public int searchModule4Blueprint(final int bpitemID) {
		try {
			if (null == ccpDatabase) {
				ccpDatabase = this.getCCPDatabase();
			}
			final Cursor cursor = ccpDatabase.rawQuery(
					"SELECT productTypeID FROM industryActivityProducts BT WHERE typeID = ? AND activityID = 1",
					new String[] { Integer.valueOf(bpitemID).toString() });
			if (null != cursor) {
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

	/**
	 * Returns the resource identifier of the station class to locate icons or other type related resources.
	 *
	 * @param stationID
	 * @return
	 */
	public int searchStationType(final long stationID) {
		//		ArrayList<Resource> inventionJob = new ArrayList<Resource>();
		int stationTypeID = 1529;
		NeoComAppConnector.getSingleton().startChrono();
		try {
			final Cursor cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.STATIONTYPE,
					new String[] { Long.valueOf(stationID).toString() });
			if (null != cursor) {
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
				.info("~~ [AndroidCCPDatabaseConnector.searchStationType]> Time lapse for [SELECT STATIONTYPEID " + stationID
						+ "] " + NeoComAppConnector.getSingleton().timeLapse());
		return stationTypeID;
	}

	/**
	 * Returns the resource identifier of the station class to locate icons or other type related resources.
	 *
	 * @param stationID
	 * @return
	 */
	public int searchStationType2(final long stationID) {
		int stationTypeID = 1529;
		NeoComAppConnector.getSingleton().startChrono();
		//	PreparedStatement prepStmt = null;
		Cursor cursor = null;
		try {
			cursor = this.getCCPDatabase().rawQuery(AndroidCCPDatabaseConnector.STATIONTYPE,
					new String[] { Long.valueOf(stationID).toString() });
			//		cursor = prepStmt.executeQuery();
			//		while (cursor.next()) {
			if (cursor.moveToFirst()) {
				stationTypeID = cursor.getInt(AndroidCCPDatabaseConnector.STATIONTYPEID_COLINDEX);
			}
		} catch (Exception ex) {
			AndroidCCPDatabaseConnector.logger
					.warning("W- [SpingDatabaseConnector.searchStationType]> Database exception: " + ex.getMessage());
		} finally {
			cursor.close();
		}
		//	logger.info("~~ Time lapse for [SELECT STATIONTYPEID " + stationID + "] " + NeoComAppConnector.getSingleton().timeLapse());
		return stationTypeID;
	}

	private SQLiteDatabase getCCPDatabase() {
		if (null == ccpDatabase) {
			this.openCCPDataBase();
		}
		return ccpDatabase;
	}

}

// - UNUSED CODE ............................................................................................
