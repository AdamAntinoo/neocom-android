//	PROJECT:      NeoCom.Databases (NEOC.D)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//								environment limits.
//								Database and model adaptations for storage model independency.
package org.dimensinfin.eveonline.neocom.connector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.dimensinfin.eveonline.neocom.database.NeocomAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.model.ApiKey;
import org.dimensinfin.eveonline.neocom.model.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.Job;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComBlueprint;
import org.dimensinfin.eveonline.neocom.model.NeoComMarketOrder;
import org.dimensinfin.eveonline.neocom.model.Property;
import org.dimensinfin.eveonline.neocom.model.Schematics;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceList;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import android.content.Context;
import android.database.Cursor;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComDatabaseConnector extends AndroidDatabaseConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final String			SELECT_SCHEMATICS_INFO	= "SELECT pstms.typeID, pstms.quantity, pstms.isInput"
			+ " FROM   planetSchematicsTypeMap pstmt, planetSchematicsTypeMap pstms" + " WHERE  pstmt.typeID = ?"
			+ " AND    pstmt.isInput = 0" + " AND    pstms.schematicID = pstmt.schematicID";

	// - F I E L D - S E C T I O N ............................................................................
	protected NeocomAndroidDBHelper	neocomDBHelper					= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComDatabaseConnector(final Context app) {
		super(app);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Dao<ApiKey, String> getApiKeysDao() throws SQLException {
		return this.getNeocomDBHelper().getApiKeysDao();
	}

	public Dao<NeoComAsset, String> getAssetDAO() throws SQLException {
		return this.getNeocomDBHelper().getAssetDAO();
	}

	public Dao<NeoComBlueprint, String> getBlueprintDAO() throws SQLException {
		return this.getNeocomDBHelper().getBlueprintDAO();
	}

	@Override
	public Dao<Job, String> getJobDAO() throws SQLException {
		return this.getNeocomDBHelper().getJobDAO();
	}

	public Dao<EveLocation, String> getLocationDAO() throws SQLException {
		return this.getNeocomDBHelper().getLocationDAO();
	}

	public Dao<NeoComMarketOrder, String> getMarketOrderDAO() throws SQLException {
		return this.getNeocomDBHelper().getMarketOrderDAO();
	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao() throws SQLException {
		return this.getNeocomDBHelper().getPlanetaryResourceDao();
	}

	@Override
	public Dao<Property, String> getPropertyDAO() throws SQLException {
		return this.getNeocomDBHelper().getPropertyDAO();
	}

	public Dao<ResourceList, String> getResourceListDao() throws SQLException {
		return this.getNeocomDBHelper().getResourceListDao();
	}

	public Dao<DatabaseVersion, String> getVersionDao() throws SQLException {
		return this.getNeocomDBHelper().getVersionDao();
	}

	/**
	 * Get the complete list of assets that are Planetary Materials.
	 * 
	 * @return
	 */
	public ArrayList<NeoComAsset> searchAllPlanetaryAssets(final long characterID) {
		// Select assets for each one of the Planetary categories.
		ArrayList<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		assetList.addAll(this.searchAsset4Category(characterID, "Planetary Commodities"));
		assetList.addAll(this.searchAsset4Category(characterID, "Planetary Resources"));
		return assetList;
	}

	/**
	 * gets the list of assets of a select Category
	 * 
	 * @param characterID
	 * @param typeID
	 * @return
	 */
	public ArrayList<NeoComAsset> searchAsset4Category(final long characterID, final String categoryName) {
		// Select assets for the owner and with an specific type id.
		List<NeoComAsset> assetList = new ArrayList<NeoComAsset>();
		try {
			Dao<NeoComAsset, String> assetDao = this.getAssetDAO();
			QueryBuilder<NeoComAsset, String> queryBuilder = assetDao.queryBuilder();
			Where<NeoComAsset, String> where = queryBuilder.where();
			where.eq("ownerID", characterID);
			where.and();
			where.eq("category", categoryName);
			PreparedQuery<NeoComAsset> preparedQuery = queryBuilder.prepare();
			assetList = assetDao.query(preparedQuery);
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
		}
		return (ArrayList<NeoComAsset>) assetList;
	}

	/**
	 * Gets the list of type ids for the required resources to get an output batch of the former planetary
	 * resource.
	 * 
	 * @param typeID
	 * @return
	 */
	public Vector<Integer> searchInputResources(final int typeID) {
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

	public int searchRawPlanetaryOutput(final int typeID) {
		int outputResourceId = typeID;
		//		PreparedStatement prepStmt = null;
		//		ResultSet cursor = null;
		//		try {
		//			prepStmt = this.getCCPDatabase().prepareStatement(SELECT_RAW_PRODUCTRESULT);
		//			prepStmt.setString(1, Integer.valueOf(typeID).toString());
		//			cursor = prepStmt.executeQuery();
		//			while (cursor.next()) {
		//				outputResourceId = cursor.getInt(1);
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
		return outputResourceId;
	}

	public Vector<Schematics> searchSchematics4Output(final int targetId) {
		Vector<Schematics> scheList = new Vector<Schematics>();
		PreparedStatement prepStmt = null;
		Cursor cursor = null;
		try {
			cursor = this.getCCPDatabase().rawQuery(NeoComDatabaseConnector.SELECT_SCHEMATICS_INFO,
					new String[] { Integer.valueOf(targetId).toString() });
			while (cursor.moveToNext()) {
				boolean flag = true;
				if (cursor.getInt(3) == 0) {
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

	private NeocomAndroidDBHelper getNeocomDBHelper() {
		if (null == neocomDBHelper) {
			AndroidDatabaseConnector.logger.warning(
					"W> [NeocomDatabaseConnector.getNeocomDBHelper]> helper not found. Creating a new one with default database.");
			neocomDBHelper = OpenHelperManager.getHelper(_context, NeocomAndroidDBHelper.class);
		}
		return neocomDBHelper;
	}

}

// - UNUSED CODE ............................................................................................
