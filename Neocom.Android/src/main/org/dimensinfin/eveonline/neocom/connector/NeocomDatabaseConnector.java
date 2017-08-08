//	PROJECT:      NeoCom.Databases (NEOC.D)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//								environment limits.
//								Database and model adaptations for storage model independency.
package org.dimensinfin.eveonline.neocom.connector;

import org.dimensinfin.eveonline.neocom.constant.DBR;
import org.dimensinfin.eveonline.neocom.model.Property;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceList;
import com.j256.ormlite.dao.Dao;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomDatabaseConnector extends AndroidDatabaseConnector {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	protected NeocomAndoidDBHelper											neocomDBHelper						= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomDatabaseConnector() {
		DATABASE_NAME = DBR.getResourceString("R.string.appdatabasepath")
				+ DBR.getResourceString("R.string.appdatabasefilename");
		DATABASE_VERSION = new Integer(R.getResourceString("R.string.databaseversion")).intValue();
		neocomDBHelper = new NeocomDBHelper(DATABASE_NAME, DATABASE_VERSION);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Dao<ApiKey, String> getApiKeysDao() throws SQLException {
		return AppConnector.getNeocomDBHelper().getApiKeysDao();
	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao() throws SQLException {
		return AppConnector.getNeocomDBHelper().getPlanetaryResourceDao();
	}

	@Override
	public Dao<Property, String> getPropertyDAO() throws SQLException {
		return AppConnector.getNeocomDBHelper().getPropertyDAO();
	}

	public Dao<ResourceList, String> getResourceListDao() throws SQLException {
		return AppConnector.getNeocomDBHelper().getResourceListDao();
	}

	public Dao<DatabaseVersion, String> getVersionDao() throws SQLException {
		return AppConnector.getNeocomDBHelper().getVersionDao();
	}
	private INeocomDBHelper getNeocomDBHelper() {
		if (null == neocomDBHelper) {
			logger.warning(
					"W> [NeocomDatabaseConnector.getNeocomDBHelper]> helper not found. Creating a new one with default database.");
			neocomDBHelper = OpenHelperManager.getHelper(this, NeocomAndroidDBHelper.class);
		}
		return neocomDBHelper;
	}

}

// - UNUSED CODE ............................................................................................
