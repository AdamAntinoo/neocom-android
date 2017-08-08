//	PROJECT:      NeoCom.Databases (NEOC.D)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//								environment limits.
//								Database and model adaptations for storage model independency.
package org.dimensinfin.eveonline.neocom.connector;

import java.sql.SQLException;
import java.util.Vector;

import org.dimensinfin.eveonline.neocom.database.NeocomAndroidDBHelper;
import org.dimensinfin.eveonline.neocom.model.ApiKey;
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

import android.content.Context;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomDatabaseConnector extends AndroidDatabaseConnector {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	protected NeocomAndroidDBHelper											neocomDBHelper						= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomDatabaseConnector(final Context app) {
		super(app);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Dao<ApiKey, String> getApiKeysDao() throws SQLException {
		return getNeocomDBHelper().getApiKeysDao();
	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao() throws SQLException {
		return getNeocomDBHelper().getPlanetaryResourceDao();
	}

	public Dao<Property, String> getPropertyDAO() throws SQLException {
		return getNeocomDBHelper().getPropertyDAO();
	}
	public Dao<NeoComMarketOrder, String> getMarketOrderDAO() throws SQLException {
		return getNeocomDBHelper().getMarketOrderDAO();
	}

	public Dao<ResourceList, String> getResourceListDao() throws SQLException {
		return getNeocomDBHelper().getResourceListDao();
	}
	public Dao<NeoComAsset, String> getAssetDAO() throws java.sql.SQLException {
		return getNeocomDBHelper().getAssetDAO();
	}

		public Dao<NeoComBlueprint, String> getBlueprintDAO() throws SQLException {
			return getNeocomDBHelper().getBlueprintDAO();
	}

	public Dao<Job, String> getJobDAO() throws SQLException {
		return getNeocomDBHelper().getJobDAO();
	}

	public Dao<EveLocation, String> getLocationDAO() throws SQLException {
		return getNeocomDBHelper().getLocationDAO();
	}

	public Dao<DatabaseVersion, String> getVersionDao() throws SQLException {
		return getNeocomDBHelper().getVersionDao();
	}
	private NeocomAndroidDBHelper getNeocomDBHelper() {
		if (null == neocomDBHelper) {
			logger.warning(
					"W> [NeocomDatabaseConnector.getNeocomDBHelper]> helper not found. Creating a new one with default database.");
			neocomDBHelper = OpenHelperManager.getHelper(_context, NeocomAndroidDBHelper.class);
		}
		return neocomDBHelper;
	}

	public Vector<Integer> searchInputResources(int target) {
		// TODO Auto-generated method stub
		return null;
	}

	public int searchRawPlanetaryOutput(int itemID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Vector<Schematics> searchSchematics4Output(int targetId) {
		// TODO Auto-generated method stub
		return null;
	}

}

// - UNUSED CODE ............................................................................................
