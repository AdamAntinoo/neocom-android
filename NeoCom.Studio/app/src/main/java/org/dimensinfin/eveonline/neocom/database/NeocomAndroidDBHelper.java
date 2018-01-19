//	PROJECT:      NeoCom.Databases (NEOC.D)
//	AUTHORS:      Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:    (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//								environment limits.
//								Database and model adaptations for storage model independency.
package org.dimensinfin.eveonline.neocom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.enums.ERequestClass;
import org.dimensinfin.eveonline.neocom.model.ApiKey;
import org.dimensinfin.eveonline.neocom.model.Credential;
import org.dimensinfin.eveonline.neocom.model.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComBlueprint;
import org.dimensinfin.eveonline.neocom.model.Property;
import org.dimensinfin.eveonline.neocom.model.TimeStamp;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceList;

import java.sql.SQLException;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomAndroidDBHelper extends OrmLiteSqliteOpenHelper implements INeoComDBHelper{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("NeocomAndroidDBHelper");

	// - F I E L D - S E C T I O N ............................................................................
	//	private final boolean										isOpen								= false;
	//	private final Connection								neocomDatabase				= null;
	//	private final Connection								neocomDatasource			= null;
	//	private Connection														ccpDatabase								= null;

	private Dao<DatabaseVersion, String> versionDao = null;
	private Dao<Credential, String> credentialDao = null;
	private Dao<TimeStamp, String> timeStampDao = null;
	private Dao<ApiKey, String> apiKeysDao = null;
	private Dao<Property, String> propertyDao = null;
	private Dao<ResourceList, String> resourceList = null;
	private Dao<PlanetaryResource, String> planetaryResourceDao = null;
	private Dao<NeoComAsset, String> assetDao = null;
	private Dao<NeoComBlueprint, String> blueprintDao = null;
	//	private Dao<Job, String>								jobDao								= null;
	//	private Dao<NeoComMarketOrder, String>	marketOrderDao				= null;
	private Dao<EveLocation, String> locationDao = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomAndroidDBHelper (final Context context) {
		super(context,
				NeoComAppConnector.getSingleton().getStorageConnector()
				                  .accessAppStorage(NeoComAppConnector.getSingleton().getResourceString(R.string.appdatabasefilename))
				                  .getAbsolutePath(),
				null, new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue(),
				R.raw.ormlite_config);
		// TODO Set this Helper to the new NeoComDatabase.
//		NeoComDatabase.setImplementer(this);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close () {
		super.close();
		assetDao = null;
		blueprintDao = null;
	}

	public Dao<ApiKey, String> getApiKeysDao () throws SQLException {
		if ( null == apiKeysDao ) {
			apiKeysDao = DaoManager.createDao(this.getConnectionSource(), ApiKey.class);
		}
		return apiKeysDao;
	}

	@Override
	public Dao<Credential, String> getCredentialDao () throws SQLException {
		if ( null == credentialDao ) {
			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
		}
		return credentialDao;
	}

	@Override
	public Dao<ColonyStorage, String> getColonyStorageDao () throws SQLException {
		return null;
	}

	public Dao<NeoComAsset, String> getAssetDAO () throws java.sql.SQLException {
		if ( null == assetDao ) {
			assetDao = DaoManager.createDao(this.getConnectionSource(), NeoComAsset.class);
		}
		return assetDao;
	}

	public Dao<NeoComBlueprint, String> getBlueprintDAO () throws java.sql.SQLException {
		if ( null == blueprintDao ) {
			blueprintDao = DaoManager.createDao(this.getConnectionSource(), NeoComBlueprint.class);
		}
		return blueprintDao;
	}

	public Dao<Credential,String> getCredentialDAO () throws java.sql.SQLException{
		if ( null == credentialDao ) {
			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
		}
		return credentialDao;
	}

	//	public Dao<Job, String> getJobDAO() throws java.sql.SQLException {
	//		if (null == jobDao) {
	//			jobDao = DaoManager.createDao(this.getConnectionSource(), Job.class);
	//		}
	//		return jobDao;
	//	}

	public Dao<EveLocation, String> getLocationDAO () throws java.sql.SQLException {
		if ( null == locationDao ) {
			locationDao = DaoManager.createDao(this.getConnectionSource(), EveLocation.class);
		}
		return locationDao;
	}

	//	public Dao<NeoComMarketOrder, String> getMarketOrderDAO() throws java.sql.SQLException {
	//		if (null == marketOrderDao) {
	//			marketOrderDao = DaoManager.createDao(this.getConnectionSource(), NeoComMarketOrder.class);
	//		}
	//		return marketOrderDao;
	//	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao () throws java.sql.SQLException {
		if ( null == planetaryResourceDao ) {
			planetaryResourceDao = DaoManager.createDao(this.getConnectionSource(), PlanetaryResource.class);
		}
		return planetaryResourceDao;
	}

	public Dao<Property, String> getPropertyDAO () throws java.sql.SQLException {
		if ( null == propertyDao ) {
			propertyDao = DaoManager.createDao(this.getConnectionSource(), Property.class);
		}
		return propertyDao;
	}

	public Dao<ResourceList, String> getResourceListDao () throws java.sql.SQLException {
		if ( null == resourceList ) {
			resourceList = DaoManager.createDao(this.getConnectionSource(), ResourceList.class);
		}
		return resourceList;
	}

	public Dao<TimeStamp, String> getTimeStampDAO () throws java.sql.SQLException {
		if ( null == timeStampDao ) {
			timeStampDao = DaoManager.createDao(this.getConnectionSource(), TimeStamp.class);
		}
		return timeStampDao;
	}

	public Dao<DatabaseVersion, String> getVersionDao () throws java.sql.SQLException {
		if ( null == versionDao ) {
			versionDao = DaoManager.createDao(this.getConnectionSource(), DatabaseVersion.class);
		}
		return versionDao;
	}

	public void loadSeedData () {
		// Add seed data to the new database is the tables are empty.
		try {
			//---  D A T A B A S E    V E R S I O N
			Dao<DatabaseVersion, String> version = this.getVersionDao();
			QueryBuilder<DatabaseVersion, String> queryBuilder = version.queryBuilder();
			queryBuilder.setCountOf(true);
			long records = version.countOf(queryBuilder.prepare());

			// If the table is empty then insert the seeded Api Keys
			if ( records < 1 ) {
				DatabaseVersion key = new DatabaseVersion(
						NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion));
			}
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}

		//--- A P I   K E Y S
		try {
			Dao<ApiKey, String> apikey = this.getApiKeysDao();
			QueryBuilder<ApiKey, String> queryBuilder = apikey.queryBuilder();
			queryBuilder.setCountOf(true);
			long records = apikey.countOf(queryBuilder.prepare());

			// If the table is empty then insert the seeded Api Keys
			if ( records < 1 ) {
				ApiKey key = new ApiKey("Beth Ripley").setKeynumber(2889577)
				                                      .setValidationcode("Mb6iDKR14m9Xjh9maGTQCGTkpjRHPjOgVUkvK6E9r6fhMtOWtipaqybp0qCzxuuw")
				                                      .setActive(true);
				key = new ApiKey("Perico").setKeynumber(3106761)
				                          .setValidationcode("gltCmvVoZl5akrM8d6DbNKZn7Jm2SaukrmqjnSOyqKbvzz5CtNfknTEwdBe6IIFf").setActive(false);
				key = new ApiKey("CapitanHaddock09").setKeynumber(924767)
				                                    .setValidationcode("2qBKUY6I9ozYhKxYUBPnSIix0fHFCqveD1UEAv0GbYqLenLLTIfkkIWeOBejKX5P").setActive(true);
				key = new ApiKey("CapitanHaddock29").setKeynumber(6472981)
				                                    .setValidationcode("pj1NJKKb0pNO8LTp0qN2yJSxZoZUO0UYYq8qLtOeFXNsNBRpiz7orcqVAu7UGF7z").setActive(true);
				key = new ApiKey("CapitanHaddock").setKeynumber(924767)
				                                  .setValidationcode("2qBKUY6I9ozYhKxYUBPnSIix0fHFCqveD1UEAv0GbYqLenLLTIfkkIWeOBejKX5P").setActive(false);
				key = new ApiKey("CapitanHaddock").setKeynumber(6472981)
				                                  .setValidationcode("pj1NJKKb0pNO8LTp0qN2yJSxZoZUO0UYYq8qLtOeFXNsNBRpiz7orcqVAu7UGF7z").setActive(false);
			}
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}
	}

	@Override
	public int getDatabaseVersion () {
		return new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue();
	}

	@Override
	public int getStoredVersion () throws SQLException {
		 		return new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue();
	}

	public void onCreate (final ConnectionSource databaseConnection) {
	}

	@Override
	public void onUpgrade (final ConnectionSource databaseConnection, final int oldVersion, final int newVersion) {
	}

	@Override
	public void onCreate (final SQLiteDatabase database, final ConnectionSource databaseConnection) {
		try {
			// Now open the DAO connector and create tables if they not exist
			TableUtils.createTableIfNotExists(databaseConnection, DatabaseVersion.class);
			TableUtils.createTableIfNotExists(databaseConnection, ApiKey.class);
			TableUtils.createTableIfNotExists(databaseConnection, Credential.class);

			TableUtils.createTableIfNotExists(databaseConnection, NeoComAsset.class);
			TableUtils.createTableIfNotExists(databaseConnection, NeoComBlueprint.class);
			//			TableUtils.createTableIfNotExists(databaseConnection, Job.class);
			TableUtils.createTableIfNotExists(databaseConnection, EveLocation.class);
			//			TableUtils.createTableIfNotExists(databaseConnection, NeoComMarketOrder.class);
			TableUtils.createTableIfNotExists(databaseConnection, PlanetaryResource.class);
			TableUtils.createTableIfNotExists(databaseConnection, Property.class);
			TableUtils.createTableIfNotExists(databaseConnection, ResourceList.class);
			TableUtils.createTableIfNotExists(databaseConnection, TimeStamp.class);
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}
		this.loadSeedData();
	}

	@Override
	public void onUpgrade (final SQLiteDatabase database, final ConnectionSource databaseConnection, final int oldVersion,
	                       final int newVersion) {
		int i = 1;
		// Execute different actions depending the version.
		if ( oldVersion < 4 ) {
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, NeoComBlueprint.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			//			try {
			//				// Delete all the CCP data tables to create then again on open.
			//				TableUtils.dropTable(databaseConnection, Job.class, true);
			//			} catch (RuntimeException rtex) {
			//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
			//				rtex.printStackTrace();
			//			} catch (SQLException sqle) {
			//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
			//				sqle.printStackTrace();
			//			}
		}
		if ( oldVersion < 6 ) {
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, NeoComBlueprint.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			//			try {
			//				// Delete all the CCP data tables to create then again on open.
			//				TableUtils.dropTable(databaseConnection, Job.class, true);
			//			} catch (RuntimeException rtex) {
			//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
			//				rtex.printStackTrace();
			//			} catch (SQLException sqle) {
			//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
			//				sqle.printStackTrace();
			//			}
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, Property.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion == 6 ) {
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, NeoComBlueprint.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
			try {
				// Delete all the CCP data tables to create then again on open.
				TableUtils.dropTable(databaseConnection, Property.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 47 ) {
			try {
				// Delete all the CCP data tables to create then again on open.
				NeoComAppConnector.getSingleton().getApplicationContext()
				                  .deleteDatabase(NeoComAppConnector.getSingleton().getResourceString(R.string.appdatabasepath)
						                  + NeoComAppConnector.getSingleton().getResourceString(R.string.appdatabasefilename));
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			}
		}
		if ( oldVersion < 48 ) {
			try {
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 49 ) {
			try {
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		//		if (oldVersion < 50) {
		//			try {
		//				TableUtils.dropTable(databaseConnection, Job.class, true);
		//				TableUtils.dropTable(databaseConnection, NeoComMarketOrder.class, true);
		//				TableUtils.dropTable(databaseConnection, Property.class, true);
		//			} catch (RuntimeException rtex) {
		//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
		//				rtex.printStackTrace();
		//			} catch (SQLException sqle) {
		//				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
		//				sqle.printStackTrace();
		//			}
		//		}
		if ( oldVersion < 51 ) {
			try {
				TableUtils.dropTable(databaseConnection, NeoComBlueprint.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 52 ) {
			try {
				TableUtils.dropTable(databaseConnection, NeoComBlueprint.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 53 ) {
			try {
				TableUtils.dropTable(databaseConnection, Property.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 64 ) {
			try {
				TableUtils.createTableIfNotExists(databaseConnection, EveLocation.class);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 65 ) {
			try {
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
				TableUtils.dropTable(databaseConnection, EveLocation.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 93 ) {
			try {
				TableUtils.dropTable(databaseConnection, ApiKey.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 94 ) {
			try {
				TableUtils.dropTable(databaseConnection, EveLocation.class, true);
				// Post the events to reload the Locations data.
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.CITADELUPDATE);
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.OUTPOSTUPDATE);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 95 ) {
			try {
				TableUtils.dropTable(databaseConnection, Credential.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		this.onCreate(database, databaseConnection);
	}
}
// - UNUSED CODE ............................................................................................
